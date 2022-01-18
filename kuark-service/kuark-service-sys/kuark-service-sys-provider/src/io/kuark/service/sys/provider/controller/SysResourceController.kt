package io.kuark.service.sys.provider.controller

import io.kuark.ability.web.common.WebResult
import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.service.sys.common.api.ISysDictApi
import io.kuark.service.sys.common.api.ISysResourceApi
import io.kuark.service.sys.common.vo.resource.*
import io.kuark.service.sys.provider.ibiz.ISysResourceBiz
import io.kuark.service.sys.provider.model.po.SysResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sys/resource")
@CrossOrigin
open class SysResourceController :
    BaseCrudController<String, ISysResourceBiz, SysResourceSearchPayload, SysResourceRecord, SysResourceDetail, SysResourcePayload>() {

    @Autowired
    private lateinit var dictApi: ISysDictApi

    @Autowired
    private lateinit var resourceApi: ISysResourceApi

    @GetMapping("/getMenus")
    fun getMenus(): List<MenuTreeNode> {
        return biz.getMenus()
    }

    @PostMapping("/loadTreeNodes")
    fun laodTreeNodes(@RequestBody searchPayload: SysResourceSearchPayload): WebResult<List<SysResourceTreeNode>> {
        val results = biz.loadDirectChildrenForTree(searchPayload)
        return WebResult(results)
    }

    @PostMapping("/searchByTree")
    fun searchByTree(@RequestBody searchPayload: SysResourceSearchPayload): WebResult<Pair<List<SysResourceRecord>, Int>> {
        return WebResult(biz.loadDirectChildrenForList(searchPayload))
    }

    @GetMapping("/getRes")
    fun get(id: String, fetchAllParentIds: Boolean = false): WebResult<SysResourceRecord> {
        return WebResult(biz.get(id, SysResourceRecord::class, fetchAllParentIds))
    }

    @GetMapping("/updateActive")
    fun updateActive(id: String, active: Boolean): WebResult<Boolean> {
        val sysResource = SysResource {
            this.id = id
            this.active = active
        }
        return WebResult(biz.update(sysResource))
    }

    @DeleteMapping("/delete")
    override fun delete(id: String): WebResult<Boolean> {
        return WebResult(biz.cascadeDeleteChildren(id))
    }

    @GetMapping("/loadSubSyses")
    fun loadModules(): WebResult<Map<String, String>> {
        return WebResult(dictApi.getDictItemMap("kuark:sys", "sub_sys"))
    }

    @GetMapping("/loadResourceTypes")
    fun loadResourceTypes(): WebResult<Map<String, String>> {
        return WebResult(dictApi.getDictItemMap("kuark:sys", "resource_type"))
    }

    @GetMapping("/getResources")
    fun getResources(subSysDictCode: String, resourceType: ResourceType): List<SysResourceRecord> {
        return resourceApi.getResources(subSysDictCode, resourceType)
    }

}