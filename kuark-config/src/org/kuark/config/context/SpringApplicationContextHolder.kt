package org.kuark.config.context

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
        KuarkContext.get()
    }

}