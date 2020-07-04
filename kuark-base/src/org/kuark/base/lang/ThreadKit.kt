package org.kuark.base.lang

import org.kuark.base.log.LogFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

/**
 * 线程相关工具类
 *
 * @author K
 * @since 1.0.0
 */
object ThreadKit {

    private val LOG = LogFactory.getLog(ThreadKit::class)

    /**
     * 让当前线程休眠指定的毫秒数, 并忽略InterruptedException.
     *
     * @param millis 休眠的毫秒数
     * @since 1.0.0
     */
    fun sleep(millis: Long) {
        try {
            Thread.sleep(millis)
        } catch (e: InterruptedException) {
            LOG.error(e)
        }
    }

    /**
     * 让当前线程休眠指定的时间, 并忽略InterruptedException.
     *
     * @param duration 休眠的时间值
     * @param unit 休眠的时间单位
     * @since 1.0.0
     */
    fun sleep(duration: Long, unit: TimeUnit) {
        try {
            Thread.sleep(unit.toMillis(duration))
        } catch (e: InterruptedException) {
            LOG.error(e)
        }
    }

    /**
     * 按照ExecutorService JavaDoc示例代码编写的Graceful Shutdown方法. 先使用shutdown, 停止接收新任务并尝试完成所有已存在任务.
     * 如果超时, 则调用shutdownNow, 取消在workQueue中Pending的任务,并中断所有阻塞函数. 如果仍超時，則強制退出.
     * 另对在shutdown时线程本身被调用中断做了处理.
     *
     * @param pool 线程池
     * @param shutdownTimeout 关闭超时时间
     * @param shutdownNowTimeout 现在关闭超时时间
     * @param timeUnit 时间单位
     * @since 1.0.0
     */
    fun gracefulShutdown(pool: ExecutorService, shutdownTimeout: Int, shutdownNowTimeout: Int, timeUnit: TimeUnit?) {
        pool.shutdown() // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(shutdownTimeout.toLong(), timeUnit)) {
                pool.shutdownNow() // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(shutdownNowTimeout.toLong(), timeUnit)) {
                    LOG.warn("线程池未结束!")
                }
            }
        } catch (ie: InterruptedException) {
            LOG.error(ie)
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow()
            // Preserve interrupt status
            Thread.currentThread().interrupt()
        }
    }

    /**
     * 直接调用shutdownNow的方法, 有timeout控制. 取消在workQueue中Pending的任务, 并中断所有阻塞函数.
     *
     * @param pool 线程池
     * @param timeout 超时时间
     * @param timeUnit 时间单位
     * @since 1.0.0
     */
    fun normalShutdown(pool: ExecutorService, timeout: Int, timeUnit: TimeUnit?) {
        try {
            pool.shutdownNow()
            if (!pool.awaitTermination(timeout.toLong(), timeUnit)) {
                LOG.warn("线程池未结束!")
            }
        } catch (ie: InterruptedException) {
            LOG.error(ie)
            Thread.currentThread().interrupt()
        }
    }

    /**
     * 当调用的方法栈里不含指定类名时，把栈信息打印到日志中.
     * 该方法可用于信息跟踪，如：跟踪资源是否被关闭。
     * 该方法只有在日志级别为DEBUG时才有效
     *
     * @param clazz 类
     * @since 1.0.0
     */
    fun printStackTraceOnNotCallByClass(clazz: KClass<*>) {
        if (LOG.isDebugEnabled()) {
            var find = false
            val stackTrace = getStackTrace()
            for (elem in stackTrace) {
                if (elem.className == clazz.java.name) {
                    find = true
                    break
                }
            }
            if (!find) {
                LOG.warn("方法栈里不含指定类: $clazz")
                for (elem in stackTrace) {
                    LOG.warn(elem.toString())
                }
            }
        }
    }

    /**
     * 抛出异常，打印方法调用栈日志.
     * 该方法只有在日志级别为DEBUG时才有效
     *
     * @since 1.0.0
     */
    fun printStackTrace() {
        if (LOG.isDebugEnabled()) {
            for (elem in getStackTrace()) {
                LOG.debug(elem.toString())
            }
        }
    }

    /**
     * 获取调用栈
     *
     * @return 调用栈
     */
    fun getStackTrace(): Array<StackTraceElement> = Thread.currentThread().stackTrace

}