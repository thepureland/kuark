package io.kuark.base.io

import de.idyl.winzipaes.AesZipFileEncrypter
import de.idyl.winzipaes.impl.AESEncrypter
import de.idyl.winzipaes.impl.AESEncrypterBC
import io.kuark.base.lang.string.StringKit
import org.apache.commons.io.FileUtils
import org.apache.commons.io.LineIterator
import org.apache.commons.io.filefilter.IOFileFilter
import java.io.*
import java.math.BigInteger
import java.net.URL
import java.util.zip.Checksum
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * 文件工具类
 *
 * @author K
 * @since 1.0.0
 */
object FileKit {

    /**
     * 文件工具类用来生成临时文件时所使用的文件名前缀
     *
     * @author K
     * @since 1.0.0
     */
    const val PREFIX_TEMP_FILE: String = "FileKit_"

    /**
     * 压缩单个文件成zip压缩包并加密
     *
     * @param file 文件实体
     * @param fileName 压缩包里面文件的完整名字，如果此值为空，则默认取file.getName()。
     * @param password 加密密码，明文传入，如果为空，则不会加密
     * @return 压缩包文件实体 (此File文件实体存储在系统的临时目录，用完请调用file.delete()删除之)
     * @author K
     * @since 1.0.0
     */
    fun zip(file: File, fileName: String?, password: String?): File? {
        var filename: String? = fileName
        var zipFile: File?
        var enc: AesZipFileEncrypter? = null
        var input: InputStream? = null
        var zipOut: ZipOutputStream? = null
        try {
            zipFile = File.createTempFile(PREFIX_TEMP_FILE, ".zip")
            if (StringKit.isNotBlank(password)) {
                val aesEncrypter: AESEncrypter = AESEncrypterBC()
                aesEncrypter.init(password, 0)
                enc = AesZipFileEncrypter(zipFile, aesEncrypter)
                if (StringKit.isNotBlank(filename)) {
                    filename = file.name
                }
                enc.add(file, filename, password)
            } else {
                input = FileInputStream(file)
                zipOut = ZipOutputStream(FileOutputStream(zipFile))
                zipOut.putNextEntry(ZipEntry(file.name))
                var temp: Int
                while ((input.read().also { temp = it }) != -1) {
                    zipOut.write(temp)
                }
            }
        } finally {
            enc?.close()
            input?.close()
            zipOut?.close()
        }
        return zipFile
    }


    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.io.FileUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

    /**
     * 从指定的父目录和名字集构造一个文件对象
     *
     * @param directory 父目录
     * @param names 名字可变数组
     * @return 文件对象
     * @author K
     * @since 1.0.0
     */
    fun getFile(directory: File, vararg names: String): File = FileUtils.getFile(directory, *names)

    /**
     * 从指定的名字集构造一个文件对象
     *
     * @param names 名字可变数组
     * @return 文件对象
     * @author K
     * @since 1.0.0
     */
    fun getFile(vararg names: String): File = FileUtils.getFile(*names)

    /**
     * 为指定的文件对象打开一个[FileInputStream]， 比简单地调用
     * `new FileInputStream(file)`提供更好的错误信息。
     * 在方法的结尾，要么流被成功打开，要么抛出一个异常。
     *
     * @param file 待打开为输入流的文件对象
     * @return 指定文件的一个新的{@link FileInputStream}
     * @throws FileNotFoundException 指定的文件不存在
     * @throws IOException 如果文件存在，但是是一个目录
     * @throws IOException 如果文件存在，但是没法被读取
     * @author K
     * @since 1.0.0
     */
    fun openInputStream(file: File): FileInputStream = FileUtils.openInputStream(file)

    /**
     * 为指定的文件对象打开一个[FileOutputStream]， 检查并创建父目录如果它不存在的话。
     *
     * 在方法的结尾，要么流被成功打开，要么抛出一个异常。
     * 父目录不存在时将被创建。文件如果不存在也将被创建
     * 如果文件存在，但是是一个目录也将抛出IOException
     * 如果文件存在，但是没法被写入也将抛出IOException
     * 如果父目录没法被创建也将抛出IOException
     *
     * @param file 待打开为输入流的文件对象
     * @param append `true`: 字节将被添加到文件的末尾而不是覆盖该文件
     * @return 指定文件的一个新的{@link FileOutputStream}
     * @author K
     * @since 1.0.0
     */
    fun openOutputStream(file: File, append: Boolean = false): FileOutputStream =
        FileUtils.openOutputStream(file, append)

    /**
     * 返回文件大小的可读版本，输入参数代表字节数
     * 如果大小超过1GB，返回整个GB数。
     * 即大小是向下调整至最接近的GB边界。
     * 同样的，1MB，1KB边界。
     *
     * @param size 字节数
     * @return 可读的大小 (单位包括- EB, PB, TB, GB, MB, KB 和 bytes)
     * @see [IO-226 -should the rounding be changed?](https://issues.apache.org/jira/browse/IO-226)
     * @author K
     * @since 1.0.0
     */
    fun byteCountToDisplaySize(size: BigInteger): String = FileUtils.byteCountToDisplaySize(size)

