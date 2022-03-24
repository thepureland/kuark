package io.kuark.service.sys.client.proxy

import io.kuark.service.sys.client.fallback.SysResourceFallback
import io.kuark.service.sys.common.api.ISysResourceApi
import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.sys.common.vo.resource.ResourceType
import io.kuark.service.sys.common.vo.resource.SysResourceDetail
import io.kuark.service.sys.common.vo.resource.SysResourceRecord
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping


@FeignClient(name = "sys-resource", fallback = SysResourceFallback::class)
interface ISysResourceProxy : ISysResourceApi {

    @GetMapping("/sys/resource/getResources")
    override fun getResources(subSysDictCode: String, resourceType: ResourceType): List<SysResourceDetail>

    @GetMapping("/sys/resource/getResourcesByIds")
    override fun getResources(
        subSysDictCode: String, resourceType: ResourceType, vararg resourceIds: String
    ): List<SysResourceDetail>

    @GetMapping("/sys/resource/getSimpleMenus")
    override fun getSimpleMenus(subSysDictCode: String): List<BaseMenuTreeNode>

}