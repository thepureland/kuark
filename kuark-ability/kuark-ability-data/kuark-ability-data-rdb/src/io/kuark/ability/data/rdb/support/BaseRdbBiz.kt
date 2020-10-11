package io.kuark.ability.data.rdb.support

import org.ktorm.entity.Entity
import org.ktorm.schema.Table

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
abstract class BaseRdbBiz<E: Entity<E>, T: Table<E>>: BaseReadOnlyRdbBiz<E, T>() {

//TODO

}