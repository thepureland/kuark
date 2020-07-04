package org.kuark.base.tree

import org.kuark.base.log.LogFactory
import java.util.*

/**
 * 列表到树结构的转换器
 *
 * @author K
 * @since 1.0.0
 */
object ListToTreeConvertor {

    private val LOG = LogFactory.getLog(ListToTreeConvertor::class)

    /**
     * 将列表结构转为树结构
     *
     * @param objectList 结点对象列表
     * @return List<树根结点>
    </树根结点> */
    fun <T, E : IListToTreeRestrict<T>> convert(objectList: List<E>): List<TreeNode<E>> {
        val treeNodeMap = HashMap<T, TreeNode<E>>(objectList.size, 1f)
        for (`object` in objectList) {
            treeNodeMap[`object`.selfUniqueIdentifier] = TreeNode(`object`)
        }
        val treeNodeList = ArrayList<TreeNode<E>>()
        for (obj in objectList) {
            val node = treeNodeMap[obj.selfUniqueIdentifier]
            val pId = obj!!.parentUniqueIdentifier
            if (pId == null || "" == pId) { // 根
                treeNodeList.add(node!!)
            } else {
                val pNode = treeNodeMap[pId]
                if (pNode != null) { // 存在父结点
                    node!!.setParentObject(pNode.getObject()!!)
                    pNode.getChildren().add(node)
                } else {
                    LOG.warn("结点#" + node!!.getObject()!!.selfUniqueIdentifier + "的父结点#" + pId + "不存在！")
                }
            }
        }
        return treeNodeList
    }

    /**
     * 将列表结构转为jsTree要求的树结构
     *
     * @param objectList 结点对象列表
     * @return List<树根结点>
     */
    fun <E : IJsTreeNode> convertToJsTree(objectList: List<E>): List<E> {
        val treeNodeMap = HashMap<String, E>(objectList.size, 1f)
        for (`object` in objectList) {
            treeNodeMap[`object`.selfUniqueIdentifier!!] = `object`
        }
        val treeNodeList = ArrayList<E>()
        for (obj in objectList) {
            val node = treeNodeMap[obj.selfUniqueIdentifier]
            val pId = obj.parentUniqueIdentifier
            if (pId == null || "" == pId) { // 根
                treeNodeList.add(node!!)
            } else {
                val pNode = treeNodeMap[pId]
                // 存在父结点
//					watcher.setParentObject(pNode.getObject());
                pNode?.getChildren()?.add(node!!)
                    ?: LOG.warn("结点#" + node!!.selfUniqueIdentifier + "的父结点#" + pId + "不存在！")
            }
        }
        return treeNodeList
    }

    /**
     *
     *
     * @param objectList
     * @param convertor
     * @param <O>
     * @return
    </O> */
    fun <O> convertToJsTree(objectList: List<O>, convertor: IJsTreeNodeConvertor<O, JsTreeNode>): List<JsTreeNode?> {
        val nodes = ArrayList<JsTreeNode>(objectList.size)
        for (t in objectList) {
            nodes.add(convertor.converter(t))
        }
        return convertToJsTree(nodes)
    }
}