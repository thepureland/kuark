package io.kuark.ability.data.rdb.support

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
abstract class BaseRdbBiz<E: Entity<E>, T: Table<E>>: BaseReadOnlyRdbBiz<E, T>() {

//TODO

}