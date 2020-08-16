package org.kuark.base.bean.validation.teminal

import com.fasterxml.jackson.annotation.JsonIgnore

data class TeminalConstraint(
    @JsonIgnore
    val prop: String,
    val constraint: String,
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