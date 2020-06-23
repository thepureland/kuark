package org.kuark.base.tree

/**
 * Create by (admin) on 6/11/15.
 */
interface IJsTreeNodeConvertor<O, N : IJsTreeNode?> {
    fun converter(obj: O): N
}