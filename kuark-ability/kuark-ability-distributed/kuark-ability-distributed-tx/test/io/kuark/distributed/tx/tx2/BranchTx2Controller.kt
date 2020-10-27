package io.kuark.distributed.tx.tx2

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@RestController
@RequestMapping("/tx2")
class BranchTx2Controller {

    @Autowired
    private lateinit var branchTx2: IBranchTx2

    /**
     * 扣减账户余额
     */
    @RequestMapping("/increase")
    fun increase(@RequestParam("userId") userId: Int, @RequestParam("money") money: Double) {
        branchTx2.increase(userId, money)
    }
}