package org.kuark.test

import org.springframework.boot.SpringApplication
import org.springframework.boot.test.context.SpringBootContextLoader


/**
 *
 *
 * @author K
 * @since 1.0.0
 */
class JunitSpringBootContextLoader: SpringBootContextLoader() {

    override fun getSpringApplication(): SpringApplication {
        val app = super.getSpringApplication()
        app.addListeners(LocalEnvironmentPrepareEventListener())
        return app
    }

}