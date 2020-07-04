package org.kuark.data.jdbc.metadata

import org.kuark.data.jdbc.support.RdbKit
import java.sql.Connection
import java.sql.DatabaseMetaData

/**
 * 关系型数据库元数据工具类
 *
 * @author K
 * @since 1.0.0
 */
object RdbMetadataKit {

    /**
     * 根据表类型取得所有表信息
     *
     * @param tableTypes 表类型枚举的可变数组
     * @param conn 数据库连接。为null将用当前上下文数据源新建一个连接，在使用完关掉。不为null时由用户自行处理连接的关闭。
     * @return List<表对象信息>
     * @since 1.0.0
     */
    fun getTablesByType(vararg tableTypes: TableType?, conn: Connection? = null): List<Table> {
        return if (conn != null) {
            _getTablesByType(conn, *tableTypes)
        } else {
            RdbKit.getDataSource().connection.use {
                _getTablesByType(it, *tableTypes)
            }
        }
    }

    /**
     * 根据表名取得对应表信息
     *
     * @param tableName 表名
     * @param conn 数据库连接。为null将用当前上下文数据源新建一个连接，在使用完关掉。不为null时由用户自行处理连接的关闭。
     * @return 表对象信息，找不到是返回null
     * @since 1.0.0
     */
    fun getTableByName(tableName: String, conn: Connection? = null): Table? {
        return if (conn != null) {
            _getTableByName(conn, tableName)
        } else {
            RdbKit.getDataSource().connection.use {
                _getTableByName(it, tableName)
            }
        }
    }

    /**
     * 根据表名取得对应表的所有列信息
     *
     * @param tableName 表名
     * @param conn 数据库连接。为null将用当前上下文数据源新建一个连接，在使用完关掉。不为null时由用户自行处理连接的关闭。
     * @return Map<列名, 列对象信息>
     * @since 1.0.0
     */
    fun getColumnsByTableName(tableName: String, conn: Connection? = null): Map<String, Column> {
        return if (conn != null) {
            _getColumnsByTableName(conn, tableName)
        } else {
            RdbKit.getDataSource().connection.use {
                _getColumnsByTableName(it, tableName)
            }
        }
    }

    private fun _getTablesByType(conn: Connection, vararg tableTypes: TableType?): List<Table> {
        val dbMetaData = conn.metaData
        val types = tableTypes?.mapTo(mutableListOf()) { it -> it!!.name }.toTypedArray()
        val talbes = mutableListOf<Table>()
        val tableRs = dbMetaData.getTables(conn.catalog, conn.schema, "%", types)
        tableRs.use {
            while (tableRs.next()) {
                talbes.add(Table().apply {
                    name = tableRs.getString("TABLE_NAME")
                    cat = tableRs.getString("TABLE_CAT")
                    schema = tableRs.getString("TABLE_SCHEM")
                    comment = tableRs.getString("REMARKS")
                    type = TableType.valueOf(tableRs.getString("TABLE_TYPE"))
                })
            }
        }
        return talbes
    }

    private fun _getTableByName(conn: Connection, tableName: String): Table? {
        val dbMetaData = conn.metaData
        val rs = dbMetaData.getTables(conn.catalog, conn.schema, tableName, null)
        rs.use {
            if (rs.next()) {
                return Table().apply {
                    name = tableName
                    cat = rs.getString("TABLE_CAT")
                    schema = rs.getString("TABLE_SCHEM")
                    comment = rs.getString("REMARKS")
                    type = TableType.valueOf(rs.getString("TABLE_TYPE"))
                }
            }
            return null
        }
    }

    private fun _getColumnsByTableName(conn: Connection, tableName: String): Map<String, Column> {
        val dbMetaData = conn.metaData
        val rdbType = RdbType.productNameOf(dbMetaData.databaseProductName)
        val linkedMap = linkedMapOf<String, Column>()

        // 获取所有列
        val columnRs = dbMetaData.getColumns(conn.catalog, conn.schema, tableName, null)
        columnRs.use {
            while (columnRs.next()) {
                val column = Column().apply {
                    name = columnRs.getString("COLUMN_NAME")
                    comment = columnRs.getString("REMARKS")
                    jdbcType = columnRs.getInt("DATA_TYPE")
                    jdbcTypeName = columnRs.getString("TYPE_NAME")
                    kotlinType = JdbcTypeToKotlinType.getKotlinType(rdbType, this)
                    length = columnRs.getInt("COLUMN_SIZE")
                    decimalDigits = columnRs.getInt("DECIMAL_DIGITS")
                    defaultValue = columnRs.getString("COLUMN_DEF")
                    isNullable = columnRs.getInt("NULLABLE") == DatabaseMetaData.columnNullable
                    isDictCode = name.toUpperCase().endsWith("__CODE")
                    autoIncrement = columnRs.getString("IS_AUTOINCREMENT")
                }
                linkedMap[column.name] = column
            }
        }

        // 主键
        val primaryKeyRs = dbMetaData.getPrimaryKeys(conn.catalog, conn.schema, tableName)
        primaryKeyRs.use {
            while (primaryKeyRs.next()) {
                val columnName = primaryKeyRs.getString("COLUMN_NAME")
                linkedMap[columnName]!!.isPrimaryKey = true
            }
        }

        // 外键
        val foreignKeyRs = dbMetaData.getImportedKeys(conn.catalog, conn.schema, tableName)
        foreignKeyRs.use {
            while (foreignKeyRs.next()) {
                val columnName = foreignKeyRs.getString("FKCOLUMN_NAME")
                linkedMap[columnName]!!.isForeignKey = true
            }
        }

        // 索引
        val indexInfoRs = dbMetaData.getIndexInfo(conn.catalog, conn.schema, tableName, false, false)
        indexInfoRs.use {
            while (indexInfoRs.next()) {
                val columnName = indexInfoRs.getString("COLUMN_NAME")
                linkedMap[columnName]!!.isIndexed = true
            }
        }


        // 惟一约束
        val uniqueInfoRs = dbMetaData.getIndexInfo(conn.catalog, conn.schema, tableName, true, false)
        uniqueInfoRs.use {
            while (uniqueInfoRs.next()) {
                val columnName = uniqueInfoRs.getString("COLUMN_NAME")
                linkedMap[columnName]!!.isUnique = true
            }
        }

        return linkedMap
    }

}