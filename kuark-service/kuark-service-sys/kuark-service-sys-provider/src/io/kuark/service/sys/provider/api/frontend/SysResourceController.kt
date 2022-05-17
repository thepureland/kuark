package io.kuark.service.sys.provider.api.frontend

import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.context.core.KuarkContextHolder
import io.kuark.service.sys.common.api.ISysDictApi
import io.kuark.service.sys.common.api.ISysResourceApi
import io.kuark.service.sys.common.vo.dict.DictModuleAndTypePayload
import io.kuark.service.sys.common.vo.dict.SysResourceCacheItem
import io.kuark.service.sys.common.vo.resource.*
import io.kuark.service.sys.provider.biz.ibiz.ISysResourceBiz
import io.kuark.service.sys.provider.model.po.SysResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sys/resource")
open class SysResourceController :
    BaseCrudController<String, ISysResourceBiz, SysResourceSearchPayload, SysResourceRecord, SysResourceDetail, SysResourcePayload>() {

    @Autowired
    private lateinit var dictApi: ISysDictApi

    @Autowired
    private lateinit var resourceApi: ISysResourceApi

    @GetMapping("/getMenus")
    fun getMenus(): List<MenuTreeNode> {
//        KuarkContextHolder.get().subSysCode
        return biz.getMenus(KuarkContextHolder.get().subSysCode!!) //TODO 从上下文取
    }

    @PostMapping("/loadTreeNodes")
    fun laodTreeNodes(@RequestBody searchPayload: SysResourceSearchPayload): List<SysResourceTreeNode> {
        return biz.loadDirectChildrenForTree(searchPayload)
    }

    @PostMapping("/searchByTree")
    fun searchByTree(@RequestBody searchPayload: SysResourceSearchPayload): Pair<List<SysResourceRecord>, Int> {
        return biz.loadDirectChildrenForList(searchPayload)
    }

    @GetMapping("/getRes")
    fun get(id: String, fetchAllParentIds: Boolean = false): SysResourceRecord {
        return biz.get(id, SysResourceRecord::class, fetchAllParentIds) ?: throw ObjectNotFoundException("找不到记录！")
    }

    @GetMapping("/updateActive")
    fun updateActive(id: String, active: Boolean): Boolean {
        val sysResource = SysResource {
            this.id = id
            this.active = active
        }
        return biz.update(sysResource)
    }

    @DeleteMapping("/delete")
    override fun delete(id: String): Boolean {
        return biz.cascadeDeleteChildren(id)
    }

    @PostMapping("/batchDelete")
    override fun batchDelete(@RequestBody ids: List<String>): Boolean {
        ids.forEach {
            biz.cascadeDeleteChildren(it)
        }
        return true
    }

    @GetMapping("/loadResourceTypes")
    fun loadResourceTypes(): Map<String, String> {
        return dictApi.getDictItemMap(DictModuleAndTypePayload("kuark:sys", "resource_type"))
    }

    @GetMapping("/getResources")
    fun getResources(subSysDictCode: String, resourceType: ResourceType): List<SysResourceCacheItem> {
        return resourceApi.getResources(subSysDictCode, resourceType)
    }

    @GetMapping("/getResourcesByIds")
    fun getResourcesByIds(
        subSysDictCode: String,
        resourceType: ResourceType,
        vararg resourceIds: String
    ): List<SysResourceCacheItem> {
        return resourceApi.getResources(subSysDictCode, resourceType, *resourceIds)
    }

    @GetMapping("/getSimpleMenus")
    fun getSimpleMenus(subSysDictCode: String): List<BaseMenuTreeNode> {
        return resourceApi.getSimpleMenus(subSysDictCode)
    }

}