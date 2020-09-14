package io.kuark.context.annotation

/**
 * 配置项注解，用于从spring配置文件或nacos读取配置信息。
 *
 * 用法与Spring的@Value一致, 如: @ConfigValue(value = "\${nacos.test:123}", autoRefreshed = true)
 *
 * @author K
 * @since 1.0.0
 */
@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ConfigValue(

    /** 配置值，如："\${nacos.test:123}"，冒号后面的为缺省值 */
    val value: String,

    /** 是否自动刷新 */
    val autoRefreshed: Boolean = false
)