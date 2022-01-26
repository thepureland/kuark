package io.kuark.service.user.common.user.vo.organization

import com.fasterxml.jackson.annotation.JsonInclude
import io.kuark.base.support.result.IJsonResult
import io.kuark.base.tree.ITreeNode
import java.beans.Transient


/**
 * 基础的组织机构树结点，用于前端展现
 *
 * @author K
 * @since 1.0.0
 */
open class BaseOrganizationTreeNode: IJsonResult, ITreeNode<String>, Comparable<BaseOrganizationTreeNode> {

    /** 名称 */
    var name: String? = null

    /** id */
    var id: String? = null
    /** 父id */
    @get:Transient
    var parentId: String? = null
    /** 在同父节点下的排序号 */
    @get:Transient
    var seqNo: Int? = null


    /** 孩子结点 */
    @get:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var children: MutableList<ITreeNode<String>> = mutableListOf()

    override fun _getId(): String = this.id!!

    override fun _getParentId(): String? = parentId

    override fun _getChildren(): MutableList<ITreeNode<String>> = children

    override fun compareTo(other: BaseOrganizationTreeNode): Int {
        if (seqNo == null || other.seqNo == null) return 0
        return seqNo!!.compareTo(other.seqNo!!)
    }

}