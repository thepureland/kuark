package io.kuark.auth.context

import org.apache.shiro.mgt.SecurityManager
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.spring.web.ShiroFilterFactoryBean
import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.annotation.Resource

@Configuration
open class AuthConfig {

    @Resource(name = IUrlAuthDefinition.BEAN_NAME)
    private lateinit var urlAuthDefinition: IUrlAuthDefinition

    @Bean(name = ["shiroFilter"])
    open fun shiroFilter(securityManager: SecurityManager): ShiroFilterFactoryBean {
        val shiroFilterFactoryBean = ShiroFilterFactoryBean()
        shiroFilterFactoryBean.securityManager = securityManager
        shiroFilterFactoryBean.loginUrl = urlAuthDefinition.getLoginUrl()
        shiroFilterFactoryBean.unauthorizedUrl = urlAuthDefinition.getUnauthorizedUrl()
        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        val filterChainDefinitionMap = urlAuthDefinition.getDefinitionMap()
        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截 剩余的都需要认证
        filterChainDefinitionMap["/**"] = "authc"
        shiroFilterFactoryBean.filterChainDefinitionMap = filterChainDefinitionMap
        return shiroFilterFactoryBean
    }

    @Bean
    open fun securityManager(): SecurityManager {
        val defaultSecurityManager = DefaultWebSecurityManager()
//        defaultSecurityManager.setRealm(customRealm()) //TODO
        return defaultSecurityManager
    }


    //TODO https://blog.csdn.net/bicheng4769/article/details/86668209
    @Bean
    @ConditionalOnMissingBean
    open fun customRealm(): AuthorizingRealm {
        return AuthRealm()
    }

    @Bean(name = [IUrlAuthDefinition.BEAN_NAME])
    @ConditionalOnMissingBean
    open fun urlAuthDefinition(): IUrlAuthDefinition {
        return TemporaryUrlAuthDefinition()
    }


}