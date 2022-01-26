package io.kuark.service.user.provider.user.model.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IMaintainableDbEntity

/**
 * 组织机构数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface UserOrganization : IMaintainableDbEntity<String, UserOrganization> {
//endregion your codes 1

    companion object : DbEntityFactory<UserOrganization>()

    /** 名称 */
    var name: String

    /** 简称 */
    var abbrName: String?

    /** 组织类型 */
    var orgTypeDictCode: String?


    /** 父id */
    var parentId: String?

    /** 在同父节点下的排序号 */
    var seqNo: Int?

    /** 子系统代码 */
    var subSysDictCode: String?

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId: String?


    //region your codes 2

    //endregion your codes 2

}