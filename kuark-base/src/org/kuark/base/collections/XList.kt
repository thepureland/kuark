package org.kuark.base.collections

import org.apache.commons.collections.Factory
import org.apache.commons.collections.ListUtils

// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
// 封装org.apache.commons.collections.ListUtils
// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

/**
 * 返回一个"懒惰"的列表， 它的元素将被按需加载
 *
 * 当传给返回列表的[get][List.get]方法的下标参数大于当前列表大小时，
 * 指定的工厂将创建一个新对象， 并将它插到列表的指定下标处。
 *
 * 例如：
 *
 * <pre>
 * val factory = object: Factory() {
 *  override fun create(): Any {
 *  return Date()
 * }
 * }
 * val lazy = ArrayList().lazyList(factory)
 * val obj = lazy.get(3)
 * </pre>
 * 当上面的代码被执行时，`obj`将包含一个新的`Date`实例。
 * 而且， 这个`Date`实例为列表中的第四个元素。前三个元素将被置为`null`。
 *
 * @param factory 创建新对象的工厂
 * @return 指定列表的"懒惰"列表
 * @since 1.0.0
 */
fun <T : Any?> List<T>.lazyList(factory: Factory): List<T> = ListUtils.lazyList(this, factory) as List<T>

// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// 封装org.apache.commons.collections.ListUtils
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^