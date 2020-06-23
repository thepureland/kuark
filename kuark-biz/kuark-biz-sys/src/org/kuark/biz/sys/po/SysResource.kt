package org.kuark.biz.sys.po

import org.kuark.data.jdbc.support.IMaintainableDbEntity

interface SysResource: IMaintainableDbEntity<String, SysResource> {

    var name: String
    var url: String?
    var resourceTypeCode: String
    var parentId: String?
    var seqNo: Int?
    var subSysCode: String?
    var permission: String?
    var iconUrl: String?

}