package org.kuark.base.bean

import java.io.Serializable

/**
 * @author admin
 * @time 2013-4-3 下午8:32:19
 */
class Address : Serializable {
    var province: String? = null
    var city: String? = null
    var street: String? = null
    var zipcode: String? = null

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (city == null) 0 else city.hashCode()
        result = prime * result + if (province == null) 0 else province.hashCode()
        result = prime * result + if (street == null) 0 else street.hashCode()
        result = prime * result + if (zipcode == null) 0 else zipcode.hashCode()
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj == null) return false
        if (javaClass != obj.javaClass) return false
        val other: Address = obj as Address
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