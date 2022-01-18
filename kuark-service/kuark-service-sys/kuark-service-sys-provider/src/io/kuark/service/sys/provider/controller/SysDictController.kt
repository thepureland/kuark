package io.kuark.service.sys.provider.controller

import io.kuark.ability.web.common.WebResult
import io.kuark.ability.web.springmvc.BaseReadOnlyController
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.vo.dict.*
import io.kuark.service.sys.provider.ibiz.ISysDictBiz
import io.kuark.service.sys.provider.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.model.table.SysDicts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/sys/dict")
@CrossOrigin
open class SysDictController :
    BaseReadOnlyController<String, ISysDictBiz, SysDictSearchPayload, SysDictRecord, SysDictDetail, SysDictPayload>() {

    @Autowired
    private lateinit var sysDictItemBiz: ISysDictItemBiz

    @PostMapping("/loadTreeNodes")
    fun loadTreeNodes(@RequestBody searchPayload: SysDictSearchPayload): WebResult<List<SysDictTreeNode>> {
        val activeOnly = searchPayload.active ?: false
        return WebResult(
            biz.loadDirectChildrenForTree(
                searchPayload.parentId, searchPayload.firstLevel ?: false, activeOnly
            )
        )
    }

    @PostMapping("/searchByTree")
    fun searchByTree(@RequestBody searchPayload: SysDictSearchPayload): WebResult<Pair<List<SysDictRecord>, Int>> {
        return WebResult(biz.loadDirectChildrenForList(searchPayload))
    }

    @GetMapping("/loadModules")
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    fun loadModules(): WebResult<List<String>> {
        val items = sysDictItemBiz.getItemsByModuleAndType("kuark:sys", "module")
        return WebResult(items.map { it.itemCode })
    }

    @GetMapping("/loadDictTypes")
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    fun loadDictTypes(): WebResult<List<String>> {
        val modules = biz.allSearchProperty(SysDicts.dictType.name) as List<String>
        return WebResult(modules.distinct())
    }

    @GetMapping("/getDict")
    fun get(id: String, isDict: Boolean?, fetchAllParentIds: Boolean = false): WebResult<SysDictRecord> {
        val dict = biz.get(id, isDict, fetchAllParentIds)
        return if (dict == null) {
            WebResult(null, "找不到对应的字典/字典项！")
        } else {
            if (isDict == true) {
                dict.dictId = id
            }
            WebResult(dict)
        }
    }

    @PostMapping("/saveOrUpdate")
    fun saveOrUpdate(@RequestBody @Valid payload: SysDictPayload, bindingResult: BindingResult): WebResult<String> {
        if (bindingResult.hasErrors()) error("数据校验失败！")
        return WebResult(biz.saveOrUpdate(payload))
    }

    @DeleteMapping("/delete")
    fun delete(id: String, isDict: Boolean): WebResult<Boolean> {
        return WebResult(biz.delete(id, isDict))
    }

    @PostMapping("/batchDelete")
    fun batchDelete(@RequestBody map: Map<String, Boolean>): WebResult<Boolean> {
        map.forEach { (id, isDict) ->
            val success = biz.delete(id, isDict)
            if (!success) {
                return WebResult(false)
            }
        }
        return WebResult(true)
    }

}