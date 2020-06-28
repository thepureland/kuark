package org.kuark.base.collections

import org.apache.commons.collections.*
import java.io.PrintStream
import java.util.*
import kotlin.reflect.KClass

/**
 * Map工具类
 *
 * @author K
 * @since 1.0.0
 */
object MapKit {

    /**
     * 是否包含子map(所有key和value都相等)
     *
     * @param map 主map，为null或空返回false
     * @param subMap 子map，为null或空返回false
     * @param <K> Key
     * @param <V> Value
     * @return 所有key和value都相等时返回true，反之返回false
     */
    fun <K, V> containsAll(map: Map<K, V?>, subMap: Map<K, V>): Boolean {
        if (isEmpty(map) || isEmpty(subMap)) {
            return false
        }
        for ((key, value) in subMap) {
            if (!map.containsKey(key)
                || value == null && map[key] != null || value != null && (map[key] == null || value != map[key])
            ) {
                return false
            }
        }
        return true
    }

    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.collections.MapUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

    /**
     * 从Map中取得指定key的值，空安全
     *
     * @param map 要获取值的map，可以为null
     * @param key 要查找的key
     * @return Map中的值, 如果map参数为null将返回`null`
     * @since 1.0.0
     */
    fun <K, V> getObject(map: Map<K?, V?>?, key: K): V = MapUtils.getObject(map, key) as V

    /**
     * 从Map中取得指定key的值的toString，空安全
     *
     * @param map 要获取值的map，可以为null
     * @param key 要查找的key
     * @return 指定key的值的toString, 如果map参数为null将返回`null`
     */
    fun <K> getString(map: Map<K?, *>?, key: K): String = MapUtils.getString(map, key)

    /**
     * 从Map中取得指定key的值的布尔值，空安全
     * 如果值为布尔值，直接返回。如果为字符串且等于'true'(忽略大小写)返回true,
     * 否则返回false。如果为整型值0返回false，非0返回true。
     * 其它情况全部返回null。
     *
     *
     * @param map 要获取值的map，可以为null
     * @param key 要查找的key
     * @return 指定key的值的布尔值, 如果map参数为null将返回`null`
     * @since 1.0.0
     */
    fun <K> getBoolean(map: Map<K?, *>?, key: K): Boolean = MapUtils.getBoolean(map, key)

    /**
     * 从Map中取得指定key的值的数值结果，空安全
     * 如果值为数值型，它将直接返回。如果为字符串，它将返回由[NumberFormat.parse]
     * 按系统默认的格式转换后的值，当转换失败时返回null。
     * 其它情况全部返回null。
     *
     * @param map 要获取值的map，可以为null
     * @param key 要查找的key
     * @return 指定key的值的数值结果, 如果map参数为null将返回`null`
     * @since 1.0.0
     */
    fun <K> getNumber(map: Map<K?, *>?, key: K): Number = MapUtils.getNumber(map, key)

    /**
     * 从Map中取得指定key的值的字节值，空安全
     * 结果为[.getNumber]的值的字节值
     *
     * @param map 要获取值的map，可以为null
     * @param key 要查找的key
     * @return 指定key的值的字节值, 如果map参数为null将返回null
     * @since 1.0.0
     */
    fun <K> getByte(map: Map<K?, *>?, key: K): Byte = MapUtils.getByte(map, key)

    /**
     * 从Map中取得指定key的值的短整型值，空安全
     * 结果为[.getNumber]的值的短整型值
     *
     * @param map 要获取值的map，可以为null
     * @param key 要查找的key
     * @return 指定key的值的短整型值, 如果map参数为null将返回null
     * @since 1.0.0
     */
    fun <K> getShort(map: Map<K?, *>?, key: K): Short = MapUtils.getShort(map, key)

    /**
     * 从Map中取得指定key的值的整型值，空安全
     * 结果为[.getNumber]的值的整型值
     *
     * @param map 要获取值的map，可以为null
     * @param key 要查找的key
     * @return 指定key的值的整型值, 如果map参数为null将返回null
     * @since 1.0.0
     */
    fun <K> getInteger(map: Map<K?, *>?, key: K): Int = MapUtils.getInteger(map, key)

    /**
     * 从Map中取得指定key的值的长整型值，空安全
     * 结果为[.getNumber]的值的长整型值
     *
     * @param map 要获取值的map，可以为null
     * @param key 要查找的key
     * @return 指定key的值的长整型值, 如果map参数为null将返回null
     * @since 1.0.0
     */
    fun <K> getLong(map: Map<K?, *>?, key: K): Long = MapUtils.getLong(map, key)

    /**
     * 从Map中取得指定key的值的浮点值，空安全
     * 结果为[.getNumber]的值的浮点值
     *
     * @param map 要获取值的map，可以为null
     * @param key 要查找的key
     * @return 指定key的值的浮点值, 如果map参数为null将返回null
     * @since 1.0.0
     */
    fun <K> getFloat(map: Map<K?, *>?, key: K): Float = MapUtils.getFloat(map, key)

