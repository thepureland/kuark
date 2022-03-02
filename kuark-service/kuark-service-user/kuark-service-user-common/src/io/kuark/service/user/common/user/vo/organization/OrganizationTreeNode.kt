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

    /** 在同父节点下的排序号 */
    var seqNo: Int? = null

    override fun compareTo(other: OrganizationTreeNode): Int {
        if (seqNo == null || other.seqNo == null) return 0
        return seqNo!!.compareTo(other.seqNo!!)
    }

}