package org.kuark.resource.sys.po

import org.kuark.data.jdbc.support.DbEntityFactory
import org.kuark.data.jdbc.support.IMaintainableDbEntity

/**
 * 资源数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface SysResource: IMaintainableDbEntity<String, SysResource> {
//endregion your codes 1

    companion object : DbEntityFactory<SysResource>()

    /** 名称，或其国际化key */
    var name: String

    /** url */
    var url: String

    /** 资源类型字典代码 */
    var resourceTypeDictCode: String

    /** 父id */
    var parentId: String

    /** 在同父节点下的排序号 */
    var seqNo: Int

    /** 子系统代码 */
    var subSysDictCode: String

    /** 权限表达式 */
    var permission: String

    /** 图标url */
    var iconUrl: String


    //region your codes 2

	//endregion your codes 2

}