    /**
     * 从Map中取得指定key的值的双精度浮点值，空安全
     * 结果为[.getNumber]的值的双精度浮点值
     *
     *
     * @param map 要获取值的map，可以为null
     * @param key 要查找的key
     * @return 指定key的值的双精度浮点值, 如果map参数为null将返回null
     * @since 1.0.0
     */
    fun <K> getDouble(map: Map<K?, *>?, key: K): Double = MapUtils.getDouble(map, key)

    /**
     * 从Map中取得指定key的Map，空安全
     * 如果从map中取出的值不是map，返回null
     *
     * @param map 要获取值的map，可以为null
     * @param key 要查找的key
     * @return 指定key的Map, 如果map参数为null将返回null
     * @since 1.0.0
     */
    fun <K> getMap(map: Map<K?, *>?, key: K): Map<*, *> = MapUtils.getMap(map, key)

    // Type safe getters with default values
    // -------------------------------------------------------------------------
    /**
     * 从Map中取得指定key的对象，如果为null返回指定的默认值
     *
     * @param map          要查找的Map，可以为null
     * @param key          要查找的key
     * @param defaultValue 对应key的值为null时要返回的默认值
     * @return map中key对应的值, 如果为null返回指定的默认值，map为null返回null
     * @since 1.0.0
     */
    fun <K> getObject(map: Map<K?, *>?, key: K, defaultValue: Any?): Any = MapUtils.getObject(map, key, defaultValue)

    /**
     * 从Map中取得指定key的对象，返回该对象的String值，如果转换失败返回指定的默认值
     *
     * @param map          要查找的Map，可以为null
     * @param key          要查找的key
     * @param defaultValue 对应key的值为null时要返回的默认值
     * @return map中key对应的值的String值，如果为原值为null、map为null或转换失败返回指定的默认值
     * @since 1.0.0
     */
    fun <K> getString(map: Map<K?, *>?, key: K, defaultValue: String?): String =
        MapUtils.getString(map, key, defaultValue)

    /**
     * 从Map中取得指定key的对象，返回该对象的Boolean值，如果转换失败返回指定的默认值
     *
     * @param map          要查找的Map，可以为null
     * @param key          要查找的key
     * @param defaultValue 对应key的值为null时要返回的默认值
     * @return map中key对应的值的Boolean值，如果为原值为null、map为null或转换失败返回指定的默认值
     * @since 1.0.0
     */
    fun <K> getBoolean(map: Map<K?, *>?, key: K, defaultValue: Boolean?): Boolean =
        MapUtils.getBoolean(map, key, defaultValue)

    /**
     * 从Map中取得指定key的对象，返回该对象的Number值，如果转换失败返回指定的默认值
     *
     *
     * @param map          要查找的Map，可以为null
     * @param key          要查找的key
     * @param defaultValue 对应key的值为null时要返回的默认值
     * @return map中key对应的值的Number值，如果为原值为null、map为null或转换失败返回指定的默认值
     * @since 1.0.0
     */
    fun <K> getNumber(map: Map<K?, *>?, key: K, defaultValue: Number?): Number =
        MapUtils.getNumber(map, key, defaultValue)

    /**
     * 从Map中取得指定key的对象，返回该对象的Byte值，如果转换失败返回指定的默认值
     *
     * @param map          要查找的Map，可以为null
     * @param key          要查找的key
     * @param defaultValue 对应key的值为null时要返回的默认值
     * @return map中key对应的值的Byte值，如果为原值为null、map为null或转换失败返回指定的默认值
     * @since 1.0.0
     */
    fun <K> getByte(map: Map<K?, *>?, key: K, defaultValue: Byte?): Byte =
        MapUtils.getByteValue(map, key, defaultValue!!)

    /**
     * 从Map中取得指定key的对象，返回该对象的Short值，如果转换失败返回指定的默认值
     *
     * @param map          要查找的Map，可以为null
     * @param key          要查找的key
     * @param defaultValue 对应key的值为null时要返回的默认值
     * @return map中key对应的值的Short值，如果为原值为null、map为null或转换失败返回指定的默认值
     * @since 1.0.0
     */
    fun <K> getShort(map: Map<K?, *>?, key: K, defaultValue: Short?): Short =
        MapUtils.getShortValue(map, key, defaultValue!!)

    /**
     * 从Map中取得指定key的对象，返回该对象的Integer值，如果转换失败返回指定的默认值
     *
     * @param map          要查找的Map，可以为null
     * @param key          要查找的key
     * @param defaultValue 对应key的值为null时要返回的默认值
     * @return map中key对应的值的Integer值，如果为原值为null、map为null或转换失败返回指定的默认值
     * @since 1.0.0
     */
    fun <K> getInteger(map: Map<K?, *>?, key: K, defaultValue: Int?): Int = MapUtils.getInteger(map, key, defaultValue)

