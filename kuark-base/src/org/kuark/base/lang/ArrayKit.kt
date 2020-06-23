package org.kuark.base.lang

import org.apache.commons.lang3.ArrayUtils
import org.kuark.base.log.LogFactory

/**
 * 数组操作工具类
 *
 * @since 1.0.0
 * @author admin
 * @time 2013-4-29 下午5:20:40
 */
object ArrayKit {
    private val LOG = LogFactory.getLog(ArrayKit::class)

    /**
     * 判断指定的对象是否为数组
     *
     * @param obj 待判断的对象
     * @return true: 指定的对象为数组，反之为false
     * @since 1.0.0
     * @author admin
     * @time 2013年11月22日 下午11:16:02
     */
    fun isArray(obj: Any?): Boolean {
        return obj != null && obj.javaClass.isArray
    }

    /**
     * 把Map转换为二维数组，每行两个元素，按顺序分别为map的key和value
     *
     *
     * @param map 待转换的Map
     * @return 二维数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:46:58
     */
    fun mapToArrOfArr(map: Map<*, *>?): Array<Array<Any?>> {
        var arr: Array<Array<Any?>>? = null
        if (map != null && map.isNotEmpty()) {
            arr = Array<Array<Any?>>(map.size) { arrayOfNulls(2) }
            var i = 0
            val entrySet = map.entries
            for ((key, value) in entrySet) {
                arr[i][0] = key
                arr[i][1] = value
                i++
            }
        }
        return arr ?: Array<Array<Any?>>(0) { arrayOfNulls(0) }
    }

    /**
     * 将字符串数组转化为指定数值类型的数组 <br></br>
     * 注：只支持基本数值类型：Byte, Short, Integer, Long, Float, Double
     *
     * @param strs 待转化的字符串数组，为null或空将返回空数组
     * @param clazz 转化后的数组元素类型
     * @param <T> 转化后的数组元素类型
     * @return
    </T> */
    fun <T : Number?> strArrToNumArr(
        strs: Array<String?>,
        clazz: Class<T>
    ): Array<T?> {
        if (ArrayUtils.isEmpty(strs)) {
            return arrayOfNulls<Number>(0) as Array<T?>
        }
        val arr = java.lang.reflect.Array.newInstance(clazz, strs.size) as Array<T?>
        for (i in strs.indices) {
            val str = strs[i]
            try {
                val method = clazz.getMethod("valueOf", String::class.java)
                val obj = method.invoke(null, str) as T
                arr[i] = obj
            } catch (e: NoSuchMethodException) {
                throw Exception("不支持的类型【${clazz}】!")
            } catch (e: Exception) {
                throw Exception("无法将字符串【${str}】转化为类型【${clazz}】！")
            }
        }
        return arr
    }
    /**
     * 获取一个数组(一维)的字符串值（与该工具类的toString不一样，前后不会带花括号）
     *
     * @param array 数组
     * @param stringIfNull 数组为null时将要返回字符串
     * @return 数组的字符串表示, 如果传入的数组参数为null将返回空串
     * @since 1.0.0
     * @author admin
     * @time 2015-02-03 下午5:20:40
     */
    /**
     * 获取一个数组(一维)的字符串值（与该工具类的toString不一样，前后不会带花括号）
     *
     * @param array 数组
     * @return 数组的字符串表示, 如果传入的数组参数为null将返回空串
     * @since 1.0.0
     * @author admin
     * @time 2015-02-03 下午5:20:40
     */
    @JvmOverloads
    fun toPlainString(array: Any?, stringIfNull: String = ""): String {
        val s = ArrayUtils.toString(array, stringIfNull)
        return if (s == stringIfNull) {
            stringIfNull
        } else s.substring(1, s.length - 1)
    }

    /**
     * 两个数组是否至少有一个相同元素
     *
     * @param array 数组
     * @param anotherArray　另一数组
     * @return true: 至少有一个相同元素，反之返回false
     */
    fun containsAny(array: Array<Any?>, anotherArray: Array<Any?>?): Boolean {
        for (o in array) {
            if (contains(anotherArray, o)) {
                return true
            }
        }
        return false
    }

    /**
     * 获取指定数组声明的元素类型
     *
     * @param obj 数组，为null或不是数组将返回null
     * @return 数组声明的元素类型
     */
    fun getElemClass(obj: Any): Class<*>? { //TODO junit
        if (isArray(obj)) {
            val name = obj.javaClass.name
            val className = name.substring(2, name.length - 1)
            return try {
                Class.forName(className)
            } catch (e: ClassNotFoundException) {
                LOG.error(e)
                null
            }
        }
        return null
    }
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.lang3.ArrayUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // Basic methods handling multi-dimensional arrays
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 获取一个数组的字符串值, `null`将被当作空数组
     *
     *
     *
     *
     * 支持多维数组,包括多维的基本类型数组
     *
     *
     *
     *
     * 输出的格式如`{a,b}`.
     *
     *
     * @param array 数组, 可以为`null`
     * @return 数组的字符串表示, 如果传入的数组参数为null将返回'{}'
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午5:20:40
     */
    fun toString(array: Any?): String {
        return ArrayUtils.toString(array)
    }

    /**
     *
     *
     * 获取一个数组的字符串值, 数组为null时将返回指定的字符串参数
     *
     *
     *
     *
     * 支持多维数组,包括多维的基本类型数组
     *
     *
     *
     *
     * 输出的格式如`{a,b}`.
     *
     *
     * @param array 数组, 可以为`null`
     * @param stringIfNull 数组为null时将要返回字符串
     * @return 数组的字符串表示
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午5:26:54
     */
    fun toString(array: Any?, stringIfNull: String?): String {
        return ArrayUtils.toString(array, stringIfNull)
    }

    /**
     *
     *
     * 获取数组的哈希值
     *
     *
     *
     *
     * 支持多维数组,包括多维的基本类型数组
     *
     *
     * @param array 要获取哈希值的数组, 数组为`null`将返回0
     * @return 数组的哈希值
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午5:28:58
     */
    fun hashCode(array: Any?): Int {
        return ArrayUtils.hashCode(array)
    }

