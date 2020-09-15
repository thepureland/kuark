package io.kuark.cache.core

import kotlin.reflect.KFunction

/**
 * 批量缓存key生成器
 *
 * @author K
 * @since 1.0.0
 */
interface IKeysGenerator {

    /**
     * 通过给定的方法和参数生成缓存的key
     *
     * @param target 目标实例
     * @param function 被调用的方法
     * @param params 方法参数
     * @return 生成的key列表
     * @author K
     * @since 1.0.0
     */
    fun generate(target: Any?, function: KFunction<*>?, vararg params: Any): List<String>

    /**
     * 返回key各部分的分隔符
     *
     * @return 分隔符
     * @author K
     * @since 1.0.0
     */
    fun getDelimiter(): String

    /**
     * 按顺序返回组成key的参数的索引
     *
     * @param function 方法
     * @param params 方法参数
     * @return 参数索引列表
     * @author K
     * @since 1.0.0
     */
    fun getParamIndexes(function: KFunction<*>?, vararg params: Any): List<Int>

}