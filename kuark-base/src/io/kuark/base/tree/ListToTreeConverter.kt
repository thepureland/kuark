package io.kuark.base.tree

import io.kuark.base.log.LogFactory
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
     * @param objectList 结点对象列表
     * @return List(树根结点)
     * @author K
     * @since 1.0.0
     */
    fun <T, E : ITreeable<T>> convert(objectList: List<E>): List<TreeNode<E>> {
        val treeNodeMap = HashMap<T, TreeNode<E>>(objectList.size, 1f)
        for (obj in objectList) {
            treeNodeMap[obj.selfUniqueIdentifier] = TreeNode(obj)
        }
        val treeNodeList = ArrayList<TreeNode<E>>()
        for (obj in objectList) {
            val node = treeNodeMap[obj.selfUniqueIdentifier]!!
            val pId = obj.parentUniqueIdentifier
            if (pId == null || "" == pId) { // 根
                treeNodeList.add(node)
            } else {
                val pNode = treeNodeMap[pId]
                if (pNode != null) { // 存在父结点
                    node.setParentUserObject(pNode.getUserObject()!!)
                    pNode.getChildren().add(node)
                } else {
                    LOG.warn("结点#${node.getUserObject()!!.selfUniqueIdentifier}的父结点#${pId}不存在！")
                }
            }
        }
        return treeNodeList
    }

    /**
     * 将列表结构转为jsTree要求的树结构
     *
     * @param objectList 结点对象列表
     * @return List(树根结点)
     * @author K
     * @since 1.0.0
     */
    fun <E : IJsTreeNode> convertToJsTree(objectList: List<E>): List<E> {
        val treeNodeMap = mutableMapOf<String, E>()
        for (obj in objectList) {
            treeNodeMap[obj.selfUniqueIdentifier!!] = obj
        }
        val treeNodeList = ArrayList<E>()
        for (obj in objectList) {
            val node = treeNodeMap[obj.selfUniqueIdentifier]!!
            val pId = obj.parentUniqueIdentifier
            if (pId == null || "" == pId) { // 根
                treeNodeList.add(node)
            } else {
                val pNode = treeNodeMap[pId]
                // 存在父结点
//					watcher.setParentObject(pNode.getObject());
                pNode?.getChildren()?.add(node)
                    ?: LOG.warn("结点#${node.selfUniqueIdentifier}的父结点#${pId}不存在！")
            }
        }
        return treeNodeList
    }

    /**
     * 将用户对象列表转换为js树结点对象列表
     *
     * @param objectList 用户对象列表
     * @param converter 转换器
     * @return js树结点对象列表
     * @author K
     * @since 1.0.0
     */
    fun <O> convertToJsTree(objectList: List<O>, converter: IJsTreeNodeConverter<O, JsTreeNode>): List<JsTreeNode> {
        val nodes = ArrayList<JsTreeNode>(objectList.size)
        for (t in objectList) {
            nodes.add(converter.convert(t))
        }
        return convertToJsTree(nodes)
    }
}