    /**
     * 返回文件大小的可读版本，输入参数代表字节数
     * 如果大小超过1GB，返回整个GB数。
     * 即大小是向下调整至最接近的GB边界。
     * 同样的，1MB，1KB边界。
     *
     * @param size 字节数
     * @return 可读的大小 (单位包括- EB, PB, TB, GB, MB, KB 和 bytes)
     * @see [IO-226 -should the rounding be changed?](https://issues.apache.org/jira/browse/IO-226)
     * @author K
     * @since 1.0.0
     */
    fun byteCountToDisplaySize(size: Long): String = FileUtils.byteCountToDisplaySize(size)

    /**
     * 实现Unix上"touch"的相同行为。它创建一个大小为0的新文件，或者如果指定的文件存在，
     * 它将不修改文件地打开、关闭它，但是有更新文件日期和时间。
     *
     * @param file 待处理的文件
     * @throws IOException 创建失败
     * @author K
     * @since 1.0.0
     */
    fun touch(file: File): Unit = FileUtils.touch(file)

    /**
     * 将包含File实例的容器转换为数组。这样做主要是为了处理
     * File.listFiles() 和 FileKit.listFiles() 返回值类型不同的问题
     *
     * @param files 包含File实例的容器
     * @return 包含File实例的数组
     * @author K
     * @since 1.0.0
     */
    fun convertFileCollectionToFileArray(files: Collection<File>): Array<File> =
        FileUtils.convertFileCollectionToFileArray(files)

    /**
     * 查找指定目录及其子目录下的文件，所有找到的文件将由IOFileFilter过滤。
     *
     * 如果您需要递归搜索子目录，您可以为目录传入一个IOFileFilter。
     * 您不需要绑定一个DirectoryFileFilter(通过逻辑与)到该过滤器上。该方法已经为您完成了。
     *
     * 该方法另一个常见的用途是在目录树中查找文件，但是忽略CVS生成的目录。
     *
     * @param directory 要检索的目录
     * @param fileFilter 查找文件时要应用的过滤器
     * @param dirFilter 查找子目录时可选的过滤器。如果为null，将不搜索子目录。 TrueFileFilter.INSTANCE将匹配所有目录
     * @return 匹配的文件对应容器
     * @author K
     * @since 1.0.0
     */
    fun listFiles(directory: File, fileFilter: IOFileFilter, dirFilter: IOFileFilter?): Collection<File> =
        FileUtils.listFiles(directory, fileFilter, dirFilter)

    /**
     * 在给定的目录(子目录可选)中查找文件
     * 包含子目录的所有容器
     * @see FileKit.listFiles
     *
     * @param directory 要查找的目录
     * @param fileFilter 查找时使用的过滤器
     * @param dirFilter 查找子目录时可选的过滤器。如果为null，将不搜索子目录。 TrueFileFilter.INSTANCE将匹配所有目录
     * @return 匹配的文件对应容器
     * @see org.apache.commons.io.filefilter.NameFileFilter
     * @author K
     * @since 1.0.0
     */
    fun listFilesAndDirs(directory: File, fileFilter: IOFileFilter, dirFilter: IOFileFilter?): Collection<File> =
        FileUtils.listFilesAndDirs(directory, fileFilter, dirFilter)

    /**
     * 允许迭代指定目录中的文件(子目录可选)
     *
     * 所有找到的文件将通过IOFileFilter过滤。该方法基于
     * [.listFiles], 它提供可迭代的功能('foreach' 循环).
     *
     * @param directory 要查找的目录
     * @param fileFilter 查找时使用的过滤器
     * @param dirFilter 查找子目录时可选的过滤器。如果为null，将不搜索子目录。 TrueFileFilter.INSTANCE将匹配所有目录
     * @return  匹配的文件的迭代器
     * @see org.apache.commons.io.filefilter.NameFileFilter
     * @author K
     * @since 1.0.0
     */
    fun iterateFiles(directory: File, fileFilter: IOFileFilter, dirFilter: IOFileFilter?): Iterator<File> =
        FileUtils.iterateFiles(directory, fileFilter, dirFilter)

    /**
     * 允许迭代指定目录中的文件(子目录可选)
     *
     * 所有找到的文件将通过IOFileFilter过滤。该方法基于
     * [.listFiles], 它提供可迭代的功能('foreach' 循环).
     * 结果包含子目录的迭代器
     *
     * @param directory 要查找的目录
     * @param fileFilter 查找时使用的过滤器
     * @param dirFilter 查找子目录时可选的过滤器。如果为null，将不搜索子目录。 TrueFileFilter.INSTANCE将匹配所有目录
     * @return 匹配的文件的迭代器
     * @see org.apache.commons.io.filefilter.NameFileFilter
     * @author K
     * @since 1.0.0
     */
    fun iterateFilesAndDirs(directory: File, fileFilter: IOFileFilter, dirFilter: IOFileFilter?): Iterator<File> =
        FileUtils.iterateFilesAndDirs(directory, fileFilter, dirFilter)

