package org.kuark.data.jdbc.support

import me.liuwj.ktorm.database.Database
import org.kuark.data.jdbc.datasource.MultipleDataSource
import org.kuark.data.jdbc.metadata.RdbType
import org.springframework.beans.factory.annotation.Autowired
import java.sql.Connection
import java.sql.DriverManager

object RdbKit {

    @Autowired
    private lateinit var dataSource: MultipleDataSource

    fun getDatabase(): Database = Database.connect(dataSource)

    fun newConnection(rdbType: RdbType, url: String, username: String, password: String?): Connection {
        Class.forName(rdbType.jdbcDriverName)
        return DriverManager.getConnection(url, username, password)
    }

    fun testConnection(rdbType: RdbType, connection: Connection): Boolean {
        val statement = connection.createStatement()
        return statement.execute(getTestStatement(rdbType))
    }

    fun getTestStatement(rdbType: RdbType): String =
        when (rdbType) {
            RdbType.H2, RdbType.POSTGRESQL -> "select 1"
            RdbType.MYSQL -> "" //TODO
            RdbType.ORACLE -> "" //TODO
            RdbType.SQLITE -> "" //TODO
            RdbType.DB2  -> "" //TODO
            RdbType.SQL_SERVER  -> "" //TODO
        }

}