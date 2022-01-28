package io.kuark.service.sys.provider.biz.impl

import io.kuark.service.sys.provider.biz.ibiz.ISysTenantBiz
import io.kuark.service.sys.provider.model.po.SysTenant
import io.kuark.service.sys.provider.dao.SysTenantDao
import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import org.springframework.stereotype.Service


/**
 * 租户业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class SysTenantBiz : BaseCrudBiz<String, SysTenant, SysTenantDao>(), ISysTenantBiz {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}