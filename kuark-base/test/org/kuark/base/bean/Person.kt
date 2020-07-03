package org.kuark.base.bean

import org.kuark.base.support.IIdEntity
import org.kuark.base.tree.IListToTreeRestrict
import java.util.*

/**
 * @author K
 */
class Person : IIdEntity<String>, IListToTreeRestrict<String?> {

    override var selfUniqueIdentifier: String? = null
    override var parentUniqueIdentifier: String? = null
        private set
    var name: String? = null
    var sex: String? = null
    var age = 0
    var weight = 0.0
    var birthday: Date? = null
    var address: Address? = null
    var goods: List<String>? = null
    var contact: Map<String, String>? = null

    constructor() {}

    constructor(name: String?) {
        this.name = name
    }

    constructor(name: String?, sex: String?) {
        this.name = name
        this.sex = sex
    }

    fun sayHello() {
        println("Hello World!")
    }

    fun getpId(): String? {
        return parentUniqueIdentifier
    }

    fun setpId(pId: String?) {
        parentUniqueIdentifier = pId
    }

    fun f(str: String) {
        println("Person.f()...$str")
    }

    override fun toString(): String {
        return "Person.toString()..."
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (address == null) 0 else address.hashCode()
        result = prime * result + age
        result = prime * result + if (birthday == null) 0 else birthday.hashCode()
        result = prime * result + if (contact == null) 0 else contact.hashCode()
        result = prime * result + if (goods == null) 0 else goods.hashCode()
        result = prime * result + if (name == null) 0 else name.hashCode()
        result = prime * result + if (sex == null) 0 else sex.hashCode()
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj == null) return false
        if (javaClass != obj.javaClass) return false
        val other = obj as Person
        if (address == null) {
            if (other.address != null) return false
        } else if (address != other.address) return false
        if (age != other.age) return false
        if (birthday == null) {
            if (other.birthday != null) return false
        } else if (birthday.toString() != other.birthday.toString()) return false
        if (contact == null) {
            if (other.contact != null) return false
        } else if (contact.toString() != other.contact.toString()) return false
        if (goods == null) {
            if (other.goods != null) return false
        } else if (goods.toString() != other.goods.toString()) return false
        if (name == null) {
            if (other.name != null) return false
        } else if (name != other.name) return false
        if (sex == null) {
            if (other.sex != null) return false
        } else if (sex != other.sex) return false
        return true
    }

    companion object {
        private const val serialVersionUID = -4651767804549188044L
    }

}