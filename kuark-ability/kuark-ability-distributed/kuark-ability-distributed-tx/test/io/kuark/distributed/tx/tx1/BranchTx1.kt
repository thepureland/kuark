package io.kuark.distributed.tx.tx1

import io.kuark.base.log.LogFactory
import io.kuark.distributed.tx.table.TestTableDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * 模拟分支事务1
 *
 * @author K
 * @since 1.0.0
 */
@Service
open class BranchTx1 : IBranchTx1 {

    @Autowired
    private lateinit var testTableDao: TestTableDao

    private val log = LogFactory.getLog(this::class)

//    @Transactional
    override fun decrease(id: Int, money: Double) {
        val entity = testTableDao.get(id)
        log.info("用户【$id】当前余额【${testTableDao.get(id).balance}】")
        log.info("为用户【$id】扣减余额【$money】")
        entity.balance -= money
        testTableDao.update(entity)
        log.info("用户【$id】当前余额【${testTableDao.get(id).balance}】")
    }

}