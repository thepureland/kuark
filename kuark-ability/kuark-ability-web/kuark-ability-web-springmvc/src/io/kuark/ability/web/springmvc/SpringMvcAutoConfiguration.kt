package io.kuark.ability.web.springmvc

import io.kuark.context.core.IContextInitializer
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.context.request.RequestContextListener
import org.springframework.web.filter.HttpPutFormContentFilter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import java.util.*
import javax.servlet.Filter


@Configuration
@EnableWebMvc
open class SpringMvcAutoConfiguration {

//    @Bean
//    open fun mutipartResolvet(): CommonsMultipartResolver {
//        return CommonsMultipartResolver()
//    }

    @Bean
    open fun requestContextListener(): RequestContextListener = RequestContextListener()

    @Bean
    open fun servletListenerRegistration(requestContextListener: RequestContextListener): ServletListenerRegistrationBean<EventListener> {
        val registrationBean = ServletListenerRegistrationBean<EventListener>()
        registrationBean.listener = requestContextListener()
        registrationBean.order = 1
        return registrationBean
    }

    @Bean
    @ConditionalOnMissingBean
    open fun webContextInitializer() : IContextInitializer = EmptyWebContextInitializer()

    @Bean
    @ConditionalOnMissingBean
    open fun webContextInitFilter(): IWebContextInitFilter = WebContextInitFilter()

    @Bean
    @ConditionalOnMissingBean
    open fun registerAuthFilter(contextInitFilter: IWebContextInitFilter): FilterRegistrationBean<*> {
        val registration = FilterRegistrationBean<Filter>()
        registration.filter = contextInitFilter
        registration.addUrlPatterns("/*")
        registration.setName("contextInitFilter")
        registration.order = Ordered.HIGHEST_PRECEDENCE
        return registration
    }

    @Bean
    open fun httpPutFormContentFilter(): HttpPutFormContentFilter? {
        return HttpPutFormContentFilter()
    }

}