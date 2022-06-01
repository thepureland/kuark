package io.kuark.context.core

import io.kuark.base.support.IIdEntity

/**
 * Kuark上下文
 *
 * @author K
 * @since 1.0.0
 */
class KuarkContext {

    companion object {
        const val OTHER_INFO_KEY_DATA_SOURCE = "_DATA_SOURCE_"
        const val OTHER_INFO_KEY_DATABASE = "_DATABASE_"
        const val OTHER_INFO_KEY_VERIFY_CODE = "_VERIFY_CODE_"
        const val SESSION_KEY_USER = "_USER_"
    }

    /** 数据源id，为null将根据路由策略决定 */
    var dataSourceId: String? = null

    /** 子系统编码 */
    var subSysCode: String? = null

    /** 租户id */
    var tenantId: String? = null

//    var userId: String? = null

    /** 用户 */
    var user: IIdEntity<String>? = null

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