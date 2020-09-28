package io.kuark.ability.distributed.lock

import org.redisson.api.RLock

import java.util.concurrent.TimeUnit


/**
 *
 *
 * @author https://zhuanlan.zhihu.com/p/92144663
 * @author K
 * @since 1.0.0
 */
interface IDistributedLock {

    fun lock(lockKey: String): RLock

    fun lock(lockKey: String, timeout: Int): RLock

    fun lock(lockKey: String, unit: TimeUnit, timeout: Int): RLock

    fun tryLock(lockKey: String, unit: TimeUnit, waitTime: Int, leaseTime: Int): Boolean

    fun unlock(lockKey: String)

    fun unlock(lock: RLock)

}