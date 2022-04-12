package io.kuark.service.sys.provider.biz.ibiz

import io.kuark.base.support.biz.IBaseCrudBiz
import io.kuark.service.sys.common.vo.dict.SysResourceCacheItem
import io.kuark.service.sys.common.vo.resource.*
import io.kuark.service.sys.provider.model.po.SysResource
import kotlin.reflect.KClass

/**
 * 资源业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface ISysResourceBiz : IBaseCrudBiz<String, SysResource> {
//endregion your codes 1

    //region your codes 2

    /**
     * 根据子系统代码和资源类型，取得对应资源(仅包括处于启用状态的)，并将结果缓存，查不到不缓存.
     * 缓存的名称为 sys_resource，key为 子系统代码:资源类型代码。
     *
     * @param subSysDictCode 子系统代码
     * @param resourceTypeDictCode 资源类型代码
     * @return List(资源详情)
     * @author K
     * @since 1.0.0
     */
    fun getResourcesFromCache(subSysDictCode: String, resourceTypeDictCode: String): List<SysResourceCacheItem>

    fun getSimpleMenus(subSysDictCode: String): List<BaseMenuTreeNode>

    fun getMenus(subSysDictCode: String): List<MenuTreeNode>

    fun loadDirectChildrenForTree(searchPayload: SysResourceSearchPayload): List<SysResourceTreeNode>

    fun loadDirectChildrenForList(searchPayload: SysResourceSearchPayload): Pair<List<SysResourceRecord>, Int>

    fun <R : Any> get(id: String, returnType: KClass<R>, fetchAllParentIds: Boolean = false): R?

    /**
     * 获取所有祖先id
     *
     * @param id 当前资源id
     * @return List(祖先id)
     * @author K
     * @since 1.0.0
     */
    fun fetchAllParentIds(id: String): List<String>

    /**
     * 删除资源并级联删除其所有孩子
     *
     * @param id 资源id
     * @return 是否删除成功
     * @author K
     * @since 1.0.0
     */
    fun cascadeDeleteChildren(id: String): Boolean

    /**
     * 根据子系统代码、资源类型和资源id，从缓存中取得对应资源(仅包括处于启用状态的)
     *
     * @param subSysDictCode 子系统代码
     * @param resourceType 资源类型枚举
     * @param resourceIds 资源id可变数组
     * @return List(资源信息)
     * @author K
     * @since 1.0.0
     */
    fun getResources(
        subSysDictCode: String, resourceType: ResourceType, vararg resourceIds: String
    ): List<SysResourceCacheItem>

    /**
     * 更新启用状态，并同步缓存
     *
     * @param id 主键
     * @param active 是否启用
     * @return 是否更新成功
     * @author K
     * @since 1.0.0
     */
    fun updateActive(id: String, active: Boolean): Boolean

    //endregion your codes 2

}