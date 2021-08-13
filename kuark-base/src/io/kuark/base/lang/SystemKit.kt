package io.kuark.base.lang

import io.kuark.base.lang.string.StringKit
import io.kuark.base.log.LogFactory
import org.apache.commons.lang3.SystemUtils
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.management.ManagementFactory
import java.util.*
import java.util.regex.Pattern

/**
 * 系统工具类
 *
 * @author K
 * @since 1.0.0
 */
object SystemKit {

    private val log = LogFactory.getLog(this::class)

    /**
     * 设置系统环境变量
     *
     * @param vars Map(变量名，变量值)
     * @author https://blog.csdn.net/n1007530194/article/details/97130931
     * @author K
     * @since 1.0.0
     */
    fun setEnvVars(vars: Map<String, String>) {
        try {
            val processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment")
            val theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment")
            theEnvironmentField.isAccessible = true
            val env = theEnvironmentField.get(null) as MutableMap<String, String>
            env.putAll(vars!!)
            val theCaseInsensitiveEnvironmentField =
                processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment")
            theCaseInsensitiveEnvironmentField.isAccessible = true
            val cienv = theCaseInsensitiveEnvironmentField.get(null) as MutableMap<String, String>
            cienv.putAll(vars)
        } catch (e: NoSuchFieldException) {
            val classes = Collections::class.java.declaredClasses
            val env = System.getenv()
            for (cl in classes) {
                if ("java.util.Collections\$UnmodifiableMap" == cl.name) {
                    val field = cl.getDeclaredField("m")
                    field.isAccessible = true
                    val obj = field.get(env)
                    val map = obj as MutableMap<String, String>
                    map.clear()
                    map.putAll(vars!!)
                }
            }
        }
    }

    /**
     * 执行单个系统命令
     *
     * @param command 命令组成部分的可变数组
     * @return Pair(是否执行成功，执行结果信息)
     * @author K
     * @since 1.0.0
     */
    fun executeCommand(vararg command: String): Pair<Boolean, String?> {
        var process: Process? = null // 也可用ProcessBuilder构建
        var message: String? = null
        val success = try {
            process = Runtime.getRuntime().exec(command)
            true
        } catch (e: Throwable) {
            message = e.message
            log.error(e, "执行系统命令【${command.joinToString(" ")}】出错！")
            false
        }

//        if (wait) {
//            try {
//                process.waitFor()
//            } catch (e: InterruptedException) {
//                e.printStackTrace()
//            }
//        }


        if (process != null) {
            message = loadStream(process!!.inputStream)
            val errorMsg = loadStream(process.errorStream)
            if (StringKit.isNotEmpty(errorMsg)) {
                message = errorMsg
            }
            process.destroy()
        }

        return success to message
    }

    private fun loadStream(inputStream: InputStream): String? {
        inputStream.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                val buffer = StringBuffer()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    buffer.append(line).append("\n")
                }
                return buffer.toString()
            }
        }
    }

    /**
     * 当前系统的回车换行符
     *
     * @author K
     * @since 1.0.0
     */
    val LINE_SEPARATOR = System.getProperty("line.separator")

    private val debugPattern = Pattern.compile("-Xdebug|jdwp")

    /**
     * 获取当前操作系统名称.
     *
     * @return 操作系统名称 例如:windows xp,linux 等.
     * @author K
     * @since 1.0.0
     */
    fun getOSName(): String = System.getProperty("os.name").toLowerCase()

    /**
     * 是否调试模式
     *
     * @return true:调试模式，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isDebug(): Boolean {
        for (arg in ManagementFactory.getRuntimeMXBean().inputArguments) {
            if (debugPattern.matcher(arg).find()) {
                return true
            }
        }
        return false
    }

    /**
     * 是否为windows操作系统
     *
     * @return true: 为windows操作系统，反之为false
     * @author K
     * @since 1.0.0
     */
    fun isWindowsOS(): Boolean = getOSName().toLowerCase().contains("windows")

    /**
     * 得到系统当前用户
     *
     * @return 当前用户名
     * @author K
     * @since 1.0.0
     */
    fun getUser(): String = System.getProperty("user.name")

    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.lang3.SystemUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    /**
     * 获取java home目录, 并以`File`返回
     *
     * @return 目录
     * @throws SecurityException 如果安全管理器存在并且它的 `checkPropertyAccess` 方法不允许访问特别的系统属性
     * @see System.getProperty
     * @author K
     * @since 1.0.0
     */
    fun getJavaHome(): File = SystemUtils.getJavaHome()

    /**
     * 获取IO临时目录, 并以`File`返回
     *
     * @return 目录
     * @throws SecurityException 如果安全管理器存在并且它的 `checkPropertyAccess` 方法不允许访问特别的系统属性
     * @see System.getProperty
     * @author K
     * @since 1.0.0
     */
    fun getJavaIoTmpDir(): File = SystemUtils.getJavaIoTmpDir()

    /**
     * 获取用户目录, 并以`File`返回
     *
     * @return 目录
     * @throws SecurityException 如果安全管理器存在并且它的 `checkPropertyAccess` 方法不允许访问特别的系统属性
     * @see System.getProperty
     * @author K
     * @since 1.0.0
     */
    fun getUserDir(): File = SystemUtils.getUserDir()

    /**
     * 获取用户home目录, 并以`File`返回
     *
     * @return 目录
     * @throws SecurityException 如果安全管理器存在并且它的 `checkPropertyAccess` 方法不允许访问特别的系统属性
     * @see System.getProperty
     * @author K
     * @since 1.0.0
     */
    fun getUserHome(): File = SystemUtils.getUserHome()

    /**
     * 检测 [.JAVA_AWT_HEADLESS] 値是否为 `true`.
     *
     * @return `true` 如果 `JAVA_AWT_HEADLESS` 为 `"true"`, 否则返回 `false`.
     * @see .JAVA_AWT_HEADLESS
     * @author K
     * @since 1.0.0
     */
    fun isJavaAwtHeadless(): Boolean = SystemUtils.isJavaAwtHeadless()

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.lang3.SystemUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

}