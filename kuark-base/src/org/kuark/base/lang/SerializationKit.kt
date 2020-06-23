package org.kuark.base.lang

import org.apache.commons.lang3.SerializationUtils
import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable

/**
 * 序列化工具类
 *
 * @since 1.0.0
 */
object SerializationKit {

    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.lang3.SerializationUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

    /**
     * 对指定的对象深度克隆
     * 该方法比直接在对象图中的所有对象重写克隆方法慢很多倍. 但是, 对于复杂的对象图, 或那些不支持深底克隆的对象, 这提供了另一种实现. 当然, 所有对象都必须实现 `Serializable`接口.
     *
     * @param <T> 待克隆的对象的类型
     * @param object 要克隆的`Serializable`对象
     * @return 克隆后的对象
     * @throws SerializationException (运行时) 如果序列化失败
     * @since 1.0.0
     */
    fun <T : Serializable?> clone(`object`: T): T = SerializationUtils.clone(`object`)

    /**
     * 序列化一个对象到指定的输出流
     * 输出流将在对象写完时关闭.这就避免了在应用程序代码中需要的finally子句，或异常处理。
     * 传入的流没有在方法内部缓存. 如果需要的话这是你的应用程序的责任.
     *
     * @param obj 要序列化为字节的对象, 可以为 null
     * @param outputStream 要写入的流, 不能为 null
     * @throws IllegalArgumentException 如果 `outputStream` 为 `null`
     * @throws SerializationException (运行时) 如果序列化失败
     * @since 1.0.0
     */
    fun serialize(obj: Serializable?, outputStream: OutputStream?) = SerializationUtils.serialize(obj, outputStream)

    /**
     * 将一个对象序列化为字节数组
     *
     * @param obj 要序列化为字节的对象, 可以为 null
     * @return 字节数组
     * @throws SerializationException (运行时) 如果序列化失败
     * @since 1.0.0
     */
    fun serialize(obj: Serializable?): ByteArray = SerializationUtils.serialize(obj)

    /**
     * 从输入流中反序列化一个对象
     * 输入流将在对象读完时关闭.这就避免了在应用程序代码中需要的finally子句，或异常处理。
     * 传入的流没有在方法内部缓存. 如果需要的话这是你的应用程序的责任.
     *
     * @param inputStream 输入流, 不能为 null
     * @return 反序列化后的对象
     * @throws IllegalArgumentException 如果 `inputStream` 为 `null`
     * @throws SerializationException (运行时) 如果反序列化失败
     * @since 1.0.0
     */
    fun deserialize(inputStream: InputStream?): Any = SerializationUtils.deserialize(inputStream)

    /**
     * 从字节数组中反序列化一个对象
     *
     * @param objectData 字节数组, 不能为 null
     * @return 反序列化后的对象
     * @throws IllegalArgumentException 如果 `objectData` 为 `null`
     * @throws SerializationException (运行时) 如果反序列化失败
     * @since 1.0.0
     */
    fun deserialize(objectData: ByteArray?): Any = SerializationUtils.deserialize(objectData)

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.lang3.SerializationUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
}