package io.kuark.base.tree

/**
 * js树结点接口
 *
 * @author K
 * @since 1.0.0
 */
interface IJsTreeNode : ITreeable<String?> {

    /**
     * 结点文本
     */
    val text: String

    /**
     * 结点图标url
     */
    fun getIcon(): String?

    /**
     * 孩子结点
     */
    fun getChildren(): MutableList<IJsTreeNode>

    /**
     * 结点状态
     */
    fun getState(): TreeNodeState?

}