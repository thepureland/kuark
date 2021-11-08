package io.kuark.ability.web.springmvc

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.FormContentFilter
import org.springframework.web.filter.HttpPutFormContentFilter
import org.springframework.web.servlet.config.annotation.EnableWebMvc


@Configuration
@EnableWebMvc
open class SpringMvcAutoConfiguration {

//    @Bean
//    open fun mutipartResolvet(): CommonsMultipartResolver {
//        return CommonsMultipartResolver()
//    }

    @Bean
    open fun httpPutFormContentFilter(): HttpPutFormContentFilter? {
        return HttpPutFormContentFilter()
    }

}