package io.kuark.distributed.tx.tx1

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * 分支事务1的Controller
 *
 * @author K
 * @since 1.0.0
 */
@RestController
@RequestMapping("/tx1")
class BranchTx1Controller {

    @Autowired
    private lateinit var branchTx1: IBranchTx1

    /**
     * 扣减账户余额
     */
    @RequestMapping("/decrease")
    fun decrease(@RequestParam("id") id: Int, @RequestParam("money") money: Double) {
        branchTx1.decrease(id, money)
    }

}