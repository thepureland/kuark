package io.kuark.data.jdbc.metadata

/**
 * 关系型数据库表类型枚举
 *
 * @author K
 * @since 1.0.0
 */
enum class TableType {

    /** 普通表 */
    TABLE,

    /** 视图 */
    VIEW,

    /** 系统表 */
    SYSTEM_TABLE,

    /** 全局临时表 */
    GLOBAL_TEMPORARY,

    /** 局部临时表 */
    LOCAL_TEMPORARY,

    /** 别名 */
    ALIAS,

    /** 同义词 */
    SYNONYM

}