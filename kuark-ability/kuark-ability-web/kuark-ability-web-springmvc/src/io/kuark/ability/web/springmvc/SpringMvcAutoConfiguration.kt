package io.kuark.ability.web.springmvc

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
    open fun getDemoListener(requestContextListener: RequestContextListener): ServletListenerRegistrationBean<EventListener> {
        val registrationBean = ServletListenerRegistrationBean<EventListener>()
        registrationBean.setListener(requestContextListener())
        registrationBean.order = 1
        return registrationBean
    }


    @Bean
    @ConditionalOnMissingBean
    open fun contextInitFilter(): IContextInitFilter = EmptyFilter()

    @Bean
    @ConditionalOnMissingBean
    open fun registerAuthFilter(contextInitFilter: IContextInitFilter): FilterRegistrationBean<*> {
        val registration = FilterRegistrationBean<Filter>()
        registration.filter = contextInitFilter
        registration.addUrlPatterns("/*")
        registration.setName("contextInitFilter")
        registration.order = 1
        return registration
    }

    @Bean
    open fun httpPutFormContentFilter(): HttpPutFormContentFilter? {
        return HttpPutFormContentFilter()
    }

}