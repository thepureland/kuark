package io.kuark.distributed.tx.tx

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.math.BigDecimal

@FeignClient(value = "branch-tx1")
interface IBranchTx1 {

    /**
     * 扣减账户余额
     */
    @RequestMapping("/tx1/decrease")
    fun decrease(@RequestParam("id") id: Int, @RequestParam("money") money: Double)

}