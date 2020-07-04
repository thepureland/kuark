package org.kuark.base.io

import org.apache.commons.io.IOUtils
import org.apache.commons.io.LineIterator
import java.io.*
import java.net.URI
import java.net.URL
import java.net.URLConnection

/**
 * IO操作工具类
 *
 * @author K
 * @since 1.0.0
 */
object IoKit {

    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.io.IOUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

    /**
     * 关闭一个URLConnection
     *
     * @param conn 要关闭的连接
     * @since 1.0.0
     */
    fun close(conn: URLConnection?): Unit = IOUtils.close(conn)

    //region toBuffered
    /**
     * 获取整个`InputStream`的内容，并用相同的数据当作结果InputStream
     * 该方法在以下的地方有用：
     *  源InputStream很慢.
     *  它关联网络资源，因此我们不能让它打开太久
     *  它关联可能超时网络.
     *
     * 它可以作为[.toByteArray]的参数，因为它避免不必要的资源分配和字节数组拷贝。
     *
     * 该方法内容有缓存，所以没有必要在使用`BufferedInputStream`
     *
     * @param input 要被完全缓存的Stream
     * @return 被完全缓存的Stream
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun toBufferedInputStream(input: InputStream?): InputStream? = IOUtils.toBufferedInputStream(input)

    /**
     * 如果指定的reader是[BufferedReader]，直接返回，否则，
     * 为指定的reader创建一个BufferedReader并返回
     *
     * @param reader 要被包装或直接返回的Reader
     * @return 指定的Reader 或 指定的Reader的一个新的 [BufferedReader]
     * @since 1.0.0
     */
    fun toBufferedReader(reader: Reader?): BufferedReader? = IOUtils.toBufferedReader(reader)
    //endregion toBuffered

    //region read toByteArray
    // -----------------------------------------------------------------------
    /**
     * 读取`InputStream`的内容为`byte[]`
     *
     * 该方法内部有对输入进行缓存，因此没有必要使用`BufferedInputStream`.
     *
     * @param input 要读取的 `InputStream`
     * @return 字节数组
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun toByteArray(input: InputStream): ByteArray = IOUtils.toByteArray(input)

    /**
     * 读取`InputStream`的内容为`byte[]`.
     * 当知道`InputStream`的大小时，使用该方法代替`toByteArray(InputStream)`方法。
     *
     * **注意:** 该方法在使用[IoKit.toByteArray]读入
     * 字节数组前， 检查长度能否被安全地转为int型(毕竟数组的长度不能超过Integer.MAX_VALUE)
     *
     * @param input 要读取的 `InputStream`
     * @param size `InputStream`的大小
     * @return 请求的字节数组
     * @throws IOException io错误发生或`InputStream`的大小与size参数不一致时
     * @throws IllegalArgumentException 如果size参数小于0或大于Integer.MAX_VALUE
     * @see IoKit.toByteArray
     * @since 1.0.0
     */
    fun toByteArray(input: InputStream, size: Long): ByteArray = IOUtils.toByteArray(input, size)

    /**
     * 读取`InputStream`的内容为`byte[]`.
     * 当知道`InputStream`的大小时，使用该方法代替`toByteArray(InputStream)`方法。
     *
     * @param input 要读取的 `InputStream`
     * @param size `InputStream`的大小
     * @return 请求的字节数组
     * @throws IOException io错误发生或`InputStream`的大小与size参数不一致时
     * @throws IllegalArgumentException 如果size参数小于0
     * @since 1.0.0
     */
    fun toByteArray(input: InputStream, size: Int): ByteArray = IOUtils.toByteArray(input, size)

    /**
     * 读取`Reader`的内容为`byte[]`，使用指定的编码
     * 字符编码名称可以在这里找到： [IANA](http://www.iana.org/assignments/character-sets).
     * 该方法内部有对输入进行缓存，因此没有必要使用`BufferedReader`.
     *
     * @param input 要读取的 `Reader`
     * @param encoding 编码
     * @return 字节数组
     * @throws IOException io错误发生时
     * @throws java.nio.charset.UnsupportedCharsetException 如果指定的编码不被支持
     * @since 1.0.0
     */
    fun toByteArray(input: Reader, encoding: String? = null): ByteArray = IOUtils.toByteArray(input, encoding)

