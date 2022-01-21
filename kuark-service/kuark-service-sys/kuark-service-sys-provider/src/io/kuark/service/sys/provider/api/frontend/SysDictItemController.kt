package io.kuark.service.sys.provider.api.frontend

import io.kuark.ability.web.springmvc.BaseReadOnlyController
import io.kuark.ability.web.springmvc.FrontEndApi
import io.kuark.base.query.sort.Order
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.api.ISysDictApi
import io.kuark.service.sys.common.vo.dict.*
import io.kuark.service.sys.provider.biz.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.model.po.SysDictItem
import io.kuark.service.sys.provider.model.table.SysDictItems
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/sys/dictItem")
@FrontEndApi
@CrossOrigin
class SysDictItemController :
    BaseReadOnlyController<String, ISysDictItemBiz, SysDictSearchPayload, SysDictRecord, SysDictItemDetail, SysDictPayload>() {

    @Autowired
    private lateinit var sysDictApi: ISysDictApi

    @GetMapping("/loadDictItemCodes")
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    fun loadDictItemCodes(): List<String> {
        val modules = biz.allSearchProperty(SysDictItems.itemCode.name) as List<String>
        return modules.distinct()
    }

    @GetMapping("/updateActive")
    fun updateActive(id: String, active: Boolean): Boolean {
        val sysDictItem = SysDictItem {
            this.id = id
            this.active = active
        }
        return biz.update(sysDictItem)
    }

    @GetMapping("/getDictItemsByDictId")
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    fun getDictItemsByDictId(@RequestParam("dictId") dictId: String): List<SysDictItemDetail> {
        val payload = SysDictSearchPayload().apply {
            this.dictId = dictId
            pageNo = null // 不分页
            returnEntityClass = SysDictItemDetail::class
            orders = listOf(Order.asc(SysDictItemDetail::seqNo.name))
        }
        return biz.search(payload) as List<SysDictItemDetail>
    }

    @GetMapping("/getDictItems")
    fun getDictItems(
        @RequestParam("module") module: String,
        @RequestParam("dictType") dictType: String
    ): List<SysDictItemRecord> {
        return sysDictApi.getDictItems(DictModuleAndTypePayload(module, dictType))
    }

    @GetMapping("/getDictItemMap")
    fun getDictItemMap(
        @RequestParam("module") module: String,
        @RequestParam("dictType") dictType: String
    ): LinkedHashMap<String, String> {
        return sysDictApi.getDictItemMap(DictModuleAndTypePayload(module, dictType))
    }

    @PostMapping("/batchGetDictItems")
    fun batchGetDictItems(@RequestBody payloads: List<DictModuleAndTypePayload>): Map<Pair<String, String>, List<SysDictItemRecord>> {
        return sysDictApi.batchGetDictItems(payloads)
    }

    @PostMapping("/batchGetDictItemMap")
    fun batchGetDictItemMap(@RequestBody payloads: List<DictModuleAndTypePayload>): Map<Pair<String, String>, LinkedHashMap<String, String>> {
        return sysDictApi.batchGetDictItemMap(payloads)
    }

}