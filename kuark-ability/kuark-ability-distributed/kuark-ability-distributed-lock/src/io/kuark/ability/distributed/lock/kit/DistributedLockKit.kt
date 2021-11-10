package io.kuark.ability.distributed.lock.kit

import io.kuark.ability.distributed.lock.core.IRedissonLock
import io.kuark.context.kit.SpringKit
import org.redisson.api.RLock
import java.util.concurrent.TimeUnit


/**
 *
 * @author https://zhuanlan.zhihu.com/p/92144663
 * @author K
 * @since 1.0.0
 */
object DistributedLockKit {

    private val distributedLock = SpringKit.getBean(IRedissonLock::class)
    
    /**
     * 加锁
     * @param lockKey
     * @return
     */
    fun lock(lockKey: String): RLock {
        return distributedLock.lock(lockKey)
    }

    /**
     * 释放锁
     * @param lockKey
     */
    fun unlock(lockKey: String) {
        distributedLock.unlock(lockKey)
    }

    /**
     * 释放锁
     * @param lock
     */
    fun unlock(lock: RLock) {
        distributedLock.unlock(lock)
    }

    /**
     * 带超时的锁
     * @param lockKey
     * @param timeout 超时时间   单位：秒
     */
    fun lock(lockKey: String, timeout: Int): RLock {
        return distributedLock.lock(lockKey, timeout)
    }

    /**
     * 带超时的锁
     * @param lockKey
     * @param unit 时间单位
     * @param timeout 超时时间
     */
    fun lock(lockKey: String, timeout: Int, unit: TimeUnit): RLock {
        return distributedLock.lock(lockKey, unit, timeout)
    }

    /**
     * 尝试获取锁
     * @param lockKey
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    fun tryLock(lockKey: String, waitTime: Int, leaseTime: Int): Boolean {
        return distributedLock.tryLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime)
    }

    /**
     * 尝试获取锁
     * @param lockKey
     * @param unit 时间单位
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    fun tryLock(lockKey: String, unit: TimeUnit, waitTime: Int, leaseTime: Int): Boolean {
        return distributedLock.tryLock(lockKey, unit, waitTime, leaseTime)
    }

}