package io.kuark.context.core

/**
 * Kuark上下文
 *
 * @author K
 * @since 1.0.0
 */
class KuarkContext private constructor(builder: Builder) {

    /** 数据源id，为null将根据路由策略决定 */
    var dataSourceId: String? = null

    /** 子系统编码 */
    var subSysCode: String? = null

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId: String? = null

    /** 用户id */
    var userId: String? = null

    /** 日志跟踪关键词串，格式可自定义 */
    var traceKey: String? = null

    /** 客户端信息对象 */
    var clientInfo = ClientInfo.Builder()

    /** 其他信息 */
    var otherInfos = hashMapOf<String, Any>()

    init {
        dataSourceId = builder.dataSourceId
        subSysCode = builder.subSysCode
        ownerId = builder.ownerId
        userId = builder.userId
        traceKey = builder.traceKey
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

        /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
        internal var ownerId: String? = null

        /** 用户id */
        internal var userId: String? = null

        /** 日志跟踪关键词串，格式可自定义 */
        internal var traceKey: String? = null

        /** 其他信息 */
        internal var otherInfos = hashMapOf<String, Any>()

        fun build(): KuarkContext = KuarkContext(this)

        fun dataSourceId(dataSourceId: String?): Builder {
            this.dataSourceId = dataSourceId
            return this
        }

        fun subSysCode(subSysCode: String?): Builder {
            this.subSysCode = subSysCode
            return this
        }

        fun ownerId(ownerId: String?): Builder {
            this.ownerId = ownerId
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

        fun addOtherInfos(vararg otherInfos: Pair<String, Any>): Builder {
            this.otherInfos.putAll(otherInfos)
            return this
        }

    }

}