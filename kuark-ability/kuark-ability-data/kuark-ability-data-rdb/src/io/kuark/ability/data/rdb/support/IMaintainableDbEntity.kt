package io.kuark.ability.data.rdb.support

import org.ktorm.entity.Entity

/**
 * 可维护型的数据库记录的实体接口
 *
 * @param ID 主键类型
 * @param E 实体类型
 * @author K
 * @since 1.0.0
 */
interface IMaintainableDbEntity<ID, E : Entity<E>> : IUpdatableDbEntity<ID, E>, IActivableDbEntity<ID, E> {

}