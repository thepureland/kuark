package org.kuark.base.tree

import org.apache.commons.lang3.mutable.Mutable

interface IJsTreeNode : IListToTreeRestrict<String?> {

    val text: String?
    fun getIcon(): String?
    fun getChildren(): MutableList<IJsTreeNode>
    fun getState(): TreeNodeState?

}