    /**
     * 读取`URI`指向的内容为`byte[]`
     *
     * @param uri 要读取的内容的`URI`
     * @return 请求的字节数组
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun toByteArray(uri: URI): ByteArray = IOUtils.toByteArray(uri)

    /**
     * 读取`URL`指向的内容为`byte[]`
     *
     * @param url 要读取的内容的`URL`
     * @return 请求的字节数组
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun toByteArray(url: URL): ByteArray = IOUtils.toByteArray(url)

    /**
     * 读取`URLConnection`指向的内容为`byte[]`
     *
     * @param urlConn 要读取的内容的`URLConnection`
     * @return 请求的字节数组
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun toByteArray(urlConn: URLConnection): ByteArray = IOUtils.toByteArray(urlConn)
    //endregion read toByteArray

    //region read char[]
    /**
     * 读取`InputStream`的内容为`char[]`.
     * 使用指定的编码。
     *
     * 字符编码可以在这里找到：
     * [IANA](http://www.iana.org/assignments/character-sets).
     *
     * 该方法内部有对输入进行缓存，因此没有必要使用`BufferedInputStream`.
     *
     * @param is 要读取的 `InputStream`
     * @param encoding 使用的编码，null表示平台默认的编码
     * @return 字节数组
     * @throws IOException io错误发生时
     * @throws java.nio.charset.UnsupportedCharsetException 指定编码不被支持时
     * @since 1.0.0
     */
    fun toCharArray(`is`: InputStream, encoding: String? = null): CharArray = IOUtils.toCharArray(`is`, encoding)

    /**
     * 读取`Reader`的内容为`char[]`.
     * 该方法内部有对输入进行缓存，因此没有必要使用`BufferedReader`.
     *
     * @param input 要读取的 `Reader`
     * @return 字节数组
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun toCharArray(input: Reader): CharArray = IOUtils.toCharArray(input)
    //endregion read char[]

    //region read toString
    /**
     * 读取`InputStream`的内容为字符串.
     * 使用指定的编码。
     * 字符编码可以在这里找到：
     * [IANA](http://www.iana.org/assignments/character-sets).
     *
     * 该方法内部有对输入进行缓存，因此没有必要使用`BufferedInputStream`.
     *
     * @param input 要读取的`InputStream`
     * @param encoding 使用的编码，null表示平台默认的编码
     * @return 请求的字符串
     * @throws IOException io错误发生时
     * @throws java.nio.charset.UnsupportedCharsetException 指定的编码不被支持时
     * @since 1.0.0
     */
    fun toString(input: InputStream, encoding: String? = null): String = IOUtils.toString(input, encoding)

    /**
     * 读取`Reader`的内容为字符串.
     * 该方法内部有对输入进行缓存，因此没有必要使用`BufferedReader`.
     *
     * @param input 要读取的`Reader`
     * @return 请求的字符串
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun toString(input: Reader): String = IOUtils.toString(input)

    /**
     * 读取`URI`指向的内容为字符串.
     *
     * @param uri URI源
     * @param encoding URL指向的内容的编码名称
     * @return URI指向的内容的字符串表示
     * @throws IOException io错误发生时
     * @throws java.nio.charset.UnsupportedCharsetException 如果指定的编码不被支持
     * @since 1.0.0
     */
    fun toString(uri: URI, encoding: String? = null): String = IOUtils.toString(uri, encoding)

    /**
     * 读取`URL`指向的内容为字符串.
     *
     * @param url URL源
     * @param encoding URL指向的内容的编码名称
     * @return URI指向的内容的字符串表示
     * @if an I/O exception occurs.
     * @throws IOException io错误发生时
     * java.nio.charset.UnsupportedCharsetException 如果指定的编码不被支持
     * @since 1.0.0
     */
    fun toString(url: URL, encoding: String? = null): String = IOUtils.toString(url, encoding)

    /**
     * 读取`byte[]`的内容为字符串，使用指定的编码
     *
     * 字符编码名称可以在这里找到： [IANA](http://www.iana.org/assignments/character-sets).
     *
     * @param input 要读取的字节数组
     * @param encoding 使用的编码，null表示平台默认的编码
     * @return 请求的字符串
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun toString(input: ByteArray, encoding: String? = null): String = IOUtils.toString(input, encoding)
    //endregion read toString

    //region readLines
    /**
     * 读取`InputStream`的内容为字符串列表，
     * 每行一个实体，使用指定的编码
     * 字符编码名称可以在这里找到： [IANA](http://www.iana.org/assignments/character-sets).
     * 该方法内部有对输入进行缓存，因此没有必要使用`BufferedInputStream`.
     *
     * @param input 要读取的`InputStream`, 不能为null
     * @param encoding 使用的编码，null表示平台默认的编码
     * @return 字符串列表，不会为null
     * @throws IOException io错误发生时
     * @throws java.nio.charset.UnsupportedCharsetException 如果指定的编码不被支持
     * @since 1.0.0
     */
    fun readLines(input: InputStream, encoding: String? = null): List<String> = IOUtils.readLines(input, encoding)

