package io.kuark.ability.web.springmvc

import io.kuark.context.core.IContextInitializer
import org.apache.catalina.connector.Connector
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.session.web.http.SessionRepositoryFilter
import org.springframework.web.context.request.RequestContextListener
import org.springframework.web.filter.HttpPutFormContentFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.*
import javax.servlet.Filter


@Configuration
@EnableWebMvc
open class SpringMvcAutoConfiguration : WebMvcConfigurer {

//    @Bean
//    open fun mutipartResolvet(): CommonsMultipartResolver {
//        return CommonsMultipartResolver()
//    }

    @Bean
    open fun webServerFactory(): TomcatServletWebServerFactory {
        val factory = TomcatServletWebServerFactory()
        factory.addConnectorCustomizers(object : TomcatConnectorCustomizer {

            override fun customize(connector: Connector) {
                // 解决用tomcat时，get请求传入特殊符号报400错误（RFC7230andRFC3986）的问题
                connector.setProperty("relaxedPathChars", "\"<>[\\]^`{|}");
                connector.setProperty("relaxedQueryChars", "\"<>[\\]^`{|}");
            }

        })
        return factory
    }

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
    open fun webContextInitializer(): IContextInitializer = EmptyWebContextInitializer()

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
        registration.order = SessionRepositoryFilter.DEFAULT_ORDER + 1


        return registration
    }

    @Bean
    open fun httpPutFormContentFilter(): HttpPutFormContentFilter? {
        return HttpPutFormContentFilter()
    }

    @Bean
    @ConditionalOnMissingBean
    open fun corsHandlerInterceptor(): CorsHandlerInterceptor = CorsHandlerInterceptor()

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(corsHandlerInterceptor()).addPathPatterns("/**");
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowCredentials(true)
            .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS", "HEAD")
            .maxAge(3600 * 24)
    }

}