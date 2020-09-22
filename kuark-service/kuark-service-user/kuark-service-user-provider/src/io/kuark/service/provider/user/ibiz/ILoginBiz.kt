package io.kuark.service.provider.user.ibiz

interface ILoginBiz {

    fun thirdLogin(type: String, code: String, state: String)

}