package io.kuark.ability.distributed.lock.core

import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import java.util.concurrent.TimeUnit


/**
 *
 *
 * @author K
 * @since 1.0.0
 */
class RedissonLock(val redissonClient: RedissonClient) : IRedissonLock {

    override fun lock(lockKey: String): RLock {
        val lock = redissonClient.getLock(lockKey)
        lock.lock()
        return lock
    }

    override fun lock(lockKey: String, timeout: Int): RLock {
        val lock = redissonClient.getLock(lockKey)
        lock.lock(timeout.toLong(), TimeUnit.SECONDS)
        return lock
    }

    override fun lock(lockKey: String, unit: TimeUnit, timeout: Int): RLock {
        val lock = redissonClient.getLock(lockKey)
        lock.lock(timeout.toLong(), unit)
        return lock
    }

    override fun tryLock(lockKey: String, unit: TimeUnit, waitTime: Int, leaseTime: Int): Boolean {
        val lock = redissonClient.getLock(lockKey)
        return try {
            lock.tryLock(waitTime.toLong(), leaseTime.toLong(), unit)
        } catch (e: InterruptedException) {
            false
        }
    }

    override fun unlock(lockKey: String) {
        val lock = redissonClient.getLock(lockKey)
        lock.unlock()
    }

    override fun unlock(lock: RLock) {
        lock.unlock()
    }

}