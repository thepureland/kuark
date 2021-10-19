package io.kuark.ability.distributed.lock.annotations

import io.kuark.ability.distributed.lock.kit.DistributedLockKit
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock


/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@Component
@Aspect
@Lazy(false)
class DistributedLockAspect {

    @Pointcut("@annotation(io.kuark.ability.distributed.lock.annotations.DistributedLock)")
    fun cut() {
    }

    @Around("cut()")
    fun around(joinPoint: ProceedingJoinPoint): Any? {
        var res = false
        var obj: Any? = null
        val seckillId = joinPoint.args[0].toString()
        try {
            res = DistributedLockKit.tryLock(seckillId, TimeUnit.SECONDS, 3, 20)
            if (res) {
                obj = joinPoint.proceed()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            throw RuntimeException()
        } finally {
            if (res) {
                DistributedLockKit.unlock(seckillId)
            }
        }
        return obj
    }

    companion object {
        private val lock = ReentrantLock(true) // 互斥锁 参数默认false，不公平锁
    }

}