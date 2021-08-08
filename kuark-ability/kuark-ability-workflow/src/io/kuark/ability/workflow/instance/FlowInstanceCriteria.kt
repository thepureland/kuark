package io.kuark.ability.workflow.instance

/**
 * 流程实例查询条件
 *
 * 当属性不为空时才会将该属性作为查询条件，各属性间是”与“的关系
 *
 * @author K
 * @since 1.0.0
 */
class FlowInstanceCriteria private constructor(builder: Builder) {

    /** 流程key(bpmn文件中process元素的id)，支持忽略大小写模糊查询 */
    var key: String? = null

    /** 业务主键，支持忽略大小写模糊查询 */
    var bizKey: String? = null

    /** 实例名称，支持忽略大小写模糊查询 */
    var instanceName: String? = null

    init {
        key = builder.key
        bizKey = builder.bizKey
        instanceName = builder.instanceName
    }


    /**
     * 流程实例查询条件构建者
     *
     * @author K
     * @since 1.0.0
     */
    class Builder {

        var key: String? = null
        var bizKey: String? = null
        var instanceName: String? = null

        /**
         * 构建流程实例查询条件对象
         */
        fun build(): FlowInstanceCriteria = FlowInstanceCriteria(this)

        /** 流程key(bpmn文件中process元素的id)，支持忽略大小写模糊查询 */
        fun key(key: String?): Builder {
            this.key = key
            return this
        }

        /** 业务主键，支持忽略大小写模糊查询 */
        fun bizKey(bizKey: String?): Builder {
            this.bizKey = bizKey
            return this
        }

        /** 实例名称，支持忽略大小写模糊查询 */
        fun instanceName(instanceName: String?): Builder {
            this.instanceName = instanceName
            return this
        }

    }

}