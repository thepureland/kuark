package io.kuark.service.sys.provider.biz.ibiz

import io.kuark.base.support.biz.IBaseCrudBiz
import io.kuark.service.sys.common.vo.dict.SysTenantCacheItem
import io.kuark.service.sys.common.vo.tenant.SysTenantRecord
import io.kuark.service.sys.provider.model.po.SysTenant


/**
 * 租户业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface ISysTenantBiz : IBaseCrudBiz<String, SysTenant> {
//endregion your codes 1

    //region your codes 2

    /**
     * 根据id取得对应租户信息，并将结果缓存，查不到不缓存
     *
     * @param id 主键
     * @return 租户信息
     * @author K
     * @since 1.0.0
     */
    fun getTenant(id: String): SysTenantCacheItem?

    /**
     * 根据id集合取得对应租户信息，并将结果缓存，查不到不缓存
     *
     * @param ids 租户id集合
     * @return Map(租户id, SysTenantCacheItem)
     * @author K
     * @since 1.0.0
     */
    fun getTenants(ids: Collection<String>): Map<String, SysTenantCacheItem>


    /**
     * 根据子系统代码，取得对应租户信息(仅包括处于启用状态的)，并将结果缓存，查不到不缓存
     *
     * @param subSysDictCode 子系统代码
     * @return List(租户信息)
     * @author K
     * @since 1.0.0
     */
    fun getTenants(subSysDictCode: String): List<SysTenantCacheItem>

    /**
     * 返回所有启用的租户
     *
     * @return Map(子系统代码，List(租户记录对象))
     * @author K
     * @since 1.0.0
     */
    fun getAllActiveTenants(): Map<String, List<SysTenantRecord>>

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