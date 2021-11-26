package io.kuark.ability.sys.common.vo.reg.resource

import com.fasterxml.jackson.annotation.JsonInclude
import io.kuark.base.tree.ITreeNode
import java.beans.Transient


/**
 * 系统菜单树结点，用于前端菜单的展现
 *
 * @author K
 * @since 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class MenuTreeNode(
    /** 名称，或其国际化key */
    val title: String,
    /** url */
    val index: String?,
    /** 图标 */
    val icon: String?,

    /** id */
    @get:Transient
    val id: String,
    /** 父id */
    @get:Transient
    val parentId: String?,
    /** 在同父节点下的排序号 */
    @get:Transient
    val seqNo: Int?,
) : ITreeNode<String>, Comparable<MenuTreeNode> {

    /** 孩子结点 */
    val subs: MutableList<ITreeNode<String>> = mutableListOf()

    override fun _getId(): String = id

    override fun _getParentId(): String? = parentId

    override fun _getChildren(): MutableList<ITreeNode<String>> = subs

    override fun compareTo(other: MenuTreeNode): Int {
        if (seqNo == null || other.seqNo == null) return 0
        return seqNo.compareTo(other.seqNo)
    }

}