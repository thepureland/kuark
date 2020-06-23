package org.kuark.base.collections

import org.apache.commons.collections.CollectionUtils
import org.kuark.base.log.LogFactory
import java.util.*

/**
 * Collection工具类
 *
 * @since 1.0.0
 */
object CollectionKit {

    private val LOG = LogFactory.getLog(CollectionKit::class)

    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 吸收并整理SpringSide项目Collections类的几个方法
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    /**
     * 转换Collection所有元素(通过toString())为String, 每个元素的前面加入prefix，后面加入postfix，如<div>mymessage</div>。
     *
     *
     * @param collection 来源容器, 可以为null, 为null将返回空串
     * @param prefix 要添加的前缀, 可以为null, 为null将被当作空串
     * @param postfix 要添加的后缀, 可以为null, 为null将被当作空串
     * @return 加上前缀和后缀的每个元素的toString值的连接串
     * @since 1.0.0
     */
    fun toString(collection: Collection<*>, prefix: String?, postfix: String?): String {
        var prefix = prefix
        var postfix = postfix
        if (isEmpty(collection)) {
            return ""
        }
        if (prefix == null) {
            prefix = ""
        }
        if (postfix == null) {
            postfix = ""
        }
        val builder = StringBuilder()
        for (o in collection) {
            builder.append(prefix).append(o).append(postfix)
        }
        return builder.toString()
    }

    /**
     * 取得Collection的第一个元素，如果collection为空返回null.
     *
     *
     * @param collection 来源容器, 可以为null, 为null将返回null
     * @return 指定容器的第一个元素
     * @since 1.0.0
     */
    fun <T> getFirst(collection: Collection<T>): T? {
        return if (isEmpty(collection)) {
            null
        } else collection.iterator().next()
    }

    /**
     * 获取Collection的最后一个元素 ，如果collection为空返回null.
     *
     *
     * @param collection 来源容器, 可以为null, 为null将返回null
     * @return 指定容器的最后一个元素
     * @since 1.0.0
     */
    fun <T> getLast(collection: Collection<T>): T? {
        if (isEmpty(collection)) {
            return null
        }

        // 当类型为List时，直接取得最后一个元素 。
        if (collection is List<*>) {
            val list = collection as List<T>
            return list[list.size - 1]
        }

        // 其他类型通过iterator滚动到最后一个元素.
        val iterator = collection.iterator()
        while (true) {
            val current = iterator.next()
            if (!iterator.hasNext()) {
                return current
            }
        }
    }
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 吸收并整理SpringSide项目Collections类的几个方法
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^


    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.collections.CollectionUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

    /**
     * 检测两个容器的差集是否不为空
     *
     *
     * @param coll1 第一个容器, 不能为null
     * @param coll2 第二个容器, 不能为null
     * @return `true` 如果两个容器的差集不为空
     * @see .intersection
     *
     * @since 1.0.0
     */
    fun containsAny(coll1: Collection<*>, coll2: Collection<*>): Boolean = CollectionUtils.containsAny(coll1, coll2)

    /**
     * 返回容器中每一个相同的元素出现的次数
     *
     *
     * @param coll 要计算相同元素出现次数的容器, 不能为null
     * @return Map<容器中的元素, 出现的次数>
     * @since 1.0.0
     */
    fun <T> getCardinalityMap(coll: Collection<T>): Map<T, Int> = CollectionUtils.getCardinalityMap(coll) as Map<T, Int>

    /**
     * 检测容器a是否为容器b的子集
     *
     * @param a 第一个容器(可能是子容器), 不能为空
     * @param b 第二个容器(可能是父容器), 不能为空
     * @return `true` 如果容器a是容器b的子集
     * @see .isProperSubCollection
     *
     * @see Collection.containsAll
     *
     * @since 1.0.0
     */
    fun isSubCollection(a: Collection<*>, b: Collection<*>): Boolean = CollectionUtils.isSubCollection(a, b)

    /**
     * 检测容器a是否为容器b的真子集
     *
     * 该实现假设
     *
     *  * `a.size()` 和 `b.size()` 代表容器 *a* 和 *b*的总个数
     *  * `a.size() < Integer.MAXVALUE`
     *
     *
     * @param a 第一个容器(可能是子容器), 不能为空
     * @param b 第二个容器(可能是父容器), 不能为空
     * @return `true` 如果容器a是容器b的真子集
     * @see .isSubCollection
     *
     * @see Collection.containsAll
     *
     * @since 1.0.0
     */
    fun isProperSubCollection(a: Collection<*>?, b: Collection<*>?): Boolean {
        return CollectionUtils.isProperSubCollection(a, b)
    }

