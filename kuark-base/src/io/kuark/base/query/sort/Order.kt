package io.kuark.base.query.sort

import java.io.Serializable

/**
 * 排序
 *
 * @author K
 * @since 1.0.0
 */
class Order : Serializable {

    var property: String = ""
    var direction: Direction = Direction.ASC

    constructor()

    constructor(property: String, direction: Direction = Direction.ASC) {
        this.property = property
        this.direction = direction
    }

    fun isAscending() = direction == Direction.ASC

    override fun toString() = "${property}: $direction"

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + direction.hashCode()
        result = prime * result + property.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null) {
            return false
        }
        if (javaClass != other.javaClass) {
            return false
        }
        val order = other as Order
        if (direction !== order.direction) {
            return false
        }
        if (property != order.property) {
            return false
        }
        return true
    }

    companion object {
        private const val serialVersionUID = 1522511010900998988L
        fun asc(property: String): Order {
            return Order(property)
        }

        fun desc(property: String): Order {
            return Order(property, Direction.DESC)
        }
    }
}