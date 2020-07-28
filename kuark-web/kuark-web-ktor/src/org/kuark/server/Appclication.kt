package org.kuark.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
//    scanBasePackages = ["com.example.org.fyc"]
//    ,
//    exclude = [DataSourceAutoConfiguration::class,
//        DataSourceTransactionManagerAutoConfiguration::class,
//        ValidationAutoConfiguration::class, HibernateJpaAutoConfiguration::class]
)
open class Appclication

fun main(args: Array<String>) {
    runApplication<Appclication>(*args)
}