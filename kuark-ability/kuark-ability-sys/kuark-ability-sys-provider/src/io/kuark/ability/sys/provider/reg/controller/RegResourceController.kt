package io.kuark.ability.sys.provider.reg.controller

import io.kuark.ability.sys.common.reg.resource.*
import io.kuark.ability.web.common.WebResult
import io.kuark.ability.web.springmvc.BaseController
import io.kuark.base.lang.string.StringKit
import io.kuark.base.support.Consts
import io.kuark.ability.sys.provider.reg.ibiz.IRegDictItemBiz
import io.kuark.ability.sys.provider.reg.ibiz.IRegResourceBiz
import io.kuark.ability.sys.provider.reg.model.po.RegResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import kotlin.reflect.KClass

@RestController
@RequestMapping("/regResource")
@CrossOrigin
open class RegResourceController : BaseController() {

    @Autowired
    private lateinit var regResourceBiz: IRegResourceBiz

    @Autowired
    private lateinit var regDictItemBiz: IRegDictItemBiz

    @GetMapping("/getMenus")
    fun getMenus(): List<MenuTreeNode> {
        return regResourceBiz.getMenus()
    }

    @PostMapping("/loadTreeNodes")
    fun laodTreeNodes(@RequestBody searchPayload: RegResourceSearchPayload): WebResult<List<RegResourceTreeNode>> {
        val results = regResourceBiz.loadDirectChildrenForTree(searchPayload)
        return WebResult(results)
    }

    @PostMapping("/search")
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    fun search(@RequestBody searchPayload: RegResourceSearchPayload): WebResult<Pair<List<RegResourceRecord>, Int>> {
        return WebResult(regResourceBiz.pagingSearch(searchPayload) as Pair<List<RegResourceRecord>, Int>)
    }

    @PostMapping("/searchByTree")
    fun searchByTree(@RequestBody searchPayload: RegResourceSearchPayload): WebResult<Pair<List<RegResourceRecord>, Int>> {
        return WebResult(regResourceBiz.loadDirectChildrenForList(searchPayload))
    }

    @GetMapping("/get")
    fun get(id: String, fetchAllParentIds: Boolean = false): WebResult<RegResourceRecord> {
        return WebResult(regResourceBiz.get(id, RegResourceRecord::class, fetchAllParentIds))
    }

    @PostMapping("/saveOrUpdate")
    fun saveOrUpdate(@RequestBody @Valid payload: RegResourcePayload, bindingResult: BindingResult): WebResult<String> {
        if (bindingResult.hasErrors()) error("数据校验失败！")
        val id = if (StringKit.isBlank(payload.id)) {
            regResourceBiz.insert(payload)
        } else {
            regResourceBiz.update(payload)
            payload.id
        }
        return WebResult(id)
    }

    @GetMapping("/updateActive")
    fun updateActive(id: String, active: Boolean): WebResult<Boolean> {
        val regResource = RegResource {
            this.id = id
            this.active = active
        }
        return WebResult(regResourceBiz.update(regResource))
    }

    @DeleteMapping("/delete")
    fun delete(id: String): WebResult<Boolean> {
        return WebResult(regResourceBiz.cascadeDeleteChildren(id))
    }

    @PostMapping("/batchDelete")
    fun batchDelete(@RequestBody ids: List<String>): WebResult<Boolean> {
        return WebResult(regResourceBiz.batchDelete(ids) == ids.size)
    }

    @GetMapping("/loadSubSyses")
    fun loadModules(): WebResult<Map<String, String>> {
        val items = regDictItemBiz.getItemsByModuleAndType("kuark:sys", "sub_sys")
        val map = mutableMapOf<String, String>()
        items.forEach { map[it.itemCode] = it.itemName }
        return WebResult(map)
    }

    @GetMapping("/loadResourceTypes")
    fun loadResourceTypes(): WebResult<Map<String, String>> {
        val items = regDictItemBiz.getItemsByModuleAndType("kuark:sys", "resource_type")
        val map = mutableMapOf<String, String>()
        items.forEach { map[it.itemCode] = it.itemName }
        return WebResult(map)
    }

    override fun getFormModelClass(): KClass<*> = RegResourcePayload::class

}