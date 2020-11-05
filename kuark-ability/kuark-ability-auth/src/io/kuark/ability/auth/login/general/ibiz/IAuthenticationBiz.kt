package io.kuark.ability.auth.login.general.ibiz

interface IAuthenticationBiz {

    fun login(map: Map<String, Any>)

    fun logout()

}