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

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }
        if (obj == null) {
            return false
        }
        if (javaClass != obj.javaClass) {
            return false
        }
        val other = obj as Order
        if (direction !== other.direction) {
            return false
        }
        if (property == null) {
            if (other.property != null) {
                return false
            }
        } else if (property != other.property) {
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