package io.kuark.distributed.tx.tx

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(value = "branch-tx2")
interface IBranchTx2 {

    /**
     * 增加账户余额
     */
    @RequestMapping("/tx2/increase")
    fun increase(@RequestParam("id") id: Int, @RequestParam("money") money: Double)

    @RequestMapping("/tx2/increaseFail")
    fun increaseFail(@RequestParam("id") id: Int, @RequestParam("money") money: Double)

}