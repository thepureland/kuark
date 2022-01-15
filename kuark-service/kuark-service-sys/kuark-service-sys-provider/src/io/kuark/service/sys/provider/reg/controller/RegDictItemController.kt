package io.kuark.service.sys.provider.reg.controller

import io.kuark.ability.web.common.WebResult
import io.kuark.base.bean.validation.kit.ValidationKit
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.vo.reg.dict.DictModuleAndTypePayload
import io.kuark.service.sys.common.vo.reg.dict.RegDictItemRecord
import io.kuark.service.sys.provider.reg.ibiz.IRegDictItemBiz
import io.kuark.service.sys.provider.reg.model.po.RegDictItem
import io.kuark.service.sys.provider.reg.model.table.RegDictItems
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/reg/dictItem")
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
    fun updateActive(id: String, active: Boolean): WebResult<Boolean> {
        val regDictItem = RegDictItem {
            this.id = id
            this.active = active
        }
        return WebResult(regDictItemBiz.update(regDictItem))
    }

    @GetMapping("/getDictItems")
    fun getDictItems(
        @RequestParam("module") module: String,
        @RequestParam("dictType") dictType: String
    ): WebResult<List<RegDictItemRecord>> {
        return WebResult(regDictItemBiz.getItemsByModuleAndType(module, dictType))
    }

    @GetMapping("/getDictItemMap")
    fun getDictItemMap(
        @RequestParam("module") module: String,
        @RequestParam("dictType") dictType: String
    ): WebResult<LinkedHashMap<String, String>> {
        val items = regDictItemBiz.getItemsByModuleAndType(module, dictType)
        val map = linkedMapOf<String, String>()
        items.forEach { map[it.itemCode] = it.itemName }
        return WebResult(map)
    }

    @PostMapping("/batchGetDictItems")
    fun batchGetDictItems(@RequestBody moduleAndTypePayloads: List<DictModuleAndTypePayload>): WebResult<Map<String, List<RegDictItemRecord>>> {
        val recordMap = mutableMapOf<String, List<RegDictItemRecord>>()
        moduleAndTypePayloads.forEach { payload ->
            val errors = ValidationKit.validateBean(payload)
            if (errors.isEmpty()) {
                val module = payload.module ?: ""
                val items = regDictItemBiz.getItemsByModuleAndType(module, payload.dictType!!)
                recordMap["${module}---${payload.dictType}"] = items
            } else {
                return WebResult(errors.first().message, 500)
            }
        }
        return WebResult(recordMap)
    }

    @PostMapping("/batchGetDictItemMap")
    fun batchGetDictItemMap(@RequestBody moduleAndTypePayloads: List<DictModuleAndTypePayload>): WebResult<Map<String, LinkedHashMap<String, String>>> {
        val recordMap = mutableMapOf<String, LinkedHashMap<String, String>>()
        moduleAndTypePayloads.forEach { payload ->
            val errors = ValidationKit.validateBean(payload)
            if (errors.isEmpty()) {
                val module = payload.module ?: ""
                val items = regDictItemBiz.getItemsByModuleAndType(module, payload.dictType!!)
                val map = linkedMapOf<String, String>()
                items.forEach { map[it.itemCode] = it.itemName }
                recordMap["${module}---${payload.dictType}"] = map
            } else {
                return WebResult(errors.first().message, 500)
            }
        }
        return WebResult(recordMap)
    }

}