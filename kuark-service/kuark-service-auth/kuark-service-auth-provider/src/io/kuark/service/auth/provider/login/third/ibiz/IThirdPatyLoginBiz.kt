package io.kuark.service.auth.provider.login.third.ibiz

interface IThirdPatyLoginBiz {

    fun login(type: String, code: String, state: String)

}