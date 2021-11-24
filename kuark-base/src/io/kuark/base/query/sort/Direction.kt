package io.kuark.base.query.sort

import com.fasterxml.jackson.annotation.JsonCreator
import java.util.*

/**
 * 排序方向
 *
 * @author K
 * @since 1.0.0
 */
enum class Direction {

    ASC,
    DESC;

    companion object {
        @JsonCreator
        fun fromString(value: String): Direction {
            return try {
                valueOf(value.uppercase(Locale.US))
            } catch (e: Exception) {
                val msg = "非法排序值${value}！取值必须为 'desc' 或 'asc' (大小写不敏感)。"
                error(msg)
            }
        }
    }

}