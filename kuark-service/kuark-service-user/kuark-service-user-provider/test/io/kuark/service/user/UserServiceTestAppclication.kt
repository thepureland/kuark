package io.kuark.service.user

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["io.kuark"])
open class UserServiceTestAppclication

fun main(args: Array<String>) {
    runApplication<UserServiceTestAppclication>(*args)
}

