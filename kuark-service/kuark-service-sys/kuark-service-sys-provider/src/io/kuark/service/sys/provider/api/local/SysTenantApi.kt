package io.kuark.service.sys.provider.api.local

import io.kuark.base.support.Consts
import io.kuark.service.sys.common.api.ISysTenantApi
import io.kuark.service.sys.common.vo.tenant.SysTenantRecord
import io.kuark.service.sys.common.vo.tenant.SysTenantSearchPayload
import io.kuark.service.sys.provider.biz.ibiz.ISysTenantBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


/**
 * 租户 API本地实现
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
@Service
open class SysTenantApi : ISysTenantApi {
//endregion your codes 1

    //region your codes 2

    @Autowired
    private lateinit var sysTenantBiz: ISysTenantBiz

    override fun getTenant(id: String): SysTenantRecord? {
        return sysTenantBiz.get(id, SysTenantRecord::class)
    }

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun getTenants(subSysDictCode: String): List<SysTenantRecord> {
        val searchPayload = SysTenantSearchPayload().apply { this.subSysDictCode = subSysDictCode }
        return sysTenantBiz.search(searchPayload) as List<SysTenantRecord>
    }

    //endregion your codes 2

}