    /**
     * 读取`InputStream`的内容为字符串列表，每行一个实体
     * 该方法内部有对输入进行缓存，因此没有必要使用`BufferedReader`.
     *
     * @param input 要读取的`Reader`, 不能为null
     * @return 字符串列表，不会为null
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun readLines(input: Reader): List<String> = IOUtils.readLines(input)
    //endregion readLines

    //region lineIterator
    /**
     * 返回`Reader`的一个行迭代器
     *
     * `LineIterator` 持有的打开的`Reader`的引用。
     * 当您完成迭代时，您必须关闭`Reader`以便释放内部的资源。
     * 这可以通过直接关闭`Reader`，或调用[LineIterator.close]，
     * 或调用[LineIterator.closeQuietly]方法。
     *
     * 建议的使用模式为：
     *
     * <pre>
     * try {
     * LineIterator it = IOUtils.lineIterator(reader);
     * while (it.hasNext()) {
     * String line = it.nextLine();
     * // / 对line的处理
     * }
     * } finally {
     * IOUtils.closeQuietly(reader);
     * }
     * </pre>
     * @param reader 要读取的`Reader`
     * @return 行迭代器
     * @since 1.0.0
     */
    fun lineIterator(reader: Reader): LineIterator = IOUtils.lineIterator(reader)

    /**
     * 返回`InputStream`的一个行迭代器，使用指定的编码(null为平台默认编码)
     *
     * `LineIterator` 持有的打开的`InputStream`的引用。
     * 当您完成迭代时，您必须关闭`InputStream`以便释放内部的资源。
     * 这可以通过直接关闭`InputStream`，或调用[LineIterator.close]，
     * 或调用[LineIterator.closeQuietly]方法。
     *
     * 建议的使用模式为：
     *
     * <pre>
     * try {
     * LineIterator it = IOUtils.lineIterator(stream, &quot;UTF-8&quot;);
     * while (it.hasNext()) {
     * String line = it.nextLine();
     * // / 对line的处理
     * }
     * } finally {
     * IOUtils.closeQuietly(stream);
     * }
     * </pre>
     * @param input 要读取的`InputStream`, 不能为null
     * @param encoding 使用的编码，null表示平台默认的编码
     * @return 行迭代器
     * @throws IOException io错误发生时
     * @throws java.nio.charset.UnsupportedCharsetException 如果指定的编码不被支持
     * @since 1.0.0
     */
    fun lineIterator(input: InputStream, encoding: String? = null): LineIterator = IOUtils.lineIterator(input, encoding)
    //endregion lineIterator

    //region toInputStream
    /**
     * 转换指定的CharSequence为InputStream，使用指定的编码
     * 字符编码名称可以在这里找到： [IANA](http://www.iana.org/assignments/character-sets).
     *
     * @param input 待转换的CharSequence
     * @param encoding 使用的编码，null表示平台默认的编码
     * @return 输入流
     * @throws IOException io错误发生时
     * @throws java.nio.charset.UnsupportedCharsetException 如果指定的编码不被支持
     * @since 1.0.0
     */
    fun toInputStream(input: CharSequence, encoding: String? = null): InputStream =
        IOUtils.toInputStream(input, encoding)

    /**
     * 转换指定的字符串为InputStream，使用指定的编码
     * 字符编码名称可以在这里找到： [IANA](http://www.iana.org/assignments/character-sets).
     *
     * @param input 待转换的字符串
     * @param encoding 使用的编码，null表示平台默认的编码
     * @return 输入流
     * @throws IOException io错误发生时
     * @throws java.nio.charset.UnsupportedCharsetException 如果指定的编码不被支持
     * @since 1.0.0
     */
    fun toInputStream(input: String, encoding: String? = null): InputStream = IOUtils.toInputStream(input, encoding)
    //endregion toInputStream

