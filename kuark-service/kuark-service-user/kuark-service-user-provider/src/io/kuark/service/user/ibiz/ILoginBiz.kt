package io.kuark.service.user.ibiz

interface ILoginBiz {

    fun thirdLogin(type: String, code: String, state: String)

}