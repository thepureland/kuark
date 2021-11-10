package io.kuark.ability.cache.core

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.base.lang.string.toType
import io.kuark.base.support.Consts
import io.kuark.context.kit.SpringKit
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.cache.annotation.CacheConfig
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.jvm.kotlinFunction


/**
 * 批量缓存的切面
 *
 * @author K
 * @since 1.0.0
 */
@Aspect
@Component
@Lazy(false)
@ConditionalOnBean(MixCacheManager::class)
class BatchCacheableAspect {

    /**
     * 定义切入点
     *
     * @author K
     * @since 1.0.0
     */
    @Pointcut("@annotation(io.kuark.ability.cache.core.BatchCacheable)")
    private fun cut() {
        // do nothing
    }

    /**
     * 环绕通知
     *
     * @return Map(缓存key, 缓存值)
     * @author K
     * @since 1.0.0
     */
    @Around("cut()")
    fun around(joinPoint: ProceedingJoinPoint): Map<String, Any?> {
        val result = linkedMapOf<String, Any?>() // Map<缓存key, 缓存值>

        // 拿到方法定义的注解信息
        val function = (joinPoint.signature as MethodSignature).method.kotlinFunction!!
        val batchCacheable = function.findAnnotation<BatchCacheable>()!!

        // 校验约束
        val cacheName = validate(joinPoint, function, batchCacheable)

        // 得到所有缓存key
        val keys = getAllCacheKeys(joinPoint, function, batchCacheable)
        keys.forEach { result[it] = null } // 保证顺序

        // 读取缓存中存在的数据
        readCachedData(keys, cacheName, batchCacheable, result)

        // 没有在缓存中的(参数为集合的，要踢除缓存中读到的部分)，从@BatchCacheable标注的方法读
        val data = readUncachedData(result, joinPoint, function, batchCacheable)

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
     *
     * @param joinPoint 切入点
     * @param function BatchCacheable所标注的方法
     * @param batchCacheable BatchCacheable注解
     * @return 缓存名称
     * @author K
     * @since 1.0.0
     */
    private fun validate(joinPoint: ProceedingJoinPoint, function: KFunction<*>, batchCacheable: BatchCacheable): String {
        var cacheName: String? = null
        val clazz = joinPoint.target::class

        // 校验缓存名称是否配置
        val cacheConfig = clazz.findAnnotation<CacheConfig>() // 读取类注解
        if (batchCacheable.cacheNames.isNotEmpty()) {
            cacheName = batchCacheable.cacheNames.first()
        } else if (cacheConfig != null && cacheConfig.cacheNames.isNotEmpty()) {
            cacheName = cacheConfig.cacheNames.first()
        }
        if (cacheName == null) {
            error("cacheNames未设置！请在类${clazz}上通过@CacheConfig指定，或在方法${function}上通过@BatchCacheable指定！")
        }

        // 验证方法返回值类型
        if (!Map::class.isSuperclassOf(function.returnType.classifier as KClass<*>)) {
            error("类${clazz}中，@BatchCacheable标注的方法【${function}】，其返回值类型必须是Map！")
        }

        return cacheName
    }

    private fun getKeysGenerator(batchCacheable: BatchCacheable) =
        SpringKit.getBean(batchCacheable.keysGenerator) as IKeysGenerator

    /**
     * 得到所有缓存key
     *
     * @param joinPoint 切入点
     * @param function BatchCacheable所标注的方法
     * @param batchCacheable BatchCacheable注解
     * @return List(缓存key)
     * @author K
     * @since 1.0.0
     */
    private fun getAllCacheKeys(
        joinPoint: ProceedingJoinPoint, function: KFunction<*>, batchCacheable: BatchCacheable
    ): List<String> {
        val keysGenerator = getKeysGenerator(batchCacheable)
        return keysGenerator.generate(joinPoint.target, function, *joinPoint.args)
    }

    /**
     * 读取缓存中存在的数据
     *
     * @param keys List(缓存key)
     * @param batchCacheable BatchCacheable注解
     * @param result Map(缓存key，缓存值)
     * @author K
     * @since 1.0.0
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
     * 读取未在缓存中的数据
     *
     * 没有在缓存中的(参数为集合的，要踢除缓存中读到的部分)，从@BatchCacheable标注的方法读
     *
     * @param result 已缓存的数据，Map(缓存key，缓存值)
     * @param joinPoint 切入点
     * @param function BatchCacheable所标注的方法
     * @param batchCacheable BatchCacheable注解
     * @return 未在缓存中的数据，Map(缓存key，缓存值)
     * @author K
     * @since 1.0.0
     */
    @Suppress(Consts.SUPPRESS_UNCHECKED_CAST)
    private fun readUncachedData(
        result: MutableMap<String, Any?>, joinPoint: ProceedingJoinPoint, function: KFunction<*>,
        batchCacheable: BatchCacheable
    ): Map<String, Any?>? {
        val noExistKeys = result.filterValues { it == null }.keys // 不在缓存中的key
        if (noExistKeys.isEmpty()) return null
        val keysGenerator = getKeysGenerator(batchCacheable)
        val delimiter = keysGenerator.getDelimiter()
        val paramIndexes = keysGenerator.getParamIndexes(function, *joinPoint.args)
        val parameterTypes = (joinPoint.signature as MethodSignature).parameterTypes
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
                    elemValues.add(elemValueStr.toType(firstElemValue!!::class))
                }
                var params: Any? = null
                when (clazz.kotlin) {
                    List::class -> params = elemValues
                    Set::class -> params = elemValues.toSet()
                    Array<String>::class -> params = (elemValues as List<String>).toTypedArray()
                    Array<Char>::class -> params = (elemValues as List<Char>).toTypedArray()
                    Array<Boolean>::class -> params = (elemValues as List<Boolean>).toTypedArray()
                    Array<Byte>::class -> params = (elemValues as List<Byte>).toTypedArray()
                    Array<Short>::class -> params = (elemValues as List<Short>).toTypedArray()
                    Array<Int>::class -> params = (elemValues as List<Int>).toTypedArray()
                    Array<Long>::class -> params = (elemValues as List<Long>).toTypedArray()
                    Array<Float>::class -> params = (elemValues as List<Float>).toTypedArray()
                    Array<Double>::class -> params = (elemValues as List<Double>).toTypedArray()
                    Array<BigDecimal>::class -> params = (elemValues as List<BigDecimal>).toTypedArray()
                    Array<BigInteger>::class -> params = (elemValues as List<BigInteger>).toTypedArray()
                }
                joinPoint.args[index] = params // 替换原来的集合或数组参数
            }
        }
        return joinPoint.proceed(joinPoint.args) as Map<String, Any?>
    }

}