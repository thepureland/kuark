package io.kuark.service.sys.common.api

import io.kuark.service.sys.common.vo.tenant.SysTenantRecord


/**
 * 租户 对外API
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface ISysTenantApi {
//endregion your codes 1

    //region your codes 2

    /**
     * 返回指定id的租户(包括未启用的)
     *
     * @param id 租户id
     * @return 租户信息对象，包括未启用的。找不到返回null
     * @author K
     * @since 1.0.0
     */
    fun getTenant(id: String): SysTenantRecord?

    /**
     * 返回指定子系统的所有租户(仅启用的)
     *
     * @param subSysDictCode 子系统代码
     * @return List(租户信息对象)
     * @author K
     * @since 1.0.0
     */
    fun getTenants(subSysDictCode: String): List<SysTenantRecord>

    //endregion your codes 2

}