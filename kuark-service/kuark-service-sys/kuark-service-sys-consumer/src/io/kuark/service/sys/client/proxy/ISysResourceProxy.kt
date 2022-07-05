package io.kuark.service.sys.client.proxy

import io.kuark.service.sys.client.fallback.SysResourceFallback
import io.kuark.service.sys.common.api.ISysResourceApi
import io.kuark.service.sys.common.vo.dict.SysResourceCacheItem
import io.kuark.service.sys.common.vo.resource.*
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping


@FeignClient(name = "sys-resource", fallback = SysResourceFallback::class)
interface ISysResourceProxy : ISysResourceApi {

    @GetMapping("/sys/resource/api/getResourcesById")
    override fun getResource(resourceId: String): SysResourceCacheItem?

    @GetMapping("/sys/resource/api/getResourcesByIds")
    override fun getResources(resourceIds: Collection<String>): Map<String, SysResourceCacheItem>

    @GetMapping("/sys/resource/api/getResourcesByType")
    override fun getResources(subSysDictCode: String, resourceType: ResourceType): List<SysResourceCacheItem>

//    @GetMapping("/sys/resource/api/getResourcesByIds")
//    override fun getResources(
//        subSysDictCode: String, resourceType: ResourceType, vararg resourceIds: String
//    ): List<SysResourceCacheItem>

    @GetMapping("/sys/resource/api/getSimpleMenus")
    override fun getSimpleMenus(subSysDictCode: String): List<BaseMenuTreeNode>

    @GetMapping("/sys/resource/api/getMenus")
    override fun getMenus(subSysDictCode: String): List<MenuTreeNode>

    @GetMapping("/sys/resource/api/getResourceIdByUrl")
    override fun getResourceId(subSysDictCode: String, url: String): String?

    @GetMapping("/sys/resource/api/getDirectChildrenResources")
    override fun getDirectChildrenResources(
        subSysDictCode: String,
        resourceType: ResourceType,
        parentId: String?
    ): List<SysResourceCacheItem>

    @GetMapping("/sys/resource/api/getChildrenResources")
    override fun getChildrenResources(
        subSysDictCode: String,
        resourceType: ResourceType,
        parentId: String
    ): List<SysResourceCacheItem>

}