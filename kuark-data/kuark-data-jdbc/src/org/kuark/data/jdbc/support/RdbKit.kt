package org.kuark.data.jdbc.support

import me.liuwj.ktorm.database.Database
import org.kuark.base.lang.string.deleteWhitespace
import org.kuark.base.lang.string.substringBetween
import org.kuark.config.context.KuarkContext
import org.kuark.data.jdbc.datasource.currentDataSource
import org.kuark.data.jdbc.metadata.RdbType
import java.sql.Connection
import java.sql.DriverManager
import javax.sql.DataSource

object RdbKit {

    fun getDataSource(): DataSource = KuarkContext.currentDataSource()

    fun getDatabase(): Database = Database.connect(getDataSource())

    fun newConnection(url: String, username: String, password: String?): Connection {
        val rdbType = determinRdbTypeByUrl(url)
        Class.forName(rdbType.jdbcDriverName)
        return DriverManager.getConnection(url, username, password)
    }

    fun testConnection(conn: Connection? = null): Boolean {
        return if (conn != null) {
            _testConnection(conn)
        } else {
            getDataSource().connection.use {
                _testConnection(it)
            }
        }
    }

    private fun _testConnection(conn: Connection): Boolean {
        val dbMetaData = conn.metaData
        val rdbType = RdbType.productNameOf(dbMetaData.databaseProductName)
        val statement = conn.createStatement()
        return statement.execute(getTestStatement(rdbType))
    }

    fun determinRdbTypeByUrl(url: String): RdbType {
        val urlStr = url.deleteWhitespace().toLowerCase()
        if (urlStr.contains(":sqlserver:"))
            return RdbType.SQLSERVER
        val type = url.substringBetween("jdbc:", ":")
        return RdbType.valueOf(type.toUpperCase())
    }

    fun getTestStatement(rdbType: RdbType): String =
        when (rdbType) {
            RdbType.ORACLE -> "select 1 from dual"
            RdbType.DB2  -> "select 1 from sysibm.sysdummy1"
            else -> "select 1"
        }

}