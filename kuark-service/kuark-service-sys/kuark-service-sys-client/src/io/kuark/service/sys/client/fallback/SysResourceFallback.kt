package io.kuark.service.sys.client.fallback

import io.kuark.service.sys.client.proxy.ISysResourceClient
import io.kuark.service.sys.common.vo.resource.ResourceType
import io.kuark.service.sys.common.vo.resource.SysResourceRecord
import org.springframework.stereotype.Component


@Component
class SysResourceFallback: ISysResourceClient {

    override fun getResources(subSysDictCode: String, resourceType: ResourceType): List<SysResourceRecord> {
        TODO("Not yet implemented")
    }

    override fun getResources(vararg resourceIds: String): List<SysResourceRecord> {
        TODO("Not yet implemented")
    }

}