    /**
     * 查找指定目录(子目录是可选的)中匹配扩展名的文件
     *
     * @param directory 要查找的目录
     * @param extensions 扩展名数组, 如： {"java","xml"}. 为null将返回所有文件
     * @param recursive true将查找所有子目录
     * @return 匹配的文件对象的列表
     * @author K
     * @since 1.0.0
     */
    fun listFiles(directory: File, extensions: Array<String>?, recursive: Boolean): List<File> =
        FileUtils.listFiles(directory, extensions, recursive) as List<File>

    /**
     * 查找指定目录(子目录是可选的)中匹配扩展名的文件.
     * 该方法基于[.listFiles]，它提供可迭代的功能('foreach' 循环).
     *
     * @param directory 要查找的目录
     * @param extensions 扩展名数组, 如： {"java","xml"}. 为null将返回所有文件
     * @param recursive true将查找所有子目录
     * @return 匹配的文件对象的迭代器
     * @author K
     * @since 1.0.0
     */
    fun iterateFiles(directory: File, extensions: Array<String>?, recursive: Boolean): Iterator<File> =
        FileUtils.iterateFiles(directory, extensions, recursive)

    /**
     * 检查两个文件的内容是否相等
     * 该方法检查两个文件的长度是否不同，或是否指向相同的文件，
     * 最后才逐字节的比较它们的内容。
     * 发现是目录时抛IOException
     *
     * @param file1 第一个文件
     * @param file2 第二个文件
     * @return true：如果它们的内容都相同或两个文件都不存在，否则返回false
     * @author K
     * @since 1.0.0
     */
    fun contentEquals(file1: File, file2: File): Boolean = FileUtils.contentEquals(file1, file2)

    /**
     * 检查两个文件的内容是否相等
     * 该方法检查两个文件是否指向相同的文件，
     * 最后才逐行的比较它们的内容。
     * 发现是目录时抛IOException
     *
     * @param file1 第一个文件
     * @param file2 第二个文件
     * @param charsetName 字符编码. 为null将使用平台的默认编码
     * @return true：如果它们的内容都相同或两个文件都不存在，否则返回false
     * @author K
     * @since 1.0.0
     */
    fun contentEqualsIgnoreEOL(file1: File, file2: File, charsetName: String?): Boolean =
        FileUtils.contentEqualsIgnoreEOL(file1, file2, charsetName)

    /**
     * 将一个`URL` 转换为一个 `File`
     *
     * @param url 待转换的url
     * @return 等效的`File` 对象, 如果url协议不是`file`则返回 `null`
     * @author K
     * @since 1.0.0
     */
    fun toFile(url: URL): File? = FileUtils.toFile(url)

    /**
     * 将每一个`URL` 都转换为 `File`
     * 返回跟输入数组相同大小的数组。
     * 如果输入的数组包含null元素，输出数组的对应元素也是null。
     *
     * 该方法将对URL进行编码。语法如：
     * `file:///my%20docs/file.txt` 将被正确的编码为
     * `/my docs/file.txt`.
     *
     * @param urls 要转换为File对象的url数组
     * @return 一个非null的数组
     *
     * @throws IllegalArgumentException 如果任何url不是file
     * @throws IllegalArgumentException 如果任何url不能被正确编码
     * @author K
     * @since 1.0.0
     */
    fun toFiles(urls: Array<URL?>): Array<File?> = FileUtils.toFiles(urls)

    /**
     * 将每一个`File` 都转换为 `URL`
     * 返回跟输入数组相同大小的数组。
     *
     * @param files 待转化的File数组
     * @return 转换后的URL数组
     * @throws IOException 文件不能被转化时
     * @author K
     * @since 1.0.0
     */
    fun toURLs(files: Array<File?>): Array<URL?> = FileUtils.toURLs(files)

    /**
     * 拷贝一个文件到指定目录，可以指定是否保留文件的日期
     * 该方法拷贝源文件的内容到指定目录的相同名字的文件。
     * 目标目录如果不存在将被创建。如果目标文件存在，它将被覆盖。
     *
     * **注意:** 该方法试图使用[File.setLastModified]保留文件最后修改的日期/时间，
     * 然而，它不保证该操作一定能成功。如果修改操作失败，将没有任何迹象。
     *
     * @param srcFile 一个已存在的待拷贝的文件
     * @param destDir 要存放拷贝后的文件的目标目录
     * @param preserveFileDate 是否保留文件原日期
     * @throws IOException 如果源或目标无效
     * @throws IOException 如果出现io错误
     * @see .copyFile
     * @author K
     * @since 1.0.0
     */
    fun copyFileToDirectory(srcFile: File, destDir: File, preserveFileDate: Boolean = true): Unit =
        FileUtils.copyFileToDirectory(srcFile, destDir, preserveFileDate)

