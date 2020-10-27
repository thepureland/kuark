package io.kuark.distributed.tx.tx2

import io.kuark.distributed.tx.table.TestTableDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@Service
open class BranchTx2 : IBranchTx2 {

    @Autowired
    private lateinit var testTableDao: TestTableDao

    @Transactional
    override fun increase(id: Int, money: Double) {
        val entity = testTableDao.getById(id)
        entity.balance += money
        testTableDao.update(entity)
    }

    override fun increaseFail(id: Int, money: Double) {
        error("模拟加款时错误发生，事务回滚.")
    }

}