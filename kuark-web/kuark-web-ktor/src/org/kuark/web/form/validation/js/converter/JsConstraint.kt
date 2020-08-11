package org.kuark.web.form.validation.js.converter

/**
 * js约束
 *
 * @author K
 * @since 1.0.0
 */
data class JsConstraint(

    /** js校验规则 */
    val rule: String? = null,

    /** 校验不通过时的提示消息 */
    val msg: String? = null
)