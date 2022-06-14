package io.kuark.service.sys.provider.api.local

import io.kuark.base.support.Consts
import io.kuark.service.sys.common.api.ISysResourceApi
import io.kuark.service.sys.common.vo.dict.SysResourceCacheItem
import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.sys.common.vo.resource.MenuTreeNode
import io.kuark.service.sys.common.vo.resource.ResourceType
import io.kuark.service.sys.provider.biz.ibiz.ISysResourceBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
open class SysResourceApi : ISysResourceApi {

    @Autowired
    private lateinit var sysResourceBiz: ISysResourceBiz

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun getResources(subSysDictCode: String, resourceType: ResourceType): List<SysResourceCacheItem> {
        return sysResourceBiz.getResourcesFromCache(subSysDictCode, resourceType.code)
    }

    override fun getResources(
        subSysDictCode: String, resourceType: ResourceType, vararg resourceIds: String
    ): List<SysResourceCacheItem> {
        return sysResourceBiz.getResourcesFromCache(subSysDictCode, resourceType, *resourceIds)
    }

    override fun getSimpleMenus(subSysDictCode: String): List<BaseMenuTreeNode> {
        return sysResourceBiz.getSimpleMenus(subSysDictCode)
    }

    override fun getMenus(subSysDictCode: String): List<MenuTreeNode> {
        return sysResourceBiz.getMenus(subSysDictCode)
    }

    override fun getResourceId(subSysDictCode: String, url: String): String? {
        return sysResourceBiz.getResourceId(subSysDictCode, url)
    }

    override fun getDirectChildrenMenu(subSysDictCode: String, parentId: String?): List<SysResourceCacheItem> {
        return sysResourceBiz.getDirectChildrenMenu(subSysDictCode, parentId)
    }

}