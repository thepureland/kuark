package io.kuark.service.sys.provider.biz.ibiz

import io.kuark.base.support.biz.IBaseCrudBiz
import io.kuark.service.sys.common.vo.cache.SysCacheCacheItem
import io.kuark.service.sys.provider.model.po.SysCache


/**
 * 缓存业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface ISysCacheBiz : IBaseCrudBiz<String, SysCache> {
//endregion your codes 1

    //region your codes 2

    /**
     * 加载指定名称的缓存，并缓存结果
     *
     * @param name 缓存名称
     * @return 缓存详情对象，找不到返回null
     * @author K
     * @since 1.0.0
     */
    fun getCacheFromCache(name: String): SysCacheCacheItem?

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