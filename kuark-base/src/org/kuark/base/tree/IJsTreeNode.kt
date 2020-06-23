package org.kuark.base.tree

import org.apache.commons.lang3.mutable.Mutable

/**
 * Create by (admin) on 6/11/15.
 */
interface IJsTreeNode : IListToTreeRestrict<String?> {
    //    id			: tid,
    //    text		: d.text || '',
    //    icon		: d.icon !== undefined ? d.icon : true,
    //    parent		: p,
    //    parents		: ps,
    //    children	: d.children || [],
    //    children_d	: d.children_d || [],
    //    data		: d.data,
    //    state		: { },
    //    li_attr		: { id : false },
    //    a_attr		: { href : '#' },
    //    original	: false
    val text: String?
    fun getIcon(): String?
    fun getChildren(): MutableList<IJsTreeNode>
    fun getState(): TreeNodeState?
}