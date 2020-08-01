package org.kuark.context.core

/**
 * Kuark的上下文
 *
 * @since K
 * @since 1.0.0
 */
object KuarkContext {

    private val contextParamThreadLocal = InheritableThreadLocal<ContextParam>()

    init {
        contextParamThreadLocal.set(ContextParam())
    }

    fun get(): ContextParam {
        return contextParamThreadLocal.get()
    }

}