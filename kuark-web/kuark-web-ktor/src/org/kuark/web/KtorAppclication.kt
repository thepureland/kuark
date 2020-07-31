package org.kuark.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["org.kuark"])
open class KtorAppclication

fun main(args: Array<String>) {
    runApplication<KtorAppclication>(*args)
}

