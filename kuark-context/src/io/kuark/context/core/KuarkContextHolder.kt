package io.kuark.context.core

/**
 * Kuark的上下文持有者
 *
 * @since K
 * @since 1.0.0
 */
object KuarkContextHolder {

    private val contextParamThreadLocal = InheritableThreadLocal<ContextParam>()

    init {
        contextParamThreadLocal.set(ContextParam())
    }

    fun get(): ContextParam {
        return contextParamThreadLocal.get()
    }

}