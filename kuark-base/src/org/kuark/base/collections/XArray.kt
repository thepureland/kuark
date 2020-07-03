package org.kuark.base.collections

import org.apache.commons.lang3.ArrayUtils
import kotlin.reflect.KClass


/**
 * 将字符串数组转化为指定数值类型的数组 <br></br>
 * 注：只支持基本数值类型：Byte, Short, Int, Long, Float, Double
 *
 * @param clazz 转化后的数组元素类型
 * @param <T> 转化后的数组元素类型
 * @return
 */
inline fun <reified T : Number> Array<String>.toNumberArray(clazz: KClass<T>): Array<T> {
    val list = mutableListOf<T>()
    for (str in this) {
        val value = when (clazz) {
            Byte::class -> str.toByte()
            Short::class -> str.toShort()
            Int::class -> str.toInt()
            Long::class -> str.toLong()
            Float::class -> str.toFloat()
            Double::class -> str.toDouble()
            else -> throw Exception("不支持的类型【${clazz}】!")
        }
        list.add(value as T)
    }
    return list.toTypedArray()
}

/**
 * 获取一个数组(一维)的字符串值（与toString不一样，前后不会带花括号）
 *
 * @param array 数组
 * @return 数组的字符串表示, 如果传入的数组参数为null将返回空串
 * @since 1.0.0
 */
fun Array<*>.toPlainString(): String {
    val s = this.toString()
    return s.substring(1, s.length - 1)
}


// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
// 封装org.apache.commons.lang3.ArrayUtils
// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv


/**
 * 产生一个新的数组, 它包含原数组的从start下标开始到end下标的元素.
 * 开始下标有被包括,而结束下标则没有.如果传入的数组为null, 将返回null.
 * 子数组的元素类型与原数组是一样的. 因此, 如果输入的数组元素类型为 `Date`, 下面的用法是意料中的事:
 *
 * @param <T> 数组元素的类型
 * @param startIndexInclusive 开始下标(包括). 小于0当作0处理, 大于数组长度将返回一个空的数组
 * @param endIndexExclusive 结束下标(不包括). 小于开始下标将返回空数组, 大于数组长度当数组长度处理
 * @return 一个包含原数组的从start下标开始到end下标的元素的新数组
 * @since 1.0.0
 */
fun <T> Array<T>.subarray(startIndexInclusive: Int, endIndexExclusive: Int): Array<T> =
    ArrayUtils.subarray(this, startIndexInclusive, endIndexExclusive)


/**
 * 将给定数组的所有元素添加到一个新数组中
 * 新的数组包含`array1`和`array2`中的所有元素.总是返回一个新的数组.
 *
 *
 * <pre>
 * array1.addAll(null)   = cloned copy of array1
 * [].addAll([])         = []
 * [null].addAll([null]) = [null, null]
 * ["a", "b", "c"].addAll(["1", "2", "3"]) = ["a", "b", "c", "1", "2", "3"]
 * </pre>
 *
 * @param <T> 数组元素的类型
 * @param array2 第二个它的元素要添加到新数组的数组, 可以为 `null`
 * @return 新的数组
 * @since 1.0.0
 */
fun <T> Array<T>.addAll(vararg array2: T?): Array<T> = ArrayUtils.addAll(this, *array2)

/**
 * 拷贝给定的数组到新数组, 并在最后添加给定的元素
 *
 * <pre>
 * ["a"].add(null)     = ["a", null]
 * ["a"].add("b")      = ["a", "b"]
 * ["a", "b"].add("c") = ["a", "b", "c"]
 * </pre>
 *
 * @param <T> 数组元素类型
 * @param element 添加到最后的元素, 可以为 `null`
 * @return 一个包含所有给定数组元素及放在最后的指定的元素的新数组
 * @since 1.0.0
 */
fun <T> Array<T>.add(element: T?): Array<T> = ArrayUtils.add(this, element)

/**
 * 插入指定的元素到数组中指定的位置, 指定位置及右边的所有元素都将右移
 *
 * <pre>
 * ["a"].add(1, null)     = ["a", null]
 * ["a"].add(1, "b")      = ["a", "b"]
 * ["a", "b"].add(3, "c") = ["a", "b", "c"]
 * </pre>
 *
 * @param <T> 数组元素的类型
 * @param index 插入的位置
 * @param element 要插入的元素
 * @return 一个包含输入数组所有元素及指定元素的数组
 * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index > array.length).
 * @since 1.0.0
 */
fun <T> Array<T>.add(index: Int, element: T?): Array<T> = ArrayUtils.add(this, index, element)

/**
 * 从数组中的指定位置移除对应的元素, 其后的元素依次左移
 *
 * <pre>
 * ["a"].remove(0)           = []
 * ["a", "b"].remove(0)      = ["b"]
 * ["a", "b"].remove(1)      = ["a"]
 * ["a", "b", "c"].remove(1) = ["a", "c"]
 * </pre>
 *
 * @param <T> 数组元素类型
 * @param index 要移除的元素的下标
 * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
 * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index >= array.length)
 * @since 1.0.0
 */
fun <T> Array<T>.remove(index: Int): Array<T> = ArrayUtils.remove(this, index)

/**
 * 从数组中移除第一个出现的指定元素, 其后的元素依次左移. 如果数组中没有包含指定的元素, 不会有任何元素从数组中移除.
 *
 * <pre>
 * [].removeElement("a")              = []
 * ["a"].removeElement("b")           = ["a"]
 * ["a", "b"].removeElement("a")      = ["b"]
 * ["a", "b", "a"].removeElement("a") = ["b", "a"]
 * </pre>
 *
 * @param <T> 数组元素类型
 * @param element 要移除的元素
 * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
 * @since 1.0.0
 */
fun <T> Array<T>.removeElement(element: T?): Array<T> = ArrayUtils.removeElement(this, element)

/**
 * 从数组中移除所有指定位置的元素, 其后的元素依次左移. 该方法返回一个新的数组
 *
 * <pre>
 * ["a", "b", "c"].removeAll(0, 2) = ["b"]
 * ["a", "b", "c"].removeAll(1, 2) = ["a"]
 * </pre>
 *
 * @param <T> 数组元素类型
 * @param indices 要移除的元素的下标可变数组
 * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
 * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index >= array.length)
 * @since 1.0.0
 */
fun <T> Array<T>.removeAll(vararg indices: Int): Array<T> = ArrayUtils.removeAll(this, *indices)

/**
 * 从数组中移除所有出现的指定元素, 其后的元素依次左移.如果指定要移除的元素不在数组中,将只移除存在数组中的元素
 * 该方法返回一个新的数组
 *
 * <pre>
 * [].removeElements("a", "b")              = []
 * ["a"].removeElements("b", "c")           = ["a"]
 * ["a", "b"].removeElements("a", "c")      = ["b"]
 * ["a", "b", "a"].removeElements("a")      = ["b", "a"]
 * ["a", "b", "a"].removeElements("a", "a") = ["b"]
 * </pre>
 *
 * @param <T> 数组元素类型
 * @param values 要从数组中移除的值
 * @return 一个包含原数组除了要移除的元素外的所有元素的新数组
 * @since 1.0.0
 */
fun <T> Array<T>.removeElements(vararg values: T): Array<T> = ArrayUtils.removeElements(this, *values)

// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// 封装org.apache.commons.lang3.ArrayUtils
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^