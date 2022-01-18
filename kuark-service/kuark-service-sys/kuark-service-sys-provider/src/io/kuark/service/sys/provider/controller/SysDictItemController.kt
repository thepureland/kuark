package io.kuark.service.sys.provider.controller

import io.kuark.ability.web.common.WebResult
import io.kuark.ability.web.springmvc.BaseReadOnlyController
import io.kuark.base.bean.validation.kit.ValidationKit
import io.kuark.base.query.sort.Order
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.vo.dict.SysDictSearchPayload
import io.kuark.service.sys.provider.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.model.po.SysDictItem
import io.kuark.service.sys.provider.model.table.SysDictItems
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/sys/dictItem")
@CrossOrigin
class SysDictItemController :
    BaseReadOnlyController<String, ISysDictItemBiz, io.kuark.service.sys.common.vo.dict.SysDictSearchPayload, io.kuark.service.sys.common.vo.dict.SysDictRecord, io.kuark.service.sys.common.vo.dict.SysDictItemDetail, io.kuark.service.sys.common.vo.dict.SysDictPayload>() {

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
    ): WebResult<List<io.kuark.service.sys.common.vo.dict.SysDictItemRecord>> {
        return WebResult(biz.getItemsByModuleAndType(module, dictType))
    }

    @GetMapping("/getDictItemsByDictId")
    fun getDictItemsByDictId(@RequestParam("dictId") dictId: String): WebResult<List<io.kuark.service.sys.common.vo.dict.SysDictItemDetail>> {
        val payload = SysDictSearchPayload().apply {
            this.dictId = dictId
            pageNo = null // 不分页
            returnEntityClass = io.kuark.service.sys.common.vo.dict.SysDictItemDetail::class
            orders = listOf(Order.asc(io.kuark.service.sys.common.vo.dict.SysDictItemDetail::seqNo.name))
        }
        return WebResult(biz.search(payload) as List<io.kuark.service.sys.common.vo.dict.SysDictItemDetail>)
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
    fun batchGetDictItems(@RequestBody moduleAndTypePayloads: List<io.kuark.service.sys.common.vo.dict.DictModuleAndTypePayload>): WebResult<Map<String, List<io.kuark.service.sys.common.vo.dict.SysDictItemRecord>>> {
        val recordMap = mutableMapOf<String, List<io.kuark.service.sys.common.vo.dict.SysDictItemRecord>>()
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
    fun batchGetDictItemMap(@RequestBody moduleAndTypePayloads: List<io.kuark.service.sys.common.vo.dict.DictModuleAndTypePayload>): WebResult<Map<String, LinkedHashMap<String, String>>> {
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