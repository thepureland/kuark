package io.kuark.service.user.provider.ibiz

interface ILoginBiz {

    fun thirdLogin(type: String, code: String, state: String)

}