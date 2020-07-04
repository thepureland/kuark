package org.kuark.config.context

/**
 * 上下文参数
 *
 * @author K
 * @since 1.0.0
 */
class ContextParam {

    /**
     * 数据源id，为null将根据路由策略决定
     */
    var dataSourceId: String? = null

    /**
     * 子系统编码
     */
    var subSysCode: String? = null

    /**
     * 所有者id，依业务可以是店铺id、站点id、商户id等
     */
    var ownerId: String? = null

    /**
     * 用户id
     */
    var userId: String? = null

    /**
     * 日志跟踪关键词串，格式可自定义
     */
    var traceKey: String? = null

    /**
     * 客户端信息对象
     */
    var clientInfo = ClientInfo()

    /**
     * 其他信息
     */
    var otherInfos = hashMapOf<String, Any>()

}