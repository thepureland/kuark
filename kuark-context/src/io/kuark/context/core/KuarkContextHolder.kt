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
        contextThreadLocal.set(KuarkContext())
    }

    /**
     * 返回当前线程关联的KuarkContext
     *
     * @return Kuark上下文对象
     * @since K
     * @since 1.0.0
     */
    fun get(): KuarkContext {
//        val kuarkContext = contextThreadLocal.get() ?: KuarkContext.Builder().build()
//        contextThreadLocal.set(kuarkContext)
//        return kuarkContext
        return contextThreadLocal.get()
    }

    fun set(context: KuarkContext) {
        contextThreadLocal.set(context)
    }

}