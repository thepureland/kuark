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
     *
     * @author K
     * @since 1.0.0
     */
    val text: String

    /**
     * 结点图标url
     *
     * @author K
     * @since 1.0.0
     */
    fun getIcon(): String?

    /**
     * 孩子结点
     *
     * @author K
     * @since 1.0.0
     */
    fun getChildren(): MutableList<IJsTreeNode>

    /**
     * 结点状态
     *
     * @author K
     * @since 1.0.0
     */
    fun getState(): TreeNodeState?

}