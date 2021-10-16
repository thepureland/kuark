package io.kuark.ability.data.rdb.support

import org.ktorm.entity.Entity

/**
 * 可启用的数据库记录的实体接口
 *
 * @param ID 主键类型
 * @param E 实体类型
 * @author K
 * @since 1.0.0
 */
interface IActivableDbEntity<ID, E : Entity<E>> : IDbEntity<ID, E> {

    /** 是否启用 */
    var active: Boolean

}