package io.kuark.base.bean

import java.io.Serializable

/**
 * 地址信息(for test)
 *
 * @author K
 * @since 1.0.0
 */
class Address : Serializable {

    var province: String? = null
    var city: String? = null
    var street: String? = null
    var zipcode: String?  = null

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (city == null) 0 else city.hashCode()
        result = prime * result + if (province == null) 0 else province.hashCode()
        result = prime * result + if (street == null) 0 else street.hashCode()
        result = prime * result + if (zipcode == null) 0 else zipcode.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        val otherAddr: Address = other as Address
        if (city == null) {
            if (otherAddr.city != null) return false
        } else if (city != otherAddr.city) return false
        if (province == null) {
            if (otherAddr.province != null) return false
        } else if (province != otherAddr.province) return false
        if (street == null) {
            if (otherAddr.street != null) return false
        } else if (street != otherAddr.street) return false
        if (zipcode == null) {
            if (otherAddr.zipcode != null) return false
        } else if (zipcode != otherAddr.zipcode) return false
        return true
    }

    companion object {
        private const val serialVersionUID = -7450588278095526959L
    }
}