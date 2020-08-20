package io.kuark.cache.core

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import io.kuark.base.lang.reflect.TypeKit
import io.kuark.cache.kit.CacheKit
import io.kuark.context.spring.SpringKit
import org.springframework.cache.annotation.CacheConfig
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.lang.reflect.Method
import java.math.BigDecimal
import java.math.BigInteger


/**
 * 批量缓存的切面
 *
 * @author K
 * @since 1.0.0
 */
@Aspect
@Component
@Lazy(false)
class BatchCacheableAspect {

    /**
     * 定义切入点
     */
    @Pointcut("@annotation(io.kuark.cache.core.BatchCacheable)")
    private fun cut() {
        // do nothing
    }

    /**
     * 环绕通知
     */
    @Around("cut()")
    fun around(joinPoint: ProceedingJoinPoint): Any? {
        val clazz = joinPoint.target.javaClass
        val parameterTypes = (joinPoint.signature as MethodSignature).parameterTypes
        val method = clazz.getMethod(joinPoint.signature.name, *parameterTypes)
        val batchCacheable = method.getDeclaredAnnotation(BatchCacheable::class.java) // 拿到方法定义的注解信息

        // 校验约束
        val cacheName = validate(joinPoint, method, batchCacheable)

        val result = linkedMapOf<String, Any?>()

        // 得到所有缓存key
        val keys = getAllCacheKeys(joinPoint, method, batchCacheable)
        keys.forEach { result[it] = null } // 保证顺序

        // 读取缓存中存在的数据
        readCachedData(keys, cacheName, batchCacheable, result)

        // 没有在缓存中的(参数为集合的，要踢除缓存中读到的部分)，从@BatchCacheable标注的方法读
        val data = readUncachedData(result, joinPoint, batchCacheable, method, *parameterTypes)

        // 缓存从@BatchCacheable标注的方法读取(未缓存)的数据(注意：已存在的缓存并不会被更新)
        data?.forEach { (k, v) -> CacheKit.putIfAbsent(cacheName, k, v) }

        // 组装两部分数据：缓存中读取的和刚加载的，并作为@BatchCacheable标注的方法的返回值返回
        if (data != null) {
            result.forEach { (k, v) -> if (v == null) result[k] = data[k] }
        }

        return result
    }

    /**
     * 校验约束
     */
    private fun validate(joinPoint: ProceedingJoinPoint, method: Method, batchCacheable: BatchCacheable): String {
        val clazz = joinPoint.target.javaClass

        // 校验缓存名称是否配置
        val cacheConfig = getClassAnnotation(joinPoint) // 读取类注解
        var cacheName: String? = null
        if (batchCacheable.cacheNames.isNotEmpty()) {
            cacheName = batchCacheable.cacheNames.first()
        } else if (cacheConfig.cacheNames.isNotEmpty()) {
            cacheName = cacheConfig.cacheNames.first()
        }
        if (cacheName == null) {
            error("cacheNames未设置！请在类${clazz}上通过@CacheConfig指定，或在方法${method}上通过@BatchCacheable指定！")
        }

        // 验证方法返回值类型
        if (!Map::class.java.isAssignableFrom(method.returnType)) {
            error("类${clazz}中，@BatchCacheable标注的方法【${method}】，其返回值类型必须是Map！")
        }

        return cacheName
    }

    private fun getKeysGenerator(batchCacheable: BatchCacheable) =
        SpringKit.getBean(batchCacheable.keysGenerator) as IKeysGenerator

    /**
     * 得到所有缓存key
     */
    private fun getAllCacheKeys(
        joinPoint: ProceedingJoinPoint, method: Method, batchCacheable: BatchCacheable
    ): List<String> {
        val keysGenerator = getKeysGenerator(batchCacheable)
        return keysGenerator.generate(joinPoint.target, method, *joinPoint.args)
    }

    /**
     * 读取缓存中存在的数据
     */
    private fun readCachedData(
        keys: List<String>, cacheName: String, batchCacheable: BatchCacheable, result: MutableMap<String, Any?>
    ) {
        keys.forEach {
            val value = CacheKit.getValue(cacheName, it, batchCacheable.valueClass) //TODO 缓存不存在时，怎么防止缓存击穿
            if (value != null) {
                result[it] = value
            }
        }
    }

