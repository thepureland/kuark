package io.kuark.service.user.provider.user.biz

import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.service.user.provider.login.general.PrincipalType
import io.kuark.service.user.provider.user.dao.UserAccountThirdDao
import io.kuark.service.user.provider.user.ibiz.IUserAccountThirdBiz
import io.kuark.service.user.provider.user.model.po.UserAccountThird
import org.springframework.stereotype.Service

/**
 * 用户账号第三方授权业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class UserAccountThirdBiz : BaseCrudBiz<String, UserAccountThird, UserAccountThirdDao>(), IUserAccountThirdBiz {
//endregion your codes 1


    //region your codes 2

    override fun isIdentifierExists(identityTypeDictCode: String, identifier: String): Boolean =
        dao.isIdentifierExists(identityTypeDictCode, identifier)

    override fun save(userAccountThirdParty: UserAccountThird): Boolean = dao.save(userAccountThirdParty)

    override fun getByPrincipal(principalType: PrincipalType, principal: String): UserAccountThird {
        TODO("Not yet implemented")
    }

    //endregion your codes 2

}