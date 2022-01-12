package io.kuark.service.user.provider.login.third.ibiz

interface IThirdPatyLoginBiz {

    fun login(type: String, code: String, state: String)

}