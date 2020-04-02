package org.kuark.spring.init

import org.kuark.spring.kit.SpringKit
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
class SpringApplicationContextHolder: ApplicationContextAware {

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        SpringKit.applicationContext = applicationContext
    }

}