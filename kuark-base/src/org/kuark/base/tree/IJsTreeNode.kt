package org.kuark.base.tree

/**
 * js树结点接口
 *
 * @author K
 * @since 1.0.0
 */
interface IJsTreeNode : IListToTreeRestrict<String?> {

    val text: String?
    fun getIcon(): String?
    fun getChildren(): MutableList<IJsTreeNode>
    fun getState(): TreeNodeState?

}