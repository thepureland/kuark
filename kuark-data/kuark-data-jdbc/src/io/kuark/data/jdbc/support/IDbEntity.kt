package io.kuark.data.jdbc.support

import me.liuwj.ktorm.entity.Entity
import io.kuark.base.support.IIdEntity

/**
 * 数据库表记录实体接口
 *
 * @author K
 * @since 1.0.0
 */
interface IDbEntity<ID, E : Entity<E>>: IIdEntity<ID>, Entity<E> {


}