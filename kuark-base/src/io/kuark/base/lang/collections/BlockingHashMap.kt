package io.kuark.base.lang.collections

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

/**
 * 阻塞的HashMap，封装了一个同步的Map(ConcurrentMap)，支持通过Key获取对应Value的阻塞功能，及阻塞过期功能
 *
 * @author K
 * @since 1.0.0
 */
class BlockingHashMap<K, V> : IBlockingMap<K, V> {

    private val map: ConcurrentMap<K, Item<V>>
    private val lock = ReentrantLock()

    /**
     * 将键-值对放入同步的Map中。键、值都不允许传null，否则会抛java.lang.NullPointerException
     */
    @Throws(InterruptedException::class)
    override fun put(key: K, value: V) {
        val lock = lock
        lock.lockInterruptibly()
        try {
            if (map.containsKey(key)) {
                val item = map[key]!!
                item.put(value)
            } else {
                val item =
                    Item<V>()
                map[key] = item
                item.put(value)
            }
        } finally {
            lock.unlock()
        }
    }

    /**
     * 根据key获取对应的value。如果获取不到会一直阻塞，直到有对应的value才返回
     */
    @Throws(InterruptedException::class)
    override fun take(key: K): V {
        val lock = lock
        lock.lockInterruptibly()
        try {
            if (!map.containsKey(key)) {
                map[key] = Item()
            }
        } finally {
            lock.unlock()
        }
        val item = map[key]!!
        val x = item.take()
        map.remove(key)
        return x
    }

    /**
     * 根据key获取对应的value。如果有值直接返回; 如果获取不到会阻塞，直到给定的过期时间到达，这时返回null
     */
    @Throws(InterruptedException::class)
    override fun poll(key: K, timeout: Long): V? {
        val lock = lock
        lock.lockInterruptibly()
        try {
            if (!map.containsKey(key)) {
                map[key] = Item()
            }
        } finally {
            lock.unlock()
        }
        val item = map[key]!!
        val x = item.poll(timeout)
        map.remove(key)
        return x
    }

    /**
     * 封装了对值的处理的对象
     */
    private inner class Item<E> {
        private val lock = ReentrantLock()
        private val cond = lock.newCondition()
        private var obj: E? = null

        @Throws(InterruptedException::class)
        fun put(o: E) {
            if (o == null) {
                throw NullPointerException()
            }
            val lock = this.lock
            lock.lockInterruptibly()
            try {
                obj = o
                cond.signal()
            } finally {
                lock.unlock()
            }
        }

        @Throws(InterruptedException::class)
        fun take(): E {
            val lock = this.lock
            lock.lockInterruptibly()
            return try {
                try {
                    while (obj == null) {
                        cond.await()
                    }
                } catch (ie: InterruptedException) {
                    cond.signal()
                    throw ie
                }
                obj
            } finally {
                lock.unlock()
            }!!
        }

        @Throws(InterruptedException::class)
        fun poll(timeout: Long): E? {
            var timeoutNanos = TimeUnit.MILLISECONDS.toNanos(timeout)
            val x: E
            val lock = this.lock
            lock.lockInterruptibly()
            try {
                while (true) {
                    if (obj != null) {
                        x = obj!!
                        break
                    }
                    if (timeoutNanos <= 0) {
                        return null
                    }
                    timeoutNanos = try {
                        cond.awaitNanos(timeoutNanos)
                    } catch (ie: InterruptedException) {
                        cond.signal()
                        throw ie
                    }
                }
            } finally {
                lock.unlock()
            }
            return x
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val hashBlockingMap: IBlockingMap<String, String> =
                BlockingHashMap()

            // 消费者
            object : Thread() {
                override fun run() {
                    while (true) {
                        try {
                            val value = hashBlockingMap.poll("1", 10000)
                            println("value: $value")
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }
                }
            }.start()

            // 生产者
            for (i in 0..9) {
                try {
                    hashBlockingMap.put("1", "1000$i")
                    Thread.sleep(3000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    init {
        map = ConcurrentHashMap()
    }
}