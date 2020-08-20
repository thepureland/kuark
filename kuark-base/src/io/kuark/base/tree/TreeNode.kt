package io.kuark.base.tree

import java.io.Serializable
import java.util.*

/**
 * 通用树结点
 *
 * @author K
 * @since 1.0.0
 */
class TreeNode<T> : Serializable {

    private var `object`: T? = null // 当前结点对象
    private var parentObject: T? = null// 父结点对象
    private var children: MutableList<TreeNode<T>> = ArrayList(0) // 孩子结点集合

    /**
     * 空构造器
     */
    constructor() {}

    /**
     * 构造器
     *
     * @param object 当前结点对象
     */
    constructor(`object`: T) {
        this.`object` = `object`
    }

    /**
     * 构造器
     *
     * @param object 当前结点对象
     * @param parentObject 父结点对象
     */
    constructor(`object`: T, parentObject: T) {
        this.`object` = `object`
        this.parentObject = parentObject
    }

    fun getObject(): T? = `object`

    fun setObject(`object`: T) {
        this.`object` = `object`
    }

    fun getParentObject(): T? = parentObject

    fun setParentObject(parentObject: T) {
        this.parentObject = parentObject
    }

    fun getChildren(): MutableList<TreeNode<T>> = children

    fun setChildren(children: MutableList<TreeNode<T>>) {
        this.children = children
    }

    fun isLeaf(): Boolean = children.isEmpty()

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (`object` == null) 0 else `object`.hashCode()
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
        if (`object` == null) {
            if (other.`object` != null) {
                return false
            }
        } else if (`object` != other.`object`) {
            return false
        }
        return true
    }

    companion object {
        private const val serialVersionUID = -7315465823402069017L
    }
}