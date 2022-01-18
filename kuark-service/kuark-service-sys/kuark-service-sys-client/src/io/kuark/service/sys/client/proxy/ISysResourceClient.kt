package io.kuark.service.sys.client.proxy

import io.kuark.service.sys.client.fallback.SysResourceFallback
import io.kuark.service.sys.common.api.ISysResourceApi
import io.kuark.service.sys.common.vo.resource.ResourceType
import io.kuark.service.sys.common.vo.resource.SysResourceRecord
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping


@FeignClient(name = "sys-resource", fallback = SysResourceFallback::class)
interface ISysResourceClient: ISysResourceApi {

    @GetMapping("/sys/resource/getResources")
    override fun getResources(subSysDictCode: String, resourceType: ResourceType): List<SysResourceRecord>

}