package io.kuark.distributed.tx.tx

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.math.BigDecimal

@FeignClient(value = "branch-tx2")
interface IBranchtx2 {
    /**
     * 增加账户余额
     */
    @RequestMapping("/tx2/increase")
    fun increase(@RequestParam("userId") userId: Int, @RequestParam("money") money: Double)
}