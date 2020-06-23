package org.kuark.base.tree

import java.io.Serializable
import java.util.*

/**
 * 通用树结点
 *
 * @since 1.0.0
 * @author admin
 * @time 2012-5-1 下午9:24:05
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

    fun getObject(): T? {
        return `object`
    }

    fun setObject(`object`: T) {
        this.`object` = `object`
    }

    fun getParentObject(): T? {
        return parentObject
    }

    fun setParentObject(parentObject: T) {
        this.parentObject = parentObject
    }

    fun getChildren(): MutableList<TreeNode<T>> {
        return children
    }

    fun setChildren(children: MutableList<TreeNode<T>>) {
        this.children = children
    }

    fun isLeaf(): Boolean {
        return children.isEmpty()
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (`object` == null) 0 else `object`.hashCode()
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }
        if (obj == null) {
            return false
        }
        if (javaClass != obj.javaClass) {
            return false
        }
        val other = obj as TreeNode<T?>
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