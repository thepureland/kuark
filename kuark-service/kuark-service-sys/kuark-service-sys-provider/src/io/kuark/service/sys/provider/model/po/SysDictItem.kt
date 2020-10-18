package io.kuark.service.sys.provider.model.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IMaintainableDbEntity

/**
 * 字典项数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface SysDictItem: IMaintainableDbEntity<String, SysDictItem> {
//endregion your codes 1

    companion object : DbEntityFactory<SysDictItem>()

    /** 外键，sys_dict表的主键 */
    var dictId: String

    /** 字典项编号 */
    var itemCode: String

    /** 父项编号 */
    var parentCode: String?

    /** 字典项名称，或其国际化key */
    var itemName: String

    /** 该字典编号在同父节点下的排序号 */
    var seqNo: Int?


    //region your codes 2

	//endregion your codes 2

}