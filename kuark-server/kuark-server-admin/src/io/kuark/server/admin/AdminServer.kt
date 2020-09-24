package io.kuark.server.admin

import de.codecentric.boot.admin.server.config.EnableAdminServer
import org.springframework.boot.SpringApplication

import org.springframework.boot.autoconfigure.SpringBootApplication




/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@EnableAdminServer
@SpringBootApplication
open class AdminServer {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(AdminServer::class.java, *args)
        }
    }

}