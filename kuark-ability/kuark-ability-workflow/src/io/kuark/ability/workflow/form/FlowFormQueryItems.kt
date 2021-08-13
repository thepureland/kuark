package io.kuark.ability.workflow.form


/**
 * 流程表单查询项
 *
 * @author K
 * @since 1.0.0
 */
class FlowFormQueryItems {

    /** 表单key，支持忽略大小定的模糊查询*/
    var key: String? = null

    /** 版本 */
    var version: Int? = null

    /** 分类字典代码 */
    var categoryDictCode: String? = null

    /** 名称，支持忽略大小定的模糊查询*/
    var name: String? = null

    /** 只查询最新版本的，默认为true */
    var latestOnly: Boolean = true

    /**
     * 空构造器，仅供框架反射使用
     */
    constructor()

    private constructor(builder: Builder) {
        key = builder.key
        version = builder.version
        categoryDictCode = builder.categoryDictCode
        name = builder.name
        latestOnly = builder.latestOnly
    }


    /**
     * 流程表单查询项构建者
     *
     * @author K
     * @since 1.0.0
     */
    class Builder {

        internal var key: String? = null
        internal var version: Int? = null
        internal var categoryDictCode: String? = null
        internal var name: String? = null
        internal var latestOnly: Boolean = true

        /**
         * 构建流程表单查询项对象
         */
        fun build(): FlowFormQueryItems = FlowFormQueryItems(this)

        /** 表单key，支持忽略大小定的模糊查询*/
        fun key(key: String?): Builder {
            this.key = key
            return this
        }

        /** 版本 */
        fun version(version: Int?): Builder {
            this.version = version
            return this
        }

        /** 分类字典代码 */
        fun categoryDictCode(categoryDictCode: String?): Builder {
            this.categoryDictCode = categoryDictCode
            return this
        }

        /** 名称，支持忽略大小定的模糊查询*/
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