package org.kuark.biz.sys.po

import org.kuark.data.jdbc.support.IMaintainableDbEntity

interface SysParam: IMaintainableDbEntity<String, SysParam> {

    var module: String?
    var paramName: String
    var paramValue: String
    var defaultValue: String?
    var seqNo: Int?

}