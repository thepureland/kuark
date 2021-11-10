package io.kuark.context.config

import io.kuark.context.context.CustomValidatorFactoryBean
import org.hibernate.validator.HibernateValidator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean


@Configuration
open class ValidationConfiguration {

    @Bean
    open fun defaultValidator(): LocalValidatorFactoryBean {
        val validator = CustomValidatorFactoryBean()
        validator.setProviderClass(HibernateValidator::class.java)
        validator.afterPropertiesSet()
        return validator
    }

}