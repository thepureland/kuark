package io.kuark.service.user.common.user.vo.menupermission

import com.fasterxml.jackson.annotation.JsonInclude
import io.kuark.base.tree.ITreeNode
import java.beans.Transient


open class MenuPermissionTreeNode : ITreeNode<String>, Comparable<MenuPermissionTreeNode> {

    /** id */
    var id: String? = null

    /** 父id */
    @get:Transient
    var parentId: String? = null

    /** 在同父节点下的排序号 */
    @get:Transient
    var seqNo: Int? = null

    /** 名称，或其国际化key */
    var name: String? = null

    /** URL */
    var url: String? = null

    /** 关联的角色 */
    var roleNames: String? = null

    /** 租户id */
    var tenantId: String? = null


    /** 孩子结点 */
    @get:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var children: MutableList<ITreeNode<String>> = mutableListOf()

    override fun _getId(): String = this.id!!

    override fun _getParentId(): String? = parentId

    override fun _getChildren(): MutableList<ITreeNode<String>> = children

    override fun compareTo(other: MenuPermissionTreeNode): Int {
        if (seqNo == null || other.seqNo == null) return 0
        return seqNo!!.compareTo(other.seqNo!!)
    }

}