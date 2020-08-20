package io.kuark.cache.core

import java.lang.reflect.Method

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
     * @param method 被调用的方法
     * @param params 方法参数
     * @return 生成的key列表
     */
    fun generate(target: Any?, method: Method?, vararg params: Any): List<String>

    /**
     * 返回key各部分的分隔符
     *
     * @return 分隔符
     */
    fun getDelimiter(): String

    /**
     * 按顺序返回组成key的参数的索引
     *
     * @param method 方法
     * @param params 方法参数
     * @return 参数索引列表
     */
    fun getParamIndexes(method: Method?,vararg params: Any): List<Int>

}