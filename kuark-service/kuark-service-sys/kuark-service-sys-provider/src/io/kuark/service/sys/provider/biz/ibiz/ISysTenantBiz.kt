package io.kuark.service.sys.provider.biz.ibiz

import io.kuark.base.support.biz.IBaseCrudBiz
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
     * 返回所有启用的租户
     *
     * @return Map(子系统代码，List(租户记录对象))
     * @author K
     * @since 1.0.0
     */
    fun getAllActiveTenants(): Map<String, List<SysTenantRecord>>

    //endregion your codes 2

}