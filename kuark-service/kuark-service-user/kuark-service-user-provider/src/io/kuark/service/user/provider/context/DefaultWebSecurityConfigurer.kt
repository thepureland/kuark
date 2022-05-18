package io.kuark.service.user.provider.context


/**
 * 默认的Security配置器
 *
 * @author K
 * @since 1.0.0
 */
open class DefaultWebSecurityConfigurer : IWebSecurityConfigurer {

    override fun permissiveUrls(): Array<String> = arrayOf("/login", "/doLogin", "/logout", "/error")

    override fun ignoringUrls(): Array<String> = arrayOf("/js/**", "/css/**", "/images/**")

    override fun loginPage(): String = "/login"

    override fun loginProcessingUrl(): String = "/doLogin"

    override fun usernameParameter(): String = "username"

    override fun passwordParameter(): String = "password"

    override fun rememberMeKey(): String = "KUARK-user-Key"

    override fun maximumSessions(): Int = 1

    override fun maxSessionsPreventsLogin(): Boolean = false

}