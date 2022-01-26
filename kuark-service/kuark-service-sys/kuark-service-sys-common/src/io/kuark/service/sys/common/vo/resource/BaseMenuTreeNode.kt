package io.kuark.service.sys.common.vo.resource

import com.fasterxml.jackson.annotation.JsonInclude
import io.kuark.base.support.result.IJsonResult
import io.kuark.base.tree.ITreeNode
import java.beans.Transient


/**
 * 基础的系统菜单树结点，用于前端菜单的展现
 *
 * @author K
 * @since 1.0.0
 */
open class BaseMenuTreeNode: IJsonResult, ITreeNode<String>, Comparable<BaseMenuTreeNode> {

    /** 名称，或其国际化key */
    var title: String? = null

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

    override fun compareTo(other: BaseMenuTreeNode): Int {
        if (seqNo == null || other.seqNo == null) return 0
        return seqNo!!.compareTo(other.seqNo!!)
    }

}