    /**
     * 检测两个容器的大小及其包含的元素是否全部相等
     * @param a 第一个容器, 不能为null
     * @param b 第二个容器, 不能为null
     * @return `true` 如果两个容器的大小及其包含的元素全部相等
     * @since 1.0.0
     */
    fun isEqualCollection(a: Collection<*>?, b: Collection<*>?): Boolean {
        return CollectionUtils.isEqualCollection(a, b)
    }
    // -----------------------------------------------------------------------
    /**
     * 添加一个元素到指定的容器, 除非该元素为null
     *
     *
     * @param collection 被添加的容器, 不能为null
     * @param object 要添加的元素, 如果为null将不会被添加
     * @return true: 如果容器有被改变
     * @throws NullPointerException 如果容器为null
     * @since 1.0.0
     */
    fun <T> addIgnoreNull(collection: Collection<T?>?, `object`: T): Boolean {
        return CollectionUtils.addIgnoreNull(collection, `object`)
    }

    /**
     * 将迭代器中的所有元素添加到指定容器
     *
     *
     * @param collection 要添加到的目标容器, 不能为null
     * @param iterator 要添加的元素的迭代器, 不能为null
     * @throws NullPointerException 如果任意参数为null
     * @since 1.0.0
     */
    fun <T> addAll(
        collection: Collection<T?>?,
        iterator: Iterator<T>?
    ) {
        CollectionUtils.addAll(collection, iterator)
    }

    /**
     * 添加枚举中所有元素到指定的容器
     *
     *
     * @param collection 要添加到的目标容器, 不能为null
     * @param enumeration 要添加的元素的枚举, 不能为null
     * @throws NullPointerException 如果任意参数为null
     * @since 1.0.0
     */
    fun <T> addAll(collection: Collection<T?>?, enumeration: Enumeration<T>?) {
        CollectionUtils.addAll(collection, enumeration)
    }

    /**
     * 添加数组中所有元素到指定的容器
     *
     *
     * @param collection 要添加到的目标容器, 不能为null
     * @param elements 要添加的元素的数组, 可以为null， 为null将什么也不做
     * @throws NullPointerException 如果容器参数为null
     * @since 1.0.0
     */
    fun <T> addAll(collection: Collection<T?>?, vararg elements: T) {
        if (ArrayKit.isNotEmpty(elements)) {
            CollectionUtils.addAll(collection, elements)
        }
    }

    /**
     * 返回指定下标的元素. 如果没有对应的元素存在将抛`IndexOutOfBoundsException`异常, 如果指定对象不支持通过下标索引元素的操作时, 将抛
     * `IllegalArgumentException`异常
     *
     * 支持的类型及其相关语法如下:
     *
     *  * Map -- 返回的值为map的`entrySet`迭代器中指定位置上的`Map.Entry`, 如果存在这样的元素的话
     *  * List -- 与list的get()方法相同
     *  * Array -- 返回指定下标对应位置上的元素, 如果存在这样的元素的话; 否则将抛`IndexOutOfBoundsException`异常.
     *  * Collection -- 返回容器默认迭代器对应位置上的元素, 如果存在这样的元素的话
     *  * Iterator or Enumeration -- 返回容器默认迭代器或枚举对应位置上的元素, 如果存在这样的元素的话.
     *
     *
     * @param object 要获取的值所在的对象
     * @param index 下标
     * @return 指定下标的对象
     * @throws IndexOutOfBoundsException 如果下标非法
     * @throws IllegalArgumentException 如果对象的类型不支持
     * @since 1.0.0
     */
    operator fun get(`object`: Any?, index: Int): Any {
        return CollectionUtils.get(`object`, index)
    }

    /**
     * 取得容器的大小
     *
     * 该方法支持的对象类型如下:
     *
     *  * Collection - 容器的大小
     *  * Map - map的大小
     *  * Array - 数组的大小
     *  * Iterator - 迭代器中的无数个数
     *  * Enumeration - 枚举中的无数个数
     *
     *
     * @param object 要获取大小的对象, 不能为null
     * @return 指定对象的大小
     * @throws IllegalArgumentException 如果对象的类型不支持或对象为null
     * @since 1.0.0
     */
    fun size(`object`: Any?): Int {
        return CollectionUtils.size(`object`)
    }

    /**
     * 检查指定的容器是否为空
     *
     * 该方法支持的对象类型如下:
     *
     *  * Collection - 通过容器的isEmpty方法
     *  * Map - 通过map的isEmpty方法
     *  * Array - 通过数组的length属性
     *  * Iterator - 通过hasNext方法
     *  * Enumeration - 通过hasMoreElements方法
     *
     * 注意: 该方法这样命名是为了不与 [.isEmpty] 方法冲突.
     *
     *
     * @param object 要获取大小的对象, 不能为null
     * @return true: 如果为空
     * @throws IllegalArgumentException 如果对象的类型不支持或对象为null
     * @since 1.0.0
     */
    fun sizeIsEmpty(`object`: Any?): Boolean {
        return CollectionUtils.sizeIsEmpty(`object`)
    }
    // -----------------------------------------------------------------------
    /**
     * 检测容器是否为空, null安全
     *
     * null将返回true
     *
     *
     * @param coll 待检查的容器, 可以为null
     * @return true: 如果为空或null
     * @since 1.0.0
     */
    fun isEmpty(coll: Collection<*>?): Boolean {
        return CollectionUtils.isEmpty(coll)
    }

