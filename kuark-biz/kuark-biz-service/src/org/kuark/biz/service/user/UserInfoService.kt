package org.kuark.biz.service.user

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.entity.toList
import org.kuark.biz.dao.user.UserInfos
import org.kuark.biz.iservice.user.IUserInfoService
import org.kuark.biz.model.user.UserInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class UserInfoService: IUserInfoService {

    @Autowired
    lateinit var database: Database

//    @Transactional
    override fun add(userInfo: UserInfo): Int {
        try {
            val sequence = database.sequenceOf(UserInfos)
            return sequence.add(userInfo)
        } catch (e: Error) {
            e.printStackTrace()
        }
        return 0
    }

//    @Transactional
    override fun all(): List<UserInfo> {
        val sequence = database.sequenceOf(UserInfos)
        val list = sequence.toList()
        return list
    }


}