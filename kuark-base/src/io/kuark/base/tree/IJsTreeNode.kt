package io.kuark.base.tree

/**
 * js树结点接口
 *
 * @author K
 * @since 1.0.0
 */
interface IJsTreeNode : ITreeable<String?> {

    val text: String?
    fun getIcon(): String?
    fun getChildren(): MutableList<IJsTreeNode>
    fun getState(): TreeNodeState?

}