    /**
     * 拷贝一个文件到新的位置，可以指定是否保留文件的日期
     * 该方法拷贝源文件的内容到指定目录的相同名字的文件。
     * 目标目录如果不存在将被创建。如果目标文件存在，它将被覆盖。
     *
     * **注意:** 该方法试图使用[File.setLastModified]保留文件最后修改的日期/时间，
     * 然而，它不保证该操作一定能成功。如果修改操作失败，将没有任何迹象。
     *
     * @param srcFile 一个已存在的待拷贝的文件
     * @param destFile 新的文件
     * @param preserveFileDate 是否保留文件原日期
     * @throws IOException 如果源或目标无效
     * @throws IOException 如果拷贝时出现io错误
     * @see .copyFileToDirectory
     * @author K
     * @since 1.0.0
     */
    fun copyFile(srcFile: File, destFile: File, preserveFileDate: Boolean = true): Unit =
        FileUtils.copyFile(srcFile, destFile, preserveFileDate)

    /**
     * 从指定的文件拷贝字节到`OutputStream`
     * 该方法在内部已经有对输入进行缓存，所以没必要在外部使用`BufferedInputStream`
     *
     * @param input 要读取数据的文件对象
     * @param output 要写入的 `OutputStream`
     * @return 拷贝的字节数
     * @throws IOException 如果拷贝时出现io错误
     * @author K
     * @since 1.0.0
     */
    fun copyFile(input: File, output: OutputStream): Long = FileUtils.copyFile(input, output)

    /**
     * 拷贝一个目录到目标目录下，保留文件日期
     * 该方法拷贝源目标和其所有内容到目标目录下的一个同名目标
     *
     * 目标目录如果不存在将被创建。
     * 如果目标目录存在，将合并源目录和目标目录的内容，源目标的内容覆盖目标目录的内容。
     *
     * **注意:** 该方法试图使用[File.setLastModified]保留文件最后修改的日期/时间，
     * 然而，它不保证该操作一定能成功。如果修改操作失败，将没有任何迹象。
     *
     * @param srcDir 一个存在的待拷贝的目录
     * @param destDir 放置拷贝后的源目标的目标目录
     * @throws IOException 如果源目录或目标目录无效
     * @throws IOException 如果拷贝时出现io错误
     * @author K
     * @since 1.0.0
     */
    fun copyDirectoryToDirectory(srcDir: File, destDir: File): Unit =
        FileUtils.copyDirectoryToDirectory(srcDir, destDir)

    /**
     * 拷贝整个目录到一个新的位置，可以指定是否保留文件的日期
     * 该方法拷贝指定的目录及其所有子目录和文件到目标目录。
     *
     * 目标目录如果不存在将被创建。
     * 如果目标目录存在，将合并源目录和目标目录的内容，源目标的内容覆盖目标目录的内容。
     *
     * **注意:** 该方法试图使用[File.setLastModified]保留文件最后修改的日期/时间，
     * 然而，它不保证该操作一定能成功。如果修改操作失败，将没有任何迹象。
     *
     * @param srcDir 一个存在的待拷贝的目录
     * @param destDir 新的目录
     * @param preserveFileDate 是否保留文件原日期
     * @throws IOException 如果源目录或目标目录无效
     * @throws IOException 如果拷贝时出现io错误
     * @author K
     * @since 1.0.0
     */
    fun copyDirectory(srcDir: File, destDir: File, preserveFileDate: Boolean = true): Unit =
        FileUtils.copyDirectory(srcDir, destDir, preserveFileDate)

    /**
     * 拷贝一个过滤过的目录到一个新的位置，可以指定是否保留文件日期
     * 该方法拷贝指定的目录及其所有子目录和文件到目标目录。
     *
     * 目标目录如果不存在将被创建。
     * 如果目标目录存在，将合并源目录和目标目录的内容，源目标的内容覆盖目标目录的内容。
     *
     * **注意:** 该方法试图使用[File.setLastModified]保留文件最后修改的日期/时间，
     * 然而，它不保证该操作一定能成功。如果修改操作失败，将没有任何迹象。
     *
     * <h4>例子: 只拷贝目录</h4>
     *
     * <pre>
     * // 只拷贝目录结构
     * FileKit.copyDirectory(srcDir, destDir, DirectoryFileFilter.DIRECTORY);
     * </pre>
     *
     * <h4>例子: 拷贝目录和txt文件</h4>
     *
     * <pre>
     *
     * // 使用过滤器进行拷贝
     * FileKit.copyDirectory(srcDir, destDir, filter);
     * </pre>
     *
     * @param srcDir 一个存在的待拷贝的目录
     * @param destDir 新的目录
     * @param filter 要使用的过滤器, null表示拷贝所有目录和文件
     * @param preserveFileDate 是否保留文件日期
     * IOException 如果源目录或目标目录无效
     * IOException 如果拷贝时出现io错误
     * @author K
     * @since 1.0.0
     */
    fun copyDirectory(srcDir: File, destDir: File, filter: FileFilter?, preserveFileDate: Boolean = true): Unit =
        FileUtils.copyDirectory(srcDir, destDir, filter, preserveFileDate)

