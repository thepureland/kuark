package io.kuark.service.user.provider.login.general.ibiz

interface IAuthenticationBiz {

    fun login(map: Map<String, Any>)

    fun logout()

}