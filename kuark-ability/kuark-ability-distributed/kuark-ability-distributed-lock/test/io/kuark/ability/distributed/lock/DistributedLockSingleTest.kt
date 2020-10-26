package io.kuark.ability.distributed.lock

import io.kuark.ability.distributed.lock.annotations.DistributedLock
import io.kuark.ability.distributed.lock.kit.DistributedLockKit
import io.kuark.base.log.LogFactory
import io.kuark.test.SpringTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


/**
 * 分布式锁单结点测试用例
 *
 * @author K
 * @since 1.0.0
 */
class DistributedLockSingleTest : SpringTest() {

    @Autowired
    private lateinit var testLockByAnnotation: TestLockByAnnotation

    val log = LogFactory.getLog(DistributedLockSingleTest::class)

    /** 锁测试共享变量 */
    private var lockCount = 10

    /** 锁测试共享变量(使用注解加锁的方式) */
    var lockCountByAnnotation = 10

    /** 无锁测试共享变量 */
    private var count = 10

    /** 模拟线程数 */
    private val threadNum = 10

    @Test
    fun lock() {
        val countDownLatch = CountDownLatch(10)

        for (i in 0 until threadNum) {
            Thread {
                // 无锁操作
                testCount()
                // 加锁操作
                testLockCount()
                // 加锁操作(使用注解加锁的方式)
                testLockByAnnotation.execute(this)
                countDownLatch.countDown()
            }.start()
        }

        countDownLatch.await()
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
            log.info("lockCount: $lockCount")
        } catch (e: Exception) {
            log.error(e, e.message)
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
        log.info("count: $count")
    }

}

/**
 * 测试使用注解加锁的方式
 */
@Component
open class TestLockByAnnotation {

    @DistributedLock
    fun execute(test: DistributedLockSingleTest) {
        test.lockCountByAnnotation--
        test.log.info("lockCountByAnnotation: ${test.lockCountByAnnotation}")
    }

}