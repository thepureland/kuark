package io.kuark.base.support

import java.util.*
import kotlin.math.ceil

/**
 * 分组执行器
 *
 *
 * <pre>
 *
 * val elems: Collection<Any> = ...
 * val groupSize: Int = ...
 * object : GroupExecutor<Any>(elems, groupSize) {
 *
 * override fun groupExecute(subList: List<Any>) {
 * ... // 如分组执行更新
 * }
 *
 * }.execute()
 *
 * </pre>
 *
 * @param E 集合元素类型
 * @author admin
 * @since 1.0.0
 *
 */
abstract class GroupExecutor<E>(
    private val elems: Collection<E>,
    private val groupSize: Int = 1000 // 分组大小默认为1000
) {

    /**
     * 执行操作
     *
     * @author admin
     * @since 1.0.0
     */
    fun execute() {
        val size = elems.size
        val groupCount = ceil(size / groupSize.toDouble()).toInt()
        val elemList: List<E> = ArrayList(elems)
        for (index in 0 until groupCount) {
            val from = index * groupSize
            val end = if (index == groupCount - 1) elemList.size else from + groupSize
            val subList = elemList.subList(from, end)
            groupExecute(subList)
        }
    }

    /**
     * 分组执行
     *
     * @param subList 分组元素列表
     * @author admin
     * @since 1.0.0
     */
    abstract fun groupExecute(subList: List<E>)

}