    /**
     * 从URL中按字节拷贝其内容到一个文件
     *
     * 目标目录如果不存在将被创建。
     * 目标文件如果存在将被覆盖。
     *
     * 警告: 该方法没有设置一个连接或读取超时，因此可以永远阻塞。
     * 要防止这种情况的出现，请使用[.copyURLToFile]
     *
     * @param source 要拷贝字节的`URL`
     * @param destination 要写入字节的非目录`File`(可能覆盖)
     * @throws IOException 如果源URL不能被打开
     * @throws IOException 如果目标是一个目录
     * @throws IOException 如果目标文件不能被写入
     * @throws IOException 如果目标需要被创建但是又创建不了
     * @throws IOException 如果拷贝时发生io错误
     * @author K
     * @since 1.0.0
     */
    fun copyURLToFile(source: URL, destination: File): Unit = FileUtils.copyURLToFile(source, destination)

    /**
     * 从URL中按字节拷贝其内容到一个文件
     * 目标目录如果不存在将被创建。
     * 目标文件如果存在将被覆盖。
     *
     * @param source 要拷贝字节的`URL`
     * @param destination 要写入字节的非目录`File`(可能覆盖)
     * @param connectionTimeout 如果`source`没有连接生成的超时毫秒数
     * @param readTimeout 如果没有数据可以从`source`读取的超时毫秒数
     * @throws IOException 如果源URL不能被打开
     * @throws IOException 如果目标是一个目录
     * @throws IOException 如果目标文件不能被写入
     * @throws IOException 如果目标需要被创建但是又创建不了
     * @throws IOException 如果拷贝时发生io错误
     * @author K
     * @since 1.0.0
     */
    fun copyURLToFile(source: URL, destination: File, connectionTimeout: Int, readTimeout: Int): Unit =
        FileUtils.copyURLToFile(source, destination, connectionTimeout, readTimeout)

    /**
     * 从[InputStream]拷贝字节到一个文件
     * 目标目录如果不存在将被创建。
     * 目标文件如果存在将被覆盖。
     *
     * @param source 拷贝数据的来源`InputStream`
     * @param destination 要写入字节的非目录`File`(可能覆盖)
     * @throws IOException 如果目标是一个目录
     * @throws IOException 如果目标文件不能被写入
     * @throws IOException 如果目标需要被创建但是又创建不了
     * @throws IOException 如果拷贝时发生io错误
     * @author K
     * @since 1.0.0
     */
    fun copyInputStreamToFile(source: InputStream, destination: File): Unit =
        FileUtils.copyInputStreamToFile(source, destination)

    /**
     * 递归删除一个目录
     *
     * @param directory 待删除的目录
     * @throws IOException 删除操作失败时
     * @author K
     * @since 1.0.0
     */
    fun deleteDirectory(directory: File): Unit = FileUtils.deleteDirectory(directory)

    /**
     * 删除一个文件，不会抛出异常。如果为目录，删除它及其所有子目录
     * 这个方法和File.delete()的差别在于：
     *
     *  * 要删除的目录不需要是一个空目录
     *  * 当一个目录或一个文件无法被删除时不会抛出异常
     *
     * @param file 要删除的文件或目录
     * @return `true`： 如果文件或目录被成功删除，否则返回 `false`
     * @author K
     * @since 1.0.0
     */
    fun deleteQuietly(file: File): Boolean = FileUtils.deleteQuietly(file)

    /**
     * 检查父目录是否包含子目录(或文件)
     * 比较之前文件不会被标准化。
     *
     * 边缘情况:
     *  * 一个目录不包含它自己：返回false
     *
     * @param directory 父目录
     * @param child 子文件或子目录
     * @return true：父目录包含子目录或文件，否则返回false
     * @throws IOException 检查文件时出现io错误
     * @throws IllegalArgumentException directory不是目录时
     * @author K
     * @since 1.0.0
     */
    fun directoryContains(directory: File, child: File): Boolean? = FileUtils.directoryContains(directory, child)

    /**
     * 清空指定目录，而不是删除它
     *
     * @param directory 待清空的目录
     * IOException 清除不成功时
     * @author K
     * @since 1.0.0
     */
    fun cleanDirectory(directory: File): Unit = FileUtils.cleanDirectory(directory)

    /**
     * 等待一个文件的创建，实行超时
     *
     * 该方法重复检查[File.exists]直到它在超时时间内返回true
     *
     * @param file 要检查的文件
     * @param seconds 等待的最大秒数
     * @return true: 如果文件存在
     * @author K
     * @since 1.0.0
     */
    fun waitFor(file: File, seconds: Int): Boolean = FileUtils.waitFor(file, seconds)

    /**
     * 读取一个文件的内容为字符串。该文件总是关闭着。
     *
     * @param file 要读取的文件
     * @param encoding 要使用的编码, `null` 将使用平台默认的编码
     * @return 文件内容
     * @throws IOException 如果发生io错误
     * @throws UnsupportedEncodingException 指定的编码不被支持时
     * @author K
     * @since 1.0.0
     */
    fun readFileToString(file: File, encoding: String? = null): String = FileUtils.readFileToString(file, encoding)

