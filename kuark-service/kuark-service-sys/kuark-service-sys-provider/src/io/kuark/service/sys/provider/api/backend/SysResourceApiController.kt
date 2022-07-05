package io.kuark.service.sys.provider.api.backend

import io.kuark.base.lang.EnumKit
import io.kuark.service.sys.common.api.ISysResourceApi
import io.kuark.service.sys.common.vo.dict.SysResourceCacheItem
import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.sys.common.vo.resource.MenuTreeNode
import io.kuark.service.sys.common.vo.resource.ResourceType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 * 资源后端控制器
 *
 * @author K
 * @since 1.0.0
 */
@RestController
//region your codes 1
@RequestMapping("/sys/resource/api")
open class SysResourceApiController {
//endregion your codes 1

    //region your codes 2

    @Autowired
    private lateinit var sysResourceApi: ISysResourceApi

    /**
     * 返回资源id对应的资源
     *
     * @param resourceId 资源id
     * @return 资源对象
     * @author K
     * @since 1.0.0
     */
    @GetMapping("/getResourcesById")
    fun getResource(resourceId: String): SysResourceCacheItem? {
        return sysResourceApi.getResource(resourceId)
    }

    /**
     * 返回资源id集合对应的资源
     *
     * @param resourceIds 资源id集合
     * @return List(资源对象)
     * @author K
     * @since 1.0.0
     */
    @GetMapping("/getResourcesByIds")
    fun getResources(resourceIds: Collection<String>): Map<String, SysResourceCacheItem> {
        return sysResourceApi.getResources(resourceIds)
    }

    /**
     * 根据子系统和资源类型，返回对应的资源
     *
     * @param subSysDictCode 子系统代码
     * @param resourceType 资源类型枚举
     * @return List(资源对象)
     * @author K
     * @since 1.0.0
     */
    @GetMapping("/getResourcesByType")
    fun getResources(subSysDictCode: String, resourceType: ResourceType): List<SysResourceCacheItem> {
        return sysResourceApi.getResources(subSysDictCode, resourceType)
    }

    /**
     * 根据子系统和资源类型，返回对应的资源
     *
     * @param subSysDictCode 子系统代码
     * @return List(基础的菜单树结点)
     * @author K
     * @since 1.0.0
     */
    @GetMapping("/getSimpleMenus")
    fun getSimpleMenus(subSysDictCode: String): List<BaseMenuTreeNode> {
        return sysResourceApi.getSimpleMenus(subSysDictCode)
    }

    /**
     * 根据子系统和资源类型，返回对应的资源
     *
     * @param subSysDictCode 子系统代码
     * @return List(菜单树结点)
     * @author K
     * @since 1.0.0
     */
    @GetMapping("/getMenus")
    fun getMenus(subSysDictCode: String): List<MenuTreeNode> {
        return sysResourceApi.getMenus(subSysDictCode)
    }

    /**
     * 返回指定子系统和url对应的资源的id
     *
     * @param subSysDictCode 子系统代码
     * @param url 资源URL
     * @return 资源id
     * @author K
     * @since 1.0.0
     */
    @GetMapping("/getResourceIdByUrl")
    fun getResourceId(subSysDictCode: String, url: String): String? {
        return sysResourceApi.getResourceId(subSysDictCode, url)
    }

    /**
     * 返回指定父菜单id的直接孩子菜单(active的)
     *
     * @param subSysDictCode 子系统代码
     * @param parentId 父菜单id，为null时返回第一层菜单
     * @return List(资源对象)
     * @author K
     * @since 1.0.0
     */
    @GetMapping("/getDirectChildrenResources")
    fun getDirectChildrenResources(subSysDictCode: String, resourceTypeDictCode: String, parentId: String?): List<SysResourceCacheItem> {
        val resourceType = EnumKit.enumOf(ResourceType::class, resourceTypeDictCode)
        return sysResourceApi.getDirectChildrenResources(subSysDictCode, resourceType!!, parentId)
    }

    @GetMapping("/getChildrenResources")
    fun getChildrenResources(
        subSysDictCode: String,
        resourceType: ResourceType,
        parentId: String
    ): List<SysResourceCacheItem> {
        return sysResourceApi.getChildrenResources(subSysDictCode, resourceType, parentId)
    }

    //endregion your codes 2

}