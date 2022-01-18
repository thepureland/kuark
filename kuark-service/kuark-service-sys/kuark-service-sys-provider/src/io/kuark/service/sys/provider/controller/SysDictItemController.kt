package io.kuark.service.sys.provider.controller

import io.kuark.ability.web.common.WebResult
import io.kuark.ability.web.springmvc.BaseReadOnlyController
import io.kuark.base.bean.validation.kit.ValidationKit
import io.kuark.base.query.sort.Order
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.vo.dict.*
import io.kuark.service.sys.provider.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.model.po.SysDictItem
import io.kuark.service.sys.provider.model.table.SysDictItems
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/sys/dictItem")
@CrossOrigin
class SysDictItemController :
    BaseReadOnlyController<String, ISysDictItemBiz, SysDictSearchPayload, SysDictRecord, SysDictItemDetail, SysDictPayload>() {

    @GetMapping("/loadDictItemCodes")
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    fun loadDictItemCodes(): WebResult<List<String>> {
        val modules = biz.allSearchProperty(SysDictItems.itemCode.name) as List<String>
        return WebResult(modules.distinct())
    }

    @GetMapping("/updateActive")
    fun updateActive(id: String, active: Boolean): WebResult<Boolean> {
        val sysDictItem = SysDictItem {
            this.id = id
            this.active = active
        }
        return WebResult(biz.update(sysDictItem))
    }

    @GetMapping("/getDictItems")
    fun getDictItems(
        @RequestParam("module") module: String,
        @RequestParam("dictType") dictType: String
    ): WebResult<List<SysDictItemRecord>> {
        return WebResult(biz.getItemsByModuleAndType(module, dictType))
    }

    @GetMapping("/getDictItemsByDictId")
    fun getDictItemsByDictId(@RequestParam("dictId") dictId: String): WebResult<List<SysDictItemDetail>> {
        val payload = SysDictSearchPayload().apply {
            this.dictId = dictId
            pageNo = null // 不分页
            returnEntityClass = SysDictItemDetail::class
            orders = listOf(Order.asc(SysDictItemDetail::seqNo.name))
        }
        return WebResult(biz.search(payload) as List<SysDictItemDetail>)
    }

    @GetMapping("/getDictItemMap")
    fun getDictItemMap(
        @RequestParam("module") module: String,
        @RequestParam("dictType") dictType: String
    ): WebResult<LinkedHashMap<String, String>> {
        val items = biz.getItemsByModuleAndType(module, dictType)
        val map = linkedMapOf<String, String>()
        items.forEach { map[it.itemCode] = it.itemName }
        return WebResult(map)
    }

    @PostMapping("/batchGetDictItems")
    fun batchGetDictItems(@RequestBody moduleAndTypePayloads: List<DictModuleAndTypePayload>): WebResult<Map<String, List<SysDictItemRecord>>> {
        val recordMap = mutableMapOf<String, List<SysDictItemRecord>>()
        moduleAndTypePayloads.forEach { payload ->
            val errors = ValidationKit.validateBean(payload)
            if (errors.isEmpty()) {
                val module = payload.module ?: ""
                val items = biz.getItemsByModuleAndType(module, payload.dictType!!)
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
                val items = biz.getItemsByModuleAndType(module, payload.dictType!!)
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