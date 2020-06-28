package org.kuark.base.collections

import org.apache.commons.collections.SetUtils
import org.apache.commons.collections.Transformer
import java.util.*

/**
 * Set工具类
 *
 * @since 1.0.0
 * @author K
 */
object SetKit {

    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.collections.SetUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

    /**
     * 测试两个集合是否相等， 包括包含的元素和集合大小
     *
     * 该方法对于不能通过继承AbstractSet类实现`Set`时非常有用。
     * 该方法参数类型为Collection是为了确保其它容器类型能够使用集合的实现算法。
     *
     * 该方法比较两个集合元素的相等性。当且仅当两个集合的大小相等，并且第一个集合中的元素在
     * 第二个集合中都存在. 该定义确保equals方法在同<tt>Set</tt>的实现中都能正确工作。
     *
     * 该实现首先检查两个集合是否为相同对象，如果是返回true；
     * 然后检查它们的大小是否相等，如果不是返回false，如果相等返回a.containsAll((Collection) b)的结果
     *
     *
     * @see Set
     *
     * @param set1 第一个集合, 可以为null
     * @param set2 第二个集合, 可以为null
     * @return 是否两个集合相等
     * @since 1.0.0
     */
    fun isEqualSet(set1: Collection<*>?, set2: Collection<*>?): Boolean = SetUtils.isEqualSet(set1, set2)

    /**
     * 为指定集合生成哈希值(使用[Set.hashCode]指定的算法)
     * 该方法对于不能通过继承AbstractSet类实现`Set`时非常有用。
     * 该方法参数类型为Collection是为了确保其它容器类型能够使用集合的实现算法。
     *
     * @see Set.hashCode
     * @param set 要生成哈希值的集合， 可以为null
     * @return 指定集合的哈希值，如果集合为null将返回0
     * @since 1.0.0
     */
    fun hashCodeForSet(set: Collection<*>?): Int = SetUtils.hashCodeForSet(set)

    /**
     * 返回指定的集合的同步集合
     * 您必须手动同步返回的缓冲区的迭代器，以避免不确定性的行为:
     *
     * <pre>
     * Set s = SetUtils.synchronizedSet(mySet);
     * synchronized (s) {
     * Iterator i = s.iterator();
     * while (i.hasNext()) {
     * process(i.next());
     * }
     * }
     * </pre>
     *
     * 该方法使用在装饰器子包中的实现.
     *
     *
     * @param set 要同步的集合, 不能为null
     * @return 同步的集合
     * @throws IllegalArgumentException 如果集合为null
     * @since 1.0.0
     */
    fun <T : Any?> synchronizedSet(set: Set<T>): Set<T> = SetUtils.synchronizedSet(set) as Set<T>

    /**
     * 转换给定的集合
     * 每一个新添加到集合中的元素都将被传递给转换器进行转换. 更为重要的是, 在调用该方法后,
     * 不要使用原来的集合, 因为它是一个可以添加未转换的对象的后门.
     *
     * @param set 要被转换的集合, 不能为null
     * @param transformer 使用的转换器, 不能为null
     * @return 转换后的集合
     * @since 1.0.0
     */
    fun transformedSet(set: Set<*>, transformer: Transformer): Set<*> = SetUtils.transformedSet(set, transformer)

    /**
     * 返回给定集合的一个维护元素顺序(自然顺序)的新集合
     * 如果一个元素被添加两次，顺序决定于第一次。
     * 顺序可通过迭代器或toArray观察。
     *
     * @param set 要排序的集合, 不能为null
     * @return 排过序的集合
     * @since 1.0.0
     */
    fun <T : Any?> orderedSet(set: Set<T>): Set<T> = SetUtils.orderedSet(set) as Set<T>

    /**
     * 返回指定的集合的同步有序集合
     * 您必须手动同步返回的缓冲区的迭代器，以避免不确定性的行为:
     *
     * <pre>
     * SortedSet s = SetUtils.synchronizedSortedSet(mySet);
     * synchronized (s) {
     * Iterator i = s.iterator();
     * while (i.hasNext()) {
     * process(i.next());
     * }
     * }
     * </pre>
     *
     * 该方法使用在装饰器子包中的实现.
     *
     * @param set 要同步的有序集合, 不能为null
     * @return 同步的有序集合
     * @since 1.0.0
     */
    fun <T: Any?> synchronizedSortedSet(set: SortedSet<out T>): SortedSet<T> = SetUtils.synchronizedSortedSet(set) as SortedSet<T>

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.collections.SetUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
}