package io.kuark.service.sys.provider.biz.ibiz

import io.kuark.base.support.biz.IBaseCrudBiz
import io.kuark.service.sys.common.vo.datasource.SysDataSourceCacheItem
import io.kuark.service.sys.provider.model.po.SysDataSource


/**
 * 数据源业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface ISysDataSourceBiz : IBaseCrudBiz<String, SysDataSource> {
//endregion your codes 1

    //region your codes 2

    /**
     * 返回指定子系统和租户的数据源，先从缓存找，找不到从数据库加载，并缓存
     * 注：如果存在多条满足条件的结果，将任意返回一条！
     *
     * @param subSysDictCode 子系统代码
     * @param tenantId 租户id
     * @return SysDataSourceCacheItem
     * @author K
     * @since 1.0.0
     */
    fun getDataSource(subSysDictCode: String, tenantId: String?): SysDataSourceCacheItem?

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

    /**
     * 重置密码
     *
     * @param id 主键
     * @param newPassword 新密码
     * @author K
     * @since 1.0.0
     */
    fun resetPassword(id: String, newPassword: String)

    //endregion your codes 2

}