    /**
     *
     *
     * 使用equals()方法比较两个数组
     *
     *
     *
     *
     * 支持多维数组,包括多维的基本类型数组
     *
     *
     * @param array1 作为左操作数数组, 可以为`null`
     * @param array2 作为右操作数数组, 可以为`null`
     * @return `true` 如果两个数组相等
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午5:34:56
     */
    fun isEquals(array1: Any?, array2: Any?): Boolean {
        return ArrayUtils.isEquals(array1, array2)
    }
    // To map
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 转化给定的数组为 [Map]. 数组的每个元素必须为 [Entry] 或一个包含至少两个元素的数组, 第一个元素被当作key,第二个元素被当作value.
     *
     *
     *
     *
     * 该方法可以被用于做初始化操作:
     *
     *
     * <pre>
     * // 创建一个颜色映射的Map
     * Map colorMap = ArrayUtils.toMap(new String[][] {{
     * {"RED", "#FF0000"},
     * {"GREEN", "#00FF00"},
     * {"BLUE", "#0000FF"}});
    </pre> *
     *
     *
     *
     * 数组为`null`将返回`null`.
     *
     *
     * @param array 一个元素为[Entry] 或一个包含至少两个元素的数组, 可以为`null`
     * @return 从数组创建的{@code Map}
     * @throws IllegalArgumentException 当数组的元素也为数组,但是包含的元素少于两个时
     * @throws IllegalArgumentException 当数组的元素即不是[Entry]也不是一个Array时
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午5:45:22
     */
    fun toMap(array: Array<Any?>?): Map<Any, Any> {
        return ArrayUtils.toMap(array)
    }
    // Generic array
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 创建一个类型安全的泛型数组
     *
     *
     *
     *
     * java语言本身不允许创建一个泛型数组:
     *
     *
     * <pre>
     * public static &lt;T&gt; T[] createAnArray(int size) {
     * return new T[size]; // 这里编译时将出错
     * }
     *
     * public static &lt;T&gt; T[] createAnArray(int size) {
     * return (T[]) new Object[size]; // 运行时将抛ClassCastException异常
     * }
    </pre> *
     *
     *
     *
     * 因此,该方法用于弥补这一缺陷.例如,一个字符串数组可以这样创建:
     *
     *
     * <pre>
     * String[] array = ArrayUtils.toArray(&quot;1&quot;, &quot;2&quot;);
     * String[] emptyArray = ArrayUtils.&lt;String&gt; toArray();
    </pre> *
     *
     *
     *
     * 这个方法的经典使用场景为: 当调用者本身使用了泛型,而它们必须被合并到一个数组时.
     *
     *
     *
     *
     * 请注意，此方法只有当提供相同类型的参数时才有意义, 这样编译器可以推断类型数组本身。 虽然可以像这样明确地选择类型:
     * `Number[] array = ArrayUtils.<Number>toArray(Integer.valueOf(42), Double.valueOf(Math.PI))` ,
     * 但相比下面的用法没有什么真正的优点: `new Number[] {Integer.valueOf(42), Double.valueOf(Math.PI)}`.
     *
     *
     * @param <T> 数组元素的类型
     * @param items 可变数组, 可以为null
     * @return 数组, 不会为null,除非传入的数组为null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午6:05:14
    </T> */
    fun <T> toArray(vararg items: T): Array<T> {
        return ArrayUtils.toArray(*items)
    }
    // Clone
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 浅克隆一个数组, 返回一个类型转换后的结果
     *
     *
     *
     *
     * 数组里的对象不会被克隆, 因此, 不支持多维数组.
     *
     *
     *
     *
     * 数组参数为`null`将返回`null`.
     *
     *
     * @param <T> 数组元素的类型
     * @param array 要被浅克隆的数组, 可以为`null`
     * @return 克隆后的数组, 数组参数为`null`将返回`null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:03:26
    </T> */
    fun <T> clone(array: Array<T>?): Array<T> {
        return ArrayUtils.clone(array)
    }

    /**
     *
     *
     * 克隆一个long元素的数组, 返回一个类型转换后的结果
     *
     *
     *
     *
     * 数组参数为`null`将返回`null`.
     *
     *
     * @param array 要被克隆的数组, 可以为`null`
     * @return 克隆后的数组, 数组参数为`null`将返回`null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:05:45
     */
    fun clone(array: LongArray?): LongArray {
        return ArrayUtils.clone(array)
    }

    /**
     *
     *
     * 克隆一个int元素的数组, 返回一个类型转换后的结果
     *
     *
     *
     *
     * 数组参数为`null`将返回`null`.
     *
     *
     * @param array 要被克隆的数组, 可以为`null`
     * @return 克隆后的数组, 数组参数为`null`将返回`null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:06:15
     */
    fun clone(array: IntArray?): IntArray {
        return ArrayUtils.clone(array)
    }

    /**
     *
     *
     * 克隆一个short元素的数组, 返回一个类型转换后的结果
     *
     *
     *
     *
     * 数组参数为`null`将返回`null`.
     *
     *
     * @param array 要被克隆的数组, 可以为`null`
     * @return 克隆后的数组, 数组参数为`null`将返回`null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:06:25
     */
    fun clone(array: ShortArray?): ShortArray {
        return ArrayUtils.clone(array)
    }

    /**
     *
     *
     * 克隆一个char元素的数组, 返回一个类型转换后的结果
     *
     *
     *
     *
     * 数组参数为`null`将返回`null`.
     *
     *
     * @param array 要被克隆的数组, 可以为`null`
     * @return 克隆后的数组, 数组参数为`null`将返回`null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:06:35
     */
    fun clone(array: CharArray?): CharArray {
        return ArrayUtils.clone(array)
    }

    /**
     *
     *
     * 克隆一个byte元素的数组, 返回一个类型转换后的结果
     *
     *
     *
     *
     * 数组参数为`null`将返回`null`.
     *
     *
     * @param array 要被克隆的数组, 可以为`null`
     * @return 克隆后的数组, 数组参数为`null`将返回`null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:06:45
     */
    fun clone(array: ByteArray?): ByteArray {
        return ArrayUtils.clone(array)
    }

    /**
     *
     *
     * 克隆一个double元素的数组, 返回一个类型转换后的结果
     *
     *
     *
     *
     * 数组参数为`null`将返回`null`.
     *
     *
     * @param array 要被克隆的数组, 可以为`null`
     * @return 克隆后的数组, 数组参数为`null`将返回`null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:07:00
     */
    fun clone(array: DoubleArray?): DoubleArray {
        return ArrayUtils.clone(array)
    }

    /**
     *
     *
     * 克隆一个float元素的数组, 返回一个类型转换后的结果
     *
     *
     *
     *
     * 数组参数为`null`将返回`null`.
     *
     *
     * @param array 要被克隆的数组, 可以为`null`
     * @return 克隆后的数组, 数组参数为`null`将返回`null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:07:10
     */
    fun clone(array: FloatArray?): FloatArray {
        return ArrayUtils.clone(array)
    }

    /**
     *
     *
     * 克隆一个boolean元素的数组, 返回一个类型转换后的结果
     *
     *
     *
     *
     * 数组参数为`null`将返回`null`.
     *
     *
     * @param array 要被克隆的数组, 可以为`null`
     * @return 克隆后的数组, 数组参数为`null`将返回`null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:07:19
     */
    fun clone(array: BooleanArray?): BooleanArray {
        return ArrayUtils.clone(array)
    }
    // nullToEmpty
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 使用防御性编程技术, 将一个`null`的数组转化为一个空的数组。
     *
     *
     *
     *
     * 数组参数如果为`null`将返回一个空的数组.
     *
     *
     *
     *
     * 作为一项内存优化技术, 传入一个空的数组将被该类中的通过`public static`定义的空数组覆盖
     *
     *
     * @param array 要检查 `null` 或 空的数组
     * @return 传入的数组. 如果传入的数组为null或空, 将返回通过`public static`定义的空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:19:32
     */
    fun nullToEmpty(array: Array<Any?>?): Array<Any> {
        return ArrayUtils.nullToEmpty(array)
    }

    /**
     *
     *
     * 使用防御性编程技术, 将一个`null`的数组转化为一个空的数组。
     *
     *
     *
     *
     * 数组参数如果为`null`将返回一个空的数组.
     *
     *
     *
     *
     * 作为一项内存优化技术, 传入一个空的数组将被该类中的通过`public static`定义的空数组覆盖
     *
     *
     * @param array 要检查 `null` 或 空的数组
     * @return 传入的数组. 如果传入的数组为null或空, 将返回通过`public static`定义的空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:19:37
     */
    fun nullToEmpty(array: Array<String?>?): Array<String> {
        return ArrayUtils.nullToEmpty(array)
    }

    /**
     *
     *
     * 使用防御性编程技术, 将一个`null`的数组转化为一个空的数组。
     *
     *
     *
     *
     * 数组参数如果为`null`将返回一个空的数组.
     *
     *
     *
     *
     * 作为一项内存优化技术, 传入一个空的数组将被该类中的通过`public static`定义的空数组覆盖
     *
     *
     * @param array 要检查 `null` 或 空的数组
     * @return 传入的数组. 如果传入的数组为null或空, 将返回通过`public static`定义的空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:19:42
     */
    fun nullToEmpty(array: LongArray?): LongArray {
        return ArrayUtils.nullToEmpty(array)
    }

    /**
     *
     *
     * 使用防御性编程技术, 将一个`null`的数组转化为一个空的数组。
     *
     *
     *
     *
     * 数组参数如果为`null`将返回一个空的数组.
     *
     *
     *
     *
     * 作为一项内存优化技术, 传入一个空的数组将被该类中的通过`public static`定义的空数组覆盖
     *
     *
     * @param array 要检查 `null` 或 空的数组
     * @return 传入的数组. 如果传入的数组为null或空, 将返回通过`public static`定义的空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:19:52
     */
    fun nullToEmpty(array: IntArray?): IntArray {
        return ArrayUtils.nullToEmpty(array)
    }

    /**
     *
     *
     * 使用防御性编程技术, 将一个`null`的数组转化为一个空的数组。
     *
     *
     *
     *
     * 数组参数如果为`null`将返回一个空的数组.
     *
     *
     *
     *
     * 作为一项内存优化技术, 传入一个空的数组将被该类中的通过`public static`定义的空数组覆盖
     *
     *
     * @param array 要检查 `null` 或 空的数组
     * @return 传入的数组. 如果传入的数组为null或空, 将返回通过`public static`定义的空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:20:12
     */
    fun nullToEmpty(array: ShortArray?): ShortArray {
        return ArrayUtils.nullToEmpty(array)
    }

    /**
     *
     *
     * 使用防御性编程技术, 将一个`null`的数组转化为一个空的数组。
     *
     *
     *
     *
     * 数组参数如果为`null`将返回一个空的数组.
     *
     *
     *
     *
     * 作为一项内存优化技术, 传入一个空的数组将被该类中的通过`public static`定义的空数组覆盖
     *
     *
     * @param array 要检查 `null` 或 空的数组
     * @return 传入的数组. 如果传入的数组为null或空, 将返回通过`public static`定义的空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:20:22
     */
    fun nullToEmpty(array: CharArray?): CharArray {
        return ArrayUtils.nullToEmpty(array)
    }

    /**
     *
     *
     * 使用防御性编程技术, 将一个`null`的数组转化为一个空的数组。
     *
     *
     *
     *
     * 数组参数如果为`null`将返回一个空的数组.
     *
     *
     *
     *
     * 作为一项内存优化技术, 传入一个空的数组将被该类中的通过`public static`定义的空数组覆盖
     *
     *
     * @param array 要检查 `null` 或 空的数组
     * @return 传入的数组. 如果传入的数组为null或空, 将返回通过`public static`定义的空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:20:28
     */
    fun nullToEmpty(array: ByteArray?): ByteArray {
        return ArrayUtils.nullToEmpty(array)
    }

    /**
     *
     *
     * 使用防御性编程技术, 将一个`null`的数组转化为一个空的数组。
     *
     *
     *
     *
     * 数组参数如果为`null`将返回一个空的数组.
     *
     *
     *
     *
     * 作为一项内存优化技术, 传入一个空的数组将被该类中的通过`public static`定义的空数组覆盖
     *
     *
     * @param array 要检查 `null` 或 空的数组
     * @return 传入的数组. 如果传入的数组为null或空, 将返回通过`public static`定义的空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:20:37
     */
    fun nullToEmpty(array: DoubleArray?): DoubleArray {
        return ArrayUtils.nullToEmpty(array)
    }

    /**
     *
     *
     * 使用防御性编程技术, 将一个`null`的数组转化为一个空的数组。
     *
     *
     *
     *
     * 数组参数如果为`null`将返回一个空的数组.
     *
     *
     *
     *
     * 作为一项内存优化技术, 传入一个空的数组将被该类中的通过`public static`定义的空数组覆盖
     *
     *
     * @param array 要检查 `null` 或 空的数组
     * @return 传入的数组. 如果传入的数组为null或空, 将返回通过`public static`定义的空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:20:44
     */
    fun nullToEmpty(array: FloatArray?): FloatArray {
        return ArrayUtils.nullToEmpty(array)
    }

    /**
     *
     *
     * 使用防御性编程技术, 将一个`null`的数组转化为一个空的数组。
     *
     *
     *
     *
     * 数组参数如果为`null`将返回一个空的数组.
     *
     *
     *
     *
     * 作为一项内存优化技术, 传入一个空的数组将被该类中的通过`public static`定义的空数组覆盖
     *
     *
     * @param array 要检查 `null` 或 空的数组
     * @return 传入的数组. 如果传入的数组为null或空, 将返回通过`public static`定义的空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:21:12
     */
    fun nullToEmpty(array: BooleanArray?): BooleanArray {
        return ArrayUtils.nullToEmpty(array)
    }

    /**
     *
     *
     * 使用防御性编程技术, 将一个`null`的数组转化为一个空的数组。
     *
     *
     *
     *
     * 数组参数如果为`null`将返回一个空的数组.
     *
     *
     *
     *
     * 作为一项内存优化技术, 传入一个空的数组将被该类中的通过`public static`定义的空数组覆盖
     *
     *
     * @param array 要检查 `null` 或 空的数组
     * @return 传入的数组. 如果传入的数组为null或空, 将返回通过`public static`定义的空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:21:21
     */
    fun nullToEmpty(array: Array<Long?>?): Array<Long> {
        return ArrayUtils.nullToEmpty(array)
    }

    /**
     *
     *
     * 使用防御性编程技术, 将一个`null`的数组转化为一个空的数组。
     *
     *
     *
     *
     * 数组参数如果为`null`将返回一个空的数组.
     *
     *
     *
     *
     * 作为一项内存优化技术, 传入一个空的数组将被该类中的通过`public static`定义的空数组覆盖
     *
     *
     * @param array 要检查 `null` 或 空的数组
     * @return 传入的数组. 如果传入的数组为null或空, 将返回通过`public static`定义的空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:21:29
     */
    fun nullToEmpty(array: Array<Int?>?): Array<Int> {
        return ArrayUtils.nullToEmpty(array)
    }

    /**
     *
     *
     * 使用防御性编程技术, 将一个`null`的数组转化为一个空的数组。
     *
     *
     *
     *
     * 数组参数如果为`null`将返回一个空的数组.
     *
     *
     *
     *
     * 作为一项内存优化技术, 传入一个空的数组将被该类中的通过`public static`定义的空数组覆盖
     *
     *
     * @param array 要检查 `null` 或 空的数组
     * @return 传入的数组. 如果传入的数组为null或空, 将返回通过`public static`定义的空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:21:31
     */
    fun nullToEmpty(array: Array<Short?>?): Array<Short> {
        return ArrayUtils.nullToEmpty(array)
    }

    /**
     *
     *
     * 使用防御性编程技术, 将一个`null`的数组转化为一个空的数组。
     *
     *
     *
     *
     * 数组参数如果为`null`将返回一个空的数组.
     *
     *
     *
     *
     * 作为一项内存优化技术, 传入一个空的数组将被该类中的通过`public static`定义的空数组覆盖
     *
     *
     * @param array 要检查 `null` 或 空的数组
     * @return 传入的数组. 如果传入的数组为null或空, 将返回通过`public static`定义的空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:24:30
     */
    fun nullToEmpty(array: Array<Char?>?): Array<Char> {
        return ArrayUtils.nullToEmpty(array)
    }

    /**
     *
     *
     * 使用防御性编程技术, 将一个`null`的数组转化为一个空的数组。
     *
     *
     *
     *
     * 数组参数如果为`null`将返回一个空的数组.
     *
     *
     *
     *
     * 作为一项内存优化技术, 传入一个空的数组将被该类中的通过`public static`定义的空数组覆盖
     *
     *
     * @param array 要检查 `null` 或 空的数组
     * @return 传入的数组. 如果传入的数组为null或空, 将返回通过`public static`定义的空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:24:36
     */
    fun nullToEmpty(array: Array<Byte?>?): Array<Byte> {
        return ArrayUtils.nullToEmpty(array)
    }

    /**
     *
     *
     * 使用防御性编程技术, 将一个`null`的数组转化为一个空的数组。
     *
     *
     *
     *
     * 数组参数如果为`null`将返回一个空的数组.
     *
     *
     *
     *
     * 作为一项内存优化技术, 传入一个空的数组将被该类中的通过`public static`定义的空数组覆盖
     *
     *
     * @param array 要检查 `null` 或 空的数组
     * @return 传入的数组. 如果传入的数组为null或空, 将返回通过`public static`定义的空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:24:45
     */
    fun nullToEmpty(array: Array<Double?>?): Array<Double> {
        return ArrayUtils.nullToEmpty(array)
    }

    /**
     *
     *
     * 使用防御性编程技术, 将一个`null`的数组转化为一个空的数组。
     *
     *
     *
     *
     * 数组参数如果为`null`将返回一个空的数组.
     *
     *
     *
     *
     * 作为一项内存优化技术, 传入一个空的数组将被该类中的通过`public static`定义的空数组覆盖
     *
     *
     * @param array 要检查 `null` 或 空的数组
     * @return 传入的数组. 如果传入的数组为null或空, 将返回通过`public static`定义的空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:24:53
     */
    fun nullToEmpty(array: Array<Float?>?): Array<Float> {
        return ArrayUtils.nullToEmpty(array)
    }

    /**
     *
     *
     * 使用防御性编程技术, 将一个`null`的数组转化为一个空的数组。
     *
     *
     *
     *
     * 数组参数如果为`null`将返回一个空的数组.
     *
     *
     *
     *
     * 作为一项内存优化技术, 传入一个空的数组将被该类中的通过`public static`定义的空数组覆盖
     *
     *
     * @param array 要检查 `null` 或 空的数组
     * @return 传入的数组. 如果传入的数组为null或空, 将返回通过`public static`定义的空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:24:59
     */
    fun nullToEmpty(array: Array<Boolean?>?): Array<Boolean> {
        return ArrayUtils.nullToEmpty(array)
    }
    // Subarrays
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 产生一个新的数组, 它包含原数组的从start下标开始到end下标的元素.
     *
     *
     *
     *
     * 开始下标有被包括,而结束下标则没有.如果传入的数组为null, 将返回null.
     *
     *
     *
     *
     * 子数组的元素类型与原数组是一样的. 因此, 如果输入的数组元素类型为 `Date`, 下面的用法是意料中的事:
     *
     *
     * <pre>
     * Date[] someDates = (Date[]) ArrayUtils.subarray(allDates, 2, 5);
    </pre> *
     *
     * @param <T> 数组元素的类型
     * @param array 数组
     * @param startIndexInclusive 开始下标(包括). 小于0当作0处理, 大于数组长度将返回一个空的数组
     * @param endIndexExclusive 结束下标(不包括). 小于开始下标将返回空数组, 大于数组长度当数组长度处理
     * @return 一个包含原数组的从start下标开始到end下标的元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:35:50
    </T> */
    fun <T> subarray(array: Array<T>?, startIndexInclusive: Int, endIndexExclusive: Int): Array<T> {
        return ArrayUtils.subarray(array, startIndexInclusive, endIndexExclusive)
    }

    /**
     *
     *
     * 产生一个新的`long`元素数组, 它包含原数组的从start下标开始到end下标的元素.
     *
     *
     *
     *
     * 开始下标有被包括,而结束下标则没有.如果传入的数组为null, 将返回null.
     *
     *
     * @param array 数组
     * @param startIndexInclusive 开始下标(包括). 小于0当作0处理, 大于数组长度将返回一个空的数组
     * @param endIndexExclusive 结束下标(不包括). 小于开始下标将返回空数组, 大于数组长度当数组长度处理
     * @return 一个包含原数组的从start下标开始到end下标的元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:37:06
     */
    fun subarray(array: LongArray?, startIndexInclusive: Int, endIndexExclusive: Int): LongArray {
        return ArrayUtils.subarray(array, startIndexInclusive, endIndexExclusive)
    }

    /**
     *
     *
     * 产生一个新的`int`元素数组, 它包含原数组的从start下标开始到end下标的元素.
     *
     *
     *
     *
     * 开始下标有被包括,而结束下标则没有.如果传入的数组为null, 将返回null.
     *
     *
     * @param array 数组
     * @param startIndexInclusive 开始下标(包括). 小于0当作0处理, 大于数组长度将返回一个空的数组
     * @param endIndexExclusive 结束下标(不包括). 小于开始下标将返回空数组, 大于数组长度当数组长度处理
     * @return 一个包含原数组的从start下标开始到end下标的元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:37:10
     */
    fun subarray(array: IntArray?, startIndexInclusive: Int, endIndexExclusive: Int): IntArray {
        return ArrayUtils.subarray(array, startIndexInclusive, endIndexExclusive)
    }

    /**
     *
     *
     * 产生一个新的`short`元素数组, 它包含原数组的从start下标开始到end下标的元素.
     *
     *
     *
     *
     * 开始下标有被包括,而结束下标则没有.如果传入的数组为null, 将返回null.
     *
     *
     * @param array 数组
     * @param startIndexInclusive 开始下标(包括). 小于0当作0处理, 大于数组长度将返回一个空的数组
     * @param endIndexExclusive 结束下标(不包括). 小于开始下标将返回空数组, 大于数组长度当数组长度处理
     * @return 一个包含原数组的从start下标开始到end下标的元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:37:13
     */
    fun subarray(array: ShortArray?, startIndexInclusive: Int, endIndexExclusive: Int): ShortArray {
        return ArrayUtils.subarray(array, startIndexInclusive, endIndexExclusive)
    }

    /**
     *
     *
     * 产生一个新的`char`元素数组, 它包含原数组的从start下标开始到end下标的元素.
     *
     *
     *
     *
     * 开始下标有被包括,而结束下标则没有.如果传入的数组为null, 将返回null.
     *
     *
     * @param array 数组
     * @param startIndexInclusive 开始下标(包括). 小于0当作0处理, 大于数组长度将返回一个空的数组
     * @param endIndexExclusive 结束下标(不包括). 小于开始下标将返回空数组, 大于数组长度当数组长度处理
     * @return 一个包含原数组的从start下标开始到end下标的元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:37:17
     */
    fun subarray(array: CharArray?, startIndexInclusive: Int, endIndexExclusive: Int): CharArray {
        return ArrayUtils.subarray(array, startIndexInclusive, endIndexExclusive)
    }

    /**
     *
     *
     * 产生一个新的`byte`元素数组, 它包含原数组的从start下标开始到end下标的元素.
     *
     *
     *
     *
     * 开始下标有被包括,而结束下标则没有.如果传入的数组为null, 将返回null.
     *
     *
     * @param array 数组
     * @param startIndexInclusive 开始下标(包括). 小于0当作0处理, 大于数组长度将返回一个空的数组
     * @param endIndexExclusive 结束下标(不包括). 小于开始下标将返回空数组, 大于数组长度当数组长度处理
     * @return 一个包含原数组的从start下标开始到end下标的元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:37:25
     */
    fun subarray(array: ByteArray?, startIndexInclusive: Int, endIndexExclusive: Int): ByteArray {
        return ArrayUtils.subarray(array, startIndexInclusive, endIndexExclusive)
    }

    /**
     *
     *
     * 产生一个新的`double`元素数组, 它包含原数组的从start下标开始到end下标的元素.
     *
     *
     *
     *
     * 开始下标有被包括,而结束下标则没有.如果传入的数组为null, 将返回null.
     *
     *
     * @param array 数组
     * @param startIndexInclusive 开始下标(包括). 小于0当作0处理, 大于数组长度将返回一个空的数组
     * @param endIndexExclusive 结束下标(不包括). 小于开始下标将返回空数组, 大于数组长度当数组长度处理
     * @return 一个包含原数组的从start下标开始到end下标的元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:37:36
     */
    fun subarray(array: DoubleArray?, startIndexInclusive: Int, endIndexExclusive: Int): DoubleArray {
        return ArrayUtils.subarray(array, startIndexInclusive, endIndexExclusive)
    }

    /**
     *
     *
     * 产生一个新的`float`元素数组, 它包含原数组的从start下标开始到end下标的元素.
     *
     *
     *
     *
     * 开始下标有被包括,而结束下标则没有.如果传入的数组为null, 将返回null.
     *
     *
     * @param array 数组
     * @param startIndexInclusive 开始下标(包括). 小于0当作0处理, 大于数组长度将返回一个空的数组
     * @param endIndexExclusive 结束下标(不包括). 小于开始下标将返回空数组, 大于数组长度当数组长度处理
     * @return 一个包含原数组的从start下标开始到end下标的元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:37:40
     */
    fun subarray(array: FloatArray?, startIndexInclusive: Int, endIndexExclusive: Int): FloatArray {
        return ArrayUtils.subarray(array, startIndexInclusive, endIndexExclusive)
    }

    /**
     *
     *
     * 产生一个新的`boolean`元素数组, 它包含原数组的从start下标开始到end下标的元素.
     *
     *
     *
     *
     * 开始下标有被包括,而结束下标则没有.如果传入的数组为null, 将返回null.
     *
     *
     * @param array 数组
     * @param startIndexInclusive 开始下标(包括). 小于0当作0处理, 大于数组长度将返回一个空的数组
     * @param endIndexExclusive 结束下标(不包括). 小于开始下标将返回空数组, 大于数组长度当数组长度处理
     * @return 一个包含原数组的从start下标开始到end下标的元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:38:06
     */
    fun subarray(array: BooleanArray?, startIndexInclusive: Int, endIndexExclusive: Int): BooleanArray {
        return ArrayUtils.subarray(array, startIndexInclusive, endIndexExclusive)
    }
    // Is same length
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 检测两个数组的长度是否一致, `null`将当作空数组
     *
     *
     *
     *
     * 数组中的任何多维元素将被忽略.
     *
     *
     * @param array1 第一个数组, 可以为 `null`
     * @param array2 第二个数组, 可以为 `null`
     * @return `true` 如果两个数组的长度一致, `null`将当作空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:43:22
     */
    fun isSameLength(array1: Array<Any?>?, array2: Array<Any?>?): Boolean {
        return ArrayUtils.isSameLength(array1, array2)
    }

    /**
     *
     *
     * 检测两个数组的长度是否一致, `null`将当作空数组
     *
     *
     * @param array1 第一个数组, 可以为 `null`
     * @param array2 第二个数组, 可以为 `null`
     * @return `true` 如果两个数组的长度一致, `null`将当作空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:43:29
     */
    fun isSameLength(array1: LongArray?, array2: LongArray?): Boolean {
        return ArrayUtils.isSameLength(array1, array2)
    }

    /**
     *
     *
     * 检测两个数组的长度是否一致, `null`将当作空数组
     *
     *
     * @param array1 第一个数组, 可以为 `null`
     * @param array2 第二个数组, 可以为 `null`
     * @return `true` 如果两个数组的长度一致, `null`将当作空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:43:39
     */
    fun isSameLength(array1: IntArray?, array2: IntArray?): Boolean {
        return ArrayUtils.isSameLength(array1, array2)
    }

    /**
     *
     *
     * 检测两个数组的长度是否一致, `null`将当作空数组
     *
     *
     * @param array1 第一个数组, 可以为 `null`
     * @param array2 第二个数组, 可以为 `null`
     * @return `true` 如果两个数组的长度一致, `null`将当作空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:43:49
     */
    fun isSameLength(array1: ShortArray?, array2: ShortArray?): Boolean {
        return ArrayUtils.isSameLength(array1, array2)
    }

    /**
     *
     *
     * 检测两个数组的长度是否一致, `null`将当作空数组
     *
     *
     * @param array1 第一个数组, 可以为 `null`
     * @param array2 第二个数组, 可以为 `null`
     * @return `true` 如果两个数组的长度一致, `null`将当作空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:43:55
     */
    fun isSameLength(array1: CharArray?, array2: CharArray?): Boolean {
        return ArrayUtils.isSameLength(array1, array2)
    }

    /**
     *
     *
     * 检测两个数组的长度是否一致, `null`将当作空数组
     *
     *
     * @param array1 第一个数组, 可以为 `null`
     * @param array2 第二个数组, 可以为 `null`
     * @return `true` 如果两个数组的长度一致, `null`将当作空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:43:59
     */
    fun isSameLength(array1: ByteArray?, array2: ByteArray?): Boolean {
        return ArrayUtils.isSameLength(array1, array2)
    }

    /**
     *
     *
     * 检测两个数组的长度是否一致, `null`将当作空数组
     *
     *
     * @param array1 第一个数组, 可以为 `null`
     * @param array2 第二个数组, 可以为 `null`
     * @return `true` 如果两个数组的长度一致, `null`将当作空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:44:07
     */
    fun isSameLength(array1: DoubleArray?, array2: DoubleArray?): Boolean {
        return ArrayUtils.isSameLength(array1, array2)
    }

    /**
     *
     *
     * 检测两个数组的长度是否一致, `null`将当作空数组
     *
     *
     * @param array1 第一个数组, 可以为 `null`
     * @param array2 第二个数组, 可以为 `null`
     * @return `true` 如果两个数组的长度一致, `null`将当作空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:44:17
     */
    fun isSameLength(array1: FloatArray?, array2: FloatArray?): Boolean {
        return ArrayUtils.isSameLength(array1, array2)
    }

    /**
     *
     *
     * 检测两个数组的长度是否一致, `null`将当作空数组
     *
     *
     * @param array1 第一个数组, 可以为 `null`
     * @param array2 第二个数组, 可以为 `null`
     * @return `true` 如果两个数组的长度一致, `null`将当作空数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:44:25
     */
    fun isSameLength(array1: BooleanArray?, array2: BooleanArray?): Boolean {
        return ArrayUtils.isSameLength(array1, array2)
    }
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 返回指定数组的长度. 该方法可以处理`Object` 数组 and 基本类型的数组.
     *
     *
     *
     *
     * 如果传入的数组为null, 将返回0
     *
     *
     * <pre>
     * ArrayUtils.getLength(null)            = 0
     * ArrayUtils.getLength([])              = 0
     * ArrayUtils.getLength([null])          = 1
     * ArrayUtils.getLength([true, false])   = 2
     * ArrayUtils.getLength([1, 2, 3])       = 3
     * ArrayUtils.getLength(["a", "b", "c"]) = 3
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @return 数组的长度, 如果传入的数组为null, 将返回0
     * @throws IllegalArgumentException 如果传入的参数不是数组.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:48:35
     */
    fun getLength(array: Any?): Int {
        return ArrayUtils.getLength(array)
    }

    /**
     *
     *
     * 检查是否两个数组是相同类型的，同时考虑到多维数组。
     *
     *
     * @param array1 第一个数组, 不能为 `null`
     * @param array2 第二个数组, 不能为 `null`
     * @return `true` 如果数组的类型匹配
     * @throws IllegalArgumentException 如果任意一个数组为 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:52:56
     */
    fun isSameType(array1: Any?, array2: Any?): Boolean {
        return ArrayUtils.isSameType(array1, array2)
    }
    // Reverse
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 反转数组
     *
     *
     *
     *
     * 没有对多维数组作特别的处理
     *
     *
     *
     *
     * 如果传入的参数为null, 将什么也不做
     *
     *
     * @param array 要反转的数组, 可以为 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:54:45
     */
    fun reverse(array: Array<Any?>?) {
        ArrayUtils.reverse(array)
    }

    /**
     *
     *
     * 反转数组
     *
     *
     *
     *
     * 如果传入的参数为null, 将什么也不做
     *
     *
     * @param array 要反转的数组, 可以为 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:55:08
     */
    fun reverse(array: LongArray?) {
        ArrayUtils.reverse(array)
    }

    /**
     *
     *
     * 反转数组
     *
     *
     *
     *
     * 如果传入的参数为null, 将什么也不做
     *
     *
     * @param array 要反转的数组, 可以为 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:55:16
     */
    fun reverse(array: IntArray?) {
        ArrayUtils.reverse(array)
    }

    /**
     *
     *
     * 反转数组
     *
     *
     *
     *
     * 如果传入的参数为null, 将什么也不做
     *
     *
     * @param array 要反转的数组, 可以为 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:55:21
     */
    fun reverse(array: ShortArray?) {
        ArrayUtils.reverse(array)
    }

    /**
     *
     *
     * 反转数组
     *
     *
     *
     *
     * 如果传入的参数为null, 将什么也不做
     *
     *
     * @param array 要反转的数组, 可以为 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:55:27
     */
    fun reverse(array: CharArray?) {
        ArrayUtils.reverse(array)
    }

    /**
     *
     *
     * 反转数组
     *
     *
     *
     *
     * 如果传入的参数为null, 将什么也不做
     *
     *
     * @param array 要反转的数组, 可以为 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:55:33
     */
    fun reverse(array: ByteArray?) {
        ArrayUtils.reverse(array)
    }

    /**
     *
     *
     * 反转数组
     *
     *
     *
     *
     * 如果传入的参数为null, 将什么也不做
     *
     *
     * @param array 要反转的数组, 可以为 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:55:39
     */
    fun reverse(array: DoubleArray?) {
        ArrayUtils.reverse(array)
    }

    /**
     *
     *
     * 反转数组
     *
     *
     *
     *
     * 如果传入的参数为null, 将什么也不做
     *
     *
     * @param array 要反转的数组, 可以为 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:55:46
     */
    fun reverse(array: FloatArray?) {
        ArrayUtils.reverse(array)
    }

    /**
     *
     *
     * 反转数组
     *
     *
     *
     *
     * 如果传入的参数为null, 将什么也不做
     *
     *
     * @param array 要反转的数组, 可以为 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:55:53
     */
    fun reverse(array: BooleanArray?) {
        ArrayUtils.reverse(array)
    }
    // IndexOf search
    // ----------------------------------------------------------------------
    // Object IndexOf
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 查找给定对象在数组中的下标
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param objectToFind 要查找的对象, 可以为 `null`
     * @return 给定对象在数组中的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午7:59:34
     */
    fun indexOf(array: Array<Any?>?, objectToFind: Any?): Int {
        return ArrayUtils.indexOf(array, objectToFind)
    }

    /**
     *
     *
     * 查找给定对象在数组中的下标, 从指定下标开始查起
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     *
     *
     * startIndex为负数将被当作0. startIndex如果大于数组长度将返回[.INDEX_NOT_FOUND] (`-1`).
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param objectToFind 要查找的对象, 可以为 `null`
     * @param startIndex 查找的起始下标
     * @return 给定对象在数组中的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:02:29
     */
    fun indexOf(array: Array<Any?>?, objectToFind: Any?, startIndex: Int): Int {
        return ArrayUtils.indexOf(array, objectToFind, startIndex)
    }

    /**
     *
     *
     * 查找给定对象在数组中的最后一次出现的下标
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param objectToFind 要查找的对象, 可以为 `null`
     * @return 给定对象在数组中最后一次出现的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:04:55
     */
    fun lastIndexOf(array: Array<Any?>?, objectToFind: Any?): Int {
        return ArrayUtils.lastIndexOf(array, objectToFind)
    }

    /**
     *
     *
     * 查找给定对象在数组中的最后一次出现的下标, 从指定下标开始查起
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     *
     *
     * startIndex为负数将返回[.INDEX_NOT_FOUND] (`-1`). startIndex如果大于数组长度将从数组尾部查起.
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param objectToFind 要查找的对象, 可以为 `null`
     * @param startIndex 开始查找的下标
     * @return 给定对象在数组中最后一次出现的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:11:12
     */
    fun lastIndexOf(array: Array<Any?>?, objectToFind: Any?, startIndex: Int): Int {
        return ArrayUtils.lastIndexOf(array, objectToFind, startIndex)
    }

    /**
     *
     *
     * 检查数组是否包含给定的对象
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`false`
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param objectToFind 要查找的对象, 可以为 `null`
     * @return `true` 如果数组包含给定的对象
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:12:59
     */
    fun contains(array: Array<Any?>?, objectToFind: Any?): Boolean {
        return ArrayUtils.contains(array, objectToFind)
    }
    // long IndexOf
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 查找给定值在数组中的下标
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return 给定值在数组中的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:16:10
     */
    fun indexOf(array: LongArray?, valueToFind: Long): Int {
        return ArrayUtils.indexOf(array, valueToFind)
    }

    /**
     *
     *
     * 查找给定值在数组中的下标, 从指定下标开始查起
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     *
     *
     * startIndex为负数将被当作0. startIndex如果大于数组长度将返回[.INDEX_NOT_FOUND] (`-1`).
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @param startIndex 开始查找的下标
     * @return 给定值在数组中的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:20:42
     */
    fun indexOf(array: LongArray?, valueToFind: Long, startIndex: Int): Int {
        return ArrayUtils.indexOf(array, valueToFind, startIndex)
    }

    /**
     *
     *
     * 查找给定值在数组中最后一次出现的下标
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return 给定值在数组中最后一次出现的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:26:35
     */
    fun lastIndexOf(array: LongArray?, valueToFind: Long): Int {
        return ArrayUtils.lastIndexOf(array, valueToFind)
    }

    /**
     *
     *
     * 查找给定值在数组中最后一次出现的下标, 从指定下标开始查起
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     *
     *
     * startIndex为负数将返回[.INDEX_NOT_FOUND] (`-1`). startIndex如果大于数组长度将从数组尾部查起.
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @param startIndex 开始查找的下标
     * @return 给定值在数组中最后一次出现的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:30:45
     */
    fun lastIndexOf(array: LongArray?, valueToFind: Long, startIndex: Int): Int {
        return ArrayUtils.lastIndexOf(array, valueToFind, startIndex)
    }

    /**
     *
     *
     * 检查数组是否包含给定的值
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`false`
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return `true` 如果数组包含给定的值
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:07:44
     */
    fun contains(array: LongArray?, valueToFind: Long): Boolean {
        return ArrayUtils.contains(array, valueToFind)
    }
    // int IndexOf
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 查找给定值在数组中的下标
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return 给定值在数组中的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:16:20
     */
    fun indexOf(array: IntArray?, valueToFind: Int): Int {
        return ArrayUtils.indexOf(array, valueToFind)
    }

    /**
     *
     *
     * 查找给定值在数组中的下标, 从指定下标开始查起
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     *
     *
     * startIndex为负数将被当作0. startIndex如果大于数组长度将返回[.INDEX_NOT_FOUND] (`-1`).
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @param startIndex 开始查找的下标
     * @return 给定值在数组中的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:20:49
     */
    fun indexOf(array: IntArray?, valueToFind: Int, startIndex: Int): Int {
        return ArrayUtils.indexOf(array, valueToFind, startIndex)
    }

    /**
     *
     *
     * 查找给定值在数组中最后一次出现的下标
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return 给定值在数组中最后一次出现的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:26:35
     */
    fun lastIndexOf(array: IntArray?, valueToFind: Int): Int {
        return ArrayUtils.lastIndexOf(array, valueToFind)
    }

    /**
     *
     *
     * 查找给定值在数组中最后一次出现的下标, 从指定下标开始查起
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     *
     *
     * startIndex为负数将返回[.INDEX_NOT_FOUND] (`-1`). startIndex如果大于数组长度将从数组尾部查起.
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @param startIndex 开始查找的下标
     * @return 给定值在数组中最后一次出现的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:30:45
     */
    fun lastIndexOf(array: IntArray?, valueToFind: Int, startIndex: Int): Int {
        return ArrayUtils.lastIndexOf(array, valueToFind, startIndex)
    }

    /**
     *
     *
     * 检查数组是否包含给定的值
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`false`
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return `true` 如果数组包含给定的值
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:07:44
     */
    fun contains(array: IntArray?, valueToFind: Int): Boolean {
        return ArrayUtils.contains(array, valueToFind)
    }
    // short IndexOf
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 查找给定值在数组中的下标
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return 给定值在数组中的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:16:30
     */
    fun indexOf(array: ShortArray?, valueToFind: Short): Int {
        return ArrayUtils.indexOf(array, valueToFind)
    }

    /**
     *
     *
     * 查找给定值在数组中的下标, 从指定下标开始查起
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     *
     *
     * startIndex为负数将被当作0. startIndex如果大于数组长度将返回[.INDEX_NOT_FOUND] (`-1`).
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @param startIndex 开始查找的下标
     * @return 给定值在数组中的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:20:57
     */
    fun indexOf(array: ShortArray?, valueToFind: Short, startIndex: Int): Int {
        return ArrayUtils.indexOf(array, valueToFind, startIndex)
    }

    /**
     *
     *
     * 查找给定值在数组中最后一次出现的下标
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return 给定值在数组中最后一次出现的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:26:35
     */
    fun lastIndexOf(array: ShortArray?, valueToFind: Short): Int {
        return ArrayUtils.lastIndexOf(array, valueToFind)
    }

    /**
     *
     *
     * 查找给定值在数组中最后一次出现的下标, 从指定下标开始查起
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     *
     *
     * startIndex为负数将返回[.INDEX_NOT_FOUND] (`-1`). startIndex如果大于数组长度将从数组尾部查起.
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @param startIndex 开始查找的下标
     * @return 给定值在数组中最后一次出现的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:30:45
     */
    fun lastIndexOf(array: ShortArray?, valueToFind: Short, startIndex: Int): Int {
        return ArrayUtils.lastIndexOf(array, valueToFind, startIndex)
    }

    /**
     *
     *
     * 检查数组是否包含给定的值
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`false`
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return `true` 如果数组包含给定的值
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:07:44
     */
    fun contains(array: ShortArray?, valueToFind: Short): Boolean {
        return ArrayUtils.contains(array, valueToFind)
    }
    // char IndexOf
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 查找给定值在数组中的下标
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return 给定值在数组中的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:16:40
     */
    fun indexOf(array: CharArray?, valueToFind: Char): Int {
        return ArrayUtils.indexOf(array, valueToFind)
    }

    /**
     *
     *
     * 查找给定值在数组中的下标, 从指定下标开始查起
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     *
     *
     * startIndex为负数将被当作0. startIndex如果大于数组长度将返回[.INDEX_NOT_FOUND] (`-1`).
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @param startIndex 开始查找的下标
     * @return 给定值在数组中的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:21:12
     */
    fun indexOf(array: CharArray?, valueToFind: Char, startIndex: Int): Int {
        return ArrayUtils.indexOf(array, valueToFind, startIndex)
    }

    /**
     *
     *
     * 查找给定值在数组中最后一次出现的下标
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return 给定值在数组中最后一次出现的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:26:35
     */
    fun lastIndexOf(array: CharArray?, valueToFind: Char): Int {
        return ArrayUtils.lastIndexOf(array, valueToFind)
    }

    /**
     *
     *
     * 查找给定值在数组中最后一次出现的下标, 从指定下标开始查起
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     *
     *
     * startIndex为负数将返回[.INDEX_NOT_FOUND] (`-1`). startIndex如果大于数组长度将从数组尾部查起.
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @param startIndex 开始查找的下标
     * @return 给定值在数组中最后一次出现的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:30:45
     */
    fun lastIndexOf(array: CharArray?, valueToFind: Char, startIndex: Int): Int {
        return ArrayUtils.lastIndexOf(array, valueToFind, startIndex)
    }

    /**
     *
     *
     * 检查数组是否包含给定的值
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`false`
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return `true` 如果数组包含给定的值
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:07:44
     */
    fun contains(array: CharArray?, valueToFind: Char): Boolean {
        return ArrayUtils.contains(array, valueToFind)
    }
    // byte IndexOf
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 查找给定值在数组中的下标
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return 给定值在数组中的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:16:50
     */
    fun indexOf(array: ByteArray?, valueToFind: Byte): Int {
        return ArrayUtils.indexOf(array, valueToFind)
    }

    /**
     *
     *
     * 查找给定值在数组中的下标, 从指定下标开始查起
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     *
     *
     * startIndex为负数将被当作0. startIndex如果大于数组长度将返回[.INDEX_NOT_FOUND] (`-1`).
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @param startIndex 开始查找的下标
     * @return 给定值在数组中的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:21:22
     */
    fun indexOf(array: ByteArray?, valueToFind: Byte, startIndex: Int): Int {
        return ArrayUtils.indexOf(array, valueToFind, startIndex)
    }

    /**
     *
     *
     * 查找给定值在数组中最后一次出现的下标
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return 给定值在数组中最后一次出现的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:26:35
     */
    fun lastIndexOf(array: ByteArray?, valueToFind: Byte): Int {
        return ArrayUtils.lastIndexOf(array, valueToFind)
    }

    /**
     *
     *
     * 查找给定值在数组中最后一次出现的下标, 从指定下标开始查起
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     *
     *
     * startIndex为负数将返回[.INDEX_NOT_FOUND] (`-1`). startIndex如果大于数组长度将从数组尾部查起.
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @param startIndex 开始查找的下标
     * @return 给定值在数组中最后一次出现的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:30:45
     */
    fun lastIndexOf(array: ByteArray?, valueToFind: Byte, startIndex: Int): Int {
        return ArrayUtils.lastIndexOf(array, valueToFind, startIndex)
    }

    /**
     *
     *
     * 检查数组是否包含给定的值
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`false`
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return `true` 如果数组包含给定的值
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:07:44
     */
    fun contains(array: ByteArray?, valueToFind: Byte): Boolean {
        return ArrayUtils.contains(array, valueToFind)
    }
    // double IndexOf
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 查找给定值在数组中的下标
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return 给定值在数组中的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:17:10
     */
    fun indexOf(array: DoubleArray?, valueToFind: Double): Int {
        return ArrayUtils.indexOf(array, valueToFind)
    }

    /**
     *
     *
     * 在指定的容差之内查找给定值在数组中的下标, 此方法将返回介于该区域(从valueToFind-容差值到valueToFind+容差值)的第一个值的索引
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @param tolerance 容差
     * @return 给定值在数组中的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:21:44
     */
    fun indexOf(array: DoubleArray?, valueToFind: Double, tolerance: Double): Int {
        return ArrayUtils.indexOf(array, valueToFind, tolerance)
    }

    /**
     *
     *
     * 查找给定值在数组中的下标, 从指定下标开始查起
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     *
     *
     * startIndex为负数将被当作0. startIndex如果大于数组长度将返回[.INDEX_NOT_FOUND] (`-1`).
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @param startIndex 开始查找的下标
     * @return 给定值在数组中的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:21:32
     */
    fun indexOf(array: DoubleArray?, valueToFind: Double, startIndex: Int): Int {
        return ArrayUtils.indexOf(array, valueToFind, startIndex)
    }

    /**
     *
     *
     * 在指定的容差之内从开始下标查找给定值在数组中的下标, 此方法将返回介于该区域(从valueToFind-容差值到valueToFind+容差值)的第一个值的索引
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     *
     *
     * startIndex为负数将被当作0. startIndex如果大于数组长度将返回[.INDEX_NOT_FOUND] (`-1`).
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @param startIndex 开始查找的下标
     * @param tolerance 容差
     * @return 给定值在数组中的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:21:44
     */
    fun indexOf(
        array: DoubleArray?,
        valueToFind: Double,
        startIndex: Int,
        tolerance: Double
    ): Int {
        return ArrayUtils.indexOf(array, valueToFind, startIndex, tolerance)
    }

    /**
     *
     *
     * 查找给定值在数组中最后一次出现的下标
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return 给定值在数组中最后一次出现的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:26:35
     */
    fun lastIndexOf(array: DoubleArray?, valueToFind: Double): Int {
        return ArrayUtils.lastIndexOf(array, valueToFind)
    }

    /**
     *
     *
     * 在指定的容差之内查找给定值在数组中最后一次出现的下标, 此方法将返回介于该区域(从valueToFind-容差值到valueToFind+容差值)的最后一个值的索引
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @param tolerance 容差
     * @return 给定值在数组中最后一次出现的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:21:44
     */
    fun lastIndexOf(array: DoubleArray?, valueToFind: Double, tolerance: Double): Int {
        return ArrayUtils.lastIndexOf(array, valueToFind, tolerance)
    }

    /**
     *
     *
     * 查找给定值在数组中最后一次出现的下标, 从指定下标开始查起
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     *
     *
     * startIndex为负数将返回[.INDEX_NOT_FOUND] (`-1`). startIndex如果大于数组长度将从数组尾部查起.
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @param startIndex 开始查找的下标
     * @return 给定值在数组中最后一次出现的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:30:45
     */
    fun lastIndexOf(array: DoubleArray?, valueToFind: Double, startIndex: Int): Int {
        return ArrayUtils.lastIndexOf(array, valueToFind, startIndex)
    }

    /**
     *
     *
     * 在指定的容差之内, 从开始下标查找给定值在数组中最后一次出现的下标, 此方法将返回介于该区域(从valueToFind-容差值到valueToFind+容差值)的最后一个值的索引
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     *
     *
     * startIndex为负数将返回[.INDEX_NOT_FOUND] (`-1`). startIndex如果大于数组长度将从数组尾部查起.
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @param startIndex 开始查找的下标
     * @param tolerance 容差
     * @return 给定值在数组中最后一次出现的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:21:44
     */
    fun lastIndexOf(
        array: DoubleArray?,
        valueToFind: Double,
        startIndex: Int,
        tolerance: Double
    ): Int {
        return ArrayUtils.lastIndexOf(array, valueToFind, startIndex, tolerance)
    }

    /**
     *
     *
     * 检查数组是否包含给定的值
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`false`
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return `true` 如果数组包含给定的值
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:07:44
     */
    fun contains(array: DoubleArray?, valueToFind: Double): Boolean {
        return ArrayUtils.contains(array, valueToFind)
    }

    /**
     *
     *
     * 检查数组在指定的容差之内是否包含给定的值, 即数组是否包含一个在从valueToFind-容差值到valueToFind+容差值范围中的一个值.
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`false`
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @param tolerance 容差值
     * @return `true` 如果数组包含容差范围中的值
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:36:44
     */
    fun contains(array: DoubleArray?, valueToFind: Double, tolerance: Double): Boolean {
        return ArrayUtils.contains(array, valueToFind, tolerance)
    }
    // float IndexOf
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 查找给定值在数组中的下标
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return 给定值在数组中的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:17:20
     */
    fun indexOf(array: FloatArray?, valueToFind: Float): Int {
        return ArrayUtils.indexOf(array, valueToFind)
    }

    /**
     *
     *
     * 查找给定值在数组中的下标, 从指定下标开始查起
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     *
     *
     * startIndex为负数将被当作0. startIndex如果大于数组长度将返回[.INDEX_NOT_FOUND] (`-1`).
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @param startIndex 开始查找的下标
     * @return 给定值在数组中的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:21:42
     */
    fun indexOf(array: FloatArray?, valueToFind: Float, startIndex: Int): Int {
        return ArrayUtils.indexOf(array, valueToFind, startIndex)
    }

    /**
     *
     *
     * 查找给定值在数组中最后一次出现的下标
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return 给定值在数组中最后一次出现的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:26:35
     */
    fun lastIndexOf(array: FloatArray?, valueToFind: Float): Int {
        return ArrayUtils.lastIndexOf(array, valueToFind)
    }

    /**
     *
     *
     * 查找给定值在数组中最后一次出现的下标, 从指定下标开始查起
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     *
     *
     * startIndex为负数将返回[.INDEX_NOT_FOUND] (`-1`). startIndex如果大于数组长度将从数组尾部查起.
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @param startIndex 开始查找的下标
     * @return 给定值在数组中最后一次出现的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:30:45
     */
    fun lastIndexOf(array: FloatArray?, valueToFind: Float, startIndex: Int): Int {
        return ArrayUtils.lastIndexOf(array, valueToFind, startIndex)
    }

    /**
     *
     *
     * 检查数组是否包含给定的值
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`false`
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return `true` 如果数组包含给定的值
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:07:44
     */
    fun contains(array: FloatArray?, valueToFind: Float): Boolean {
        return ArrayUtils.contains(array, valueToFind)
    }
    // boolean IndexOf
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 查找给定值在数组中的下标
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return 给定值在数组中的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:17:30
     */
    fun indexOf(array: BooleanArray?, valueToFind: Boolean): Int {
        return ArrayUtils.indexOf(array, valueToFind)
    }

    /**
     *
     *
     * 查找给定值在数组中的下标, 从指定下标开始查起
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     *
     *
     * startIndex为负数将被当作0. startIndex如果大于数组长度将返回[.INDEX_NOT_FOUND] (`-1`).
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @param startIndex 开始查找的下标
     * @return 给定值在数组中的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:21:52
     */
    fun indexOf(array: BooleanArray?, valueToFind: Boolean, startIndex: Int): Int {
        return ArrayUtils.indexOf(array, valueToFind, startIndex)
    }

    /**
     *
     *
     * 查找给定值在数组中最后一次出现的下标
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return 给定值在数组中最后一次出现的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:26:35
     */
    fun lastIndexOf(array: BooleanArray?, valueToFind: Boolean): Int {
        return ArrayUtils.lastIndexOf(array, valueToFind)
    }

    /**
     *
     *
     * 查找给定值在数组中最后一次出现的下标, 从指定下标开始查起
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回[.INDEX_NOT_FOUND] (`-1`)
     *
     *
     *
     *
     * startIndex为负数将返回[.INDEX_NOT_FOUND] (`-1`). startIndex如果大于数组长度将从数组尾部查起.
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @param startIndex 开始查找的下标
     * @return 给定值在数组中最后一次出现的下标, 如果没有找到或传入的数组为`null`, 将返回 [.INDEX_NOT_FOUND] (`-1`)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午8:30:45
     */
    fun lastIndexOf(array: BooleanArray?, valueToFind: Boolean, startIndex: Int): Int {
        return ArrayUtils.lastIndexOf(array, valueToFind, startIndex)
    }

    /**
     *
     *
     * 检查数组是否包含给定的值
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`false`
     *
     *
     * @param array 被查找的数组, 可以为 `null`
     * @param valueToFind 要查找的值
     * @return `true` 如果数组包含给定的值
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:07:44
     */
    fun contains(array: BooleanArray?, valueToFind: Boolean): Boolean {
        return ArrayUtils.contains(array, valueToFind)
    }
    // Primitive/Object array converters
    // ----------------------------------------------------------------------
    // Character array converters
    // ----------------------------------------------------------------------
    /**
     *
     *
     * 将一个Character的数组转化为一个char的数组
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `Character`数组, 可以为 `null`
     * @return `char` 数组, 如果传入的数组为`null`, 将返回`null`
     * @throws NullPointerException 如果数组元素为 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:42:02
     */
    fun toPrimitive(array: Array<Char?>?): CharArray {
        return ArrayUtils.toPrimitive(array)
    }

    /**
     *
     *
     * 将一个Character的数组转化为一个char的数组, 如果数组中有`null`元素, 用指定值替换
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `Character`数组, 可以为 `null`
     * @param valueForNull 要替换`null`元素的值
     * @return `char` 数组, 如果传入的数组为`null`, 将返回`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:45:02
     */
    fun toPrimitive(array: Array<Char?>?, valueForNull: Char): CharArray {
        return ArrayUtils.toPrimitive(array, valueForNull)
    }

    /**
     *
     *
     * 将一个char的数组转化为一个Character的数组
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `char` 数组
     * @return `Character` 数组, 如果传入的数组为`null`, 将返回`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:49:08
     */
    fun toObject(array: CharArray?): Array<Char> {
        return ArrayUtils.toObject(array)
    }
    // Long array converters
    // ----------------------------------------------------------------------
    /**
     *
     *
     * 将一个Long的数组转化为一个long的数组
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `Long`数组, 可以为 `null`
     * @return `long` 数组, 如果传入的数组为`null`, 将返回`null`
     * @throws NullPointerException 如果数组元素为 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:42:02
     */
    fun toPrimitive(array: Array<Long?>?): LongArray {
        return ArrayUtils.toPrimitive(array)
    }

    /**
     *
     *
     * 将一个Long的数组转化为一个long的数组, 如果数组中有`null`元素, 用指定值替换
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `Long`数组, 可以为 `null`
     * @param valueForNull 要替换`null`元素的值
     * @return `long` 数组, 如果传入的数组为`null`, 将返回`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:45:02
     */
    fun toPrimitive(array: Array<Long?>?, valueForNull: Long): LongArray {
        return ArrayUtils.toPrimitive(array, valueForNull)
    }

    /**
     *
     *
     * 将一个long的数组转化为一个Long的数组
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `long` 数组
     * @return `Long` 数组, 如果传入的数组为`null`, 将返回`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:49:08
     */
    fun toObject(array: LongArray?): Array<Long> {
        return ArrayUtils.toObject(array)
    }
    // Int array converters
    // ----------------------------------------------------------------------
    /**
     *
     *
     * 将一个Integer的数组转化为一个int的数组
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `Integer`数组, 可以为 `null`
     * @return `int` 数组, 如果传入的数组为`null`, 将返回`null`
     * @throws NullPointerException 如果数组元素为 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:42:02
     */
    fun toPrimitive(array: Array<Int?>?): IntArray {
        return ArrayUtils.toPrimitive(array)
    }

    /**
     *
     *
     * 将一个Integer的数组转化为一个int的数组, 如果数组中有`null`元素, 用指定值替换
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `Integer`数组, 可以为 `null`
     * @param valueForNull 要替换`null`元素的值
     * @return `int` 数组, 如果传入的数组为`null`, 将返回`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:45:02
     */
    fun toPrimitive(array: Array<Int?>?, valueForNull: Int): IntArray {
        return ArrayUtils.toPrimitive(array, valueForNull)
    }

    /**
     *
     *
     * 将一个int的数组转化为一个Integer的数组
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `int` 数组
     * @return `Integer` 数组, 如果传入的数组为`null`, 将返回`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:49:08
     */
    fun toObject(array: IntArray?): Array<Int> {
        return ArrayUtils.toObject(array)
    }
    // Short array converters
    // ----------------------------------------------------------------------
    /**
     *
     *
     * 将一个Short的数组转化为一个short的数组
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `Short`数组, 可以为 `null`
     * @return `short` 数组, 如果传入的数组为`null`, 将返回`null`
     * @throws NullPointerException 如果数组元素为 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:42:02
     */
    fun toPrimitive(array: Array<Short?>?): ShortArray {
        return ArrayUtils.toPrimitive(array)
    }

    /**
     *
     *
     * 将一个Short的数组转化为一个short的数组, 如果数组中有`null`元素, 用指定值替换
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `Short`数组, 可以为 `null`
     * @param valueForNull 要替换`null`元素的值
     * @return `short` 数组, 如果传入的数组为`null`, 将返回`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:45:02
     */
    fun toPrimitive(array: Array<Short?>?, valueForNull: Short): ShortArray {
        return ArrayUtils.toPrimitive(array, valueForNull)
    }

    /**
     *
     *
     * 将一个short的数组转化为一个Short的数组
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `short` 数组
     * @return `Short` 数组, 如果传入的数组为`null`, 将返回`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:49:08
     */
    fun toObject(array: ShortArray?): Array<Short> {
        return ArrayUtils.toObject(array)
    }
    // Byte array converters
    // ----------------------------------------------------------------------
    /**
     *
     *
     * 将一个Byte的数组转化为一个byte的数组
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `Byte`数组, 可以为 `null`
     * @return `byte` 数组, 如果传入的数组为`null`, 将返回`null`
     * @throws NullPointerException 如果数组元素为 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:42:02
     */
    fun toPrimitive(array: Array<Byte?>?): ByteArray {
        return ArrayUtils.toPrimitive(array)
    }

    /**
     *
     *
     * 将一个Byte的数组转化为一个byte的数组, 如果数组中有`null`元素, 用指定值替换
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `Byte`数组, 可以为 `null`
     * @param valueForNull 要替换`null`元素的值
     * @return `byte` 数组, 如果传入的数组为`null`, 将返回`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:45:02
     */
    fun toPrimitive(array: Array<Byte?>?, valueForNull: Byte): ByteArray {
        return ArrayUtils.toPrimitive(array, valueForNull)
    }

    /**
     *
     *
     * 将一个byte的数组转化为一个Byte的数组
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `byte` 数组
     * @return `Byte` 数组, 如果传入的数组为`null`, 将返回`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:49:08
     */
    fun toObject(array: ByteArray?): Array<Byte> {
        return ArrayUtils.toObject(array)
    }
    // Double array converters
    // ----------------------------------------------------------------------
    /**
     *
     *
     * 将一个Double的数组转化为一个double的数组
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `Double`数组, 可以为 `null`
     * @return `double` 数组, 如果传入的数组为`null`, 将返回`null`
     * @throws NullPointerException 如果数组元素为 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:42:02
     */
    fun toPrimitive(array: Array<Double?>?): DoubleArray {
        return ArrayUtils.toPrimitive(array)
    }

    /**
     *
     *
     * 将一个Double的数组转化为一个double的数组, 如果数组中有`null`元素, 用指定值替换
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `Double`数组, 可以为 `null`
     * @param valueForNull 要替换`null`元素的值
     * @return `double` 数组, 如果传入的数组为`null`, 将返回`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:45:02
     */
    fun toPrimitive(array: Array<Double?>?, valueForNull: Double): DoubleArray {
        return ArrayUtils.toPrimitive(array, valueForNull)
    }

    /**
     *
     *
     * 将一个double的数组转化为一个Double的数组
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `double` 数组
     * @return `Double` 数组, 如果传入的数组为`null`, 将返回`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:49:08
     */
    fun toObject(array: DoubleArray?): Array<Double> {
        return ArrayUtils.toObject(array)
    }
    // Float array converters
    // ----------------------------------------------------------------------
    /**
     *
     *
     * 将一个Float的数组转化为一个float的数组
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `Float`数组, 可以为 `null`
     * @return `float` 数组, 如果传入的数组为`null`, 将返回`null`
     * @throws NullPointerException 如果数组元素为 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:42:02
     */
    fun toPrimitive(array: Array<Float?>?): FloatArray {
        return ArrayUtils.toPrimitive(array)
    }

    /**
     *
     *
     * 将一个Float的数组转化为一个float的数组, 如果数组中有`null`元素, 用指定值替换
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `Float`数组, 可以为 `null`
     * @param valueForNull 要替换`null`元素的值
     * @return `float` 数组, 如果传入的数组为`null`, 将返回`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:45:02
     */
    fun toPrimitive(array: Array<Float?>?, valueForNull: Float): FloatArray {
        return ArrayUtils.toPrimitive(array, valueForNull)
    }

    /**
     *
     *
     * 将一个float的数组转化为一个Float的数组
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `float` 数组
     * @return `Float` 数组, 如果传入的数组为`null`, 将返回`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:49:08
     */
    fun toObject(array: FloatArray?): Array<Float> {
        return ArrayUtils.toObject(array)
    }
    // Boolean array converters
    // ----------------------------------------------------------------------
    /**
     *
     *
     * 将一个Boolean的数组转化为一个boolean的数组
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `Boolean`数组, 可以为 `null`
     * @return `boolean` 数组, 如果传入的数组为`null`, 将返回`null`
     * @throws NullPointerException 如果数组元素为 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:42:02
     */
    fun toPrimitive(array: Array<Boolean?>?): BooleanArray {
        return ArrayUtils.toPrimitive(array)
    }

    /**
     *
     *
     * 将一个Boolean的数组转化为一个boolean的数组, 如果数组中有`null`元素, 用指定值替换
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `Boolean`数组, 可以为 `null`
     * @param valueForNull 要替换`null`元素的值
     * @return `boolean` 数组, 如果传入的数组为`null`, 将返回`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:45:02
     */
    fun toPrimitive(array: Array<Boolean?>?, valueForNull: Boolean): BooleanArray {
        return ArrayUtils.toPrimitive(array, valueForNull)
    }

    /**
     *
     *
     * 将一个boolean的数组转化为一个Boolean的数组
     *
     *
     *
     *
     * 如果传入的数组为`null`, 将返回`null`
     *
     *
     * @param array `boolean` 数组
     * @return `Boolean` 数组, 如果传入的数组为`null`, 将返回`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午9:49:08
     */
    fun toObject(array: BooleanArray?): Array<Boolean> {
        return ArrayUtils.toObject(array)
    }
    // ----------------------------------------------------------------------
    /**
     *
     *
     * 检查数组是否为空或`null`
     *
     *
     * @param array 要检测的数组
     * @return `true` 如果数组为空或`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:09:54
     */
    fun isEmpty(array: Array<Any?>?): Boolean {
        return ArrayUtils.isEmpty(array)
    }

    /**
     *
     *
     * 检查数组是否为空或`null`
     *
     *
     * @param array 要检测的数组
     * @return `true` 如果数组为空或`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:09:54
     */
    fun isEmpty(array: LongArray?): Boolean {
        return ArrayUtils.isEmpty(array)
    }

    /**
     *
     *
     * 检查数组是否为空或`null`
     *
     *
     * @param array 要检测的数组
     * @return `true` 如果数组为空或`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:09:54
     */
    fun isEmpty(array: IntArray?): Boolean {
        return ArrayUtils.isEmpty(array)
    }

    /**
     *
     *
     * 检查数组是否为空或`null`
     *
     *
     * @param array 要检测的数组
     * @return `true` 如果数组为空或`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:09:54
     */
    fun isEmpty(array: ShortArray?): Boolean {
        return ArrayUtils.isEmpty(array)
    }

    /**
     *
     *
     * 检查数组是否为空或`null`
     *
     *
     * @param array 要检测的数组
     * @return `true` 如果数组为空或`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:09:54
     */
    fun isEmpty(array: CharArray?): Boolean {
        return ArrayUtils.isEmpty(array)
    }

    /**
     *
     *
     * 检查数组是否为空或`null`
     *
     *
     * @param array 要检测的数组
     * @return `true` 如果数组为空或`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:09:54
     */
    fun isEmpty(array: ByteArray?): Boolean {
        return ArrayUtils.isEmpty(array)
    }

    /**
     *
     *
     * 检查数组是否为空或`null`
     *
     *
     * @param array 要检测的数组
     * @return `true` 如果数组为空或`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:09:54
     */
    fun isEmpty(array: DoubleArray?): Boolean {
        return ArrayUtils.isEmpty(array)
    }

    /**
     *
     *
     * 检查数组是否为空或`null`
     *
     *
     * @param array 要检测的数组
     * @return `true` 如果数组为空或`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:09:54
     */
    fun isEmpty(array: FloatArray?): Boolean {
        return ArrayUtils.isEmpty(array)
    }

    /**
     *
     *
     * 检查数组是否为空或`null`
     *
     *
     * @param array 要检测的数组
     * @return `true` 如果数组为空或`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:09:54
     */
    fun isEmpty(array: BooleanArray?): Boolean {
        return ArrayUtils.isEmpty(array)
    }
    // ----------------------------------------------------------------------
    /**
     *
     *
     * 检查数组是否不为空且不为`null`
     *
     *
     * @param <T> 数组元素类型
     * @param array 要检测的数组
     * @return `true` 如果数组不为空且不为`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:12:57
    </T> */
    fun <T> isNotEmpty(array: Array<T>?): Boolean {
        return ArrayUtils.isNotEmpty(array)
    }

    /**
     *
     *
     * 检查数组是否不为空且不为`null`
     *
     *
     * @param array 要检测的数组
     * @return `true` 如果数组不为空且不为`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:12:57
     */
    fun isNotEmpty(array: LongArray?): Boolean {
        return ArrayUtils.isNotEmpty(array)
    }

    /**
     *
     *
     * 检查数组是否不为空且不为`null`
     *
     *
     * @param array 要检测的数组
     * @return `true` 如果数组不为空且不为`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:12:57
     */
    fun isNotEmpty(array: IntArray?): Boolean {
        return ArrayUtils.isNotEmpty(array)
    }

    /**
     *
     *
     * 检查数组是否不为空且不为`null`
     *
     *
     * @param array 要检测的数组
     * @return `true` 如果数组不为空且不为`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:12:57
     */
    fun isNotEmpty(array: ShortArray?): Boolean {
        return ArrayUtils.isNotEmpty(array)
    }

    /**
     *
     *
     * 检查数组是否不为空且不为`null`
     *
     *
     * @param array 要检测的数组
     * @return `true` 如果数组不为空且不为`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:12:57
     */
    fun isNotEmpty(array: CharArray?): Boolean {
        return ArrayUtils.isNotEmpty(array)
    }

    /**
     *
     *
     * 检查数组是否不为空且不为`null`
     *
     *
     * @param array 要检测的数组
     * @return `true` 如果数组不为空且不为`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:12:57
     */
    fun isNotEmpty(array: ByteArray?): Boolean {
        return ArrayUtils.isNotEmpty(array)
    }

    /**
     *
     *
     * 检查数组是否不为空且不为`null`
     *
     *
     * @param array 要检测的数组
     * @return `true` 如果数组不为空且不为`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:12:57
     */
    fun isNotEmpty(array: DoubleArray?): Boolean {
        return ArrayUtils.isNotEmpty(array)
    }

    /**
     *
     *
     * 检查数组是否不为空且不为`null`
     *
     *
     * @param array 要检测的数组
     * @return `true` 如果数组不为空且不为`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:12:57
     */
    fun isNotEmpty(array: FloatArray?): Boolean {
        return ArrayUtils.isNotEmpty(array)
    }

    /**
     *
     *
     * 检查数组是否不为空且不为`null`
     *
     *
     * @param array 要检测的数组
     * @return `true` 如果数组不为空且不为`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:12:57
     */
    fun isNotEmpty(array: BooleanArray?): Boolean {
        return ArrayUtils.isNotEmpty(array)
    }

    /**
     *
     *
     * 将给定数组的所有元素添加到一个新数组中
     *
     *
     *
     *
     * 新的数组包含`array1`和`array2`中的所有元素.总是返回一个新的数组.
     *
     *
     * <pre>
     * ArrayUtils.addAll(null, null)     = null
     * ArrayUtils.addAll(array1, null)   = cloned copy of array1
     * ArrayUtils.addAll(null, array2)   = cloned copy of array2
     * ArrayUtils.addAll([], [])         = []
     * ArrayUtils.addAll([null], [null]) = [null, null]
     * ArrayUtils.addAll(["a", "b", "c"], ["1", "2", "3"]) = ["a", "b", "c", "1", "2", "3"]
    </pre> *
     *
     * @param <T> 数组元素的类型
     * @param array1 第一个它的元素要添加到新数组的数组, 可以为 `null`
     * @param array2 第二个它的元素要添加到新数组的数组, 可以为 `null`
     * @return 新的数组. `null` 如果两个数组都为 `null`. 新数组的类型为第一个数组的类型, 除非第一个数组为null, 这样新数组的类型就为第二个数组的类型.
     * @throws IllegalArgumentException 如果数组类型不匹配
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:25:57
    </T> */
    fun <T> addAll(array1: Array<T>?, vararg array2: T): Array<T> {
        return ArrayUtils.addAll(array1, *array2)
    }

    /**
     *
     *
     * 将给定数组的所有元素添加到一个新数组中
     *
     *
     *
     *
     * 新的数组包含`array1`和`array2`中的所有元素.总是返回一个新的数组.
     *
     *
     * <pre>
     * ArrayUtils.addAll(array1, null)   = cloned copy of array1
     * ArrayUtils.addAll(null, array2)   = cloned copy of array2
     * ArrayUtils.addAll([], [])         = []
    </pre> *
     *
     * @param array1 第一个它的元素要添加到新数组的数组, 可以为 `null`
     * @param array2 第二个它的元素要添加到新数组的数组, 可以为 `null`
     * @return 新的数组, 如果两个数组都为null将返回null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:31:26
     */
    fun addAll(array1: BooleanArray?, vararg array2: Boolean): BooleanArray {
        return ArrayUtils.addAll(array1, *array2)
    }

    /**
     *
     *
     * 将给定数组的所有元素添加到一个新数组中
     *
     *
     *
     *
     * 新的数组包含`array1`和`array2`中的所有元素.总是返回一个新的数组.
     *
     *
     * <pre>
     * ArrayUtils.addAll(array1, null)   = cloned copy of array1
     * ArrayUtils.addAll(null, array2)   = cloned copy of array2
     * ArrayUtils.addAll([], [])         = []
    </pre> *
     *
     * @param array1 第一个它的元素要添加到新数组的数组, 可以为 `null`
     * @param array2 第二个它的元素要添加到新数组的数组, 可以为 `null`
     * @return 新的数组, 如果两个数组都为null将返回null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:31:26
     */
    fun addAll(array1: CharArray?, vararg array2: Char): CharArray {
        return ArrayUtils.addAll(array1, *array2)
    }

    /**
     *
     *
     * 将给定数组的所有元素添加到一个新数组中
     *
     *
     *
     *
     * 新的数组包含`array1`和`array2`中的所有元素.总是返回一个新的数组.
     *
     *
     * <pre>
     * ArrayUtils.addAll(array1, null)   = cloned copy of array1
     * ArrayUtils.addAll(null, array2)   = cloned copy of array2
     * ArrayUtils.addAll([], [])         = []
    </pre> *
     *
     * @param array1 第一个它的元素要添加到新数组的数组, 可以为 `null`
     * @param array2 第二个它的元素要添加到新数组的数组, 可以为 `null`
     * @return 新的数组, 如果两个数组都为null将返回null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:31:26
     */
    fun addAll(array1: ByteArray?, vararg array2: Byte): ByteArray {
        return ArrayUtils.addAll(array1, *array2)
    }

    /**
     *
     *
     * 将给定数组的所有元素添加到一个新数组中
     *
     *
     *
     *
     * 新的数组包含`array1`和`array2`中的所有元素.总是返回一个新的数组.
     *
     *
     * <pre>
     * ArrayUtils.addAll(array1, null)   = cloned copy of array1
     * ArrayUtils.addAll(null, array2)   = cloned copy of array2
     * ArrayUtils.addAll([], [])         = []
    </pre> *
     *
     * @param array1 第一个它的元素要添加到新数组的数组, 可以为 `null`
     * @param array2 第二个它的元素要添加到新数组的数组, 可以为 `null`
     * @return 新的数组, 如果两个数组都为null将返回null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:31:26
     */
    fun addAll(array1: ShortArray?, vararg array2: Short): ShortArray {
        return ArrayUtils.addAll(array1, *array2)
    }

    /**
     *
     *
     * 将给定数组的所有元素添加到一个新数组中
     *
     *
     *
     *
     * 新的数组包含`array1`和`array2`中的所有元素.总是返回一个新的数组.
     *
     *
     * <pre>
     * ArrayUtils.addAll(array1, null)   = cloned copy of array1
     * ArrayUtils.addAll(null, array2)   = cloned copy of array2
     * ArrayUtils.addAll([], [])         = []
    </pre> *
     *
     * @param array1 第一个它的元素要添加到新数组的数组, 可以为 `null`
     * @param array2 第二个它的元素要添加到新数组的数组, 可以为 `null`
     * @return 新的数组, 如果两个数组都为null将返回null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:31:26
     */
    fun addAll(array1: IntArray?, vararg array2: Int): IntArray {
        return ArrayUtils.addAll(array1, *array2)
    }

    /**
     *
     *
     * 将给定数组的所有元素添加到一个新数组中
     *
     *
     *
     *
     * 新的数组包含`array1`和`array2`中的所有元素.总是返回一个新的数组.
     *
     *
     * <pre>
     * ArrayUtils.addAll(array1, null)   = cloned copy of array1
     * ArrayUtils.addAll(null, array2)   = cloned copy of array2
     * ArrayUtils.addAll([], [])         = []
    </pre> *
     *
     * @param array1 第一个它的元素要添加到新数组的数组, 可以为 `null`
     * @param array2 第二个它的元素要添加到新数组的数组, 可以为 `null`
     * @return 新的数组, 如果两个数组都为null将返回null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:31:26
     */
    fun addAll(array1: LongArray?, vararg array2: Long): LongArray {
        return ArrayUtils.addAll(array1, *array2)
    }

    /**
     *
     *
     * 将给定数组的所有元素添加到一个新数组中
     *
     *
     *
     *
     * 新的数组包含`array1`和`array2`中的所有元素.总是返回一个新的数组.
     *
     *
     * <pre>
     * ArrayUtils.addAll(array1, null)   = cloned copy of array1
     * ArrayUtils.addAll(null, array2)   = cloned copy of array2
     * ArrayUtils.addAll([], [])         = []
    </pre> *
     *
     * @param array1 第一个它的元素要添加到新数组的数组, 可以为 `null`
     * @param array2 第二个它的元素要添加到新数组的数组, 可以为 `null`
     * @return 新的数组, 如果两个数组都为null将返回null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:31:26
     */
    fun addAll(array1: FloatArray?, vararg array2: Float): FloatArray {
        return ArrayUtils.addAll(array1, *array2)
    }

    /**
     *
     *
     * 将给定数组的所有元素添加到一个新数组中
     *
     *
     *
     *
     * 新的数组包含`array1`和`array2`中的所有元素.总是返回一个新的数组.
     *
     *
     * <pre>
     * ArrayUtils.addAll(array1, null)   = cloned copy of array1
     * ArrayUtils.addAll(null, array2)   = cloned copy of array2
     * ArrayUtils.addAll([], [])         = []
    </pre> *
     *
     * @param array1 第一个它的元素要添加到新数组的数组, 可以为 `null`
     * @param array2 第二个它的元素要添加到新数组的数组, 可以为 `null`
     * @return 新的数组, 如果两个数组都为null将返回null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午10:31:26
     */
    fun addAll(array1: DoubleArray?, vararg array2: Double): DoubleArray {
        return ArrayUtils.addAll(array1, *array2)
    }

    /**
     *
     *
     * 拷贝给定的数组到新数组, 并在最后添加给定的元素
     *
     *
     *
     *
     * 新数组的类型和输入的数组一样(不为null的话)
     *
     *
     *
     *
     * 如果输入的数组为`null`, 返回的新数组只包含给定的元素, 新数组元素的类型和指定的元素的类型一样, 除非该元素本身为null, 这时新数组的类型为Object[]
     *
     *
     * <pre>
     * ArrayUtils.add(null, null)      = [null]
     * ArrayUtils.add(null, "a")       = ["a"]
     * ArrayUtils.add(["a"], null)     = ["a", null]
     * ArrayUtils.add(["a"], "b")      = ["a", "b"]
     * ArrayUtils.add(["a", "b"], "c") = ["a", "b", "c"]
    </pre> *
     *
     * @param <T> 数组元素类型
     * @param array 数组, 可以为 `null`
     * @param element 添加到最后的元素, 可以为 `null`
     * @return 一个包含所有给定数组元素及放在最后的指定的元素的新数组. 新数组的类型和输入的数组一样(不为null的话), 如果输入的数组为null, 新数组的类型为指定元素的类型. 如果两个输入参数都为null,
     * 将抛出IllegalArgumentException异常.
     * @throws IllegalArgumentException 如果两个输入参数都为null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午11:03:57
    </T> */
    fun <T> add(array: Array<T>?, element: T): Array<T> {
        return ArrayUtils.add(array, element)
    }

    /**
     *
     *
     * 拷贝给定的数组到新数组, 并在最后添加给定的值
     *
     *
     *
     *
     * 如果输入的数组为`null`, 返回的新数组只包含给定的值
     *
     *
     * <pre>
     * ArrayUtils.add(null, true)          = [true]
     * ArrayUtils.add([true], false)       = [true, false]
     * ArrayUtils.add([true, false], true) = [true, false, true]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param element 添加到最后的元素, 可以为 `null`
     * @return 一个包含所有给定数组元素及放在最后的指定的值的新数组.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午11:07:38
     */
    fun add(array: BooleanArray?, element: Boolean): BooleanArray {
        return ArrayUtils.add(array, element)
    }

    /**
     *
     *
     * 拷贝给定的数组到新数组, 并在最后添加给定的值
     *
     *
     *
     *
     * 如果输入的数组为`null`, 返回的新数组只包含给定的值
     *
     *
     * <pre>
     * ArrayUtils.add(null, 0)   = [0]
     * ArrayUtils.add([1], 0)    = [1, 0]
     * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param element 添加到最后的元素, 可以为 `null`
     * @return 一个包含所有给定数组元素及放在最后的指定的值的新数组.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午11:08:38
     */
    fun add(array: ByteArray?, element: Byte): ByteArray {
        return ArrayUtils.add(array, element)
    }

    /**
     *
     *
     * 拷贝给定的数组到新数组, 并在最后添加给定的值
     *
     *
     *
     *
     * 如果输入的数组为`null`, 返回的新数组只包含给定的值
     *
     *
     * <pre>
     * ArrayUtils.add(null, '0')       = ['0']
     * ArrayUtils.add(['1'], '0')      = ['1', '0']
     * ArrayUtils.add(['1', '0'], '1') = ['1', '0', '1']
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param element 添加到最后的元素, 可以为 `null`
     * @return 一个包含所有给定数组元素及放在最后的指定的值的新数组.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午11:08:58
     */
    fun add(array: CharArray?, element: Char): CharArray {
        return ArrayUtils.add(array, element)
    }

    /**
     *
     *
     * 拷贝给定的数组到新数组, 并在最后添加给定的值
     *
     *
     *
     *
     * 如果输入的数组为`null`, 返回的新数组只包含给定的值
     *
     *
     * <pre>
     * ArrayUtils.add(null, 0)   = [0]
     * ArrayUtils.add([1], 0)    = [1, 0]
     * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param element 添加到最后的元素, 可以为 `null`
     * @return 一个包含所有给定数组元素及放在最后的指定的值的新数组.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午11:09:38
     */
    fun add(array: DoubleArray?, element: Double): DoubleArray {
        return ArrayUtils.add(array, element)
    }

    /**
     *
     *
     * 拷贝给定的数组到新数组, 并在最后添加给定的值
     *
     *
     *
     *
     * 如果输入的数组为`null`, 返回的新数组只包含给定的值
     *
     *
     * <pre>
     * ArrayUtils.add(null, 0)   = [0]
     * ArrayUtils.add([1], 0)    = [1, 0]
     * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param element 添加到最后的元素, 可以为 `null`
     * @return 一个包含所有给定数组元素及放在最后的指定的值的新数组.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午11:10:38
     */
    fun add(array: FloatArray?, element: Float): FloatArray {
        return ArrayUtils.add(array, element)
    }

    /**
     *
     *
     * 拷贝给定的数组到新数组, 并在最后添加给定的值
     *
     *
     *
     *
     * 如果输入的数组为`null`, 返回的新数组只包含给定的值
     *
     *
     * <pre>
     * ArrayUtils.add(null, 0)   = [0]
     * ArrayUtils.add([1], 0)    = [1, 0]
     * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param element 添加到最后的元素, 可以为 `null`
     * @return 一个包含所有给定数组元素及放在最后的指定的值的新数组.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午11:11:38
     */
    fun add(array: IntArray?, element: Int): IntArray {
        return ArrayUtils.add(array, element)
    }

    /**
     *
     *
     * 拷贝给定的数组到新数组, 并在最后添加给定的值
     *
     *
     *
     *
     * 如果输入的数组为`null`, 返回的新数组只包含给定的值
     *
     *
     * <pre>
     * ArrayUtils.add(null, 0)   = [0]
     * ArrayUtils.add([1], 0)    = [1, 0]
     * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param element 添加到最后的元素, 可以为 `null`
     * @return 一个包含所有给定数组元素及放在最后的指定的值的新数组.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午11:11:58
     */
    fun add(array: LongArray?, element: Long): LongArray {
        return ArrayUtils.add(array, element)
    }

    /**
     *
     *
     * 拷贝给定的数组到新数组, 并在最后添加给定的值
     *
     *
     *
     *
     * 如果输入的数组为`null`, 返回的新数组只包含给定的值
     *
     *
     * <pre>
     * ArrayUtils.add(null, 0)   = [0]
     * ArrayUtils.add([1], 0)    = [1, 0]
     * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param element 添加到最后的元素, 可以为 `null`
     * @return 一个包含所有给定数组元素及放在最后的指定的值的新数组.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-29 下午11:12:11
     */
    fun add(array: ShortArray?, element: Short): ShortArray {
        return ArrayUtils.add(array, element)
    }

    /**
     *
     *
     * 插入指定的元素到数组中指定的位置, 指定位置及右边的所有元素都将右移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含所有指定数组的元素, 加上在指定位置插入的元素. 新数组的元素类型和输入数组的元素类型总是一样.
     *
     *
     *
     *
     * 如果输入的数组为`null`, 返回的新数组只包含给定的元素
     *
     *
     * <pre>
     * ArrayUtils.add(null, 0, null)      = [null]
     * ArrayUtils.add(null, 0, "a")       = ["a"]
     * ArrayUtils.add(["a"], 1, null)     = ["a", null]
     * ArrayUtils.add(["a"], 1, "b")      = ["a", "b"]
     * ArrayUtils.add(["a", "b"], 3, "c") = ["a", "b", "c"]
    </pre> *
     *
     * @param <T> 数组元素的类型
     * @param array 数组, 可以为 `null`
     * @param index 插入的位置
     * @param element 要插入的元素
     * @return 一个包含输入数组所有元素及指定元素的数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index > array.length).
     * @throws IllegalArgumentException 如果输入的数组和元素都为null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午09:44:11
    </T> */
    fun <T> add(array: Array<T>?, index: Int, element: T): Array<T> {
        return ArrayUtils.add(array, index, element)
    }

    /**
     *
     *
     * 插入指定的元素到数组中指定的位置, 指定位置及右边的所有元素都将右移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含所有指定数组的元素, 加上在指定位置插入的元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 返回的新数组只包含给定的元素
     *
     *
     * <pre>
     * ArrayUtils.add(null, 0, true)          = [true]
     * ArrayUtils.add([true], 0, false)       = [false, true]
     * ArrayUtils.add([false], 1, true)       = [false, true]
     * ArrayUtils.add([true, false], 1, true) = [true, true, false]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param index 插入的位置
     * @param element 要插入的元素
     * @return 一个包含输入数组所有元素及指定元素的数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index > array.length).
     * @throws IllegalArgumentException 如果输入的数组和元素都为null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午09:44:11
     */
    fun add(array: BooleanArray?, index: Int, element: Boolean): BooleanArray {
        return ArrayUtils.add(array, index, element)
    }

    /**
     *
     *
     * 插入指定的元素到数组中指定的位置, 指定位置及右边的所有元素都将右移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含所有指定数组的元素, 加上在指定位置插入的元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 返回的新数组只包含给定的元素
     *
     *
     * <pre>
     * ArrayUtils.add(null, 0, 'a')            = ['a']
     * ArrayUtils.add(['a'], 0, 'b')           = ['b', 'a']
     * ArrayUtils.add(['a', 'b'], 0, 'c')      = ['c', 'a', 'b']
     * ArrayUtils.add(['a', 'b'], 1, 'k')      = ['a', 'k', 'b']
     * ArrayUtils.add(['a', 'b', 'c'], 1, 't') = ['a', 't', 'b', 'c']
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param index 插入的位置
     * @param element 要插入的元素
     * @return 一个包含输入数组所有元素及指定元素的数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index > array.length).
     * @throws IllegalArgumentException 如果输入的数组和元素都为null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午09:44:11
     */
    fun add(array: CharArray?, index: Int, element: Char): CharArray {
        return ArrayUtils.add(array, index, element)
    }

    /**
     *
     *
     * 插入指定的元素到数组中指定的位置, 指定位置及右边的所有元素都将右移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含所有指定数组的元素, 加上在指定位置插入的元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 返回的新数组只包含给定的元素
     *
     *
     * <pre>
     * ArrayUtils.add([1], 0, 2)         = [2, 1]
     * ArrayUtils.add([2, 6], 2, 3)      = [2, 6, 3]
     * ArrayUtils.add([2, 6], 0, 1)      = [1, 2, 6]
     * ArrayUtils.add([2, 6, 3], 2, 1)   = [2, 6, 1, 3]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param index 插入的位置
     * @param element 要插入的元素
     * @return 一个包含输入数组所有元素及指定元素的数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index > array.length).
     * @throws IllegalArgumentException 如果输入的数组和元素都为null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午09:44:11
     */
    fun add(array: ByteArray?, index: Int, element: Byte): ByteArray {
        return ArrayUtils.add(array, index, element)
    }

    /**
     *
     *
     * 插入指定的元素到数组中指定的位置, 指定位置及右边的所有元素都将右移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含所有指定数组的元素, 加上在指定位置插入的元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 返回的新数组只包含给定的元素
     *
     *
     * <pre>
     * ArrayUtils.add([1], 0, 2)         = [2, 1]
     * ArrayUtils.add([2, 6], 2, 10)     = [2, 6, 10]
     * ArrayUtils.add([2, 6], 0, -4)     = [-4, 2, 6]
     * ArrayUtils.add([2, 6, 3], 2, 1)   = [2, 6, 1, 3]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param index 插入的位置
     * @param element 要插入的元素
     * @return 一个包含输入数组所有元素及指定元素的数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index > array.length).
     * @throws IllegalArgumentException 如果输入的数组和元素都为null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午09:44:11
     */
    fun add(array: ShortArray?, index: Int, element: Short): ShortArray {
        return ArrayUtils.add(array, index, element)
    }

    /**
     *
     *
     * 插入指定的元素到数组中指定的位置, 指定位置及右边的所有元素都将右移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含所有指定数组的元素, 加上在指定位置插入的元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 返回的新数组只包含给定的元素
     *
     *
     * <pre>
     * ArrayUtils.add([1], 0, 2)         = [2, 1]
     * ArrayUtils.add([2, 6], 2, 10)     = [2, 6, 10]
     * ArrayUtils.add([2, 6], 0, -4)     = [-4, 2, 6]
     * ArrayUtils.add([2, 6, 3], 2, 1)   = [2, 6, 1, 3]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param index 插入的位置
     * @param element 要插入的元素
     * @return 一个包含输入数组所有元素及指定元素的数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index > array.length).
     * @throws IllegalArgumentException 如果输入的数组和元素都为null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午09:44:11
     */
    fun add(array: IntArray?, index: Int, element: Int): IntArray {
        return ArrayUtils.add(array, index, element)
    }

    /**
     *
     *
     * 插入指定的元素到数组中指定的位置, 指定位置及右边的所有元素都将右移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含所有指定数组的元素, 加上在指定位置插入的元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 返回的新数组只包含给定的元素
     *
     *
     * <pre>
     * ArrayUtils.add([1L], 0, 2L)           = [2L, 1L]
     * ArrayUtils.add([2L, 6L], 2, 10L)      = [2L, 6L, 10L]
     * ArrayUtils.add([2L, 6L], 0, -4L)      = [-4L, 2L, 6L]
     * ArrayUtils.add([2L, 6L, 3L], 2, 1L)   = [2L, 6L, 1L, 3L]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param index 插入的位置
     * @param element 要插入的元素
     * @return 一个包含输入数组所有元素及指定元素的数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index > array.length).
     * @throws IllegalArgumentException 如果输入的数组和元素都为null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午09:44:11
     */
    fun add(array: LongArray?, index: Int, element: Long): LongArray {
        return ArrayUtils.add(array, index, element)
    }

    /**
     *
     *
     * 插入指定的元素到数组中指定的位置, 指定位置及右边的所有元素都将右移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含所有指定数组的元素, 加上在指定位置插入的元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 返回的新数组只包含给定的元素
     *
     *
     * <pre>
     * ArrayUtils.add([1.1f], 0, 2.2f)               = [2.2f, 1.1f]
     * ArrayUtils.add([2.3f, 6.4f], 2, 10.5f)        = [2.3f, 6.4f, 10.5f]
     * ArrayUtils.add([2.6f, 6.7f], 0, -4.8f)        = [-4.8f, 2.6f, 6.7f]
     * ArrayUtils.add([2.9f, 6.0f, 0.3f], 2, 1.0f)   = [2.9f, 6.0f, 1.0f, 0.3f]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param index 插入的位置
     * @param element 要插入的元素
     * @return 一个包含输入数组所有元素及指定元素的数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index > array.length).
     * @throws IllegalArgumentException 如果输入的数组和元素都为null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午09:44:11
     */
    fun add(array: FloatArray?, index: Int, element: Float): FloatArray {
        return ArrayUtils.add(array, index, element)
    }

    /**
     *
     *
     * 插入指定的元素到数组中指定的位置, 指定位置及右边的所有元素都将右移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含所有指定数组的元素, 加上在指定位置插入的元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 返回的新数组只包含给定的元素
     *
     *
     * <pre>
     * ArrayUtils.add([1.1], 0, 2.2)              = [2.2, 1.1]
     * ArrayUtils.add([2.3, 6.4], 2, 10.5)        = [2.3, 6.4, 10.5]
     * ArrayUtils.add([2.6, 6.7], 0, -4.8)        = [-4.8, 2.6, 6.7]
     * ArrayUtils.add([2.9, 6.0, 0.3], 2, 1.0)    = [2.9, 6.0, 1.0, 0.3]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param index 插入的位置
     * @param element 要插入的元素
     * @return 一个包含输入数组所有元素及指定元素的数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index > array.length).
     * @throws IllegalArgumentException 如果输入的数组和元素都为null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午09:44:11
     */
    fun add(array: DoubleArray?, index: Int, element: Double): DoubleArray {
        return ArrayUtils.add(array, index, element)
    }

    /**
     *
     *
     * 从数组中的指定位置移除对应的元素, 其后的元素依次左移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定位置外的所有元素, 新数组的元素类型和输入数组的元素类型总是一样.
     *
     *
     *
     *
     * 如果输入的数组为`null`, 将抛出IndexOutOfBoundsException异常,因此这样没有可用的位置可以指定.
     *
     *
     * <pre>
     * ArrayUtils.remove(["a"], 0)           = []
     * ArrayUtils.remove(["a", "b"], 0)      = ["b"]
     * ArrayUtils.remove(["a", "b"], 1)      = ["a"]
     * ArrayUtils.remove(["a", "b", "c"], 1) = ["a", "c"]
    </pre> *
     *
     * @param <T> 数组元素类型
     * @param array 数组, 可以为 `null`
     * @param index 要移除的元素的下标
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index >= array.length), 或如果数组参数为 `null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午10:14:02
    </T> */
    fun <T> remove(array: Array<T>?, index: Int): Array<T> {
        return ArrayUtils.remove(array, index)
    }

    /**
     *
     *
     * 从数组中移除第一个出现的指定元素, 其后的元素依次左移. 如果数组中没有包含指定的元素, 不会有任何元素从数组中移除.
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了第一个出现的指定元素外的所有元素, 新数组的元素类型和输入数组的元素类型总是一样.
     *
     *
     * <pre>
     * ArrayUtils.removeElement(null, "a")            = null
     * ArrayUtils.removeElement([], "a")              = []
     * ArrayUtils.removeElement(["a"], "b")           = ["a"]
     * ArrayUtils.removeElement(["a", "b"], "a")      = ["b"]
     * ArrayUtils.removeElement(["a", "b", "a"], "a") = ["b", "a"]
    </pre> *
     *
     * @param <T> 数组元素类型
     * @param array 数组, 可以为 `null`
     * @param element 要移除的元素
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:18:36
    </T> */
    fun <T> removeElement(array: Array<T>?, element: Any?): Array<T> {
        return ArrayUtils.removeElement(array, element)
    }

    /**
     *
     *
     * 从数组中的指定位置移除对应的元素, 其后的元素依次左移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定位置外的所有元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 将抛出IndexOutOfBoundsException异常,因此这样没有可用的位置可以指定.
     *
     *
     * <pre>
     * ArrayUtils.remove([true], 0)              = []
     * ArrayUtils.remove([true, false], 0)       = [false]
     * ArrayUtils.remove([true, false], 1)       = [true]
     * ArrayUtils.remove([true, true, false], 1) = [true, false]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param index 要移除的元素的下标
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index >= array.length), 或如果数组参数为 `null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午10:14:02
     */
    fun remove(array: BooleanArray?, index: Int): BooleanArray {
        return ArrayUtils.remove(array, index)
    }

    /**
     *
     *
     * 从数组中移除第一个出现的指定元素, 其后的元素依次左移. 如果数组中没有包含指定的元素, 不会有任何元素从数组中移除.
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了第一个出现的指定元素外的所有元素
     *
     *
     * <pre>
     * ArrayUtils.removeElement(null, true)                = null
     * ArrayUtils.removeElement([], true)                  = []
     * ArrayUtils.removeElement([true], false)             = [true]
     * ArrayUtils.removeElement([true, false], false)      = [true]
     * ArrayUtils.removeElement([true, false, true], true) = [false, true]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param element 要移除的元素
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:18:36
     */
    fun removeElement(array: BooleanArray?, element: Boolean): BooleanArray {
        return ArrayUtils.removeElement(array, element)
    }

    /**
     *
     *
     * 从数组中的指定位置移除对应的元素, 其后的元素依次左移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定位置外的所有元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 将抛出IndexOutOfBoundsException异常,因此这样没有可用的位置可以指定.
     *
     *
     * <pre>
     * ArrayUtils.remove([1], 0)          = []
     * ArrayUtils.remove([1, 0], 0)       = [0]
     * ArrayUtils.remove([1, 0], 1)       = [1]
     * ArrayUtils.remove([1, 0, 1], 1)    = [1, 1]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param index 要移除的元素的下标
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index >= array.length), 或如果数组参数为 `null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午10:14:02
     */
    fun remove(array: ByteArray?, index: Int): ByteArray {
        return ArrayUtils.remove(array, index)
    }

    /**
     *
     *
     * 从数组中移除第一个出现的指定元素, 其后的元素依次左移. 如果数组中没有包含指定的元素, 不会有任何元素从数组中移除.
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了第一个出现的指定元素外的所有元素
     *
     *
     * <pre>
     * ArrayUtils.removeElement(null, 1)        = null
     * ArrayUtils.removeElement([], 1)          = []
     * ArrayUtils.removeElement([1], 0)         = [1]
     * ArrayUtils.removeElement([1, 0], 0)      = [1]
     * ArrayUtils.removeElement([1, 0, 1], 1)   = [0, 1]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param element 要移除的元素
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:18:36
     */
    fun removeElement(array: ByteArray?, element: Byte): ByteArray {
        return ArrayUtils.removeElement(array, element)
    }

    /**
     *
     *
     * 从数组中的指定位置移除对应的元素, 其后的元素依次左移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定位置外的所有元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 将抛出IndexOutOfBoundsException异常,因此这样没有可用的位置可以指定.
     *
     *
     * <pre>
     * ArrayUtils.remove(['a'], 0)           = []
     * ArrayUtils.remove(['a', 'b'], 0)      = ['b']
     * ArrayUtils.remove(['a', 'b'], 1)      = ['a']
     * ArrayUtils.remove(['a', 'b', 'c'], 1) = ['a', 'c']
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param index 要移除的元素的下标
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index >= array.length), 或如果数组参数为 `null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午10:14:02
     */
    fun remove(array: CharArray?, index: Int): CharArray {
        return ArrayUtils.remove(array, index)
    }

    /**
     *
     *
     * 从数组中移除第一个出现的指定元素, 其后的元素依次左移. 如果数组中没有包含指定的元素, 不会有任何元素从数组中移除.
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了第一个出现的指定元素外的所有元素
     *
     *
     * <pre>
     * ArrayUtils.removeElement(null, 'a')            = null
     * ArrayUtils.removeElement([], 'a')              = []
     * ArrayUtils.removeElement(['a'], 'b')           = ['a']
     * ArrayUtils.removeElement(['a', 'b'], 'a')      = ['b']
     * ArrayUtils.removeElement(['a', 'b', 'a'], 'a') = ['b', 'a']
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param element 要移除的元素
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:18:36
     */
    fun removeElement(array: CharArray?, element: Char): CharArray {
        return ArrayUtils.removeElement(array, element)
    }

    /**
     *
     *
     * 从数组中的指定位置移除对应的元素, 其后的元素依次左移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定位置外的所有元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 将抛出IndexOutOfBoundsException异常,因此这样没有可用的位置可以指定.
     *
     *
     * <pre>
     * ArrayUtils.remove([1.1], 0)           = []
     * ArrayUtils.remove([2.5, 6.0], 0)      = [6.0]
     * ArrayUtils.remove([2.5, 6.0], 1)      = [2.5]
     * ArrayUtils.remove([2.5, 6.0, 3.8], 1) = [2.5, 3.8]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param index 要移除的元素的下标
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index >= array.length), 或如果数组参数为 `null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午10:14:02
     */
    fun remove(array: DoubleArray?, index: Int): DoubleArray {
        return ArrayUtils.remove(array, index)
    }

    /**
     *
     *
     * 从数组中移除第一个出现的指定元素, 其后的元素依次左移. 如果数组中没有包含指定的元素, 不会有任何元素从数组中移除.
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了第一个出现的指定元素外的所有元素
     *
     *
     * <pre>
     * ArrayUtils.removeElement(null, 1.1)            = null
     * ArrayUtils.removeElement([], 1.1)              = []
     * ArrayUtils.removeElement([1.1], 1.2)           = [1.1]
     * ArrayUtils.removeElement([1.1, 2.3], 1.1)      = [2.3]
     * ArrayUtils.removeElement([1.1, 2.3, 1.1], 1.1) = [2.3, 1.1]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param element 要移除的元素
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:18:36
     */
    fun removeElement(array: DoubleArray?, element: Double): DoubleArray {
        return ArrayUtils.removeElement(array, element)
    }

    /**
     *
     *
     * 从数组中的指定位置移除对应的元素, 其后的元素依次左移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定位置外的所有元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 将抛出IndexOutOfBoundsException异常,因此这样没有可用的位置可以指定.
     *
     *
     * <pre>
     * ArrayUtils.remove([1.1], 0)           = []
     * ArrayUtils.remove([2.5, 6.0], 0)      = [6.0]
     * ArrayUtils.remove([2.5, 6.0], 1)      = [2.5]
     * ArrayUtils.remove([2.5, 6.0, 3.8], 1) = [2.5, 3.8]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param index 要移除的元素的下标
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index >= array.length), 或如果数组参数为 `null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午10:14:02
     */
    fun remove(array: FloatArray?, index: Int): FloatArray {
        return ArrayUtils.remove(array, index)
    }

    /**
     *
     *
     * 从数组中移除第一个出现的指定元素, 其后的元素依次左移. 如果数组中没有包含指定的元素, 不会有任何元素从数组中移除.
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了第一个出现的指定元素外的所有元素
     *
     *
     * <pre>
     * ArrayUtils.removeElement(null, 1.1)            = null
     * ArrayUtils.removeElement([], 1.1)              = []
     * ArrayUtils.removeElement([1.1], 1.2)           = [1.1]
     * ArrayUtils.removeElement([1.1, 2.3], 1.1)      = [2.3]
     * ArrayUtils.removeElement([1.1, 2.3, 1.1], 1.1) = [2.3, 1.1]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param element 要移除的元素
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:18:36
     */
    fun removeElement(array: FloatArray?, element: Float): FloatArray {
        return ArrayUtils.removeElement(array, element)
    }

    /**
     *
     *
     * 从数组中的指定位置移除对应的元素, 其后的元素依次左移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定位置外的所有元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 将抛出IndexOutOfBoundsException异常,因此这样没有可用的位置可以指定.
     *
     *
     * <pre>
     * ArrayUtils.remove([1], 0)         = []
     * ArrayUtils.remove([2, 6], 0)      = [6]
     * ArrayUtils.remove([2, 6], 1)      = [2]
     * ArrayUtils.remove([2, 6, 3], 1)   = [2, 3]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param index 要移除的元素的下标
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index >= array.length), 或如果数组参数为 `null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午10:14:02
     */
    fun remove(array: IntArray?, index: Int): IntArray {
        return ArrayUtils.remove(array, index)
    }

    /**
     *
     *
     * 从数组中移除第一个出现的指定元素, 其后的元素依次左移. 如果数组中没有包含指定的元素, 不会有任何元素从数组中移除.
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了第一个出现的指定元素外的所有元素
     *
     *
     * <pre>
     * ArrayUtils.removeElement(null, 1)      = null
     * ArrayUtils.removeElement([], 1)        = []
     * ArrayUtils.removeElement([1], 2)       = [1]
     * ArrayUtils.removeElement([1, 3], 1)    = [3]
     * ArrayUtils.removeElement([1, 3, 1], 1) = [3, 1]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param element 要移除的元素
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:18:36
     */
    fun removeElement(array: IntArray?, element: Int): IntArray {
        return ArrayUtils.removeElement(array, element)
    }

    /**
     *
     *
     * 从数组中的指定位置移除对应的元素, 其后的元素依次左移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定位置外的所有元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 将抛出IndexOutOfBoundsException异常,因此这样没有可用的位置可以指定.
     *
     *
     * <pre>
     * ArrayUtils.remove([1], 0)         = []
     * ArrayUtils.remove([2, 6], 0)      = [6]
     * ArrayUtils.remove([2, 6], 1)      = [2]
     * ArrayUtils.remove([2, 6, 3], 1)   = [2, 3]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param index 要移除的元素的下标
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index >= array.length), 或如果数组参数为 `null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午10:14:02
     */
    fun remove(array: LongArray?, index: Int): LongArray {
        return ArrayUtils.remove(array, index)
    }

    /**
     *
     *
     * 从数组中移除第一个出现的指定元素, 其后的元素依次左移. 如果数组中没有包含指定的元素, 不会有任何元素从数组中移除.
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了第一个出现的指定元素外的所有元素
     *
     *
     * <pre>
     * ArrayUtils.removeElement(null, 1)      = null
     * ArrayUtils.removeElement([], 1)        = []
     * ArrayUtils.removeElement([1], 2)       = [1]
     * ArrayUtils.removeElement([1, 3], 1)    = [3]
     * ArrayUtils.removeElement([1, 3, 1], 1) = [3, 1]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param element 要移除的元素
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:18:36
     */
    fun removeElement(array: LongArray?, element: Long): LongArray {
        return ArrayUtils.removeElement(array, element)
    }

    /**
     *
     *
     * 从数组中的指定位置移除对应的元素, 其后的元素依次左移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定位置外的所有元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 将抛出IndexOutOfBoundsException异常,因此这样没有可用的位置可以指定.
     *
     *
     * <pre>
     * ArrayUtils.remove([1], 0)         = []
     * ArrayUtils.remove([2, 6], 0)      = [6]
     * ArrayUtils.remove([2, 6], 1)      = [2]
     * ArrayUtils.remove([2, 6, 3], 1)   = [2, 3]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param index 要移除的元素的下标
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index >= array.length), 或如果数组参数为 `null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午10:14:02
     */
    fun remove(array: ShortArray?, index: Int): ShortArray {
        return ArrayUtils.remove(array, index)
    }

    /**
     *
     *
     * 从数组中移除第一个出现的指定元素, 其后的元素依次左移. 如果数组中没有包含指定的元素, 不会有任何元素从数组中移除.
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了第一个出现的指定元素外的所有元素
     *
     *
     * <pre>
     * ArrayUtils.removeElement(null, 1)      = null
     * ArrayUtils.removeElement([], 1)        = []
     * ArrayUtils.removeElement([1], 2)       = [1]
     * ArrayUtils.removeElement([1, 3], 1)    = [3]
     * ArrayUtils.removeElement([1, 3, 1], 1) = [3, 1]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param element 要移除的元素
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:18:36
     */
    fun removeElement(array: ShortArray?, element: Short): ShortArray {
        return ArrayUtils.removeElement(array, element)
    }

    /**
     *
     *
     * 从数组中移除所有指定位置的元素, 其后的元素依次左移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定位置外的所有元素, 新数组的元素类型和输入数组的元素类型总是一样.
     *
     *
     *
     *
     * 如果输入的数组为`null`, 将抛出IndexOutOfBoundsException异常,因此这样没有可用的位置可以指定.
     *
     *
     * <pre>
     * ArrayUtils.removeAll(["a", "b", "c"], 0, 2) = ["b"]
     * ArrayUtils.removeAll(["a", "b", "c"], 1, 2) = ["a"]
    </pre> *
     *
     * @param <T> 数组元素类型
     * @param array 数组, 可以为 `null`
     * @param indices 要移除的元素的下标可变数组
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index >= array.length), 或如果数组参数为 `null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:30:21
    </T> */
    fun <T> removeAll(array: Array<T>?, vararg indices: Int): Array<T> {
        return ArrayUtils.removeAll(array, *indices)
    }

    /**
     *
     *
     * 从数组中移除所有出现的指定元素, 其后的元素依次左移.如果指定要移除的元素不在数组中,将只移除存在数组中的元素
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定元素外的所有元素, 新数组的元素类型和输入数组的元素类型总是一样.
     *
     *
     * <pre>
     * ArrayUtils.removeElements(null, "a", "b")            = null
     * ArrayUtils.removeElements([], "a", "b")              = []
     * ArrayUtils.removeElements(["a"], "b", "c")           = ["a"]
     * ArrayUtils.removeElements(["a", "b"], "a", "c")      = ["b"]
     * ArrayUtils.removeElements(["a", "b", "a"], "a")      = ["b", "a"]
     * ArrayUtils.removeElements(["a", "b", "a"], "a", "a") = ["b"]
    </pre> *
     *
     * @param <T> 数组元素类型
     * @param array 数组, 可以为 `null`
     * @param values 要从数组中移除的值
     * @return 一个包含原数组除了要移除的元素外的所有元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:38:01
    </T> */
    fun <T> removeElements(array: Array<T>?, vararg values: T): Array<T> {
        return ArrayUtils.removeElements(array, *values)
    }

    /**
     *
     *
     * 从数组中移除所有指定位置的元素, 其后的元素依次左移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定位置外的所有元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 将抛出IndexOutOfBoundsException异常,因此这样没有可用的位置可以指定.
     *
     *
     * <pre>
     * ArrayUtils.removeAll([1], 0)             = []
     * ArrayUtils.removeAll([2, 6], 0)          = [6]
     * ArrayUtils.removeAll([2, 6], 0, 1)       = []
     * ArrayUtils.removeAll([2, 6, 3], 1, 2)    = [2]
     * ArrayUtils.removeAll([2, 6, 3], 0, 2)    = [6]
     * ArrayUtils.removeAll([2, 6, 3], 0, 1, 2) = []
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param indices 要移除的元素的下标可变数组
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index >= array.length), 或如果数组参数为 `null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:30:21
     */
    fun removeAll(array: ByteArray?, vararg indices: Int): ByteArray {
        return ArrayUtils.removeAll(array, *indices)
    }

    /**
     *
     *
     * 从数组中移除所有出现的指定元素, 其后的元素依次左移.如果指定要移除的元素不在数组中,将只移除存在数组中的元素
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定元素外的所有元素
     *
     *
     * <pre>
     * ArrayUtils.removeElements(null, 1, 2)      = null
     * ArrayUtils.removeElements([], 1, 2)        = []
     * ArrayUtils.removeElements([1], 2, 3)       = [1]
     * ArrayUtils.removeElements([1, 3], 1, 2)    = [3]
     * ArrayUtils.removeElements([1, 3, 1], 1)    = [3, 1]
     * ArrayUtils.removeElements([1, 3, 1], 1, 1) = [3]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param values 要从数组中移除的值
     * @return 一个包含原数组除了要移除的元素外的所有元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:38:01
     */
    fun removeElements(array: ByteArray?, vararg values: Byte): ByteArray {
        return ArrayUtils.removeElements(array, *values)
    }

    /**
     *
     *
     * 从数组中移除所有指定位置的元素, 其后的元素依次左移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定位置外的所有元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 将抛出IndexOutOfBoundsException异常,因此这样没有可用的位置可以指定.
     *
     *
     * <pre>
     * ArrayUtils.removeAll([1], 0)             = []
     * ArrayUtils.removeAll([2, 6], 0)          = [6]
     * ArrayUtils.removeAll([2, 6], 0, 1)       = []
     * ArrayUtils.removeAll([2, 6, 3], 1, 2)    = [2]
     * ArrayUtils.removeAll([2, 6, 3], 0, 2)    = [6]
     * ArrayUtils.removeAll([2, 6, 3], 0, 1, 2) = []
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param indices 要移除的元素的下标可变数组
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index >= array.length), 或如果数组参数为 `null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:30:21
     */
    fun removeAll(array: ShortArray?, vararg indices: Int): ShortArray {
        return ArrayUtils.removeAll(array, *indices)
    }

    /**
     *
     *
     * 从数组中移除所有出现的指定元素, 其后的元素依次左移.如果指定要移除的元素不在数组中,将只移除存在数组中的元素
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定元素外的所有元素
     *
     *
     * <pre>
     * ArrayUtils.removeElements(null, 1, 2)      = null
     * ArrayUtils.removeElements([], 1, 2)        = []
     * ArrayUtils.removeElements([1], 2, 3)       = [1]
     * ArrayUtils.removeElements([1, 3], 1, 2)    = [3]
     * ArrayUtils.removeElements([1, 3, 1], 1)    = [3, 1]
     * ArrayUtils.removeElements([1, 3, 1], 1, 1) = [3]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param values 要从数组中移除的值
     * @return 一个包含原数组除了要移除的元素外的所有元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:38:01
     */
    fun removeElements(array: ShortArray?, vararg values: Short): ShortArray {
        return ArrayUtils.removeElements(array, *values)
    }

    /**
     *
     *
     * 从数组中移除所有指定位置的元素, 其后的元素依次左移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定位置外的所有元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 将抛出IndexOutOfBoundsException异常,因此这样没有可用的位置可以指定.
     *
     *
     * <pre>
     * ArrayUtils.removeAll([1], 0)             = []
     * ArrayUtils.removeAll([2, 6], 0)          = [6]
     * ArrayUtils.removeAll([2, 6], 0, 1)       = []
     * ArrayUtils.removeAll([2, 6, 3], 1, 2)    = [2]
     * ArrayUtils.removeAll([2, 6, 3], 0, 2)    = [6]
     * ArrayUtils.removeAll([2, 6, 3], 0, 1, 2) = []
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param indices 要移除的元素的下标可变数组
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index >= array.length), 或如果数组参数为 `null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:30:21
     */
    fun removeAll(array: IntArray?, vararg indices: Int): IntArray {
        return ArrayUtils.removeAll(array, *indices)
    }

    /**
     *
     *
     * 从数组中移除所有出现的指定元素, 其后的元素依次左移.如果指定要移除的元素不在数组中,将只移除存在数组中的元素
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定元素外的所有元素
     *
     *
     * <pre>
     * ArrayUtils.removeElements(null, 1, 2)      = null
     * ArrayUtils.removeElements([], 1, 2)        = []
     * ArrayUtils.removeElements([1], 2, 3)       = [1]
     * ArrayUtils.removeElements([1, 3], 1, 2)    = [3]
     * ArrayUtils.removeElements([1, 3, 1], 1)    = [3, 1]
     * ArrayUtils.removeElements([1, 3, 1], 1, 1) = [3]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param values 要从数组中移除的值
     * @return 一个包含原数组除了要移除的元素外的所有元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:38:01
     */
    fun removeElements(array: IntArray?, vararg values: Int): IntArray {
        return ArrayUtils.removeElements(array, *values)
    }

    /**
     *
     *
     * 从数组中移除所有指定位置的元素, 其后的元素依次左移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定位置外的所有元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 将抛出IndexOutOfBoundsException异常,因此这样没有可用的位置可以指定.
     *
     *
     * <pre>
     * ArrayUtils.removeAll([1], 0)             = []
     * ArrayUtils.removeAll([2, 6], 0)          = [6]
     * ArrayUtils.removeAll([2, 6], 0, 1)       = []
     * ArrayUtils.removeAll([2, 6, 3], 1, 2)    = [2]
     * ArrayUtils.removeAll([2, 6, 3], 0, 2)    = [6]
     * ArrayUtils.removeAll([2, 6, 3], 0, 1, 2) = []
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param indices 要移除的元素的下标可变数组
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index >= array.length), 或如果数组参数为 `null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:30:21
     */
    fun removeAll(array: CharArray?, vararg indices: Int): CharArray {
        return ArrayUtils.removeAll(array, *indices)
    }

    /**
     *
     *
     * 从数组中移除所有出现的指定元素, 其后的元素依次左移.如果指定要移除的元素不在数组中,将只移除存在数组中的元素
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定元素外的所有元素
     *
     *
     * <pre>
     * ArrayUtils.removeElements(null, 1, 2)      = null
     * ArrayUtils.removeElements([], 1, 2)        = []
     * ArrayUtils.removeElements([1], 2, 3)       = [1]
     * ArrayUtils.removeElements([1, 3], 1, 2)    = [3]
     * ArrayUtils.removeElements([1, 3, 1], 1)    = [3, 1]
     * ArrayUtils.removeElements([1, 3, 1], 1, 1) = [3]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param values 要从数组中移除的值
     * @return 一个包含原数组除了要移除的元素外的所有元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:38:01
     */
    fun removeElements(array: CharArray?, vararg values: Char): CharArray {
        return ArrayUtils.removeElements(array, *values)
    }

    /**
     *
     *
     * 从数组中移除所有指定位置的元素, 其后的元素依次左移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定位置外的所有元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 将抛出IndexOutOfBoundsException异常,因此这样没有可用的位置可以指定.
     *
     *
     * <pre>
     * ArrayUtils.removeAll([1], 0)             = []
     * ArrayUtils.removeAll([2, 6], 0)          = [6]
     * ArrayUtils.removeAll([2, 6], 0, 1)       = []
     * ArrayUtils.removeAll([2, 6, 3], 1, 2)    = [2]
     * ArrayUtils.removeAll([2, 6, 3], 0, 2)    = [6]
     * ArrayUtils.removeAll([2, 6, 3], 0, 1, 2) = []
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param indices 要移除的元素的下标可变数组
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index >= array.length), 或如果数组参数为 `null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:30:21
     */
    fun removeAll(array: LongArray?, vararg indices: Int): LongArray {
        return ArrayUtils.removeAll(array, *indices)
    }

    /**
     *
     *
     * 从数组中移除所有出现的指定元素, 其后的元素依次左移.如果指定要移除的元素不在数组中,将只移除存在数组中的元素
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定元素外的所有元素
     *
     *
     * <pre>
     * ArrayUtils.removeElements(null, 1, 2)      = null
     * ArrayUtils.removeElements([], 1, 2)        = []
     * ArrayUtils.removeElements([1], 2, 3)       = [1]
     * ArrayUtils.removeElements([1, 3], 1, 2)    = [3]
     * ArrayUtils.removeElements([1, 3, 1], 1)    = [3, 1]
     * ArrayUtils.removeElements([1, 3, 1], 1, 1) = [3]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param values 要从数组中移除的值
     * @return 一个包含原数组除了要移除的元素外的所有元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:38:01
     */
    fun removeElements(array: LongArray?, vararg values: Long): LongArray {
        return ArrayUtils.removeElements(array, *values)
    }

    /**
     *
     *
     * 从数组中移除所有指定位置的元素, 其后的元素依次左移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定位置外的所有元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 将抛出IndexOutOfBoundsException异常,因此这样没有可用的位置可以指定.
     *
     *
     * <pre>
     * ArrayUtils.removeAll([1], 0)             = []
     * ArrayUtils.removeAll([2, 6], 0)          = [6]
     * ArrayUtils.removeAll([2, 6], 0, 1)       = []
     * ArrayUtils.removeAll([2, 6, 3], 1, 2)    = [2]
     * ArrayUtils.removeAll([2, 6, 3], 0, 2)    = [6]
     * ArrayUtils.removeAll([2, 6, 3], 0, 1, 2) = []
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param indices 要移除的元素的下标可变数组
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index >= array.length), 或如果数组参数为 `null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:30:21
     */
    fun removeAll(array: FloatArray?, vararg indices: Int): FloatArray {
        return ArrayUtils.removeAll(array, *indices)
    }

    /**
     *
     *
     * 从数组中移除所有出现的指定元素, 其后的元素依次左移.如果指定要移除的元素不在数组中,将只移除存在数组中的元素
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定元素外的所有元素
     *
     *
     * <pre>
     * ArrayUtils.removeElements(null, 1, 2)      = null
     * ArrayUtils.removeElements([], 1, 2)        = []
     * ArrayUtils.removeElements([1], 2, 3)       = [1]
     * ArrayUtils.removeElements([1, 3], 1, 2)    = [3]
     * ArrayUtils.removeElements([1, 3, 1], 1)    = [3, 1]
     * ArrayUtils.removeElements([1, 3, 1], 1, 1) = [3]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param values 要从数组中移除的值
     * @return 一个包含原数组除了要移除的元素外的所有元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:38:01
     */
    fun removeElements(array: FloatArray?, vararg values: Float): FloatArray {
        return ArrayUtils.removeElements(array, *values)
    }

    /**
     *
     *
     * 从数组中移除所有指定位置的元素, 其后的元素依次左移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定位置外的所有元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 将抛出IndexOutOfBoundsException异常,因此这样没有可用的位置可以指定.
     *
     *
     * <pre>
     * ArrayUtils.removeAll([1], 0)             = []
     * ArrayUtils.removeAll([2, 6], 0)          = [6]
     * ArrayUtils.removeAll([2, 6], 0, 1)       = []
     * ArrayUtils.removeAll([2, 6, 3], 1, 2)    = [2]
     * ArrayUtils.removeAll([2, 6, 3], 0, 2)    = [6]
     * ArrayUtils.removeAll([2, 6, 3], 0, 1, 2) = []
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param indices 要移除的元素的下标可变数组
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index >= array.length), 或如果数组参数为 `null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:30:21
     */
    fun removeAll(array: DoubleArray?, vararg indices: Int): DoubleArray {
        return ArrayUtils.removeAll(array, *indices)
    }

    /**
     *
     *
     * 从数组中移除所有出现的指定元素, 其后的元素依次左移.如果指定要移除的元素不在数组中,将只移除存在数组中的元素
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定元素外的所有元素
     *
     *
     * <pre>
     * ArrayUtils.removeElements(null, 1, 2)      = null
     * ArrayUtils.removeElements([], 1, 2)        = []
     * ArrayUtils.removeElements([1], 2, 3)       = [1]
     * ArrayUtils.removeElements([1, 3], 1, 2)    = [3]
     * ArrayUtils.removeElements([1, 3, 1], 1)    = [3, 1]
     * ArrayUtils.removeElements([1, 3, 1], 1, 1) = [3]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param values 要从数组中移除的值
     * @return 一个包含原数组除了要移除的元素外的所有元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:38:01
     */
    fun removeElements(array: DoubleArray?, vararg values: Double): DoubleArray {
        return ArrayUtils.removeElements(array, *values)
    }

    /**
     *
     *
     * 从数组中移除所有指定位置的元素, 其后的元素依次左移
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定位置外的所有元素
     *
     *
     *
     *
     * 如果输入的数组为`null`, 将抛出IndexOutOfBoundsException异常,因此这样没有可用的位置可以指定.
     *
     *
     * <pre>
     * ArrayUtils.removeAll([true, false, true], 0, 2) = [false]
     * ArrayUtils.removeAll([true, false, true], 1, 2) = [true]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param indices 要移除的元素的下标可变数组
     * @return 一个包含原数组除了指定位置对应元素外的所有元素的新数组
     * @throws IndexOutOfBoundsException 如果下标越界 (index < 0 || index >= array.length), 或如果数组参数为 `null`.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:30:21
     */
    fun removeAll(array: BooleanArray?, vararg indices: Int): BooleanArray {
        return ArrayUtils.removeAll(array, *indices)
    }

    /**
     *
     *
     * 从数组中移除所有出现的指定元素, 其后的元素依次左移.如果指定要移除的元素不在数组中,将只移除存在数组中的元素
     *
     *
     *
     *
     * 该方法返回一个新的数组, 它包含指定数组的除了指定元素外的所有元素
     *
     *
     * <pre>
     * ArrayUtils.removeElements(null, true, false)               = null
     * ArrayUtils.removeElements([], true, false)                 = []
     * ArrayUtils.removeElements([true], false, false)            = [true]
     * ArrayUtils.removeElements([true, false], true, true)       = [false]
     * ArrayUtils.removeElements([true, false, true], true)       = [false, true]
     * ArrayUtils.removeElements([true, false, true], true, true) = [false]
    </pre> *
     *
     * @param array 数组, 可以为 `null`
     * @param values 要从数组中移除的值
     * @return 一个包含原数组除了要移除的元素外的所有元素的新数组
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 上午11:38:01
     */
    fun removeElements(array: BooleanArray?, vararg values: Boolean): BooleanArray {
        return ArrayUtils.removeElements(array, *values)
    } // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.lang3.ArrayUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
}