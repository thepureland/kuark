package io.kuark.service.sys.common.api

import io.kuark.service.sys.common.vo.dict.SysResourceCacheItem
import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.sys.common.vo.resource.MenuTreeNode
import io.kuark.service.sys.common.vo.resource.ResourceType


/**
 * 系统资源服务对外的接口
 *
 * @author K
 * @since 1.0.0
 */
interface ISysResourceApi {

    /**
     * 根据子系统和资源类型，返回对应的资源
     *
     * @param subSysDictCode 子系统代码
     * @param resourceType 资源类型枚举
     * @return List(资源对象)
     * @author K
     * @since 1.0.0
     */
    fun getResources(subSysDictCode: String, resourceType: ResourceType): List<SysResourceCacheItem>

    /**
     * 根据资源id返回对应的资源
     *
     * @param subSysDictCode 子系统代码
     * @param resourceType 资源类型枚举
     * @param resourceIds 资源id可变数组
     * @return List(资源对象)
     * @author K
     * @since 1.0.0
     */
    fun getResources(
        subSysDictCode: String, resourceType: ResourceType, vararg resourceIds: String
    ): List<SysResourceCacheItem>

    /**
     * 根据子系统和资源类型，返回对应的资源
     *
     * @param subSysDictCode 子系统代码
     * @return List(基础的菜单树结点)
     * @author K
     * @since 1.0.0
     */
    fun getSimpleMenus(subSysDictCode: String): List<BaseMenuTreeNode>

    /**
     * 根据子系统和资源类型，返回对应的资源
     *
     * @param subSysDictCode 子系统代码
     * @return List(菜单树结点)
     * @author K
     * @since 1.0.0
     */
    fun getMenus(subSysDictCode: String): List<MenuTreeNode>

    /**
     * 返回指定子系统和url对应的资源的id
     *
     * @param subSysDictCode 子系统代码
     * @param url 资源URL
     * @return 资源id
     * @author K
     * @since 1.0.0
     */
    fun getResourceId(subSysDictCode: String, url: String): String?

    /**
     * 返回指定父菜单id的直接孩子菜单(active的)
     *
     * @param subSysDictCode 子系统代码
     * @param parentId 父菜单id，为null时返回第一层菜单
     * @return List(资源对象)
     * @author K
     * @since 1.0.0
     */
    fun getDirectChildrenMenu(subSysDictCode: String, parentId: String?): List<SysResourceCacheItem>

}