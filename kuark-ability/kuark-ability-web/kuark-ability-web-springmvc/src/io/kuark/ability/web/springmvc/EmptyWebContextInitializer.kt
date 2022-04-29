package io.kuark.ability.web.springmvc

import io.kuark.context.core.IContextInitializer
import io.kuark.context.core.KuarkContext

class EmptyWebContextInitializer: IContextInitializer {

    override fun init(builder: KuarkContext.Builder, context: KuarkContext) {
    }

}