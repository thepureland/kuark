package org.kuark.auth.context

import org.apache.shiro.spring.web.ShiroFilterFactoryBean
import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.apache.shiro.mgt.SecurityManager

@Configuration
class AuthConfig {

    @Bean(name = ["shiroFilter"])
    fun shiroFilter(securityManager: SecurityManager): ShiroFilterFactoryBean {
        val shiroFilterFactoryBean = ShiroFilterFactoryBean()
        shiroFilterFactoryBean.securityManager = securityManager
        shiroFilterFactoryBean.loginUrl = "/login"
        shiroFilterFactoryBean.unauthorizedUrl = "/notRole"
        val filterChainDefinitionMap: MutableMap<String, String> =
            LinkedHashMap()
        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        //TODO 抽离
        filterChainDefinitionMap["/webjars/**"] = "anon"
        filterChainDefinitionMap["/login"] = "anon"
        filterChainDefinitionMap["/"] = "anon"
        filterChainDefinitionMap["/front/**"] = "anon"
        filterChainDefinitionMap["/api/**"] = "anon"
        filterChainDefinitionMap["/admin/**"] = "authc"
        filterChainDefinitionMap["/user/**"] = "authc"
        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截 剩余的都需要认证
        filterChainDefinitionMap["/**"] = "authc"
        shiroFilterFactoryBean.filterChainDefinitionMap = filterChainDefinitionMap
        return shiroFilterFactoryBean
    }

    @Bean
    fun securityManager(): SecurityManager {
        val defaultSecurityManager = DefaultWebSecurityManager()
//        defaultSecurityManager.setRealm(customRealm()) //TODO
        return defaultSecurityManager
    }


    //TODO https://blog.csdn.net/bicheng4769/article/details/86668209
//    @Bean
//    fun customRealm(): CustomRealm {
//        return CustomRealm()
//    }


}