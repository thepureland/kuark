package io.kuark.ability.workflow.definition

/**
 * 流程定义查询项构建者
 *
 * 当属性不为空时才会将该属性作为查询条件，各属性间是”与“的关系
 *
 * @author K
 * @since 1.0.0
 */
class FlowDefinitionQueryItems {

    /** 流程定义key(bpmn文件中process元素的id)，支持忽略大小写模糊搜索 */
    var key: String? = null

    /** 流程名称，支持忽略大小写模糊搜索 */
    var name: String? = null

    /** 分类 */
    var category: String? = null

    /** 租户(所属系统)id */
    var tenantId: String? = null

    /** 只查询最新版本的，默认为true */
    var latestOnly: Boolean = true

    /** 是否已部署，为null时查询所有 */
    var isDeployed: Boolean? = null

    /**
     * 空构造器，仅供框架反射使用
     */
    constructor()

    private constructor(builder: Builder) {
        key = builder.key
        name = builder.name
        category = builder.category
        tenantId = builder.tenantId
        latestOnly = builder.latestOnly
        isDeployed = builder.isDeployed
    }


    /**
     * 流程定义查询项构建者
     *
     * @author K
     * @since 1.0.0
     */
    class Builder {

        var key: String? = null
        var name: String? = null
        var category: String? = null
        var tenantId: String? = null
        var latestOnly: Boolean = true
        var isDeployed: Boolean? = null

        /**
         * 构建流程定义查询项对象
         */
        fun build(): FlowDefinitionQueryItems = FlowDefinitionQueryItems(this)

        /** 流程定义key(bpmn文件中process元素的id)，支持忽略大小写模糊搜索 */
        fun key(key: String?): Builder {
            this.key = key
            return this
        }

        /** 流程名称，支持忽略大小写模糊搜索 */
        fun name(name: String?): Builder {
            this.name = name
            return this
        }

        /** 分类 */
        fun category(category: String?): Builder {
            this.category = category
            return this
        }

        /** 租户(所属系统)id */
        fun tenantId(tenantId: String?): Builder {
            this.tenantId = tenantId
            return this
        }

        /** 只查询最新版本的，默认为true */
        fun latestOnly(latestOnly: Boolean): Builder {
            this.latestOnly = latestOnly
            return this
        }

        /** 是否已部署，为null时查询所有 */
        fun isDeployed(isDeployed: Boolean): Builder {
            this.isDeployed = isDeployed
            return this
        }

    }

}