package io.kuark.service.user.ibiz

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
interface IUserAccountThirdPartyAuthBiz {


    fun isIdentifierExists(
        identityTypeDictCode: String,
        identifier: String,
        subSysDictCode: String? = null,
        ownerId: String? = null
    ): Boolean

}