    /**
     * 从Map中取得指定key的对象，返回该对象的Long值，如果转换失败返回指定的默认值
     *
     *
     * @param map          要查找的Map，可以为null
     * @param key          要查找的key
     * @param defaultValue 对应key的值为null时要返回的默认值
     * @return map中key对应的值的Long值，如果为原值为null、map为null或转换失败返回指定的默认值
     * @since 1.0.0
     */
    fun <K> getLong(map: Map<K?, *>?, key: K, defaultValue: Long?): Long = MapUtils.getLong(map, key, defaultValue)

    /**
     * 从Map中取得指定key的对象，返回该对象的Float值，如果转换失败返回指定的默认值
     *
     *
     * @param map          要查找的Map，可以为null
     * @param key          要查找的key
     * @param defaultValue 对应key的值为null时要返回的默认值
     * @return map中key对应的值的Float值，如果为原值为null、map为null或转换失败返回指定的默认值
     * @since 1.0.0
     */
    fun <K> getFloat(map: Map<K?, *>?, key: K, defaultValue: Float?): Float = MapUtils.getFloat(map, key, defaultValue)

    /**
     * 从Map中取得指定key的对象，返回该对象的Double值，如果转换失败返回指定的默认值
     *
     *
     * @param map          要查找的Map，可以为null
     * @param key          要查找的key
     * @param defaultValue 对应key的值为null时要返回的默认值
     * @return map中key对应的值的Double值，如果为原值为null、map为null或转换失败返回指定的默认值
     * @since 1.0.0
     */
    fun <K> getDouble(map: Map<K?, *>?, key: K, defaultValue: Double?): Double =
        MapUtils.getDouble(map, key, defaultValue)

    /**
     * 从Map中取得指定key的对象，返回该对象的Map值，如果转换失败返回指定的默认值
     *
     *
     * @param map          要查找的Map，可以为null
     * @param key          要查找的key
     * @param defaultValue 对应key的值为null时要返回的默认值
     * @return map中key对应的值的Map值，如果为原值为null、map为null或转换失败返回指定的默认值
     * @since 1.0.0
     */
    fun <K> getMap(map: Map<K?, *>?, key: K, defaultValue: Map<*, *>?): Map<*, *> =
        MapUtils.getMap(map, key, defaultValue)

    // Type safe primitive getters
    // -------------------------------------------------------------------------
    /**
     * 从Map中取得指定key的值的布尔值，空安全
     *
     * 如果值为布尔值，直接返回。如果为字符串且等于'true'(忽略大小写)返回true,
     * 否则返回false。如果为整型值0返回false，非0返回true。
     * 其它情况全部返回false。
     *
     *
     * @param map 要获取值的map，可以为null
     * @param key 要查找的key
     * @return 指定key的值的布尔值, 如果map参数为null将返回`false`
     * @since 1.0.0
     */
    fun <K> getBooleanValue(map: Map<K?, *>?, key: K): Boolean = MapUtils.getBooleanValue(map, key)

    /**
     * 从Map中取得指定key的值的字节值，空安全
     * 结果为[.getNumber]的值的字节值
     *
     *
     * @param map 要获取值的map，可以为null
     * @param key 要查找的key
     * @return 指定key的值的字节值, 如果map参数为null将返回0
     * @since 1.0.0
     */
    fun <K> getByteValue(map: Map<K?, *>?, key: K): Byte = MapUtils.getByteValue(map, key)

    /**
     * 从Map中取得指定key的值的短整型值，空安全
     * 结果为[.getNumber]的值的短整型值
     *
     * @param map 要获取值的map，可以为null
     * @param key 要查找的key
     * @return 指定key的值的短整型值, 如果map参数为null将返回0
     * @since 1.0.0
     */
    fun <K> getShortValue(map: Map<K?, *>?, key: K): Short = MapUtils.getShortValue(map, key)

    /**
     * 从Map中取得指定key的值的整型值，空安全
     * 结果为[.getNumber]的值的整型值
     *
     *
     * @param map 要获取值的map，可以为null
     * @param key 要查找的key
     * @return 指定key的值的整型值, 如果map参数为null将返回0
     * @since 1.0.0
     */
    fun <K> getIntValue(map: Map<K?, *>?, key: K): Int = MapUtils.getIntValue(map, key)

    /**
     * 从Map中取得指定key的值的长整型值，空安全
     * 结果为[.getNumber]的值的长整型值
     *
     * @param map 要获取值的map，可以为null
     * @param key 要查找的key
     * @return 指定key的值的长整型值, 如果map参数为null将返回0L
     * @since 1.0.0
     */
    fun <K> getLongValue(map: Map<K?, *>?, key: K): Long = MapUtils.getLongValue(map, key)

