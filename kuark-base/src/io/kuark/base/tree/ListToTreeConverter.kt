package io.kuark.base.tree

import io.kuark.base.lang.collections.CollectionKit
import io.kuark.base.log.LogFactory
import io.kuark.base.query.sort.Direction
import io.kuark.base.support.ICallback
import java.util.*

/**
 * 列表到树结构的转换器
 *
 * @author K
 * @since 1.0.0
 */
object ListToTreeConverter {

    private val LOG = LogFactory.getLog(ListToTreeConverter::class)


    /**
     * 将列表结构转为树结构
     *
     * @param T 惟一标识的类型
     * @param E 树结点类型
     * @param treeNodeList 结点对象列表
     * @param direction 排序，指定排序时E必须实现Comparable接口，为null将不做排序，默认为null
     * @param callback 结点挂载后的回调
     * @return List(树根结点)
     * @author K
     * @since 1.0.0
     */
    fun <T, E : ITreeNode<T>> convert(
        treeNodeList: List<E>,
        direction: Direction? = null,
        callback: ICallback<E, Unit>? = null
    ): List<E> {
        val treeNodeMap = HashMap<T, E>(treeNodeList.size, 1f)
        for (obj in treeNodeList) {
            treeNodeMap[obj._getId()] = obj
        }
        val nodeList = ArrayList<E>()
        for (obj in treeNodeList) {
            val node = treeNodeMap[obj._getId()]!!
            val pId = obj._getParentId()
            if (pId == null || "" == pId) { // 根
                nodeList.add(node)
                callback?.execute(node)
            } else {
                val pNode = treeNodeMap[pId]
                if (pNode != null) { // 存在父结点
                    pNode._getChildren().add(node)
                } else {
                    LOG.warn("结点#${node._getId()}的父结点#${pId}不存在！")
                }
            }
        }

        // 排序
        if (direction != null && treeNodeList.isNotEmpty()) {
            return sort(nodeList, direction)
        }

        return nodeList
    }


    @Suppress("UNCHECKED_CAST")
    private fun <T, E : ITreeNode<T>> sort(nodes: List<E>, direction: Direction): List<E> {
        if (nodes.first() !is Comparable<*>) {
            error("类${nodes.first()::class.simpleName}必须实现Comparable接口！")
        }

        val nodeList = nodes.sortedWith { o1, o2 ->
            val result = (o1 as Comparable<E>).compareTo(o2)
            if (direction == Direction.ASC) result else 0 - result
        }

        nodeList.forEach {
            val origChildren = it._getChildren()
            if (CollectionKit.isNotEmpty(origChildren)) {
                val children = sort(origChildren as List<E>, direction)
                (origChildren as MutableList).clear()
                origChildren.addAll(children)
            }
        }

        return nodeList
    }

}