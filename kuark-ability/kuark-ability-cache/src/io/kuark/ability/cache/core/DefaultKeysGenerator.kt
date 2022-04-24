package io.kuark.ability.cache.core

import io.kuark.base.support.Consts
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation

/**
 * 默认的批量缓存key生成器
 *
 *
 * 适用条件：
 *
 * 1.缓存方法参数，除集合与数组外，其他类型将被toString()
 *
 * 2.数组需要用Array<Int>、Array<String>等形式，不要使用IntArray、CharArray等形式
 *
 * 3.不支持无参
 *
 *
 * key的组装规则为：
 *
 * 1.由除ignoreParamIndexes指定的参数索引外的所有参数组成
 *
 * 2.各部分以半角冒号分隔
 *
 * 3.各部分顺序同参数顺序
 *
 * 4.总的key个数为各参数元素个数的积，如各参数分别为："1", listOf(2,3,4), arrayOf("5","6"),7，那么组装完的key共6个，分别为：
 *   "1:2:5:7", "1:3:6:7", "1:4:5:7", "1:2:6:7", "1:3:5:7", "1:4:6:7"
 *
 * @author K
 * @since 1.0.0
 */
class DefaultKeysGenerator : IKeysGenerator {

    override fun generate(target: Any?, function: KFunction<*>?, vararg params: Any): List<String> {
        validParamType(*params)
        val totalCount = calTotalCount(*params)
        return generateKeys(function, totalCount, *params)
    }

    override fun getDelimiter(): String = ":"

    override fun getParamIndexes(function: KFunction<*>?, vararg params: Any): List<Int> {
        var ignoreParamIndexes =
            if (function == null) {
                intArrayOf()
            } else {
                val batchCacheable = function.findAnnotation<BatchCacheable>()!!
                batchCacheable.ignoreParamIndexes
            }
        return params.indices.filter { it !in ignoreParamIndexes }
    }

    /**
     * 校验参数的类型
     *
     * @param params 方法参数
     * @author K
     * @since 1.0.0
     */
    private fun validParamType(vararg params: Any) {
        params.forEach {
            if (it is IntArray || it is CharArray || it is ByteArray || it is ShortArray || it is LongArray ||
                it is FloatArray || it is DoubleArray || it is BooleanArray
            ) {
                error("缓存方法参数如果是数组，请使用Array<Any>类型，不支持IntArray、CharArray等类型！")
            }
        }
    }

    /**
     * 计算key的总个数
     *
     * @param params 方法参数
     * @return key的总数
     * @author K
     * @since 1.0.0
     */
    private fun calTotalCount(vararg params: Any): Int {
        var totalCount = 1
        params.forEach {
            val count = when (it) {
                is Collection<*> -> it.size
                is Array<*> -> it.size
                else -> 1
            }
            totalCount *= count
        }
        return totalCount
    }

    /**
     * 生成key
     * 算法：
     * 1.将每个参数都转换为totalCount大小的List
     * 2.以原来参数的元素为分组，重复分组以补足List的大小到totalCount
     * 3.迭代各参数对应的List，将相同下标的元素组装成一个完整的key
     *
     * @param function 方法
     * @param totalCount 要生成的key总数
     * @param params 方法参数
     * @return key列表
     * @author K
     * @since 1.0.0
     */
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    private fun generateKeys(function: KFunction<*>?, totalCount: Int, vararg params: Any): List<String> {
        val keys = mutableListOf<List<Any>>() // List<List<key同一分段的部分>>
        val paramIndexes = getParamIndexes(function, *params)
        params.filterIndexed { index, _ -> index in paramIndexes }.forEach {
            val parts = mutableListOf<Any>()
            when (it) {
                is Collection<*> -> {
                    if (it.size != 0) {
                        val groupCount = totalCount / it.size
                        for (group in 0 until groupCount) {
                            parts.addAll(it as Collection<Any>)
                        }
                    }
                }
                is Array<*> -> {
                    if (it.size != 0) {
                        val groupCount = totalCount / it.size
                        for (group in 0 until groupCount) {
                            parts.addAll(it as Array<Any>)
                        }
                    }
                }
                else -> {
                    for (group in 0 until totalCount) {
                        parts.add(it)
                    }
                }
            }
            keys.add(parts)
        }

        val result = mutableListOf<String>()
        for (index in 0 until totalCount) {
            val sb = StringBuilder()
            keys.forEachIndexed { segIndex, _ -> sb.append(keys[segIndex][index]).append(getDelimiter()) }
            result.add(sb.substring(0, sb.length - 1))
        }
        return result
    }

}