    /**
     * 从Map中取得指定key的值的浮点值，空安全
     * 结果为[.getNumber]的值的浮点值
     *
     * @param map 要获取值的map，可以为null
     * @param key 要查找的key
     * @return 指定key的值的浮点值, 如果map参数为null将返回0.0F
     * @since 1.0.0
     */
    fun <K> getFloatValue(map: Map<K?, *>?, key: K): Float = MapUtils.getFloatValue(map, key)

    /**
     * 从Map中取得指定key的值的双精度浮点值，空安全
     * 结果为[.getNumber]的值的双精度浮点值
     *
     * @param map 要获取值的map，可以为null
     * @param key 要查找的key
     * @return 指定key的值的双精度浮点值, 如果map参数为null将返回0.0
     * @since 1.0.0
     */
    fun <K> getDoubleValue(map: Map<K?, *>?, key: K): Double = MapUtils.getDoubleValue(map, key)

    // Type safe primitive getters with default values
    // -------------------------------------------------------------------------
    /**
     * 从Map中取得指定key的对象，返回该对象的Boolean值，如果转换失败返回指定的默认值
     * 如果值为布尔值，直接返回。如果为字符串且等于'true'(忽略大小写)返回true,
     * 否则返回false。如果为整型值0返回false，非0返回true。
     * 其它情况全部返回指定的默认值。
     *
     *
     * @param map          要查找的Map，可以为null
     * @param key          要查找的key
     * @param defaultValue 对应key的值为null或转换失败时要返回的默认值
     * @return map中key对应的值的Boolean值，如果map为null或转换失败时返回指定的默认值
     * @since 1.0.0
     */
    fun <K> getBooleanValue(map: Map<K?, *>?, key: K, defaultValue: Boolean): Boolean =
        MapUtils.getBooleanValue(map, key, defaultValue)

    /**
     * 从Map中取得指定key的对象，返回该对象的byte值，如果转换失败返回指定的默认值
     *
     *
     * @param map          要查找的Map，可以为null
     * @param key          要查找的key
     * @param defaultValue 对应key的值为null或转换失败时要返回的默认值
     * @return map中key对应的值的byte值，如果map为null或转换失败时返回指定的默认值
     * @since 1.0.0
     */
    fun <K> getByteValue(map: Map<K?, *>?, key: K, defaultValue: Byte): Byte =
        MapUtils.getByteValue(map, key, defaultValue)

    /**
     * 从Map中取得指定key的对象，返回该对象的short值，如果转换失败返回指定的默认值
     *
     *
     * @param map          要查找的Map，可以为null
     * @param key          要查找的key
     * @param defaultValue 对应key的值为null或转换失败时要返回的默认值
     * @return map中key对应的值的short值，如果map为null或转换失败时返回指定的默认值
     * @since 1.0.0
     */
    fun <K> getShortValue(map: Map<K?, *>?, key: K, defaultValue: Short): Short =
        MapUtils.getShortValue(map, key, defaultValue)

    /**
     * 从Map中取得指定key的对象，返回该对象的int值，如果转换失败返回指定的默认值
     *
     *
     * @param map          要查找的Map，可以为null
     * @param key          要查找的key
     * @param defaultValue 对应key的值为null或转换失败时要返回的默认值
     * @return map中key对应的值的int值，如果map为null或转换失败时返回指定的默认值
     * @since 1.0.0
     */
    fun <K> getIntValue(map: Map<K?, *>?, key: K, defaultValue: Int): Int = MapUtils.getIntValue(map, key, defaultValue)

    /**
     * 从Map中取得指定key的对象，返回该对象的long值，如果转换失败返回指定的默认值
     *
     * @param map          要查找的Map，可以为null
     * @param key          要查找的key
     * @param defaultValue 对应key的值为null或转换失败时要返回的默认值
     * @return map中key对应的值的long值，如果map为null或转换失败时返回指定的默认值
     * @since 1.0.0
     */
    fun <K> getLongValue(map: Map<K?, *>?, key: K, defaultValue: Long): Long =
        MapUtils.getLongValue(map, key, defaultValue)

    /**
     * 从Map中取得指定key的对象，返回该对象的float值，如果转换失败返回指定的默认值
     *
     *
     * @param map          要查找的Map，可以为null
     * @param key          要查找的key
     * @param defaultValue 对应key的值为null或转换失败时要返回的默认值
     * @return map中key对应的值的float值，如果map为null或转换失败时返回指定的默认值
     * @since 1.0.0
     */
    fun <K> getFloatValue(map: Map<K?, *>?, key: K, defaultValue: Float): Float =
        MapUtils.getFloatValue(map, key, defaultValue)

    /**
     * 从Map中取得指定key的对象，返回该对象的double值，如果转换失败返回指定的默认值
     *
     * @param map          要查找的Map，可以为null
     * @param key          要查找的key
     * @param defaultValue 对应key的值为null或转换失败时要返回的默认值
     * @return map中key对应的值的double值，如果map为null或转换失败时返回指定的默认值
     * @since 1.0.0
     */
    fun <K> getDoubleValue(map: Map<K?, *>?, key: K, defaultValue: Double): Double =
        MapUtils.getDoubleValue(map, key, defaultValue)

