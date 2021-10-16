package io.kuark.ability.data.rdb.support

import org.ktorm.entity.Entity
import java.time.LocalDateTime

/**
 * 可更新的数据库记录的实体接口
 *
 * @param ID 主键类型
 * @param E 实体类型
 * @author K
 * @since 1.0.0
 */
interface IUpdatableDbEntity<ID, E : Entity<E>> : IDbEntity<ID, E> {

    /** 记录创建时间 */
    val createTime: LocalDateTime?

    /** 记录创建用户 */
    var createUser: String?

    /** 记录更新时间 */
    var updateTime: LocalDateTime?

    /** 记录更新用户 */
    var updateUser: String?

    /** 是否内置 */
    var builtIn: Boolean

    /** 备注 */
    var remark: String?

}