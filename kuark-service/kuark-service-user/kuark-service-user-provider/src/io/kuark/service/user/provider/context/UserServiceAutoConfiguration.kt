package io.kuark.service.user.provider.context

import io.kuark.context.core.IContextInitializer
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary


@Configuration
open class UserServiceAutoConfiguration {

    @Bean
    @Primary
    @ConditionalOnMissingBean
    open fun sysContextInitializer(): IContextInitializer = UserContextInitializer()

}