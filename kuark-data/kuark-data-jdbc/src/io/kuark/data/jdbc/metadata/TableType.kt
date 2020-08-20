package io.kuark.data.jdbc.metadata

/**
 * 关系型数据库表类型枚举
 *
 * @author K
 * @since 1.0.0
 */
enum class TableType {

    TABLE,
    VIEW,
    SYSTEM_TABLE,
    GLOBAL_TEMPORARY,
    LOCAL_TEMPORARY,
    ALIAS,
    SYNONYM

}