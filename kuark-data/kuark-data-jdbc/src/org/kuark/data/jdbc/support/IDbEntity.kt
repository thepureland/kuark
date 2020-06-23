package org.kuark.data.jdbc.support

import me.liuwj.ktorm.entity.Entity
import org.kuark.base.support.IdEntity

/**
 * 数据库表记录实体接口
 * @since 1.0.0
 */
interface IDbEntity<ID, E : Entity<E>>: IdEntity<ID>, Entity<E> {


}