package org.kuark.config.annotation

@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ConfigValue(
    val value: String,
    val autoRefreshed: Boolean = false
)