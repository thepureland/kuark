package io.kuark.base.bean.validation.teminal

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * 终端约束数据类
 *
 * @author K
 * @since 1.0.0
 */
data class TeminalConstraint(
    /** bean属性名 */
    @JsonIgnore
    val prop: String,
    /** 约束名 */
    val constraint: String,
    /** 约束规则，Array(Map(约束注解的属性名，约束注解的属性值)) */
    val rule: Array<Map<String, Any>>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TeminalConstraint

        if (prop != other.prop) return false
        if (constraint != other.constraint) return false

        return true
    }

    override fun hashCode(): Int {
        var result = prop.hashCode()
        result = 31 * result + constraint.hashCode()
        return result
    }
}