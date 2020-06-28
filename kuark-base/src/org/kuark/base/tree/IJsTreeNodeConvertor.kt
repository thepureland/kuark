package org.kuark.base.tree

interface IJsTreeNodeConvertor<O, N : IJsTreeNode?> {
    fun converter(obj: O): N
}