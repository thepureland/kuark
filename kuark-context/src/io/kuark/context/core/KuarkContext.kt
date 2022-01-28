package io.kuark.context.core

import org.springframework.session.MapSession
import org.springframework.session.Session

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

    /** 租户id */
    var tenantId: String? = null

    /** 用户id */
    var userId: String? = null

    /** 日志跟踪关键词串，格式可自定义 */
    var traceKey: String? = null

    /** 客户端信息对象 */
    var clientInfo = ClientInfo.Builder()

    /** 其他信息 */
    var otherInfos = hashMapOf<String, Any>()

    /** Session */
    var session: Session = MapSession()

    init {
        dataSourceId = builder.dataSourceId
        subSysCode = builder.subSysCode
        tenantId = builder.tenantId
        userId = builder.userId
        traceKey = builder.traceKey
        otherInfos = builder.otherInfos
        session = builder.session
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

        /** 其他信息 */
        internal var otherInfos = hashMapOf<String, Any>()

        /** Session */
        internal var session: Session = MapSession()

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

        fun addOtherInfos(vararg otherInfos: Pair<String, Any>): Builder {
            this.otherInfos.putAll(otherInfos)
            return this
        }

        fun session(session: Session): Builder {
            this.session = session
            return this
        }

    }

}