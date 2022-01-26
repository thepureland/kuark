package io.kuark.service.sys.provider.api.frontend

import io.kuark.ability.web.springmvc.BaseReadOnlyController
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.vo.dict.*
import io.kuark.service.sys.provider.biz.ibiz.ISysDictBiz
import io.kuark.service.sys.provider.biz.ibiz.ISysDictItemBiz
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
    fun loadTreeNodes(@RequestBody searchPayload: SysDictSearchPayload): List<SysDictTreeNode> {
        val activeOnly = searchPayload.active ?: false
        return biz.loadDirectChildrenForTree(searchPayload.parentId, searchPayload.firstLevel ?: false, activeOnly)
    }

    @PostMapping("/searchByTree")
    fun searchByTree(@RequestBody searchPayload: SysDictSearchPayload): Pair<List<SysDictRecord>, Int> {
        return biz.loadDirectChildrenForList(searchPayload)
    }

    @GetMapping("/loadModules")
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    fun loadModules(): List<String> {
        val items = sysDictItemBiz.getItemsByModuleAndType("kuark:sys", "module")
        return items.map { it.itemCode }
    }

    @GetMapping("/loadDictTypes")
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    fun loadDictTypes(): List<String> {
        val modules = biz.allSearchProperty(SysDicts.dictType.name) as List<String>
        return modules.distinct()
    }

    @GetMapping("/getDict")
    fun get(id: String, isDict: Boolean?, fetchAllParentIds: Boolean = false): SysDictRecord {
        val dict = biz.get(id, isDict, fetchAllParentIds)
        return if (dict == null) {
            throw ObjectNotFoundException("找不到对应的字典/字典项！")
        } else {
            if (isDict == true) {
                dict.dictId = id
            }
            dict
        }
    }

    @PostMapping("/saveOrUpdate")
    fun saveOrUpdate(@RequestBody @Valid payload: SysDictPayload, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) error("数据校验失败！")
        return biz.saveOrUpdate(payload)
    }

    @DeleteMapping("/delete")
    fun delete(id: String, isDict: Boolean): Boolean {
        return biz.delete(id, isDict)
    }

    @PostMapping("/batchDelete")
    fun batchDelete(@RequestBody map: Map<String, Boolean>): Boolean {
        map.forEach { (id, isDict) ->
            val success = biz.delete(id, isDict)
            if (!success) {
                return false
            }
        }
        return true
    }

}