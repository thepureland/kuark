package io.kuark.ability.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["io.kuark"])
open class KtorAppclication

fun main(args: Array<String>) {
    runApplication<KtorAppclication>(*args)
}

