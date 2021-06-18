package io.kuark.ability.auth.context

/**
 * url授权定义
 * 扩展点：使用者需将该接口的实现定义为spring的bean，bean name为urlAuthDefinition，并用spring的@Primary注解标注。
 *
 * @author K
 * @since 1.0.0
 */
interface IUrlAuthDefinition {

    /**
     * 返回登录的url
     *
     * @return 登录的url
     */
    fun getLoginUrl(): String

    /**
     * 返回权限不足跳转url
     *
     * @return 权限不足跳转的url
     */
    fun getUnauthorizedUrl(): String

    /**
     * 返回url过滤器定义规则
     * 默认的过滤器有：anno、authc、authcBasic、logout、noSessionCreation、perms、port、rest、roles、ssl、user
     *
     * @return Map<url模式串，过滤器>
     */
    fun getDefinitionMap(): LinkedHashMap<String, String>

    companion object {
        const val BEAN_NAME = "urlAuthDefinition"
    }

}