package io.kuark.service.sys.common.api

import io.kuark.service.sys.common.vo.dict.SysResourceCacheItem
import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
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

}