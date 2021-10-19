package io.kuark.distributed.tx.tx2

import io.kuark.base.log.LogFactory
import io.kuark.distributed.tx.table.TestTableDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * 模拟分支事务2
 *
 * @author K
 * @since 1.0.0
 */
@Service
open class BranchTx2 : IBranchTx2 {

    @Autowired
    private lateinit var testTableDao: TestTableDao

    private val log = LogFactory.getLog(this::class)

//    @Transactional
    override fun increase(id: Int, money: Double) {
        val entity = testTableDao.getById(id)
        log.info("用户【$id】当前余额【${testTableDao.getById(id).balance}】")
        log.info("为用户【$id】增加余额【$money】")
        entity.balance += money
        testTableDao.update(entity)
        log.info("用户【$id】当前余额【${testTableDao.getById(id).balance}】")
    }

    override fun increaseFail(id: Int, money: Double) {
        error("模拟加款时错误发生，事务回滚.")
    }

}