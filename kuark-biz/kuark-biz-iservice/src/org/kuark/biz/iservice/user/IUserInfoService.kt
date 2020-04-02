package org.kuark.biz.iservice.user

import org.kuark.biz.model.user.UserInfo

interface IUserInfoService {

    fun add(userInfo: UserInfo): Int

    fun all(): List<UserInfo>

}