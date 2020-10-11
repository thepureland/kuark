package io.kuark.ability.auth.login.third.ibiz

interface IThirdPatyLoginBiz {

    fun login(type: String, code: String, state: String)

}