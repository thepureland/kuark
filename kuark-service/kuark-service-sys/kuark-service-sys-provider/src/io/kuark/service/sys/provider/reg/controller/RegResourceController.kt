package io.kuark.service.sys.provider.reg.controller

import io.kuark.ability.web.common.WebResult
import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.service.sys.common.api.reg.IDictApi
import io.kuark.service.sys.common.vo.reg.resource.*
import io.kuark.service.sys.provider.reg.ibiz.IRegResourceBiz
import io.kuark.service.sys.provider.reg.model.po.RegResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/reg/resource")
@CrossOrigin
open class RegResourceController :
    BaseCrudController<String, IRegResourceBiz, RegResourceSearchPayload, RegResourceRecord, RegResourcePayload>() {

    @Autowired
    private lateinit var dictApi: IDictApi

    @GetMapping("/getMenus")
    fun getMenus(): List<MenuTreeNode> {
        return biz.getMenus()
    }

    @PostMapping("/loadTreeNodes")
    fun laodTreeNodes(@RequestBody searchPayload: RegResourceSearchPayload): WebResult<List<RegResourceTreeNode>> {
        val results = biz.loadDirectChildrenForTree(searchPayload)
        return WebResult(results)
    }

    @PostMapping("/searchByTree")
    fun searchByTree(@RequestBody searchPayload: RegResourceSearchPayload): WebResult<Pair<List<RegResourceRecord>, Int>> {
        return WebResult(biz.loadDirectChildrenForList(searchPayload))
    }

    @GetMapping("/getRes")
    fun get(id: String, fetchAllParentIds: Boolean = false): WebResult<RegResourceRecord> {
        return WebResult(biz.get(id, RegResourceRecord::class, fetchAllParentIds))
    }

    @GetMapping("/updateActive")
    fun updateActive(id: String, active: Boolean): WebResult<Boolean> {
        val regResource = RegResource {
            this.id = id
            this.active = active
        }
        return WebResult(biz.update(regResource))
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

}