package io.kuark.base.support

import kotlin.math.ceil

/**
 * 分组执行器
 *
 *
 * <pre>
 *
 * val elems: Collection<Any> = ...
 * val groupSize: Int = ...
 * GroupExecutor(elems, groupSize) {
 *   ... // 如分组执行更新
 * }.execute()
 *
 * </pre>
 *
 * @param E 集合元素类型
 * @author K
 * @since 1.0.0
 *
 */
class GroupExecutor<E>(
    /** 待分组的集合 */
    private val elems: Collection<E>,
    /** 每个分组大小默认为1000 */
    private val groupSize: Int = 1000,
    /** 每个分组执行的操作 */
    private val operation: (List<E>) -> Unit
) {

    /**
     * 执行操作
     *
     * @author K
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
            operation(subList)
        }
    }


}