package org.kuark.config.context

import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

//@Component
class SpringApplicationContextHolder {

//    override fun setApplicationContext(applicationContext: ApplicationContext) {
//        SpringKit.getApplicationContext() = applicationContext
//    }

    fun test() {
        KuarkContext.get()
    }

}