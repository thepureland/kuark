package io.kuark.base.lang.collections

import org.apache.commons.collections.Factory
import org.apache.commons.collections.MapUtils
import org.apache.commons.collections.Transformer
import java.io.PrintStream
import java.util.*
import kotlin.reflect.KClass

/**
 * kotlin.Map扩展函数
 *
 * @author K
 * @since 1.0.0
 */


/**
 * 把Map转换为二维数组，每行两个元素，按顺序分别为map的key和value
 *
 * @return 二维数组
 * @author K
 * @since 1.0.0
 */
fun Map<*, *>.toArrOfArr(): Array<Array<Any?>> {
    var arr: Array<Array<Any?>>? = null
    if (this.isNotEmpty()) {
        arr = Array<Array<Any?>>(this.size) { arrayOfNulls(2) }
        var i = 0
        for ((key, value) in this) {
            arr[i][0] = key
            arr[i][1] = value
            i++
        }
    }
    return arr ?: Array<Array<Any?>>(0) { arrayOfNulls(0) }
}


/**
 * 是否包含子map(所有key和value都相等)
 *
 * @param <K> Key
 * @param V Value
 * @param subMap 子map，为null或空返回false
 * @return 所有key和value都相等时返回true，反之返回false
 * @author K
 * @since 1.0.0
 */
fun <K, V> Map<*, *>.containsAll(subMap: Map<K, V>): Boolean {
    if (this.isEmpty() || subMap.isEmpty()) {
        return false
    }
    for ((key, value) in subMap) {
        if (!this.containsKey(key)
            || value == null && this[key] != null || value != null && (this[key] == null || value != this[key])
        ) {
            return false
        }
    }
    return true
}

// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
// 封装org.apache.commons.collections.MapUtils
// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv


// Conversion methods
// -------------------------------------------------------------------------

/**
 * 将ResourceBundle转为Map
 *
 * @return HashMap
 * @author K
 * @since 1.0.0
 */
fun ResourceBundle.toMap(): MutableMap<Any?, Any?>? = MapUtils.toMap(this)


//region Printing
/**
 * 将指定的Map的内容分行打印
 * 该方法打印出Map的良好格式的字符串描述。
 * 每个Map实体将打印出key和value。当值为Map时，该行为将递归。
 * 该方法不是线程安全的。您必须手动同步该类或请求的流。
 *
 * @param out   打印要输出的流,
 * @param label 要使用的标签, 可以为null. 为null该标签将不被输出. 它经常代表bean(或类似)的属性名
 * @author K
 * @since 1.0.0
 */
fun Map<*, *>.verbosePrint(out: PrintStream, label: Any?) = MapUtils.verbosePrint(out, label, this)

/**
 * 将指定的Map的内容分行打印
 * 该方法打印出Map的良好格式的字符串描述。
 * 每个Map实体将打印出key、value和类名。当值为Map时，该行为将递归。
 * 该方法不是线程安全的。您必须手动同步该类或请求的流。
 *
 * @param out   打印要输出的流
 * @param label 要使用的标签, 可以为null. 为null该标签将不被输出. 它经常代表bean(或类似)的属性名
 * @author K
 * @since 1.0.0
 */
fun Map<*, *>.debugPrint(out: PrintStream, label: Any?) = MapUtils.debugPrint(out, label, this)

//endregion Printing

/**
 * 反转Map。返回一个指定Map的key和value对换过的新HashMap。
 * 该方法假设要反转的Map是定义良好的。如果输入的map有多个
 * 相同值映射到不同key的实体，返回的map将只映射其中一个key
 * 到该value，但是具体是哪一个key是不确定的。
 *
 * @param K Key
 * @param V Value
 * @return 一个包含反转后的数据的新HashMap
 * @author K
 * @since 1.0.0
 */
fun <K, V> Map<K?, V?>.invertMap(): Map<V?, K?> = MapUtils.invertMap(this) as Map<V?, K?>

