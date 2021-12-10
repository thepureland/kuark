package io.kuark.service.auth.provider.context

/**
 * 临时的Url授权定义, 将被使用者定义的相同bean name的Url授权定义Bean替代。
 *
 * @author K
 * @since 1.0.0
 */
open class TemporaryUrlAuthDefinition : IUrlAuthDefinition {

    override fun getLoginUrl(): String = "/login"

    override fun getUnauthorizedUrl(): String = "/notRole"

    override fun getDefinitionMap(): LinkedHashMap<String, String> =
        linkedMapOf(
            "/webjars/**" to "anon",
            "/login" to "anon",
            "/" to "anon",
            "/front/**" to "anon",
            "/api/**" to "anon",
            "/admin/**" to "authc",
            "/user/**" to "authc"
        )

}