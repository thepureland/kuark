package io.kuark.context.spring

import io.kuark.context.core.KuarkContextHolder

/**
 * spring应用上下文持有者
 *
 * @author K
 * @since 1.0.0
 */
//@Component
class SpringApplicationContextHolder {

//    override fun setApplicationContext(applicationContext: ApplicationContext) {
//        SpringKit.getApplicationContext() = applicationContext
//    }

    fun test() {
        KuarkContextHolder.get()
    }

}