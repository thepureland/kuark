package io.kuark.base.lang

import org.apache.commons.lang3.SystemUtils
import java.io.File
import java.lang.management.ManagementFactory
import java.util.regex.Pattern

/**
 * 系统工具类
 *
 * @author K
 * @since 1.0.0
 */
object SystemKit {

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
    fun isWindowsOs(): Boolean = getOSName().toLowerCase().contains("windows")

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