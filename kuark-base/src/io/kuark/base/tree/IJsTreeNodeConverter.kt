package io.kuark.base.tree

/**
 * js树结点转换器接口
 *
 * @param O 结点用户对象类型
 * @param N 结点对象类型
 * @author K
 * @since 1.0.0
 */
interface IJsTreeNodeConverter<O, N : IJsTreeNode?> {

    /**
     * 将用户对象转换为js树结点对象
     *
     * @param userObject 用户对象
     * @return js树结点对象
     * @author K
     * @since 1.0.0
     */
    fun convert(userObject: O): N

}