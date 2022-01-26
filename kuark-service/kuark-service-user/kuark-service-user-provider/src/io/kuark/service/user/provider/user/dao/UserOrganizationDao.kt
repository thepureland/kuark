package io.kuark.service.user.provider.user.dao

import io.kuark.ability.data.rdb.support.BaseCrudDao
import io.kuark.service.user.provider.user.model.po.UserOrganization
import io.kuark.service.user.provider.user.model.table.UserOrganizations
import org.springframework.stereotype.Repository

/**
 * 组织机构数据访问对象
 *
 * @author K
 * @since 1.0.0
 */
@Repository
//region your codes 1
open class UserOrganizationDao : BaseCrudDao<String, UserOrganization, UserOrganizations>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}