    /**
     * 读取未在缓存中的数组
     * 没有在缓存中的(参数为集合的，要踢除缓存中读到的部分)，从@BatchCacheable标注的方法读
     */
    private fun readUncachedData(
        result: MutableMap<String, Any?>, joinPoint: ProceedingJoinPoint, batchCacheable: BatchCacheable,
        method: Method, vararg parameterTypes: Class<*>
    ): Map<String, Any?>? {
        val noExistKeys = result.filterValues { it == null }.keys // 不在缓存中的key
        if (noExistKeys.isEmpty()) return null
        val keysGenerator = getKeysGenerator(batchCacheable)
        val delimiter = keysGenerator.getDelimiter()
        val paramIndexes = keysGenerator.getParamIndexes(method, *joinPoint.args)
        parameterTypes.forEachIndexed { index, clazz ->
            val paramValue = joinPoint.args[index]
            if (index in paramIndexes && (paramValue is Collection<*> || paramValue is Array<*>)) {
                val segIndex = paramIndexes.indexOf(index) // 在key中分段索引
                val elemValues = mutableListOf<Any>()
                noExistKeys.forEach {
                    val elemValueStr = it.split(delimiter)[segIndex]
                    var firstElemValue: Any? = null
                    if (paramValue is Collection<*>) {
                        firstElemValue = paramValue.first()
                    } else if (paramValue is Array<*>) {
                        firstElemValue = paramValue.first()
                    }
                    elemValues.add(TypeKit.valueOf(elemValueStr, firstElemValue!!::class))
                }
                var params: Any? = null
                when (clazz) {
                    List::class.java -> params = elemValues
                    Set::class.java -> params = elemValues.toSet()
                    Array<String>::class.java -> params = (elemValues as List<String>).toTypedArray()
                    Array<Char>::class.java -> params = (elemValues as List<Char>).toTypedArray()
                    Array<Boolean>::class.java -> params = (elemValues as List<Boolean>).toTypedArray()
                    Array<Byte>::class.java -> params = (elemValues as List<Byte>).toTypedArray()
                    Array<Short>::class.java -> params = (elemValues as List<Short>).toTypedArray()
                    Array<Int>::class.java -> params = (elemValues as List<Int>).toTypedArray()
                    Array<Long>::class.java -> params = (elemValues as List<Long>).toTypedArray()
                    Array<Float>::class.java -> params = (elemValues as List<Float>).toTypedArray()
                    Array<Double>::class.java -> params = (elemValues as List<Double>).toTypedArray()
                    Array<BigDecimal>::class.java -> params = (elemValues as List<BigDecimal>).toTypedArray()
                    Array<BigInteger>::class.java -> params = (elemValues as List<BigInteger>).toTypedArray()
                }
                joinPoint.args[index] = params // 替换原来的集合或数组参数
            }
        }
        return joinPoint.proceed(joinPoint.args) as Map<String, Any?>
    }


    /**
     * 获取方法中声明的注解
     */
    private fun getMethodAnnotation(joinPoint: JoinPoint): BatchCacheable {
        // 获取方法名
        val methodName = joinPoint.signature.name
        // 反射获取目标类
        val targetClass: Class<*> = joinPoint.target.javaClass
        // 拿到方法对应的参数类型
        val parameterTypes = (joinPoint.signature as MethodSignature).parameterTypes
        // 根据类、方法、参数类型（重载）获取到方法的具体信息
        val objMethod = targetClass.getMethod(methodName, *parameterTypes)
        // 拿到方法定义的注解信息
        return objMethod.getDeclaredAnnotation(BatchCacheable::class.java)
    }

    /**
     * 获取类中声明的注解
     *
     * @param joinPoint
     * @return
     * @throws NoSuchMethodException
     */
    private fun getClassAnnotation(joinPoint: JoinPoint): CacheConfig {
        // 反射获取目标类
        val targetClass: Class<*> = joinPoint.target.javaClass
        return targetClass.getDeclaredAnnotation(CacheConfig::class.java)
    }

}