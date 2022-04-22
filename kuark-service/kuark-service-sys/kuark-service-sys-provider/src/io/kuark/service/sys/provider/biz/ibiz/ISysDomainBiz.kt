package io.kuark.service.sys.provider.biz.ibiz

import io.kuark.base.support.biz.IBaseCrudBiz
import io.kuark.service.sys.common.vo.domain.SysDomainCacheItem
import io.kuark.service.sys.provider.model.po.SysDomain


/**
 * 域名业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface ISysDomainBiz : IBaseCrudBiz<String, SysDomain> {
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