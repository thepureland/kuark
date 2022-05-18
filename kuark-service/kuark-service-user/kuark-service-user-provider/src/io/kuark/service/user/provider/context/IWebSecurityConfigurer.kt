package io.kuark.service.user.provider.context


/**
 * Security配置器接口
 *
 * @author K
 * @since 1.0.0
 */
interface IWebSecurityConfigurer {

    /**
     * 允许的url（不会绕开springsecurity的过滤器验证，相当于只是允许该路径通过过滤器）
     */
    fun permissiveUrls(): Array<String>

    /**
     * 忽略的url（直接绕开spring security的所有filter，直接跳过验证）
     */
    fun ignoringUrls(): Array<String>

    /**
     * 登录页url
     */
    fun loginPage(): String

    /**
     * 登录处理的url
     */
    fun loginProcessingUrl(): String

    /**
     * 登录用户名参数名称
     */
    fun usernameParameter(): String

    /**
     * 登录密码参数名称
     */
    fun passwordParameter(): String

    /**
     * "记住我"的key
     * 若没有设置 key，key 默认值是一个 UUID 字符串，这样会带来一个问题：如果服务端重启，这个 key 会变，这样就导致之前派发出去的所有 remember-me 自动登录令牌失效
     */
    fun rememberMeKey(): String

    /**
     * 最大会话数
     * 比如配置最大会话数为 1，这样后面的登录就会自动踢掉前面的登录
     */
    fun maximumSessions(): Int

    /**
     * 是否禁止新的登录
     */
    fun maxSessionsPreventsLogin(): Boolean

}