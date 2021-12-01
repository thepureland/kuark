package io.kuark.service.sys.provider.reg.controller

import io.kuark.ability.web.common.WebResult
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.vo.reg.dict.RegDictItemRecord
import io.kuark.service.sys.provider.reg.ibiz.IRegDictItemBiz
import io.kuark.service.sys.provider.reg.model.po.RegDictItem
import io.kuark.service.sys.provider.reg.model.table.RegDictItems
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/regDictItem")
@CrossOrigin
class RegDictItemController {

    @Autowired
    private lateinit var regDictItemBiz: IRegDictItemBiz

    @GetMapping("/loadDictItemCodes")
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    fun loadDictItemCodes(): WebResult<List<String>> {
        val modules = regDictItemBiz.allSearchProperty(RegDictItems.itemCode.name) as List<String>
        return WebResult(modules.distinct())
    }

    @GetMapping("/updateActive")
    fun updateActive(id:String, active: Boolean): WebResult<Boolean> {
        val regDictItem = RegDictItem {
            this.id = id
            this.active = active
        }
        return WebResult(regDictItemBiz.update(regDictItem))
    }

    @GetMapping("/getDictItems")
    fun getDictItems(module: String, type: String): List<RegDictItemRecord> {
        return regDictItemBiz.getItemsByModuleAndType(module, type)
    }

    @GetMapping("/getDictItemMap")
    fun getDictItemMap(module: String, type: String): LinkedHashMap<String, String> {
        val items = regDictItemBiz.getItemsByModuleAndType(module, type)
        val map = linkedMapOf<String, String>()
        items.forEach { map[it.itemCode] = it.itemName }
        return map
    }

}