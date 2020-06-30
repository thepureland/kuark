package org.kuark.config.context

object KuarkContext {

    private val contextParamThreadLocal = InheritableThreadLocal<ContextParam>()

    init {
        contextParamThreadLocal.set(ContextParam())
    }

    fun get(): ContextParam {
        return contextParamThreadLocal.get()
    }

}