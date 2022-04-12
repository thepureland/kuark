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

    //endregion your codes 2

}