    //region write byte[]
    /**
     * 将一个`byte[]`的内容写入一个`OutputStream`
     *
     * @param data 待写入的字节数组, 在输出时不会被修改
     * @param output 要写入的 `OutputStream`
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun write(data: ByteArray, output: OutputStream): Unit = IOUtils.write(data, output)

    /**
     * 将一个`byte[]`的内容写入一个`Writer`,
     * 使用指定的默认编码
     * 字符编码名称可以在这里找到： [IANA](http://www.iana.org/assignments/character-sets).
     * 该方法使用 [String.String].
     *
     * @param data 待写入的字节数组, 在输出时不会被修改
     * @param output 要写入的`Writer`
     * @param encoding 使用的编码，null表示平台默认的编码
     * @throws IOException io错误发生时
     * @throws java.nio.charset.UnsupportedCharsetException 如果指定的编码不被支持
     * @since 1.0.0
     */
    fun write(data: ByteArray, output: Writer, encoding: String? = null): Unit = IOUtils.write(data, output, encoding)
    //endregion write byte[]

    //region write char[]
    /**
     * 将一个`char[]`的内容写入一个`Writer`,
     * 使用平台的默认编码
     * @param data 待写入的字符数组, 在输出时不会被修改
     * @param output 要写入的`Writer`
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun write(data: CharArray, output: Writer): Unit = IOUtils.write(data, output)

    /**
     * 将一个`char[]`的内容写入一个`OutputStream`,
     * 使用指定的编码
     * 字符编码名称可以在这里找到： [IANA](http://www.iana.org/assignments/character-sets).
     * 该方法使用 [String.String] 和 [String.getBytes].
     *
     * @param data 待写入的字符数组, 在输出时不会被修改
     * @param output 要写入的`OutputStream`
     * @param encoding 使用的编码，null表示平台默认的编码
     * @throws IOException io错误发生时
     * @throws java.nio.charset.UnsupportedCharsetException 如果指定的编码不被支持
     * @since 1.0.0
     */
    fun write(data: CharArray, output: OutputStream, encoding: String? = null): Unit =
        IOUtils.write(data, output, encoding)
    //endregion write char[]

    //region write CharSequence
    /**
     * 将一个`CharSequence`的内容写入一个`Writer`
     *
     * @param data 待写入的`CharSequence`
     * @param output 要写入的`Writer`
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun write(data: CharSequence, output: Writer): Unit = IOUtils.write(data, output)

    /**
     * 将一个`CharSequence`的内容写入一个`OutputStream`,
     * 使用指定的编码
     * 字符编码名称可以在这里找到： [IANA](http://www.iana.org/assignments/character-sets).
     *
     * 该方法使用 [String.getBytes].
     *
     * @param data 待写入的`CharSequence`
     * @param output 要写入的`OutputStream`
     * @param encoding 使用的编码，null表示平台默认的编码
     * @throws IOException io错误发生时
     * @throws java.nio.charset.UnsupportedCharsetException 如果指定的编码不被支持
     * @since 1.0.0
     */
    fun write(data: CharSequence, output: OutputStream, encoding: String? = null): Unit =
        IOUtils.write(data, output, encoding)
    //endregion write CharSequence

    //region write String
    /**
     * 将一个`String`的内容写入一个`Writer`
     *
     * @param data 待写入的`String`
     * @param output 要写入的`Writer`
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun write(data: String, output: Writer): Unit = IOUtils.write(data, output)

    /**
     * 将一个`CharSequence`的内容写入一个`OutputStream`,
     * 使用指定的编码
     * 字符编码名称可以在这里找到： [IANA](http://www.iana.org/assignments/character-sets).
     * 该方法使用 [String.getBytes].
     *
     * @param data 待写入的`String`
     * @param output 要写入的`OutputStream`
     * @param encoding 使用的编码，null表示平台默认的编码
     * @throws IOException io错误发生时
     * @throws java.nio.charset.UnsupportedCharsetException 如果指定的编码不被支持
     * @since 1.0.0
     */
    fun write(data: String, output: OutputStream, encoding: String? = null): Unit =
        IOUtils.write(data, output, encoding)
    //endregion write String

    //region writeLines
    /**
     * 将容器中的每个一元素的toString()结果逐行写入到`OutputStream`中，
     * 使用指定的行分隔符和编码。
     *
     * 字符编码名称可以在这里找到： [IANA](http://www.iana.org/assignments/character-sets).
     *
     * @param lines 要写入的行，null的实体产生空白行
     * @param lineEnding 要使用的行分隔符，null将用系统默认行分隔符
     * @param output 要写入的`OutputStream`, 不能为null, 不能是已关闭的
     * @param encoding 使用的编码，null表示平台默认的编码
     * @throws IOException io错误发生时
     * @throws java.nio.charset.UnsupportedCharsetException 如果指定的编码不被支持
     * @since 1.0.0
     */
    fun writeLines(
        lines: Collection<*>, lineEnding: String? = null, output: OutputStream, encoding: String? = null
    ): Unit = IOUtils.writeLines(lines, lineEnding, output, encoding)

