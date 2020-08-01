package org.kuark.config.annotation

/**
 * 配置项注解，用于从spring配置文件或nacos读取配置信息
 *
 * @author K
 * @since 1.0.0
 */
@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ConfigValue(
    val value: String,
    val autoRefreshed: Boolean = false
)