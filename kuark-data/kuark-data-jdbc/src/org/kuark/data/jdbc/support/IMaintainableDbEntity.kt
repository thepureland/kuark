package org.kuark.data.jdbc.support

import me.liuwj.ktorm.entity.Entity
import java.time.LocalDateTime

/**
 * 可维护型的数据库记录的实体接口
 *
 * @author K
 * @since 1.0.0
 */
interface IMaintainableDbEntity<ID, E : Entity<E>> : IDbEntity<ID, E> {

    /** 记录创建时间 */
    val createTime: LocalDateTime?
    /** 记录创建用户 */
    var createUser: String?
    /** 记录更新时间 */
    var updateTime: LocalDateTime?
    /** 记录更新用户 */
    var updateUser: String?
    /** 是否启用 */
    var isActive: Boolean
    /** 是否内置 */
    var isBuiltIn: Boolean
    /** 备注 */
    var remark: String?

}