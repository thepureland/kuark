package org.kuark.biz.sys.po

import org.kuark.data.jdbc.support.IMaintainableDbEntity

interface SysDataSource: IMaintainableDbEntity<String, SysDataSource> {

    var name: String
    var url: String
    var username: String
    var password: String
    var initialSize: Int?
    var maxActive: Int?
    var minIdle: Int?
    var maxWait: Int?

}