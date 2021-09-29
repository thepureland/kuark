package io.kuark.service.sys.provider.model.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IMaintainableDbEntity

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
    var url: String?

    /** 资源类型字典代码 */
    var resourceTypeDictCode: String

    /** 父id */
    var parentId: String?

    /** 在同父节点下的排序号 */
    var seqNo: Int?

    /** 子系统代码 */
    var subSysDictCode: String?

    /** 图标 */
    var icon: String?


    //region your codes 2

	//endregion your codes 2

}