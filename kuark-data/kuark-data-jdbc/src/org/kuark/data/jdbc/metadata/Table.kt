package org.kuark.data.jdbc.metadata

class Table {

    lateinit var name: String
    var comment: String? = null
    var schema: String? = null
    var cat: String? = null
    lateinit var type: TableType

}