package io.kuark.service.sys.client.fallback

import io.kuark.service.sys.client.proxy.ISysResourceProxy
import io.kuark.service.sys.common.vo.dict.SysResourceCacheItem
import io.kuark.service.sys.common.vo.resource.*
import org.springframework.stereotype.Component


@Component
class SysResourceFallback : ISysResourceProxy {

    override fun getResources(subSysDictCode: String, resourceType: ResourceType): List<SysResourceCacheItem> {
        TODO("Not yet implemented")
    }

    override fun getResources(
        subSysDictCode: String, resourceType: ResourceType, vararg resourceIds: String
    ): List<SysResourceCacheItem> {
        TODO("Not yet implemented")
    }

    override fun getSimpleMenus(subSysDictCode: String): List<BaseMenuTreeNode> {
        TODO("Not yet implemented")
    }

    override fun getMenus(subSysDictCode: String): List<MenuTreeNode> {
        TODO("Not yet implemented")
    }

    override fun getResourceId(subSysDictCode: String, url: String): String? {
        TODO("Not yet implemented")
    }

}