    // Conversion methods
    // -------------------------------------------------------------------------
    /**
     * 将Map转为Properties，传入null将返回空的Properties对象
     *
     *
     * @param map 要转化为Properties的Map, 可以为null
     * @return Properties对象
     * @since 1.0.0
     */
    fun toProperties(map: Map<*, *>?): Properties = MapUtils.toProperties(map)

    /**
     * 将ResourceBundle转为Map，传入null将返回空的Map对象
     *
     * @param resourceBundle 要转化为Map的ResourceBundle, 可以为null
     * @return HashMap
     * @since 1.0.0
     */
    fun toMap(resourceBundle: ResourceBundle?): Map<String?, *> {
        return (if (resourceBundle == null) {
            Collections.EMPTY_MAP
        } else MapUtils.toMap(resourceBundle)) as Map<String?, *>
    }
    // Printing methods
    // -------------------------------------------------------------------------
    /**
     * 将指定的Map的内容分行打印
     * 该方法打印出Map的良好格式的字符串描述。
     * 每个Map实体将打印出key和value。当值为Map时，该行为将递归。
     * 该方法不是线程安全的。您必须手动同步该类或请求的流。
     *
     *
     * @param out   打印要输出的流, 不能为null
     * @param label 要使用的标签, 可以为null. 为null该标签将不被输出. 它经常代表bean(或类似)的属性名
     * @param map   要打印的map, 可以为null，如果为null将输出"null"
     * @throws NullPointerException 如果stream参数为`null`
     * @since 1.0.0
     */
    fun verbosePrint(out: PrintStream, label: Any?, map: Map<*, *>?) = MapUtils.verbosePrint(out, label, map)

    /**
     * 将指定的Map的内容分行打印
     * 该方法打印出Map的良好格式的字符串描述。
     * 每个Map实体将打印出key、value和类名。当值为Map时，该行为将递归。
     * 该方法不是线程安全的。您必须手动同步该类或请求的流。
     *
     *
     * @param out   打印要输出的流, 不能为null
     * @param label 要使用的标签, 可以为null. 为null该标签将不被输出. 它经常代表bean(或类似)的属性名
     * @param map   要打印的map, 可以为null，如果为null将输出"null"
     * @throws NullPointerException 如果stream参数为`null`
     * @since 1.0.0
     */
    fun debugPrint(out: PrintStream, label: Any?, map: Map<*, *>?) = MapUtils.debugPrint(out, label, map)

    // Misc
    // -----------------------------------------------------------------------
    /**
     * 反转Map。返回一个指定Map的key和value对换过的新HashMap。
     * 该方法假设要反转的Map是定义良好的。如果输入的map有多个
     * 相同值映射到不同key的实体，返回的map将只映射其中一个key
     * 到该value，但是具体是哪一个key是不确定的。
     *
     *
     * @param map 要反转的Map, 可以为null
     * @return 一个包含反转后的数据的新HashMap，转入的map为null将返回空的HashMap
     * @since 1.0.0
     */
    fun <K, V> invertMap(map: Map<K?, V?>?): Map<V?, K?> {
        return if (map == null) {
            HashMap(0)
        } else MapUtils.invertMap(map) as Map<V?, K?>
    }

