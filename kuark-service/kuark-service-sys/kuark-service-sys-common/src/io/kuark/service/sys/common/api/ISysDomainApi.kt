package io.kuark.service.sys.common.api

import io.kuark.service.sys.common.vo.domain.SysDomainCacheItem


/**
 * 域名 对外API
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface ISysDomainApi {
//endregion your codes 1

    //region your codes 2

    /**
     * 返回指定名称的域名信息
     *
     * @param domainName 域名名称
     * @return 域名信息缓存项，如果找不到返回null
     * @author K
     * @since 1.0.0
     */
    fun getDomainByName(domainName: String): SysDomainCacheItem?

    //endregion your codes 2

}