package io.kuark.service.sys.provider.controller

import io.kuark.ability.web.common.WebResult
import io.kuark.ability.web.springmvc.BaseController
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.model.dict.SysDictPayload
import io.kuark.service.sys.common.model.dict.SysDictRecord
import io.kuark.service.sys.common.model.dict.SysDictSearchPayload
import io.kuark.service.sys.common.model.dict.SysDictTreeNode
import io.kuark.service.sys.provider.ibiz.ISysDictBiz
import io.kuark.service.sys.provider.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.model.table.SysDicts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import kotlin.reflect.KClass

@RestController
@RequestMapping("/sysDict")
@CrossOrigin
open class SysDictController : BaseController() {

    @Autowired
    private lateinit var sysDictBiz: ISysDictBiz

    @Autowired
    private lateinit var sysDictItemBiz: ISysDictItemBiz

    @PostMapping("/loadTreeNodes")
    fun laodTreeNodes(@RequestBody searchPayload: SysDictSearchPayload): WebResult<List<SysDictTreeNode>> {
        val activeOnly = searchPayload.active ?: false
        return WebResult(
            sysDictBiz.loadDirectChildrenForTree(
                searchPayload.parentId, searchPayload.firstLevel ?: false, activeOnly
            )
        )
    }

    @PostMapping("/searchByTree")
    fun searchByTree(@RequestBody searchPayload: SysDictSearchPayload): WebResult<Pair<List<SysDictRecord>, Int>> {
        return WebResult(sysDictBiz.loadDirectChildrenForList(searchPayload))
    }

    @PostMapping("/search")
    fun search(@RequestBody searchPayload: SysDictSearchPayload): WebResult<Pair<List<SysDictRecord>, Int>> {
        return WebResult(sysDictBiz.pagingSearch(searchPayload))
    }

    @GetMapping("/loadModules")
    @Suppress(Consts.SUPPRESS_UNCHECKED_CAST)
    fun loadModules(): WebResult<List<String>> {
        val items = sysDictItemBiz.getItemsByModuleAndType("kuark:sys", "module")
        return WebResult(items.map { it.itemCode })
    }

    @GetMapping("/loadDictTypes")
    @Suppress(Consts.SUPPRESS_UNCHECKED_CAST)
    fun loadDictTypes(): WebResult<List<String>> {
        val modules = sysDictBiz.allSearchProperty(SysDicts.dictType.name) as List<String>
        return WebResult(modules.distinct())
    }

    @GetMapping("/get")
    fun get(id: String, isDict: Boolean?, fetchAllParentIds: Boolean = false): WebResult<SysDictRecord> {
        val dict = sysDictBiz.get(id, isDict, fetchAllParentIds)
        return if (dict == null) {
            WebResult(null, "找不到对应的字典/字典项！")
        } else {
            WebResult(dict)
        }
    }

    @PostMapping("/saveOrUpdate")
    fun saveOrUpdate(@RequestBody @Valid payload: SysDictPayload, bindingResult: BindingResult): WebResult<String> {
        if (bindingResult.hasErrors()) error("数据校验失败！")
        return WebResult(sysDictBiz.saveOrUpdate(payload))
    }

    @DeleteMapping("/delete")
    fun delete(id: String, isDict: Boolean): WebResult<Boolean> {
        return WebResult(sysDictBiz.delete(id, isDict))
    }

    @PostMapping("/batchDelete")
    fun batchDelete(@RequestBody map: Map<String, Boolean>): WebResult<Boolean> {
        map.forEach { (id, isDict) ->
            val success = sysDictBiz.delete(id, isDict)
            if (!success) {
                return WebResult(false)
            }
        }
        return WebResult(true)
    }

    override fun getFormModelClass(): KClass<SysDictPayload> = SysDictPayload::class

}