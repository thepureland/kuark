package org.kuark.tools.codegen.po

import org.kuark.data.jdbc.support.IDbEntity

interface CodeGenColumn : IDbEntity<String, CodeGenColumn> {

    var name: String
    var objectName: String
    var comment: String?
    var isSearchable: Boolean
    var isSortable: Boolean
    var orderInList: Int?
    var defaultOrder: Int?
    var orderInEdit: Int?
    var orderInView: Int?

}