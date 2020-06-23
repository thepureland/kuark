package org.kuark.config.context

object RequestContext {

    private val contextParamThreadLocal = InheritableThreadLocal<ContextParam>()

    fun set(contextParam: ContextParam) {
        contextParamThreadLocal.set(contextParam)
    }

    fun get(): ContextParam {
        return contextParamThreadLocal.get()
    }

}