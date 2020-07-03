package org.kuark.base.tree

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class JsTreeNode(
    override var selfUniqueIdentifier: String?,
    override var text: String?,
    override var parentUniqueIdentifier: String?
) : IJsTreeNode {

    private var icon: String? = null
    private var children: MutableList<IJsTreeNode> = mutableListOf()
    private var state: TreeNodeState? = null

    constructor(id: String?, text: String?, parentId: String?, state: TreeNodeState?) : this(id, text, parentId) {
        this.state = state
    }


    override fun getIcon(): String? {
        return icon
    }

    override fun getChildren(): MutableList<IJsTreeNode> {
        return children
    }

    override fun getState(): TreeNodeState? {
        return state
    }

    fun setIcon(icon: String?) {
        this.icon = icon
    }

    fun setState(state: TreeNodeState?) {
        this.state = state
    }

    fun setChildren(children: MutableList<IJsTreeNode>) {
        this.children = children
    }

    companion object {
        private const val serialVersionUID = -6972101037527773627L
    }

}