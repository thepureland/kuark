package io.kuark.service.auth.provider.login.general.ibiz

interface IAuthenticationBiz {

    fun login(map: Map<String, Any>)

    fun logout()

}