package org.kuark.validation.constraints.anno

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * 验证规则匹配（对嵌套集合这种的model的属性校验）
 * 用于remote异步校验
 *
 *
 * Created by admin 2019/1/18.
 */
@Documented
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
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