    /**
     * 将容器中的每个一元素的toString()结果逐行写入到`Writer`中
     *
     * @param lines 要写入的行，null的实体产生空白行
     * @param lineEnding 要使用的行分隔符，null将用系统默认行分隔符
     * @param writer 要写入的`Writer`, 不能为null, 不能是已关闭的
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun writeLines(lines: Collection<*>, lineEnding: String? = null, writer: Writer): Unit =
        IOUtils.writeLines(lines, lineEnding, writer)
    //endregion writeLines

    //region copy from InputStream
    /**
     * 从`InputStream`中拷贝字节到`OutputStream`中
     * 该方法内部有对输入进行缓存，因此没有必要使用`BufferedInputStream`.
     *
     * 大的流(超过2GB)在完成拷贝后，将返回一个`-1`的字节拷贝值，
     * 因为无法返回正确的整型字节数。大的流对象的拷贝应该使用
     * `copyLarge(InputStream, OutputStream)` 方法.
     *
     * @param input 要读取的`InputStream`
     * @param output 要写入的`OutputStream`
     * @return 拷贝的字节数, 如果大于Integer.MAX_VALUE返回-1
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun copy(input: InputStream, output: OutputStream): Int = IOUtils.copy(input, output)

    /**
     * 从一个大的(超过2GB)`InputStream`中拷贝字节到`OutputStream`中
     * 该方法内部有对输入进行缓存，因此没有必要使用`BufferedInputStream`.
     *
     * @param input 要读取的`InputStream`
     * @param output 要写入的`OutputStream`
     * @return 拷贝的字节数
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun copyLarge(input: InputStream, output: OutputStream): Long = IOUtils.copyLarge(input, output)

    /**
     * 从一个大的(超过2GB)`InputStream`中拷贝字节到`OutputStream`中
     * 该方法使用提供的缓存, 因此没有必要使用`BufferedInputStream`.
     *
     * @param input 要读取的`InputStream`
     * @param output 要写入的`OutputStream`
     * @param buffer 拷贝时要使用的缓存
     * @return 拷贝的字节数
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun copyLarge(input: InputStream, output: OutputStream, buffer: ByteArray): Long =
        IOUtils.copyLarge(input, output, buffer)

    /**
     * 从一个大的(超过2GB)`InputStream`中拷贝所有或部分字节到`OutputStream`中，
     * 可以选择跳过某些输入的字节
     * 该方法内部有对输入进行缓存，因此没有必要使用`BufferedInputStream`.
     *
     * @param input 要读取的`InputStream`
     * @param output 要写入的`OutputStream`
     * @param inputOffset : 拷贝前从输入跳过的字节数，负数将拷贝所有
     * @param length : 要拷贝的字节数. 负数将拷贝所有
     * @return 拷贝的字节数
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun copyLarge(input: InputStream, output: OutputStream, inputOffset: Long, length: Long): Long =
        IOUtils.copyLarge(input, output, inputOffset, length)

    /**
     * 从一个大的(超过2GB)`InputStream`中拷贝所有或部分字节到`OutputStream`中，
     * 可以选择跳过某些输入的字节
     * 该方法使用提供的缓存, 因此没有必要使用`BufferedInputStream`.
     *
     * @param input 要读取的`InputStream`
     * @param output 要写入的`OutputStream`
     * @param inputOffset 拷贝前从输入跳过的字节数，负数将拷贝所有
     * @param length 要拷贝的字节数. 负数将拷贝所有
     * @param buffer 拷贝时要使用的缓存
     * @return 拷贝的字节数
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun copyLarge(input: InputStream, output: OutputStream, inputOffset: Long, length: Long, buffer: ByteArray): Long =
        IOUtils.copyLarge(input, output, inputOffset, length, buffer)

    /**
     * 将`InputStream`的内容拷贝到`Writer`，使用指定的编码
     * 该方法内部有对输入进行缓存，因此没有必要使用`BufferedInputStream`.
     * 字符编码名称可以在这里找到： [IANA](http://www.iana.org/assignments/character-sets).
     * 该方法使用 [InputStreamReader].
     *
     * @param input 要读取的`InputStream`
     * @param output 要写入的`Writer`
     * @param encoding 使用的编码，null表示平台默认的编码
     * @throws IOException io错误发生时
     * @throws java.nio.charset.UnsupportedCharsetException 如果指定的编码不被支持
     * @since 1.0.0
     */
    fun copy(input: InputStream, output: Writer, encoding: String? = null): Unit =
        IOUtils.copy(input, output, encoding)
    //endregion copy from InputStream