    /**
     * 从指定的文件读取内容到字节数组. 该文件总是关闭着。
     *
     * @param file 要读取的文件
     * @return 文件内容的字节数组
     * @throws IOException 如果发生io错误
     * @author K
     * @since 1.0.0
     */
    fun readFileToByteArray(file: File): ByteArray = FileUtils.readFileToByteArray(file)

    /**
     * 逐行读取指定文件的内容到字符串列表。该文件总是关闭着。
     *
     * @param file 要读取的文件
     * @param encoding 要使用的编码, `null` 将使用平台默认的编码
     * @return 字符串列表，每一个元素代表文件中的每一行
     * @throws IOException 如果发生io错误
     * @throws UnsupportedEncodingException 如果编码不被支持
     * @author K
     * @since 1.0.0
     */
    fun readLines(file: File, encoding: String? = null): List<String> = FileUtils.readLines(file, encoding)

    /**
     * 返回文件中每行的迭代器
     * 该方法为指定的文件打开一个`InputStream`。当您完成迭代后，必须
     * 关闭流以便释放内部资源。这可以通过调用[LineIterator.close]或
     * [LineIterator.closeQuietly]方法来完成。
     *
     * 使用例子:
     *
     * <pre>
     * LineIterator it = FileKit.lineIterator(file, &quot;UTF-8&quot;);
     * try {
     * while (it.hasNext()) {
     * String line = it.nextLine();
     * // / 对line的处理
     * }
     * } finally {
     * LineIterator.closeQuietly(iterator);
     * }
     * </pre>
     *
     * 如果在创建迭代器的过程中产生异常，底层的流将被关闭。
     *
     * @param file 要读取的文件对象
     * @param encoding 要使用的编码, `null` 将使用平台默认的编码
     * @return 代表文件中每行的迭代器
     * @throws IOException 如果发生io错误
     * @author K
     * @since 1.0.0
     */
    fun lineIterator(file: File, encoding: String? = null): LineIterator = FileUtils.lineIterator(file, encoding)

    /**
     * 将一个字符串写入到文件中，如果文件不存在将被创建
     *
     * @param file 要写入的文件
     * @param data 要写入到文件的内容
     * @param encoding 要使用的编码, `null` 将使用平台默认的编码
     * @throws IOException 如果发生io错误
     * @throws UnsupportedEncodingException 如果指定的编码不被虚拟机支持
     * @author K
     * @since 1.0.0
     */
    fun writeStringToFile(file: File, data: String, encoding: String? = null): Unit =
        FileUtils.writeStringToFile(file, data, encoding)

    /**
     * 将一个字符串写入到文件中，如果文件不存在将被创建
     *
     * @param file 要写入的文件
     * @param data 要写入到文件的内容
     * @param encoding 要使用的编码, `null` 将使用平台默认的编码
     * @param append 字符序列是否被拼接到文件末尾，而不是覆盖原来文件的内容
     * @throws IOException 如果发生io错误
     * @throws UnsupportedEncodingException 如果指定的编码不被虚拟机支持
     * @author K
     * @since 1.0.0
     */
    fun writeStringToFile(file: File, data: String, encoding: String? = null, append: Boolean): Unit =
        FileUtils.writeStringToFile(file, data, encoding, append)

    /**
     * 将一个字符序列写入到文件中，如果文件不存在将被创建
     *
     * @param file 要写入的文件
     * @param data 要写入到文件的内容
     * @param encoding 要使用的编码, `null` 将使用平台默认的编码
     * @param append 字符序列是否被拼接到文件末尾，而不是覆盖原来文件的内容
     * @throws IOException 如果发生io错误
     * @throws UnsupportedEncodingException 如果指定的编码不被虚拟机支持
     * @author K
     * @since 1.0.0
     */
    fun write(file: File, data: CharSequence, encoding: String? = null, append: Boolean = false): Unit =
        FileUtils.write(file, data, encoding, append)

    /**
     * 将一个字节数组写入到文件中，使用虚拟机默认的编码，如果文件不存在将被创建
     *
     * @param file 要写入的文件
     * @param data 要写入到文件的内容
     * @param append 数据是否被拼接到文件末尾，而不是替换原文件的内容
     * @throws IOException 如果发生io错误
     * @author K
     * @since 1.0.0
     */
    fun writeByteArrayToFile(file: File, data: ByteArray, append: Boolean = false): Unit =
        FileUtils.writeByteArrayToFile(file, data, append)

    /**
     * 将容器中每个元素的toString()值逐行地写入到指定的文件。
     * 使用指定的编码和行分隔符。
     * 文件不存在将被创建
     *
     * @param file 要写入的文件 to
     * @param encoding 要使用的编码, `null` 将使用平台默认的编码
     * @param lines 要写入的容器, `null` 将写入空行
     * @param lineEnding 行分隔符, `null`将使用系统默认的行分隔符
     * @param append 数据是否被拼接到文件末尾，而不是替换原文件的内容
     * @throws IOException 如果发生io错误
     * @throws UnsupportedEncodingException 如果指定的编码不被虚拟机支持
     * @author K
     * @since 1.0.0
     */
    fun writeLines(
        file: File,
        encoding: String? = null,
        lines: Collection<*>? = null,
        lineEnding: String? = null,
        append: Boolean = false
    ): Unit =
        FileUtils.writeLines(file, encoding, lines, lineEnding, append)