    // -----------------------------------------------------------------------
    /**
     * Protects against adding null values to a map.
     * 该方法检查要添加到Map的值，如果为null将被替换为空串
     * 这个方法在map不能接受null值，或在从一个可能提交null或空串(它在map中保持原样)
     * 的源接收数据时非常有用。
     * map中的key不会被校验。
     *
     * @param map   要添加元素的Map, 可以为null，为null将不做任何操作
     * @param key   map的key
     * @param value map的value，null将被转换为空串
     * @since 1.0.0
     */
    fun <K> safeAddToMap(map: Map<K?, *>?, key: K, value: Any?) {
        if (map != null) {
            MapUtils.safeAddToMap(map, key, value)
        }
    }
    // -----------------------------------------------------------------------
    /**
     * 将指定数组中的所有key和value放到map中
     * 该方法是[Map.putAll]方法及Map的构造方法的另一种选择。
     * 它允许您从一个各种可能类型的数组创建Map。
     * 如果数组中第一个元素实现[Map.Entry] 或 [KeyValue]，
     * 那么key和value将从该元素添加到Map中。如果数组中第一个元素也是一个数组，
     * 那么它假设子数组中0下标的元素为key，1下标的元素为value。
     * 否则，输入的数组的元素将被间隔的当作key或value。
     * 例如，下面的代码创建一个颜色Map:
     *
     *
     * <pre>
     * Map colorMap = MapTool.putAll(new HashMap(), new String[][] { { &quot;RED&quot;, &quot;#FF0000&quot; }, { &quot;GREEN&quot;, &quot;#00FF00&quot; },
     * { &quot;BLUE&quot;, &quot;#0000FF&quot; } });
     * </pre>
     * 或:
     *
     * <pre>
     * Map colorMap = MapUtils.putAll(new HashMap(), new String[] { &quot;RED&quot;, &quot;#FF0000&quot;, &quot;GREEN&quot;, &quot;#00FF00&quot;, &quot;BLUE&quot;, &quot;#0000FF&quot; });
     * </pre>
     * 或:
     * <pre>
     * Map colorMap = MapUtils.putAll(new HashMap(), new Map.Entry[] { new DefaultMapEntry(&quot;RED&quot;, &quot;#FF0000&quot;),
     * new DefaultMapEntry(&quot;GREEN&quot;, &quot;#00FF00&quot;), new DefaultMapEntry(&quot;BLUE&quot;, &quot;#0000FF&quot;) });
     * </pre>
     *
     * @param map   要添加到的Map, 可以为null，为null将会新建一个LinkedHashMap
     * @param array an array to populate from, null ignored
     * @return 输入的map，当其为null时，该方法会新建一个LinkedHashMap
     * @throws IllegalArgumentException 如果子数组或使用的匹配的实体的条目某一个项是无效的
     * @throws ClassCastException       如果数组的内容混合的
     * @since 1.0.0
     */
    fun <K, V> putAll(map: Map<K?, V?>?, array: Array<Any?>?): Map<K?, V?> {
        var map = map
        if (map == null) {
            map = LinkedHashMap()
        }
        return MapUtils.putAll(map, array) as Map<K?, V?>
    }

    // -----------------------------------------------------------------------
    /**
     * 检查指定的map是否为null或空
     *
     *
     * @param map 待检查的Map, 可以为null
     * @return true 指定的map为null或空
     * @since 1.0.0
     */
    fun isEmpty(map: Map<*, *>?): Boolean = MapUtils.isEmpty(map)

    /**
     * 检查指定的map是否非null且非空
     *
     *
     * @param map 待检查的Map, 可以为null
     * @return true 指定的map为非null且非空
     * @since 1.0.0
     */
    fun isNotEmpty(map: Map<*, *>?): Boolean = MapUtils.isNotEmpty(map)

    // Map decorators
    // -----------------------------------------------------------------------
    /**
     * 返回指定的Map的同步Map
     * 您必须手动同步返回的缓冲区的迭代器，以避免不确定性的行为:
     *
     *
     * <pre>
     * Map m = MapTool.synchronizedMap(myMap);
     * Set s = m.keySet(); // 同步块之外
     * synchronized (m) { // 同步Map
     * Iterator i = s.iterator();
     * while (i.hasNext()) {
     * process(i.next());
     * }
     * }
     * </pre>
     *
     *
     * 该方法使用[Collections]的实现。
     *
     * @param map 要同步的Map, 不能为null
     * @return 同步的Map
     * @throws IllegalArgumentException 如果指定的map为null
     * @since 1.0.0
     */
    fun <K : Any?, V : Any?> synchronizedMap(map: Map<K, V>): Map<K, V> = MapUtils.synchronizedMap(map) as Map<K, V>

    /**
     * 返回一个不可修改的Map
     * 该方法使用在装饰器子包中的实现.
     *
     *
     * @param map 要置为不可修改的Map, 不能为null
     * @return 一个不可修改的Map
     * @throws IllegalArgumentException 如果指定的map为null
     * @since 1.0.0
     */
    fun <K : Any?, V : Any?> unmodifiableMap(map: Map<K, V>): Map<K, V> = MapUtils.unmodifiableMap(map) as Map<K, V>


    /**
     * 返回给定Map的一个固定大小的Map。
     * Map不能移除和添加元素， 但是已经存在Map中的元素可以被改变(例如，
     * 通过[Map.put]方法)
     *
     *
     * @param map 要固定大小的Map, 不能为null
     * @return 给定Map的一个固定大小的Map
     * @throws IllegalArgumentException      如果指定的Map为null
     * @throws UnsupportedOperationException 如果试图从返回的Map中添加或移除元素
     * @since 1.0.0
     */
    fun <K : Any?, V : Any?> fixedSizeMap(map: Map<K, V>): Map<K, V> = MapUtils.fixedSizeMap(map) as Map<K, V>

    /**
     * 返回一个"懒惰"的Map， 它的值将被按需加载
     * 当传给返回的map的[Map.get]方法的key未在map中出现时，
     * 指定的工厂将创建一个新对象， 并将key和该对象放入Map
     * 例如:
     *
     *
     * <pre>
     * Factory factory = new Factory() {
     * public Object create() {
     * return new Date();
     * }
     * }
     * Map lazyMap = MapTool.lazyMap(new HashMap(), factory);
     * Object obj = lazyMap.get("test");
     * </pre>
     *
     *
     * 当上面的代码被执行时，`obj`将包含一个新的`Date`实例。
     * 而且， 这个`Date`实例为Map中key为 `"test"`关联的对象。
     *
     *
     * @param map     要设置为"懒惰"的map, 不能为null
     * @param factory 创建新对象的工厂, 不能为null
     * @return 指定Map的"懒惰"map
     * @throws IllegalArgumentException 如果任意参数null
     * @since 1.0.0
     */
    fun <K : Any?, V : Any?> lazyMap(map: Map<K, V>, factory: Factory): Map<K, V> =
        MapUtils.lazyMap(map, factory) as Map<K, V>