/**
 * 返回一个"懒惰"的Map， 它的值将被按需加载
 * 当传给返回的map的[Map.get]方法的key未在map中出现时，
 * 指定的工厂将创建一个新对象， 并将key和该对象放入Map
 * 例如:
 *
 * <pre>
 * val factory = object: Factory() {
 * override fun create(): Any {
 * return Date()
 * }
 * }
 * val lazyMap = HashMap().lazyMap(factory)
 * val obj = lazyMap.get("test")
 * </pre>
 *
 *
 * 当上面的代码被执行时，`obj`将包含一个新的`Date`实例。
 * 而且， 这个`Date`实例为Map中key为 `"test"`关联的对象。
 *
 *
 * @param K Key
 * @param V Value
 * @param factory 创建新对象的工厂
 * @return 指定Map的"懒惰"map
 * @author K
 * @since 1.0.0
 */
fun <K : Any?, V : Any?> Map<K, V>.lazyMap(factory: Factory): Map<K, V> = MapUtils.lazyMap(this, factory) as Map<K, V>

/**
 * 返回一个"懒惰"的Map， 它的值将被按需加载
 * 当传给返回的map的[Map.get]方法的key未在map中出现时，
 * 指定的工厂将创建一个新对象， 并将key和该对象放入Map.
 * 这里的工厂是一个[Transformer]， 它被传入要转换为value的key。
 * 例如:
 * <pre>
 * val factory = object: Transformer() {
 * override fun transform(mapKey: Any): Any {
 * return File(mapKey)
 * }
 * }
 * val lazyMap = HashMap().lazyMap(factory)
 * val obj = lazyMap.get("C:/dev")
 * </pre>
 *
 * 当上面的代码被执行时，`obj`将包含一个新的`File`实例。
 * 而且， 这个`File`实例为Map中key为 `"C:/dev"`关联的对象。
 *
 * 如果一个"懒惰"的map被一个同步的map包装，结果将是一个简单的缓存。
 * 当要获取的对象不在缓存中时，缓存将自己调用转换工厂来创建该对象，
 * 所有操作都是在同步块中完成的。
 *
 * @param K Key
 * @param V Value
 * @param transformerFactory 创建新对象的工厂
 * @return 指定Map的"懒惰"map
 * @author K
 * @since 1.0.0
 */
fun <K : Any?, V : Any?> Map<K, V>.lazyMap(transformerFactory: Transformer): Map<K, V> =
    MapUtils.lazyMap(this, transformerFactory) as Map<K, V>

/**
 * 创建一个多值的map，它支持将单值加入(通过调用Map.put(key, value))到key对应的集合中
 *
 * @param K Key
 * @param V Value
 * @return 一个多值的Map，它的value的类型为ArrayList
 * @see org.apache.commons.collections.map.MultiValueMap
 * @author K
 * @since 1.0.0
 */
fun <K : Any?, V : Any?> Map<K, MutableCollection<out V>?>.multiValueMap(): Map<K, ArrayList<V>> =
    MapUtils.multiValueMap(this) as Map<K, java.util.ArrayList<V>>

/**
 * 创建一个多值的map，它支持将单值加入(通过调用Map.put(key, value))到key对应的集合中
 *
 *
 * @param K Key
 * @param V Value
 * @param collectionClass 返回的map的值的类型(必须包含public的无参构造器并且实现Collection接口)
 * @return 一个多值的Map，它的value的类型为指定的collectionClass
 * @see org.apache.commons.collections.map.MultiValueMap
 * @author K
 * @since 1.0.0
 */
fun <K : Any?, V : Any?, C : Collection<V>> Map<K, MutableCollection<out V>?>.multiValueMap(
    collectionClass: KClass<C>
): Map<K, C> = MapUtils.multiValueMap(this, collectionClass.java) as Map<K, C>

/**
 * 创建一个多值的map，它支持将单值加入(通过调用Map.put(key, value))到key对应的集合中
 * 集合将由指定的集合工厂创建
 *
 * @param K Key
 * @param V Value
 * @param collectionFactory 创建返回的Map的值的集合的工厂
 * @return 一个多值的Map，它的value的类型为指定的集合工厂创建的集合
 * @see org.apache.commons.collections.map.MultiValueMap
 * @author K
 * @since 1.0.0
 */
fun <K : Any?, V : Any?> Map<K, MutableCollection<V>?>.multiValueMap(
    collectionFactory: Factory?
): Map<K, MutableCollection<V>?> = MapUtils.multiValueMap(this, collectionFactory) as Map<K, MutableCollection<V>?>

// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// 封装org.apache.commons.collections.MapUtils
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^