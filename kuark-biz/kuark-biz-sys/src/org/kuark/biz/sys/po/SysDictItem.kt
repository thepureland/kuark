package org.kuark.biz.sys.po

import org.kuark.data.jdbc.support.IMaintainableDbEntity

interface SysDictItem: IMaintainableDbEntity<String, SysDictItem> {

    var sysDict: SysDict
    var itemCode: String
    var parentCode: String?
    var itemName: String
    var seqNo: Int?

}