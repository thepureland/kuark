package io.kuark.context.core

/**
 * Kuark上下文
 *
 * @author K
 * @since 1.0.0
 */
class KuarkContext private constructor(builder: Builder) {

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
    var sessionAttributes: Map<String, Any?>? = null

    /** Cookie属性信息 */
    var cookieAttributes: Map<String, String?>? = null

    /** Header属性信息 */
    var headerAttributes: MutableMap<String, String?>? = null

    /** 其他信息 */
    var otherInfos: MutableMap<String, Any?>? = null

    init {
        dataSourceId = builder.dataSourceId
        subSysCode = builder.subSysCode
        tenantId = builder.tenantId
        userId = builder.userId
        traceKey = builder.traceKey
        clientInfo = builder.clientInfo
        sessionAttributes = builder.sessionAttributes
        cookieAttributes = builder.cookieAttributes
        headerAttributes = builder.headerAttributes
        otherInfos = builder.otherInfos
    }

    /**
     * Kuark上下文对象构建者
     *
     * @author K
     * @since 1.0.0
     */
    class Builder {

        /** 数据源id，为null将根据路由策略决定 */
        internal var dataSourceId: String? = null

        /** 子系统编码 */
        internal var subSysCode: String? = null

        /** 租户id */
        internal var tenantId: String? = null

        /** 用户id */
        internal var userId: String? = null

        /** 日志跟踪关键词串，格式可自定义 */
        internal var traceKey: String? = null

        /** 客户端信息对象 */
        var clientInfo: ClientInfo? = null

        /** 其他信息 */
        internal var otherInfos: MutableMap<String, Any?>? = null

        /** Session属性信息 */
        internal var sessionAttributes: MutableMap<String, Any?>? = null

        /** Cookie属性信息 */
        internal var cookieAttributes: MutableMap<String, String?>? = null

        /** Header属性信息 */
        internal var headerAttributes: MutableMap<String, String?>? = null

        fun build(): KuarkContext = KuarkContext(this)

        fun dataSourceId(dataSourceId: String?): Builder {
            this.dataSourceId = dataSourceId
            return this
        }

        fun subSysCode(subSysCode: String?): Builder {
            this.subSysCode = subSysCode
            return this
        }

        fun tenantId(tenantId: String?): Builder {
            this.tenantId = tenantId
            return this
        }

        fun userId(userId: String?): Builder {
            this.userId = userId
            return this
        }

        fun traceKey(traceKey: String?): Builder {
            this.traceKey = traceKey
            return this
        }

        fun clientInfo(clientInfo: ClientInfo): Builder {
            this.clientInfo = clientInfo
            return this
        }

        fun addSessionAttributes(vararg sessionAttributes: Pair<String, Any?>): Builder {
            if (this.sessionAttributes == null) {
                this.sessionAttributes = mutableMapOf()
            }
            this.sessionAttributes!!.putAll(sessionAttributes)
            return this
        }

        fun addCookieAttributes(vararg cookieAttributes: Pair<String, String?>): Builder {
            if (this.cookieAttributes == null) {
                this.cookieAttributes = mutableMapOf()
            }
            this.cookieAttributes!!.putAll(cookieAttributes)
            return this
        }

        fun addHeaderAttributes(vararg headerAttributes: Pair<String, String?>): Builder {
            if (this.headerAttributes == null) {
                this.headerAttributes = mutableMapOf()
            }
            this.headerAttributes!!.putAll(headerAttributes)
            return this
        }

        fun addOtherInfos(vararg otherInfos: Pair<String, Any?>): Builder {
            if (this.otherInfos == null) {
                this.otherInfos = mutableMapOf()
            }
            this.otherInfos!!.putAll(otherInfos)
            return this
        }

    }

}