    /**
     * 删除一个文件。如果为目录，删除它及其所有子目录
     * 该方法与File.delete()的差别为：
     *   要删除的目录可以为空
     *   当一个目录或文件不能被删除时将抛出异常。(File的方法将返回一个布尔值)
     *
     * @param file 要删除的文件或目录
     * @throws FileNotFoundException 如果目录或文件找不到
     * @throws IOException 删除操作失败时
     * @author K
     * @since 1.0.0
     */
    fun forceDelete(file: File): Unit = FileUtils.forceDelete(file)

    /**
     * 当java虚拟机退出时，删除指定的文件或目录。如果是目录，删除该目录及其所有子目录。
     *
     * @param file 要删除的文件或目录
     * @throws IOException 删除操作失败时
     * @author K
     * @since 1.0.0
     */
    fun forceDeleteOnExit(file: File): Unit = FileUtils.forceDeleteOnExit(file)

    /**
     * 创建目录，包含任何需要但不存在的父目录。
     * 如果指定名称的文件已经存在，但是不是一个目录，将抛出IOException异常。
     * 如果目录不能被创建(或存在但不是一个目录)时将抛出IOException异常
     *
     * @param directory 要创建的目录
     * @throws IOException 如果目录不能被创建或存在但不是一个目录
     * @author K
     * @since 1.0.0
     */
    fun forceMkdir(directory: File): Unit = FileUtils.forceMkdir(directory)

    /**
     * 返回指定文件或目录的大小。
     * 如果提供的[File]是一个正规的文件，文件的长度将被返回。
     * 如果参数是目录，将递归计算目录的大小。如果目录或子目录有安全限制，
     * 它的大小将不被包括。
     *
     * @param file 要返回大小的正规文件或目录
     * @return 文件的长度, 或目录的递归大小(字节数)
     * @throws IllegalArgumentException 如果文件不存在
     * @author K
     * @since 1.0.0
     */
    fun sizeOf(file: File): Long = FileUtils.sizeOf(file)

    /**
     * 返回指定文件或目录的大小。
     * 如果提供的[File]是一个正规的文件，文件的长度将被返回。
     * 如果参数是目录，将递归计算目录的大小。如果目录或子目录有安全限制，
     * 它的大小将不被包括。
     *
     * @param file 要返回大小的正规文件或目录
     * @return 文件的长度, 或目录的递归大小(字节数)
     * @throws IllegalArgumentException 如果文件不存在
     * @author K
     * @since 1.0.0
     */
    fun sizeOfAsBigInteger(file: File): BigInteger = FileUtils.sizeOfAsBigInteger(file)

    /**
     * 递归计算目录的大小(所有文件的大小之和)
     *
     * @param directory 待检查的目录
     * @return 目录的大小(字节数), 如果目录有安全限制返回0, 当总大小大于[Long.MAX_VALUE]时返回一个负数
     * @author K
     * @since 1.0.0
     */
    fun sizeOfDirectory(directory: File): Long = FileUtils.sizeOfDirectory(directory)

    /**
     * 递归计算目录的大小(所有文件的大小之和)
     *
     * @param directory 待检查的目录
     * @return 目录的大小(字节数), 如果目录有安全限制返回0
     * @author K
     * @since 1.0.0
     */
    fun sizeOfDirectoryAsBigInteger(directory: File): BigInteger = FileUtils.sizeOfDirectoryAsBigInteger(directory)

    /**
     * 检查指定的第一个文件是否比第二文件新(修改日期)
     *
     * @param file 第一个文件
     * @param reference 第二个文件
     * @return true：如果文件存在并且比第二个文件新
     * @throws IllegalArgumentException 如果文件不存在
     * @author K
     * @since 1.0.0
     */
    fun isFileNewer(file: File, reference: File): Boolean = FileUtils.isFileNewer(file, reference)

    /**
     * 检查指定的文件的修改日期是否比指定的日期新
     *
     * @param file 待比较的文件
     * @param timeMillis 日期的毫秒表示
     * @return true：如果文件存在并且在指定日期之后被修改
     * @throws IllegalArgumentException 如果文件为 `null`
     * @author K
     * @since 1.0.0
     */
    fun isFileNewer(file: File, timeMillis: Long): Boolean = FileUtils.isFileNewer(file, timeMillis)

    /**
     * 检查指定的第一个文件是否比第二文件旧(修改日期)
     *
     * @param file 第一个文件
     * @param reference 第二个文件
     * @return true：如果文件存在并且比第二个文件旧
     * @throws    IllegalArgumentException 如果文件不存在
     * @author K
     * @since 1.0.0
     */
    fun isFileOlder(file: File, reference: File): Boolean = FileUtils.isFileOlder(file, reference)

