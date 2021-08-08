package io.kuark.ability.workflow.model

/**
 * 流程模型查询条件
 *
 * 当属性不为空时才会将该属性作为查询条件，各属性间是”与“的关系
 *
 * @author K
 * @since 1.0.0
 */
class FlowModelCriteria private constructor(builder: Builder)  {

    /** 流程key(bpmn文件中process元素的id)，支持忽略大小写模糊搜索 */
    var key: String? = null

    /** 流程名称，支持忽略大小写模糊搜索 */
    var name: String? = null

    /** 只查询最新版本的，默认为true */
    var latestOnly: Boolean = true

    init {
        this.key = builder.key
        this.name = builder.name
        this.latestOnly = builder.latestOnly
    }


    /**
     * 流程模型查询条件构建者
     *
     * @author K
     * @since 1.0.0
     */
    class Builder {

        var key: String? = null
        var name: String? = null
        var latestOnly: Boolean = true

        /**
         * 构建流程模型查询条件对象
         */
        fun build(): FlowModelCriteria = FlowModelCriteria(this)

        /** 流程key(bpmn文件中process元素的id)，支持忽略大小写模糊搜索 */
        fun key(key: String?): Builder {
            this.key = key
            return this
        }

        /** 流程名称，支持忽略大小写模糊搜索 */
        fun name(name: String?): Builder {
            this.name = name
            return this
        }

        /** 只查询最新版本的，默认为true */
        fun latestOnly(latestOnly: Boolean): Builder {
            this.latestOnly = latestOnly
            return this
        }

    }


}