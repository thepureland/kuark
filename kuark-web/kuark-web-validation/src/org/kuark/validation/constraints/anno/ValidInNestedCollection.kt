package org.kuark.validation.constraints.anno

/**
 * 验证规则匹配（对嵌套集合这种的model的属性校验）
 * 用于remote异步校验
 */
@MustBeDocumented
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidInNestedCollection(

        /**
         * 验证类型 list, map
         */
        val type: String = "list",

        /**
         * 索引.
         */
        val key: String = "",

        /**
         * 集合外的字段
         */
        val exclude: Array<String> = [""]

) 