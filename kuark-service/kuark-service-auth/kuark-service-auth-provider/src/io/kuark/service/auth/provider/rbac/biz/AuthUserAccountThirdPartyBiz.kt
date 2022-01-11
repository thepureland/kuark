package io.kuark.service.auth.provider.rbac.biz

import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.service.auth.provider.login.general.PrincipalType
import io.kuark.service.auth.provider.rbac.dao.AuthUserAccountThirdPartyDao
import io.kuark.service.auth.provider.rbac.ibiz.IAuthUserAccountThirdPartyBiz
import io.kuark.service.auth.provider.rbac.model.po.AuthUserAccountThirdParty
import org.springframework.stereotype.Service

/**
 * 用户账号第三方授权业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class AuthUserAccountThirdPartyBiz :
    BaseCrudBiz<String, AuthUserAccountThirdParty, AuthUserAccountThirdPartyDao>(),
    IAuthUserAccountThirdPartyBiz {
//endregion your codes 1


    //region your codes 2

    override fun isIdentifierExists(identityTypeDictCode: String, identifier: String): Boolean =
        dao.isIdentifierExists(identityTypeDictCode, identifier)

    override fun save(userAccountThirdParty: AuthUserAccountThirdParty): Boolean = dao.save(userAccountThirdParty)

    override fun getByPrincipal(principalType: PrincipalType, principal: String): AuthUserAccountThirdParty {
        TODO("Not yet implemented")
    }

    //endregion your codes 2

}