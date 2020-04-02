package org.kuark.biz.model.user

import me.liuwj.ktorm.entity.Entity
import java.time.LocalDate

interface UserInfo : Entity<UserInfo> {
    companion object : Entity.Factory<UserInfo>()

    var id: Int?
    var name: String
    var birthday: LocalDate?
}