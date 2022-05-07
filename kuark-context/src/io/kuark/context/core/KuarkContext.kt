package io.kuark.context.core

/**
 * Kuark上下文
 *
 * @author K
 * @since 1.0.0
 */
class KuarkContext {

    companion object {
        const val OTHER_INFO_KEY_DATA_SOURCE = "DATA_SOURCE"
        const val OTHER_INFO_KEY_DATABASE = "DATABASE"
        const val OTHER_INFO_KEY_VERIFY_CODE = "VERIFY_CODE"
        const val SESSION_KEY_USER_ID = "USER_ID"
    }

    /** 数据源id，为null将根据路由策略决定 */
    var dataSourceId: String? = null

    /** 子系统编码 */
    var subSysCode: String? = null

    /** 租户id */
    var tenantId: String? = null

    /** 用户id */
    var userId: String? = null

    /** 日志跟踪关键词串，格式可自定义 */
    var traceKey: String? = null

    /** 客户端信息对象 */
    var clientInfo: ClientInfo? = null

    /** Session属性信息 */
    var sessionAttributes: MutableMap<String, Any?>? = null

    /** Cookie属性信息 */
    var cookieAttributes: MutableMap<String, String?>? = null

    /** Header属性信息 */
    var headerAttributes: MutableMap<String, String?>? = null

    /** 其他信息 */
    var otherInfos: MutableMap<String, Any?>? = null

    fun addSessionAttributes(vararg sessionAttributes: Pair<String, Any?>): KuarkContext {
        if (this.sessionAttributes == null) {
            this.sessionAttributes = mutableMapOf()
        }
        this.sessionAttributes!!.putAll(sessionAttributes)
        return this
    }

    fun addCookieAttributes(vararg cookieAttributes: Pair<String, String?>): KuarkContext {
        if (this.cookieAttributes == null) {
            this.cookieAttributes = mutableMapOf()
        }
        this.cookieAttributes!!.putAll(cookieAttributes)
        return this
    }

    fun addHeaderAttributes(vararg headerAttributes: Pair<String, String?>): KuarkContext {
        if (this.headerAttributes == null) {
            this.headerAttributes = mutableMapOf()
        }
        this.headerAttributes!!.putAll(headerAttributes)
        return this
    }

    fun addOtherInfos(vararg otherInfos: Pair<String, Any?>): KuarkContext {
        if (this.otherInfos == null) {
            this.otherInfos = mutableMapOf()
        }
        this.otherInfos!!.putAll(otherInfos)
        return this
    }

}