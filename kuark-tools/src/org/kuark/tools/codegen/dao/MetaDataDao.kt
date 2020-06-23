package org.kuark.tools.codegen.dao

import com.zaxxer.hikari.HikariDataSource
import org.kuark.base.lang.string.StringKit.isEmpty
import org.kuark.data.jdbc.datasource.DataSourceKit
import org.kuark.tools.codegen.vo.ColumnInfo
import java.sql.ResultSet

object MetaDataDao {

    fun getTables(schema: String?): MutableMap<String, String?> {
        val dataSource = DataSourceKit.getCurrentDataSource()
        val result = mutableMapOf<String, String?>()
        val conn = dataSource.connection
        conn.use {
            val metaData = it.metaData
            val schemaPattern =
                (if (isEmpty(schema)) (dataSource as HikariDataSource).username else schema)!!
            val rsTable = metaData.getTables(null, schemaPattern, null, null)
            while (rsTable.next()) {
                val tableType = rsTable.getString("TABLE_TYPE")
                if ("TABLE" == tableType || "VIEW" == tableType) {
                    val tableName = rsTable.getString("TABLE_NAME")
                    val remarks = rsTable.getString("REMARKS")
                    result[tableName] = remarks
                }
            }
        }

        return result
    }

//    private fun getConnection(config: Config): Connection {
//        return DbUtils.getConnection(config.getDbUrl(), config.getDbUser(), config.getDbPassword())
//    }

    fun getColumns(schema: String?, table: String?): List<ColumnInfo> {
        val dataSource = DataSourceKit.getCurrentDataSource()
        val columnInfos = mutableListOf<ColumnInfo>()
        val conn = dataSource.connection
        conn.use {
            val metaData = it.metaData
            val schemaPattern =
                (if (isEmpty(schema)) (dataSource as HikariDataSource).username else schema)!!
            val rs = metaData.getColumns(null, schemaPattern, table, null)
            while (rs.next()) {
                val column = createColumn(rs)
                columnInfos.add(column)
            }
        }

        return columnInfos
    }

    private fun createColumn(columns: ResultSet): ColumnInfo {
        val column = ColumnInfo()
        //        column.setType(columns.getString("TYPE_NAME"));
        column.setName(columns.getString("COLUMN_NAME"))
        val columnDef = columns.getString("COLUMN_DEF")
        //        column.setDefaultValue(columnDef);
        column.setOrigComment(columns.getString("REMARKS"))
        //        int nullable = columns.getInt("NULLABLE");
//        switch (nullable) {
//            case DatabaseMetaData.columnNoNulls:
//                column.setNullable(false);
//                break;
//            case DatabaseMetaData.columnNullable:
//                column.setNullable(true);
//                break;
//            case DatabaseMetaData.columnNullableUnknown:
//                column.setNullable(null);
//                break;
//        }
//        column.setLength(columns.getInt("COLUMN_SIZE"));
//        column.setPrecision(new BigDecimal(columns.getInt("DECIMAL_DIGITS")));
        return column
    }
}