    /**
     * 检查指定的文件的修改日期是否比指定的日期旧
     *
     * @param file 待比较的文件
     * @param timeMillis 日期的毫秒表示
     * @return true：如果文件存在并且在指定日期之前被修改
     * @author K
     * @since 1.0.0
     */
    fun isFileOlder(file: File, timeMillis: Long): Boolean = FileUtils.isFileOlder(file, timeMillis)

    /**
     * 计算文件的校验和，使用CRC32校验和算法。返回校验和。
     *
     * @param file 待计算校验和的文件
     * @return 校验和
     * @throws IllegalArgumentException 如果指定的文件是一个目录
     * @throws IOException 读取文件时发生io异常
     * @author K
     * @since 1.0.0
     */
    fun checksumCRC32(file: File): Long = FileUtils.checksumCRC32(file)

    /**
     * 使用指定的校验和对象计算文件的校验和。
     * 多个文件可以被同一个`Checksum`对象校验。例如：
     *
     * <pre>
     * long csum = FileKit.checksum(file, new CRC32()).getValue();
     * </pre>
     *
     * @param file 要计算校验和的文件
     * @param checksum 使用的校验和对象
     * @return 指定的校验和对象，已经由文件的内容更新过
     * @throws IllegalArgumentException 如果指定的文件是一个目录
     * @throws IOException 读取文件时发生io异常
     * @author K
     * @since 1.0.0
     */
    fun checksum(file: File, checksum: Checksum): Checksum = FileUtils.checksum(file, checksum)

    /**
     * 移动一个目录到另一个目录
     * 当目标目录在另一个文件系统时，执行拷贝和删除
     *
     * @param srcDir 要移动的目录
     * @param destDir 目标目录
     * @throws FileNotFoundException 如果目标目录存在
     * @throws IOException 源或目标不可用时
     * @throws IOException 如果移动时发生io错误
     * @author K
     * @since 1.0.0
     */
    fun moveDirectory(srcDir: File, destDir: File): Unit = FileUtils.moveDirectory(srcDir, destDir)

    /**
     * 移动一个目录到另一个目录
     * 当目标目录在另一个文件系统时，执行拷贝和删除
     *
     * @param srcDir 要移动的目录
     * @param destDir 目标目录
     * @param createDestDir `true`：创建目录
     * @throws FileNotFoundException 如果目标目录存在
     * @throws IOException 源或目标不可用时
     * @throws IOException 如果移动时发生io错误
     * @author K
     * @since 1.0.0
     */
    fun moveDirectoryToDirectory(src: File, destDir: File, createDestDir: Boolean): Unit =
        FileUtils.moveDirectoryToDirectory(src, destDir, createDestDir)

    /**
     * 移动一个文件
     * 当目标文件在另一个文件系统时，执行拷贝和删除
     *
     * @param srcFile 要移动的文件
     * @param destFile 目标文件
     * @throws FileNotFoundException 如果目标文件存在
     * @throws IOException 源或目标不可用时
     * @throws IOException 如果移动时发生io错误
     * @author K
     * @since 1.0.0
     */
    fun moveFile(srcFile: File, destFile: File): Unit = FileUtils.moveFile(srcFile, destFile)

    /**
     * 移动一个文件
     * 当目标文件在另一个文件系统时，执行拷贝和删除
     *
     * @param srcFile 要移动的文件
     * @param destDir 目标目录
     * @param createDestDir `true`：创建目录
     * @throws FileNotFoundException 如果目标文件存在
     * @throws IOException 源或目标不可用时
     * @throws IOException 如果移动时发生io错误
     * @author K
     * @since 1.0.0
     */
    fun moveFileToDirectory(srcFile: File, destDir: File, createDestDir: Boolean): Unit =
        FileUtils.moveFileToDirectory(srcFile, destDir, createDestDir)

    /**
     * 移动一个文件或目录到目标目录
     * 当目标在另一个文件系统时，执行拷贝和删除
     *
     * @param src 要移动的文件或目录
     * @param destDir 目标目录
     * @throws FileNotFoundException 如果文件或目录存在于目标目录中
     * @throws IOException 源或目标不可用时
     * @throws IOException 如果移动时发生io错误
     * @author K
     * @since 1.0.0
     */
    fun moveToDirectory(src: File, destDir: File, createDestDir: Boolean): Unit =
        FileUtils.moveToDirectory(src, destDir, createDestDir)

    /**
     * 确定指定的文件是否是一个符号链接，而不是一个实际的文件。
     * 如果在路径中的任意位置有一个符号链接将返回false. 只有当指定的文件是一个实际文件时才返回true.
     *
     * **注意:** 如果当前使用的系统由[FilenameKit.isSystemWindows]检测到是Windows,
     * 那么当前的实现总是返回`false`
     *
     * @param file 要检查的文件
     * @return true：如果文件是一个符号链接
     * @throws IOException 如果检查时发生io错误
     * @author K
     * @since 1.0.0
     */
    fun isSymlink(file: File): Boolean = FileUtils.isSymlink(file)

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.io.FileUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

}