    //region copy from Reader
    /**
     * 将字符从`Reader` 拷贝到 `Writer`.
     * 该方法内部有对输入进行缓存，因此没有必要使用`BufferedReader`.
     *
     * 大的流(超过2GB)在完成拷贝后，将返回一个`-1`的字节拷贝值，
     * 因为无法返回正确的整型字节数。大的流对象的拷贝应该使用
     * `copyLarge(Reader, Writer)` 方法.
     *
     * @param input 要读取的`Reader`
     * @param output 要写入的`Writer`
     * @return 拷贝的字符数，如果大于Integer.MAX_VALUE将返回-1
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun copy(input: Reader, output: Writer): Int = IOUtils.copy(input, output)

    /**
     * 将字符从大的(超过2GB)`Reader` 拷贝到 `Writer`.
     * 该方法内部有对输入进行缓存，因此没有必要使用`BufferedReader`.
     *
     * @param input 要读取的`Reader`
     * @param output 要写入的`Writer`
     * @return 拷贝的字符数
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun copyLarge(input: Reader, output: Writer): Long = IOUtils.copyLarge(input, output)

    /**
     * 将字符从大的(超过2GB)`Reader` 拷贝到 `Writer`.
     * 该方法使用提供的缓存, 因此没有必要使用`BufferedReader`.
     *
     * @param input 要读取的`Reader`
     * @param output 要写入的`Writer`
     * @param buffer 拷贝时使用的缓存
     * @return 拷贝的字符数
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun copyLarge(input: Reader, output: Writer, buffer: CharArray): Long = IOUtils.copyLarge(input, output, buffer)

    /**
     * 将所有或部分字符从大的(超过2GB)`Reader` 拷贝到 `Writer`，
     * 可以选择跳过部分字符。
     * 该方法内部有对输入进行缓存，因此没有必要使用`BufferedReader`.
     *
     * @param input 要读取的`Reader`
     * @param output 要写入的`Writer`
     * @param inputOffset : 拷贝前从输入跳过的字符数，负数将拷贝所有
     * @param length : 要拷贝的字符数. 负数将拷贝所有
     * @return 拷贝的字符数
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun copyLarge(input: Reader, output: Writer, inputOffset: Long, length: Long): Long =
        IOUtils.copyLarge(input, output, inputOffset, length)

    /**
     * 将所有或部分字符从大的(超过2GB)`Reader` 拷贝到 `Writer`，
     * 可以选择跳过部分字符。
     *
     * 该方法使用提供的缓存, 因此没有必要使用`BufferedReader`.
     * @param input 要读取的`Reader`
     * @param output 要写入的`Writer`
     * @param inputOffset : 拷贝前从输入跳过的字符数，负数将拷贝所有
     * @param length : 要拷贝的字符数. 负数将拷贝所有
     * @param buffer 拷贝时使用的缓存
     * @return 拷贝的字符数
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun copyLarge(input: Reader, output: Writer, inputOffset: Long, length: Long, buffer: CharArray): Long =
        IOUtils.copyLarge(input, output, inputOffset, length, buffer)

    /**
     * 将`Reader` 的内容拷贝到 `OutputStream`，
     * 使用指定的编码，并调用flush
     * 该方法内部有对输入进行缓存，因此没有必要使用`BufferedReader`.
     * 字符编码名称可以在这里找到： [IANA](http://www.iana.org/assignments/character-sets).
     * 由于OutputStreamWriter的实现，该方法可以执行flush
     * 该方法使用 [OutputStreamWriter].
     *
     * @param input 要读取的`Reader`
     * @param output 要写入的`OutputStream`
     * @param encoding 使用的编码，null表示平台默认的编码
     * @throws IOException io错误发生时
     * @throws java.nio.charset.UnsupportedCharsetException 如果指定的编码不被支持
     * @since 1.0.0
     */
    fun copy(input: Reader, output: OutputStream, encoding: String? = null): Unit =
        IOUtils.copy(input, output, encoding)
    //endregion copy from Reader

