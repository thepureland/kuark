package io.kuark.service.sys.common.api

import io.kuark.service.sys.common.vo.datasource.SysDataSourceCacheItem


/**
 * 数据源 对外API
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface ISysDataSourceApi {
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

    //endregion your codes 2

}