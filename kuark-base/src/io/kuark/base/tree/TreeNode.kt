package io.kuark.base.tree

import java.io.Serializable

/**
 * 通用树结点
 *
 * @author K
 * @since 1.0.0
 */
class TreeNode<T> : Serializable {

    private var userObject: T? = null // 当前结点用户对象
    private var parentUserObject: T? = null// 父结点用户对象
    private var children = mutableListOf<TreeNode<T>>() // 孩子结点集合

    /**
     * 空构造器
     */
    constructor() {}

    /**
     * 构造器
     *
     * @param userObject 当前结点对象
     */
    constructor(userObject: T) {
        this.userObject = userObject
    }

    /**
     * 构造器
     *
     * @param userObject 当前结点对象
     * @param parentObject 父结点对象
     */
    constructor(userObject: T, parentObject: T) {
        this.userObject = userObject
        this.parentUserObject = parentObject
    }

    fun getUserObject(): T? = userObject

    fun setObject(userObject: T) {
        this.userObject = userObject
    }

    fun getParentObject(): T? = parentUserObject

    fun setParentUserObject(parentObject: T) {
        this.parentUserObject = parentObject
    }

    fun getChildren(): MutableList<TreeNode<T>> = children

    fun setChildren(children: MutableList<TreeNode<T>>) {
        this.children = children
    }

    fun isLeaf(): Boolean = children.isEmpty()

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (userObject == null) 0 else userObject.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null) {
            return false
        }
        if (javaClass != other.javaClass) {
            return false
        }
        val other = other as TreeNode<T?>
        if (userObject == null) {
            if (other.userObject != null) {
                return false
            }
        } else if (userObject != other.userObject) {
            return false
        }
        return true
    }

    companion object {
        private const val serialVersionUID = -7315465823402887866L
    }
}