    //region content equals
    /**
     * 检查两个输入流的内容是否相等
     * 如果输入没有缓存，该方法将在内部使用`BufferedInputStream`对输入进行缓存
     *
     * @param input1 第一个输入流
     * @param input2 第二个输入流
     * @return true：两个输入流的内容相等，或它们都不存在，否则返回false
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun contentEquals(input1: InputStream, input2: InputStream): Boolean = IOUtils.contentEquals(input1, input2)

    /**
     * 检查两个Reader的内容是否相等
     * 如果输入没有缓存，该方法将在内部使用`BufferedReader`对输入进行缓存
     *
     * @param input1 第一个reader
     * @param input2 第二个reader
     * @return true：两个reader的内容相等，或它们都不存在，否则返回false
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun contentEquals(input1: Reader, input2: Reader): Boolean = IOUtils.contentEquals(input1, input2)

    /**
     * 检查两个Reader的内容是否相等，忽略EOL字符
     * 如果输入没有缓存，该方法将在内部使用`BufferedReader`对输入进行缓存
     *
     * @param input1 第一个reader
     * @param input2 第二个reader
     * @return true：两个reader的内容相等(忽略EOL字符)，或它们都不存在，否则返回false
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun contentEqualsIgnoreEOL(input1: Reader, input2: Reader): Boolean = IOUtils.contentEqualsIgnoreEOL(input1, input2)
    //endregion content equals

    //region skip
    /**
     * 从字节流中跳过部分字节。该实现保证会在放弃之前读取尽可能多的字节，
     * 这与它的子类[Reader]不同。
     *
     * @param input 待跳过的字节流
     * @param toSkip 跳过的字节数
     * @return 实际跳过的字节数
     * @throws IllegalArgumentException 如果toSkip参数为负数
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun skip(input: InputStream, toSkip: Long): Long = IOUtils.skip(input, toSkip)

    /**
     * 从字符流中跳过部分字符。该实现保证会在放弃之前读取尽可能多的字符，
     * 这与它的子类[Reader]不同。
     *
     * @param input 待跳过的字节流
     * @param toSkip 跳过的字节数
     * @return 实际跳过的字节数
     * @see Reader.skip
     * @throws IllegalArgumentException 如果toSkip参数为负数
     * @throws IOException io错误发生时
     * @since 1.0.0
     */
    fun skip(input: Reader, toSkip: Long): Long = IOUtils.skip(input, toSkip)

    /**
     * 跳过请求的字节数，或如果没有足够的字节数将失败
     * 该方法允许[InputStream.skip]可以不跳过指定的参数那么多的字节
     * (最可能的原因是到达文件末尾)
     *
     * @param input 要跳过的流
     * @param toSkip 要跳过的字节数，不能为负数
     * @see InputStream.skip
     * @throws IOException 读取时发生错误
     * @throws IllegalArgumentException 如果指定的字节数为负数
     * @throws EOFException 如果要跳过的字节数不正确
     * @since 1.0.0
     */
    fun skipFully(input: InputStream, toSkip: Long): Unit = IOUtils.skipFully(input, toSkip)

    /**
     * 跳过请求的字符数，或如果没有足够的字符数将失败
     * 该方法允许[Reader.skip]可以不跳过指定的参数那么多的字符
     * (最可能的原因是到达文件末尾)
     *
     * @param input 要跳过的流
     * @param toSkip 要跳过的字符数，不能为负数
     * @see Reader.skip
     * @throws IOException 读取时发生错误
     * IllegalArgumentException 如果指定的字符数为负数
     * EOFException 如果要跳过的字符数不正确
     * @since 1.0.0
     */
    fun skipFully(input: Reader?, toSkip: Long): Unit = IOUtils.skipFully(input, toSkip)
    //endregion skip

    //region read
    /**
     * 从字符流中读取字符。该实现保证在放弃前尽可能多的读取字符。
     * 这与它的子类[Reader]不同。
     *
     * @param input 要读入字符的字符流
     * @param buffer 目标
     * @param offset 初始读入缓冲区的偏移量
     * @param length 要读取的长度, 必须 >= 0
     * @return 实际读取的长度; 可能比请求的小(如果到达文件末尾)
     * @throws IOException 读取时发生错误
     * @since 1.0.0
     */
    fun read(input: Reader, buffer: CharArray, offset: Int, length: Int): Int =
        IOUtils.read(input, buffer, offset, length)

