package io.kuark.ability.distributed.lock

import io.kuark.base.log.LogFactory
import io.kuark.test.SpringTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


/**
 *
 * @author https://zhuanlan.zhihu.com/p/92144663
 * @author K
 * @since 1.0.0
 */
class DistributedLockTest: SpringTest() {

    private val log = LogFactory.getLog(DistributedLockTest::class)

    /**
     * 锁测试共享变量
     */
    private var lockCount = 10

    /**
     * 无锁测试共享变量
     */
    private var count = 10

    /**
     * 模拟线程数
     */
    private val threadNum = 10

    /**
     * 模拟并发测试加锁和不加锁
     * @return
     */
    fun lock() {
        // 计数器
        val countDownLatch = CountDownLatch(1)
        for (i in 0 until threadNum) {
            val myThread = Thread {
                try {
                    // 阻塞当前线程，直到计时器的值为0
                    countDownLatch.await()
                } catch (e: InterruptedException) {
                    log.error(e, e.message)
                }
                // 无锁操作
                testCount()
                // 加锁操作
                testLockCount()
            }
            myThread.start()
        }
        // 释放所有线程
        countDownLatch.countDown()
    }

    /**
     * 加锁测试
     */
    private fun testLockCount() {
        val lockKey = "lock-test"
        try {
            // 加锁，设置超时时间2s
            DistributedLockKit.lock(lockKey, 2, TimeUnit.SECONDS)
            lockCount--
            log.info("lockCount值：$lockCount")
        } catch (e: Exception) {
            log.error(e.message, e)
        } finally {
            // 释放锁
            DistributedLockKit.unlock(lockKey)
        }
    }

    /**
     * 无锁测试
     */
    private fun testCount() {
        count--
        log.info("count值：$count")
    }

}