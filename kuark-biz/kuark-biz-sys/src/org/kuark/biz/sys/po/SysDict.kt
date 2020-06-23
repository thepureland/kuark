package org.kuark.biz.sys.po

import org.kuark.data.jdbc.support.IMaintainableDbEntity

interface SysDict: IMaintainableDbEntity<String, SysDict> {

    var module: String?
    var dictType: String
    var dictName: String?

}