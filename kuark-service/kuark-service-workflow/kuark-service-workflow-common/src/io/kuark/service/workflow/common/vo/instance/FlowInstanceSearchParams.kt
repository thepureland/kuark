package io.kuark.service.workflow.common.vo.instance

import java.util.*

/**
 * 流程实例查询参数
 *
 * 当属性不为空时才会将该属性作为查询条件，各属性间是”与“的关系
 *
 * @author K
 * @since 1.0.0
 */
class FlowInstanceSearchParams {

    /** 流程定义key(bpmn文件中process元素的id)，支持忽略大小写模糊查询 */
    var definitionKey: String? = null

    /** 业务主键，支持忽略大小写模糊查询 */
    var bizKey: String? = null

    /** 实例名称，支持忽略大小写模糊查询 */
    var name: String? = null

    /** 发起时间始(大于等于) */
    var startTimeFrom: Date? = null

    /** 发起时间止(小于等于) */
    var startTimeTo: Date? = null
    
    /** 发起者id */
    var startUserId: String? = null
    
    /** 流程定义名称，支持忽略大小写模糊查询 */
    var definitionName: String? = null
    
    /** 流程定义版本 */
    var definitionVersion: Int? = null

    /** 是否挂起 */
    var suspend: Boolean? = null

    /**
     * 空构造器，仅供框架反射使用
     */
    constructor()

    private constructor(builder: Builder) {
        definitionKey = builder.definitionKey
        bizKey = builder.bizKey
        name = builder.name
        startTimeFrom = builder.startTimeFrom
        startTimeTo = builder.startTimeTo
        startUserId = builder.startUserId
        definitionName = builder.definitionName
        definitionVersion = builder.definitionVersion
        suspend = builder.suspend
    }


    /**
     * 流程实例查询参数构建者
     *
     * @author K
     * @since 1.0.0
     */
    class Builder {

        var definitionKey: String? = null
        var bizKey: String? = null
        var name: String? = null
        var startTimeFrom: Date? = null
        var startTimeTo: Date? = null
        var startUserId: String? = null
        var definitionName: String? = null
        var definitionVersion: Int? = null
        var suspend: Boolean? = null

        /**
         * 构建流程实例查询参数对象
         */
        fun build(): FlowInstanceSearchParams = FlowInstanceSearchParams(this)

        /** 流程定义key(bpmn文件中process元素的id)，支持忽略大小写模糊查询 */
        fun definitionKey(definitionKey: String?): Builder {
            this.definitionKey = definitionKey
            return this
        }

        /** 业务主键，支持忽略大小写模糊查询 */
        fun bizKey(bizKey: String?): Builder {
            this.bizKey = bizKey
            return this
        }

        /** 实例名称，支持忽略大小写模糊查询 */
        fun name(name: String?): Builder {
            this.name = name
            return this
        }

        /** 发起时间始(大于等于) */
        fun startTimeFrom(startTimeFrom: Date?): Builder {
            this.startTimeFrom = startTimeFrom
            return this
        }

        /** 发起时间止(小于等于) */
        fun startTimeTo(startTimeTo: Date?): Builder {
            this.startTimeTo = startTimeTo
            return this
        }

        /** 发起者id */
        fun startUserId(startUserId: String?): Builder {
            this.startUserId = startUserId
            return this
        }

        /** 流程定义名称，支持忽略大小写模糊查询 */
        fun definitionName(definitionName: String?): Builder {
            this.definitionName = definitionName
            return this
        }

        /** 流程定义版本 */
        fun definitionVersion(definitionVersion: Int?): Builder {
            this.definitionVersion = definitionVersion
            return this
        }

        /** 是否挂起 */
        fun isSuspend(isSuspend: Boolean?): Builder {
            this.suspend = isSuspend
            return this
        }

    }

}