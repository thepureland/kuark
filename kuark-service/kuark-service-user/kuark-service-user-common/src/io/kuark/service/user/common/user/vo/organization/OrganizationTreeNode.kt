package io.kuark.service.user.common.user.vo.organization

import com.fasterxml.jackson.annotation.JsonInclude
import io.kuark.base.tree.ITreeNode
import java.beans.Transient
import java.time.LocalDateTime

open class OrganizationTreeNode : BaseOrganizationTreeNode(), ITreeNode<String>, Comparable<OrganizationTreeNode> {

    /** 简称 */
    var abbrName: String? = null

    /** 组织类型 */
    var orgTypeDictCode: String? = null

    /** 是否启用 */
    var active: Boolean? = null

    /** 创建时间 */
    var createTime: LocalDateTime? = null

    /** 父id */
    @get:Transient
    var parentId: String? = null

    /** 在同父节点下的排序号 */
    var seqNo: Int? = null


    /** 孩子结点 */
    @get:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var children: MutableList<ITreeNode<String>> = mutableListOf()

    override fun _getId(): String = this.id!!

    override fun _getParentId(): String? = parentId

    override fun _getChildren(): MutableList<ITreeNode<String>> = children

    override fun compareTo(other: OrganizationTreeNode): Int {
        if (seqNo == null || other.seqNo == null) return 0
        return seqNo!!.compareTo(other.seqNo!!)
    }

}