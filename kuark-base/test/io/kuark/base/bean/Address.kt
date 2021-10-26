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
        val other: Address = other as Address
        if (city == null) {
            if (other.city != null) return false
        } else if (city != other.city) return false
        if (province == null) {
            if (other.province != null) return false
        } else if (province != other.province) return false
        if (street == null) {
            if (other.street != null) return false
        } else if (street != other.street) return false
        if (zipcode == null) {
            if (other.zipcode != null) return false
        } else if (zipcode != other.zipcode) return false
        return true
    }

    companion object {
        private const val serialVersionUID = -7450588278095526959L
    }
}