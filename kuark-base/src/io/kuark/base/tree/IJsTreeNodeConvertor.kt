package io.kuark.base.tree

/**
 * js树结点转换器接口
 *
 * @author K
 * @since 1.0.0
 */
interface IJsTreeNodeConvertor<O, N : IJsTreeNode?> {

    fun converter(obj: O): N

}