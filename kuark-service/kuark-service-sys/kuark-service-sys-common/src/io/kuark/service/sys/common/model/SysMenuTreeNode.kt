package io.kuark.service.sys.common.model

import com.fasterxml.jackson.annotation.JsonInclude
import io.kuark.base.tree.ITreeNode

class SysMenuTreeNode(
    /** id */
    val id: String,
    /** 父id */
    val parentId: String?,
    /** 名称，或其国际化key */
    val name: String,
    /** 在同父节点下的排序号 */
    val seqNo: Int?,
    /** 图标 */
    val icon: String?,
    /** url */
    val url: String?
) : ITreeNode<String>, Comparable<SysMenuTreeNode> {

    /** 孩子结点 */
    @get:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val children: MutableList<ITreeNode<String>> = mutableListOf()

    override fun _getId(): String = id

    override fun _getParentId(): String? = parentId

    override fun _getChildren(): MutableList<ITreeNode<String>> = children

    override fun compareTo(other: SysMenuTreeNode): Int {
        if (seqNo == null || other.seqNo == null) return 0
        return seqNo.compareTo(other.seqNo)
    }



}