    /**
     * 返回一个"懒惰"的Map， 它的值将被按需加载
     * 当传给返回的map的[Map.get]方法的key未在map中出现时，
     * 指定的工厂将创建一个新对象， 并将key和该对象放入Map.
     * 这里的工厂是一个[Transformer]， 它被传入要转换为value的key。
     * 例如:
     *
     *
     * <pre>
     * Transformer factory = new Transformer() {
     * public Object transform(Object mapKey) {
     * return new File(mapKey);
     * }
     * }
     * Map lazyMap = MapTool.lazyMap(new HashMap(), factory);
     * Object obj = lazyMap.get("C:/dev");
     * </pre>
     *
     * 当上面的代码被执行时，`obj`将包含一个新的`File`实例。
     * 而且， 这个`File`实例为Map中key为 `"C:/dev"`关联的对象。
     *
     * 如果一个"懒惰"的map被一个同步的map包装，结果将是一个简单的缓存。
     * 当要获取的对象不在缓存中时，缓存将自己调用转换工厂来创建该对象，
     * 所有操作都是在同步块中完成的。
     *
     * @param map                要设置为"懒惰"的map, 不能为null
     * @param transformerFactory 创建新对象的工厂, 不能为null
     * @return 指定Map的"懒惰"map
     * @throws IllegalArgumentException 如果任意参数null
     * @since 1.0.0
     */
    fun <K : Any?, V : Any?> lazyMap(map: Map<K, V>, transformerFactory: Transformer): Map<K, V> =
        MapUtils.lazyMap(map, transformerFactory) as Map<K, V>

    /**
     * 返回给定Map的一个维护key顺序(自然顺序)的新Map
     * 如果一个key被添加两次，顺序决定于第一次。
     * 顺序可通过keySet, value和entrySet观察。
     *
     *
     * @param map 要排序的Map, 不能为null
     * @return 排过序的Map
     * @throws IllegalArgumentException 如果指定的Map为null
     * @since 1.0.0
     */
    fun <K : Any?, V : Any?> orderedMap(map: Map<K, V>): Map<K, V> = MapUtils.orderedMap(map) as Map<K, V>

    /**
     * 创建一个多值的map，它支持将单值加入(通过调用Map.put(key, value))到key对应的集合中
     *
     *
     * @param map 要装饰的Map
     * @return 一个多值的Map，它的value的类型为ArrayList
     * @see MultiValueMap
     *
     * @since 1.0.0
     */
    fun <K : Any?, V : Any?> multiValueMap(map: Map<K, MutableCollection<out V>?>): Map<K, ArrayList<V>> =
        MapUtils.multiValueMap(map) as Map<K, ArrayList<V>>

    /**
     * 创建一个多值的map，它支持将单值加入(通过调用Map.put(key, value))到key对应的集合中
     *
     *
     * @param map             要装饰的Map
     * @param collectionClass 返回的map的值的类型(必须包含public的无参构造器并且实现Collection接口)
     * @return 一个多值的Map，它的value的类型为指定的collectionClass
     * @see MultiValueMap
     *
     * @since 1.0.0
     */
    fun <K : Any?, V : Any?, C : Collection<V>> multiValueMap(
        map: Map<K, MutableCollection<out V>?>, collectionClass: KClass<C>
    ): Map<K, C> = MapUtils.multiValueMap(map, collectionClass.java) as Map<K, C>

    /**
     * 创建一个多值的map，它支持将单值加入(通过调用Map.put(key, value))到key对应的集合中
     * 集合将由指定的集合工厂创建
     *
     *
     * @param map               要装饰的Map
     * @param collectionFactory 创建返回的Map的值的集合的工厂
     * @return 一个多值的Map，它的value的类型为指定的集合工厂创建的集合
     * @see MultiValueMap
     *
     * @since 1.0.0
     */
    fun <K : Any?, V : Any?> multiValueMap(
        map: Map<K, MutableCollection<V>?>?, collectionFactory: Factory?
    ): Map<K, MutableCollection<V>?> = MapUtils.multiValueMap(map, collectionFactory) as Map<K, MutableCollection<V>?>

