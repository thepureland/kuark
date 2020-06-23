package org.kuark.data.jdbc.support

import me.liuwj.ktorm.entity.Entity
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

/**
 * 可维护型的数据库记录的实体接口
 * @since 1.0.0
 */
interface IMaintainableDbEntity<ID, E : Entity<E>> : IDbEntity<ID, E> {

    val createTime: LocalTime?
    var createUser: String?
    var updateTime: LocalTime?
    var updateUser: String?
    var isActive: Boolean
    var isBuiltIn: Boolean
    var remark: String?

}