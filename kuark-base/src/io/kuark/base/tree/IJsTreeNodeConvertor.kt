package io.kuark.base.tree

/**
 * js树结点转换器接口
 *
 * @author K
 * @since 1.0.0
 */
interface IJsTreeNodeConvertor<O, N : IJsTreeNode?> {

    /**
     * 将用户对象转换为js树结点对象
     *
     * @param userObject 用户对象
     * @return js树结点对象
     */
    fun converter(userObject: O): N

}