    /**
     * 从字符流中读取字符。该实现保证在放弃前尽可能多的读取字符。
     * 这与它的子类[Reader]不同。
     *
     * @param input 要读入字符的字符流
     * @param buffer 目标
     * @return 实际读取的长度; 可能比请求的小(如果到达文件末尾)
     * @throws IOException 读取时发生错误
     * @since 1.0.0
     */
    fun read(input: Reader, buffer: CharArray): Int? = IOUtils.read(input, buffer)

    /**
     * 从字节流中读取字节。该实现保证在放弃前尽可能多的读取字节。
     * 这与它的子类[InputStream]不同。
     *
     * @param input 要读入字符的字节流
     * @param buffer 目标
     * @param offset 初始读入缓冲区的偏移量
     * @param length 要读取的长度, 必须 >= 0
     * @return 实际读取的长度; 可能比请求的小(如果到达文件末尾)
     * @throws IOException 读取时发生错误
     * @since 1.0.0
     */
    fun read(input: InputStream, buffer: ByteArray, offset: Int, length: Int): Int =
        IOUtils.read(input, buffer, offset, length)

    /**
     * 从字节流中读取字符。该实现保证在放弃前尽可能多的读取字节。
     * 这与它的子类[InputStream]不同。
     *
     * @param input 要读入字符的字节流
     * @param buffer 目标
     * @return 实际读取的长度; 可能比请求的小(如果到达文件末尾)
     * @throws IOException 读取时发生错误
     * @since 1.0.0
     */
    fun read(input: InputStream, buffer: ByteArray): Int = IOUtils.read(input, buffer)

    /**
     * 读取请求数量的字符，或如果没有足够数量的字符时将失败
     *
     * 该方法允许[Reader.read]可以不跳过指定的参数那么多的字符
     * (最可能的原因是到达文件末尾)
     *
     * @param input 要读入字符的字节流
     * @param buffer 目标
     * @param offset 初始读入缓冲区的偏移量
     * @param length 要读取的长度, 必须 >= 0
     * @return 实际读取的长度
     * @throws IOException 读取时发生错误
     * @throws IllegalArgumentException 如果指定的字符数为负数
     * @throws EOFException 如果要跳过的字符数不正确
     * @since 1.0.0
     */
    fun readFully(input: Reader, buffer: CharArray, offset: Int, length: Int): Int =
        IOUtils.read(input, buffer, offset, length)

    /**
     * 读取请求数量的字符，或如果没有足够数量的字符时将失败
     *
     * 该方法允许[Reader.read]可以不跳过指定的参数那么多的字符
     * (最可能的原因是到达文件末尾)
     *
     * @param input 要读入字符的字节流
     * @param buffer 目标
     * @throws IOException 读取时发生错误
     * @throws IllegalArgumentException 如果指定的字符数为负数
     * @throws EOFException 如果要跳过的字符数不正确
     * @since 1.0.0
     */
    fun readFully(input: Reader, buffer: CharArray): Unit = IOUtils.readFully(input, buffer)

    /**
     * 读取请求数量的字节，或如果没有足够数量的字节时将失败
     * 该方法允许[InputStream.read]可以不跳过指定的参数那么多的字节
     * (最可能的原因是到达文件末尾)
     *
     * @param input 要读入字节的字节流
     * @param buffer 目标
     * @param offset 初始读入缓冲区的偏移量
     * @param length 要读取的长度, 必须 >= 0
     * @throws IOException 读取时发生错误
     * @throws IllegalArgumentException 如果指定的字节数为负数
     * @throws EOFException 如果要跳过的字节数不正确
     * @since 1.0.0
     */
    fun readFully(input: InputStream, buffer: ByteArray, offset: Int, length: Int): Unit =
        IOUtils.readFully(input, buffer, offset, length)

    /**
     * 读取请求数量的字节，或如果没有足够数量的字节时将失败
     * 该方法允许[InputStream.read]可以不跳过指定的参数那么多的字节
     * (最可能的原因是到达文件末尾)
     *
     * @param input 要读入字节的字节流
     * @param buffer 目标
     * @throws IOException 读取时发生错误
     * @throws IllegalArgumentException 如果指定的字节数为负数
     * @throws EOFException 如果要跳过的字节数不正确
     * @since 1.0.0
     */
    fun readFully(input: InputStream, buffer: ByteArray): Unit = IOUtils.readFully(input, buffer)
    //endregion read

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.io.IOUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

}