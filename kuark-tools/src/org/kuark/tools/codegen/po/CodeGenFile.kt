package org.kuark.tools.codegen.po

import org.kuark.data.jdbc.support.IDbEntity

interface CodeGenFile : IDbEntity<String, CodeGenFile> {

    var filename: String
    var objectName: String

}