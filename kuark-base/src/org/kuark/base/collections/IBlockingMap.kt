package org.kuark.base.collections

/**
 * 阻塞的Map接口，支持通过Key获取对应Value的阻塞功能，及阻塞过期功能
 *
 * @author K
 * @since 1.0.0
 */
interface IBlockingMap<K, V> {

    /**
     * 将键-值对放入Map中
     * @param key
     * @param value
     * @throws InterruptedException
     */
    @Throws(InterruptedException::class)
    fun put(key: K, value: V)

    /**
     * 根据key获取对应的value。如果获取不到会一直阻塞，直到有对应的value才返回
     * @param key
     * @return
     * @throws InterruptedException
     */
    @Throws(InterruptedException::class)
    fun take(key: K): V

    /**
     * 根据key获取对应的value。如果有值直接返回; 如果获取不到会阻塞，直到给定的过期时间到达，这时返回null
     * @param key
     * @param timeout
     * @return
     * @throws InterruptedException
     */
    @Throws(InterruptedException::class)
    fun poll(key: K, timeout: Long): V?
}