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

    fun getAllActiveTenants(): Map<String, List<SysTenantRecord>>

    //endregion your codes 2

}