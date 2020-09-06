package io.kuark.context.core

/**
 * Kuark的上下文持有者
 *
 * @since K
 * @since 1.0.0
 */
object KuarkContextHolder {

    private val contextThreadLocal = InheritableThreadLocal<KuarkContext>()

    init {
        contextThreadLocal.set(KuarkContext.Builder().build())
    }

    fun get(): KuarkContext = contextThreadLocal.get()

}