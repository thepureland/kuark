package io.kuark.service.sys.client.fallback

import io.kuark.service.sys.client.proxy.ISysResourceProxy
import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.sys.common.vo.resource.ResourceType
import io.kuark.service.sys.common.vo.resource.SysResourceRecord
import org.springframework.stereotype.Component


@Component
class SysResourceFallback: ISysResourceProxy {

    override fun getResources(subSysDictCode: String, resourceType: ResourceType): List<SysResourceRecord> {
        TODO("Not yet implemented")
    }

    override fun getResources(vararg resourceIds: String): List<SysResourceRecord> {
        TODO("Not yet implemented")
    }

    override fun getSimpleMenus(subSysDictCode: String): List<BaseMenuTreeNode> {
        TODO("Not yet implemented")
    }

}