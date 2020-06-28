package org.kuark.base.collections

import org.apache.commons.collections.Factory
import org.apache.commons.collections.ListUtils

/**
 * List工具类
 *
 * @since 1.0.0
 * @author K
 */
object ListKit {


    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.collections.ListUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

    /**
     * 测试两个列表是否相等， 包括包含的元素、元素顺序和列表大小
     *
     * 该方法对于不能通过继承AbstractList类实现`List`时非常有用。
     * 该方法参数类型为Collection是为了确保其它容器类型能够使用列表的实现算法。
     *
     * 该方法比较两个列表元素的相等性。当且仅当两个列表的大小相等，并且两个列表中 所有对应元素相等时返回<tt>true</tt>。两个元素
     * <tt>e1</tt> 和 <tt>e2</tt>当
     * <tt>(e1==null ? e2==null : e1.equals(e2))为true时是相等的。换句话说，
     * 如果它们包含相同元素并且顺序是相同的时返回true. 该定义确保equals方法在
     * 不同<tt>List</tt>的实现中都能正确工作。
     *
     * **注意:** 如果在相等比较的过程中列表被修改， 那么该方法的行为将是不确定的。
     *
     * @param list1 第一个列表, 可以为null
     * @param list2 第二个列表, 可以为null
     * @return 是否两个列表相等
     * @since 1.0.0
     */
    fun isEqualList(list1: Collection<*>?, list2: Collection<*>?): Boolean = ListUtils.isEqualList(list1, list2)

    /**
     * 为指定列表生成哈希值(使用[List.hashCode]指定的算法)
     *
     * 该方法对于不能通过继承AbstractList类实现`List`时非常有用。
     * 该方法参数类型为Collection是为了确保其它容器类型能够使用列表的实现算法。
     *
     * @param list 要生成哈希值的列表， 可以为null
     * @return 指定列表的哈希值，如果列表为null将返回0
     * @since 1.0.0
     */
    fun hashCodeForList(list: Collection<*>?): Int = ListUtils.hashCodeForList(list)

    // -----------------------------------------------------------------------
    /**
     * 返回一个列表, 它的元素存在于`collection`中同时也在`retain`中.
     * 返回的容器的元素的基数与`collection`元素的基数一样, 除非 `retain`
     * 没有包含这个元素, 此时, 基数为0. 该方法在你想更改一个容器但又不能调用该容器的retainAll(retain)方法时非常有用.
     *
     * @param collection 其内容为#retailAll操作的目标的容器
     * @param retain 返回的容器中可能包含的元素的容器
     * @return 一个由所有在`collection`中同时至少出现一次在`retain`
     * 的元素组成的列表
     * @since 1.0.0
     */
    fun retainAll(collection: Collection<*>, retain: Collection<*>): List<*> = ListUtils.retainAll(collection, retain)

    /**
     * 从`collection`中移除`remove`中的元素. 也就是说, 该方法返回一个由不在
     * `remove`中元素组成的列表. 返回的容器的元素的基数与 `collection`元素的基数一样,
     * 除非`remove`包含这个元素, 此时, 基数为0.
     * 该方法在你想更改一个容器但又不能调用该容器的removeAll(retain)方法时非常有用.
     *
     * @param collection 要被移除元素的容器
     * @param remove 要移除的元素的容器
     * @return 一个由`collection`容器中所有除了在`remove`出现的元素组成的列表
     * @since 1.0.0
     */
    fun removeAll(collection: Collection<*>, remove: Collection<*>): List<*> = ListUtils.removeAll(collection, remove)

    /**
     * 返回指定的列表的同步列表
     * 您必须手动同步返回的缓冲区的迭代器，以避免不确定性的行为:
     *
     * <pre>
     * List list = ListUtils.synchronizedList(myList);
     * synchronized (list) {
     * Iterator i = list.iterator();
     * while (i.hasNext()) {
     * process(i.next());
     * }
     * }
     * </pre>
     *
     * 该方法使用在装饰器子包中的实现.
     *
     * @param list 要同步的列表, 不能为null
     * @return 同步的列表
     * @since 1.0.0
     */
    fun <T : Any?> synchronizedList(list: List<T>): List<T> = ListUtils.synchronizedList(list) as List<T>

    /**
     * 返回一个"懒惰"的列表， 它的元素将被按需加载
     *
     * 当传给返回列表的[get][List.get]方法的下标参数大于当前列表大小时，
     * 指定的工厂将创建一个新对象， 并将它插到列表的指定下标处。
     *
     * 例如：
     *
     * <pre>
     * Factory factory = new Factory() {
     * public Object create() {
     * return new Date();
     * }
     * }
     * List lazy = ListUtils.lazyList(new ArrayList(), factory);
     * Object obj = lazy.get(3);
     * </pre>
     * 当上面的代码被执行时，`obj`将包含一个新的`Date`实例。
     * 而且， 这个`Date`实例为列表中的第四个元素。前三个元素将被置为`null`。
     *
     * @param list 要设置为"懒惰"的列表, 不能为null
     * @param factory 创建新对象的工厂, 不能为null
     * @return 指定列表的"懒惰"列表
     * @since 1.0.0
     */
    fun <T : Any?> lazyList(list: List<T>, factory: Factory): List<T> = ListUtils.lazyList(list, factory) as List<T>

    /**
     * 返回给定列表的一个固定大小的列表。
     * 列表不能移除和添加元素， 但是已经存在列表中的元素可以被改变(例如，
     * 通过[List.set]方法)
     *
     * @param list 要固定大小的列表, 不能为null
     * @return 给定列表的一个固定大小的列表
     * @throws UnsupportedOperationException 如果试图从返回的列表中添加或移除元素
     * @since 1.0.0
     */
    fun <T : Any?> fixedSizeList(list: List<T>): List<T> = ListUtils.fixedSizeList(list) as List<T>

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.collections.ListUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
}