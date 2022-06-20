package io.kuark.service.sys.client.proxy

import io.kuark.service.sys.client.fallback.SysResourceFallback
import io.kuark.service.sys.common.api.ISysResourceApi
import io.kuark.service.sys.common.vo.dict.SysResourceCacheItem
import io.kuark.service.sys.common.vo.resource.*
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping


@FeignClient(name = "sys-resource", fallback = SysResourceFallback::class)
interface ISysResourceProxy : ISysResourceApi {

    @GetMapping("/sys/resource/getResources")
    override fun getResources(subSysDictCode: String, resourceType: ResourceType): List<SysResourceCacheItem>

    @GetMapping("/sys/resource/getResourcesByIds")
    override fun getResources(
        subSysDictCode: String, resourceType: ResourceType, vararg resourceIds: String
    ): List<SysResourceCacheItem>

    @GetMapping("/sys/resource/getSimpleMenus")
    override fun getSimpleMenus(subSysDictCode: String): List<BaseMenuTreeNode>

    @GetMapping("/sys/resource/getMenus")
    override fun getMenus(subSysDictCode: String): List<MenuTreeNode>

    @GetMapping("/sys/resource/getResourceId")
    override fun getResourceId(subSysDictCode: String, url: String): String?

    @GetMapping("/sys/resource/getDirectChildrenMenu")
    override fun getDirectChildrenMenu(subSysDictCode: String, parentId: String?): List<SysResourceCacheItem>

}