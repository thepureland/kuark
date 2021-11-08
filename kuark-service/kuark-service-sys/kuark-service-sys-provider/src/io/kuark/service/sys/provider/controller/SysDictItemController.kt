package io.kuark.service.sys.provider.controller

import io.kuark.ability.web.common.WebResult
import io.kuark.base.support.Consts
import io.kuark.service.sys.provider.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.model.po.SysDictItem
import io.kuark.service.sys.provider.model.table.SysDictItems
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/sysDictItem")
@CrossOrigin
class SysDictItemController {

    @Autowired
    private lateinit var sysDictItemBiz: ISysDictItemBiz

    @GetMapping("/loadDictItemCodes")
    @Suppress(Consts.SUPPRESS_UNCHECKED_CAST)
    fun loadDictItemCodes(): WebResult<List<String>> {
        val modules = sysDictItemBiz.allSearchProperty(SysDictItems.itemCode.name) as List<String>
        return WebResult(modules.distinct())
    }

    @GetMapping("/updateActive")
    fun updateActive(id:String, active: Boolean): WebResult<Boolean> {
        val sysDictItem = SysDictItem {
            this.id = id
            this.active = active
        }
        return WebResult(sysDictItemBiz.update(sysDictItem))
    }

}