    // SortedMap decorators
    // -----------------------------------------------------------------------
    /**
     * 返回指定的Map的同步有序Map
     * 您必须手动同步返回的缓冲区的迭代器，以避免不确定性的行为:
     *
     *
     * <pre>
     * Map m = MapUtils.synchronizedSortedMap(myMap);
     * Set s = m.keySet(); // outside synchronized block
     * synchronized (m) { // synchronized on MAP!
     * Iterator i = s.iterator();
     * while (i.hasNext()) {
     * process(i.next());
     * }
     * }
     * </pre>
     *
     *
     * 该方法使用[Collections]的实现.
     *
     *
     * @param map 要同步的map, 不能为null
     * @return 指定map的同步map
     * @throws IllegalArgumentException 如果指定的map为null
     * @since 1.0.0
     */
    fun <K : Any?, V : Any?> synchronizedSortedMap(map: SortedMap<K, V>): Map<K, V> =
        MapUtils.synchronizedSortedMap(map) as Map<K, V>

    /**
     * 返回一个不可修改的有序Map
     * 该方法使用在装饰器子包中的实现.
     *
     *
     * @param map 要转换为不可修改的有序Map, 不能为null
     * @return 一个不可修改的有序Map
     * @throws IllegalArgumentException 如果指定的map为null
     * @since 1.0.0
     */
    fun <K : Any?, V : Any?> unmodifiableSortedMap(map: SortedMap<K, V>): Map<K, V> =
        MapUtils.unmodifiableSortedMap(map) as Map<K, V>


    /**
     * 返回给定有序Map的一个固定大小的有序Map。
     * 该有序map不能移除和添加元素， 但是已经存在map中的元素可以被改变(例如，
     * 通过[Map.put]方法)
     *
     *
     * @param map 要固定大小的有序Map, 不能为null
     * @return 给定列表的一个固定大小的有序Map
     * @throws IllegalArgumentException      如果指定的有序Map为null
     * @throws UnsupportedOperationException 如果试图从返回的有序Map中添加或移除元素
     * @since 1.0.0
     */
    fun <K : Any?, V : Any?> fixedSizeSortedMap(map: SortedMap<K, V>): SortedMap<K, V> =
        MapUtils.fixedSizeSortedMap(map) as SortedMap<K, V>

    /**
     * 返回一个"懒惰"的有序Map， 它的值将被按需加载
     * 当传给返回的map的[Map.get]方法的key未在map中出现时，
     * 指定的工厂将创建一个新对象， 并将key和该对象放入Map
     * 例如:
     *
     *
     * <pre>
     * Factory factory = new Factory() {
     * public Object create() {
     * return new Date();
     * }
     * }
     * SortedMap lazy = MapUtils.lazySortedMap(new TreeMap(), factory);
     * Object obj = lazy.get("test");
     * </pre>
     *
     *
     * 当上面的代码被执行时，`obj`将包含一个新的`Date`实例。
     * 而且， 这个`Date`实例为Map中key为 `"test"`关联的对象。
     *
     * @param map     要设置为"懒惰"的有序map, 不能为null
     * @param factory 创建新对象的工厂, 不能为null
     * @return 指定Map的"懒惰"map
     * @throws IllegalArgumentException 如果任意参数null
     * @since 1.0.0
     */
    fun <K : Any?, V : Any?> lazySortedMap(map: SortedMap<K, V>, factory: Factory): SortedMap<K, V> =
        MapUtils.lazySortedMap(map, factory) as SortedMap<K, V>

    /**
     * 返回一个"懒惰"的有序Map， 它的值将被按需加载
     * 当传给返回的map的[Map.get]方法的key未在map中出现时，
     * 指定的工厂将创建一个新对象， 并将key和该对象放入Map.
     * 这里的工厂是一个[Transformer]， 它被传入要转换为value的key。
     * 例如:
     *
     *
     * <pre>
     * Transformer factory = new Transformer() {
     * public Object transform(Object mapKey) {
     * return new File(mapKey);
     * }
     * }
     * SortedMap lazy = MapUtils.lazySortedMap(new TreeMap(), factory);
     * Object obj = lazy.get("C:/dev");
     * </pre>
     *
     *
     * 当上面的代码被执行时，`obj`将包含一个新的`File`实例。
     * 而且， 这个`File`实例为Map中key为 `"C:/dev"`关联的对象。
     *
     *
     * 如果一个"懒惰"的map被一个同步的map包装，结果将是一个简单的缓存。
     * 当要获取的对象不在缓存中时，缓存将自己调用转换工厂来创建该对象，
     * 所有操作都是在同步块中完成的。
     *
     *
     * @param map                要设置为"懒惰"的有序map, 不能为null
     * @param transformerFactory 创建新对象的工厂, 不能为null
     * @return 指定Map的"懒惰"有序map
     * @throws IllegalArgumentException 如果任意参数null
     * @since 1.0.0
     */
    fun <K, V> lazySortedMap(map: SortedMap<K, V>, transformerFactory: Transformer): SortedMap<K, V> =
        MapUtils.lazySortedMap(map, transformerFactory) as SortedMap<K, V>

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.collections.MapUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

}