    /**
     * 检测容器是否非空, null安全
     *
     * null将返回false
     *
     *
     * @param coll 待检查的容器, 可以为null
     * @return true: 如果非空或非null
     * @since 1.0.0
     */
    fun isNotEmpty(coll: Collection<*>?): Boolean {
        return CollectionUtils.isNotEmpty(coll)
    }
    // -----------------------------------------------------------------------
    /**
     * 检测容器是否已满(不能再添加元素)
     *
     * 该方法使用[BoundedCollection]接口来确定容器是否已满. 如果容器未实现该接口, 将返回false.
     *
     * 容器一不定要直接实现该接口. 如果容器已经被装饰器子包修饰, 那么这些将被移除以访问BoundedCollection.
     *
     *
     * @param coll 要检查的容器, 不能为null
     * @return true: 如果容器已满
     * @throws NullPointerException 如果容器为null
     * @since 1.0.0
     */
    fun isFull(coll: Collection<*>?): Boolean {
        return CollectionUtils.isFull(coll)
    }

    /**
     * 返回容器能容纳的最大元素个数
     *
     * 该方法使用[BoundedCollection]接口来确定容器大小. 如果容器未实现该接口, 将返回-1.
     *
     * 容器一不定要直接实现该接口. 如果容器已经被装饰器子包修饰, 那么这些将被移除以访问BoundedCollection.
     *
     *
     * @param coll 要检查的容器, 不能为null
     * @return 容器能容纳的最大元素个数, 如果没有返回-1
     * @throws NullPointerException 如果容器为null
     * @since 1.0.0
     */
    fun maxSize(coll: Collection<*>?): Int {
        return CollectionUtils.maxSize(coll)
    }
    // -----------------------------------------------------------------------
    /**
     * 返回一个容器, 它的元素存在于`collection`中同时也在`retain`中. 返回的容器的元素的基数与`collection`元素的基数一样, 除非
     * `retain`没有包含这个元素, 此时, 基数为0. 该方法在你想更改一个容器但又不能调用该容器的retainAll(retain)方法时非常有用.
     *
     *
     * @param collection 其内容为#retailAll操作的目标的容器
     * @param retain 返回的容器中可能包含的元素的容器
     * @return 一个由所有在`collection`中同时至少出现一次在`retain`的元素组成的容器
     * @throws NullPointerException 如果任意参数为null
     * @since 1.0.0
     */
    fun retainAll(
        collection: Collection<*>?,
        retain: Collection<*>?
    ): Collection<*> {
        return CollectionUtils.retainAll(collection, retain)
    }

    /**
     * 从`collection`中移除`remove`中的元素. 也就是说, 该方法返回一个由不在`remove`中元素组成的容器.
     * 返回的容器的元素的基数与`collection`元素的基数一样, 除非`remove`包含这个元素, 此时, 基数为0.
     * 该方法在你想更改一个容器但又不能调用该容器的removeAll(retain)方法时非常有用.
     *
     *
     * @param collection 要被移除元素的容器
     * @param remove 要移除的元素的容器
     * @return 一个由`collection`容器中所有除了在`remove`出现的元素组成的容器
     * @throws NullPointerException 如果任意参数为null
     * @since 1.0.0
     */
    fun removeAll(
        collection: Collection<*>?,
        remove: Collection<*>?
    ): Collection<*> {
        return CollectionUtils.removeAll(collection, remove)
    }
    // -----------------------------------------------------------------------
    /**
     * 返回指定的容器的同步容器
     *
     * 您必须手动同步返回的缓冲区的迭代器，以避免不确定性的行为:
     *
     * <pre>
     * Collection c = CollectionUtils.synchronizedCollection(myCollection);
     * synchronized (c) {
     * Iterator i = c.iterator();
     * while (i.hasNext()) {
     * process(i.next());
     * }
     * }
     * </pre>
     *
     *
     * 该方法使用在装饰器子包中的实现.
     *
     *
     * @param collection 要同步的容器, 不能为null
     * @return 同步的容器
     * @since 1.0.0
     */
    fun <T> synchronizedCollection(collection: Collection<T?>): Collection<T?> {
        return CollectionUtils.synchronizedCollection(collection) as Collection<T?>
    }

    /**
     * 返回一个不可修改的容器
     *
     * 该方法使用在装饰器子包中的实现.
     *
     *
     * @param collection 要置为不可修改的容器, 不能为null
     * @return 一个不可修改的容器
     * @throws IllegalArgumentException 如果容器为null
     * @since 1.0.0
     */
    fun <T> unmodifiableCollection(collection: Collection<T?>): Collection<T?> {
        return CollectionUtils.unmodifiableCollection(collection) as Collection<T?>
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.collections.CollectionUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
}