package io.kuark.ability.workflow.task

/**
 * 流程任务查询项
 *
 * 当属性不为空时才会将该属性作为查询条件，各属性间是”与“的关系
 *
 * @author K
 * @since 1.0.0
 */
class FlowTaskQueryItems {

    /** 任务受理人id */
    var assignee: String? = null

    /** 业务主键，忽略大小写模糊匹配 */
    var bizKey: String? = null

    /** 任务定义key(bpmn文件userTask元素的id)，忽略大小写模糊匹配 */
    var taskDefinitionKey: String? = null

    /** 任务名称，忽略大小写模糊匹配 */
    var name: String? = null

    /** 流程定义key(bpmn文件中process元素的id)，忽略大小写模糊匹配 */
    var flowDefinitionKey: String? = null

    /** 流程版本 */
    var flowVersion: Int? = null

    /**
     * 空构造器，仅供框架反射使用
     */
    constructor()

    private constructor(builder: Builder) {
        assignee = builder.assignee
        bizKey = builder.bizKey
        taskDefinitionKey = builder.taskDefinitionKey
        name = builder.name
        flowDefinitionKey = builder.flowDefinitionKey
        flowVersion = builder.flowVersion
    }

    /**
     * 流程任务查询项构建者
     *
     * @author K
     * @since 1.0.0
     */
    class Builder {

        internal var assignee: String? = null
        var bizKey: String? = null
        var taskDefinitionKey: String? = null
        var name: String? = null
        var flowDefinitionKey: String? = null
        var flowVersion: Int? = null

        /**
         * 构建流程任务查询项对象
         */
        fun build(): FlowTaskQueryItems = FlowTaskQueryItems(this)

        /** 任务受理人id */
        fun assignee(assignee: String?): Builder {
            this.assignee = assignee
            return this
        }

        /** 业务主键 */
        fun bizKey(bizKey: String?): Builder {
            this.bizKey = bizKey
            return this
        }

        /** 任务定义key(bpmn文件userTask元素的id) */
        fun taskDefinitionKey(taskDefinitionKey: String?): Builder {
            this.taskDefinitionKey = taskDefinitionKey
            return this
        }

        /** 任务名称，忽略大小写模糊匹配 */
        fun name(name: String?): Builder {
            this.name = name
            return this
        }

        /** 流程定义key(bpmn文件中process元素的id) */
        fun flowDefinitionKey(flowDefinitionKey: String?): Builder {
            this.flowDefinitionKey = flowDefinitionKey
            return this
        }

        /** 流程版本 */
        fun flowVersion(flowVersion: Int?): Builder {
            this.flowVersion = flowVersion
            return this
        }

    }

}