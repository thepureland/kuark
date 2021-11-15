package io.kuark.service.sys.provider.controller

import io.kuark.ability.web.common.WebResult
import io.kuark.ability.web.springmvc.BaseController
import io.kuark.base.lang.string.StringKit
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.model.SysMenuTreeNode
import io.kuark.service.sys.common.model.dict.SysDictRecord
import io.kuark.service.sys.common.model.dict.SysDictSearchPayload
import io.kuark.service.sys.common.model.resource.SysResourcePayload
import io.kuark.service.sys.common.model.resource.SysResourceRecord
import io.kuark.service.sys.common.model.resource.SysResourceSearchPayload
import io.kuark.service.sys.common.model.resource.SysResourceTreeNode
import io.kuark.service.sys.provider.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.ibiz.ISysResourceBiz
import io.kuark.service.sys.provider.model.po.SysResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import kotlin.reflect.KClass

@RestController
@RequestMapping("/sysResource")
@CrossOrigin
open class SysResourceController : BaseController() {

    @Autowired
    private lateinit var sysResourceBiz: ISysResourceBiz

    @Autowired
    private lateinit var sysDictItemBiz: ISysDictItemBiz

    @GetMapping("/getMenus")
    fun getMenus(): List<SysMenuTreeNode> {
        return sysResourceBiz.getMenus()
    }

    @PostMapping("/loadTreeNodes")
    fun laodTreeNodes(@RequestBody searchPayload: SysResourceSearchPayload): WebResult<List<SysResourceTreeNode>> {
        val results = sysResourceBiz.loadDirectChildrenForTree(searchPayload)
        return WebResult(results)
    }

    @PostMapping("/search")
    @Suppress(Consts.SUPPRESS_UNCHECKED_CAST)
    fun search(@RequestBody searchPayload: SysResourceSearchPayload): WebResult<Pair<List<SysResourceRecord>, Int>> {
        return WebResult(sysResourceBiz.pagingSearch(searchPayload) as Pair<List<SysResourceRecord>, Int>)
    }

    @PostMapping("/searchByTree")
    fun searchByTree(@RequestBody searchPayload: SysResourceSearchPayload): WebResult<Pair<List<SysResourceRecord>, Int>> {
        return WebResult(sysResourceBiz.loadDirectChildrenForList(searchPayload))
    }

    @GetMapping("/get")
    fun get(id: String, fetchAllParentIds: Boolean = false): WebResult<SysResourceRecord> {
        return WebResult(sysResourceBiz.get(id, SysResourceRecord::class, fetchAllParentIds))
    }

    @PostMapping("/saveOrUpdate")
    fun saveOrUpdate(@RequestBody @Valid payload: SysResourcePayload, bindingResult: BindingResult): WebResult<String> {
        if (bindingResult.hasErrors()) error("数据校验失败！")
        val id = if (StringKit.isBlank(payload.id)) {
            sysResourceBiz.insert(payload)
        } else {
            sysResourceBiz.update(payload)
            payload.id
        }
        return WebResult(id)
    }

    @GetMapping("/updateActive")
    fun updateActive(id: String, active: Boolean): WebResult<Boolean> {
        val sysResource = SysResource {
            this.id = id
            this.active = active
        }
        return WebResult(sysResourceBiz.update(sysResource))
    }

    @DeleteMapping("/delete")
    fun delete(id: String): WebResult<Boolean> {
        return WebResult(sysResourceBiz.cascadeDeleteChildren(id))
    }

    @PostMapping("/batchDelete")
    fun batchDelete(@RequestBody ids: List<String>): WebResult<Boolean> {
        return WebResult(sysResourceBiz.batchDelete(ids) == ids.size)
    }

    @GetMapping("/loadSubSyses")
    fun loadModules(): WebResult<Map<String, String>> {
        val items = sysDictItemBiz.getItemsByModuleAndType("kuark:sys", "sub_sys")
        val map = mutableMapOf<String, String>()
        items.forEach { map[it.itemCode] = it.itemName }
        return WebResult(map)
    }

    @GetMapping("/loadResourceTypes")
    fun loadResourceTypes(): WebResult<Map<String, String>> {
        val items = sysDictItemBiz.getItemsByModuleAndType("kuark:sys", "resource_type")
        val map = mutableMapOf<String, String>()
        items.forEach { map[it.itemCode] = it.itemName }
        return WebResult(map)
    }

    override fun getFormModelClass(): KClass<*> = SysResourcePayload::class

}