package io.kuark.ability.distributed.tx.context

import com.alibaba.druid.sql.ast.SQLStatement
import io.seata.common.loader.LoadLevel
import io.seata.sqlparser.SQLRecognizer
import io.seata.sqlparser.druid.SQLOperateRecognizerHolder
import io.seata.sqlparser.util.JdbcConstants

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@LoadLevel(name = JdbcConstants.H2)
class H2OperateRecognizerHolder : SQLOperateRecognizerHolder {

    override fun getDeleteRecognizer(sql: String?, ast: SQLStatement?): SQLRecognizer {
        TODO("Not yet implemented")
    }

    override fun getInsertRecognizer(sql: String?, ast: SQLStatement?): SQLRecognizer {
        TODO("Not yet implemented")
    }

    override fun getUpdateRecognizer(sql: String?, ast: SQLStatement?): SQLRecognizer {
        TODO("Not yet implemented")
    }

    override fun getSelectForUpdateRecognizer(sql: String?, ast: SQLStatement?): SQLRecognizer {
        TODO("Not yet implemented")
    }

}