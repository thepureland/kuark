package io.kuark.service.user.provider.user.model.table

import org.ktorm.schema.*
import io.kuark.service.user.provider.user.model.po.UserOrganization
import io.kuark.ability.data.rdb.support.MaintainableTable

/**
 * 组织机构数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object UserOrganizations : MaintainableTable<UserOrganization>("user_organization") {
//endregion your codes 1

    /** 名称 */
    var name = varchar("name").bindTo { it.name }

    /** 简称 */
    var abbrName = varchar("abbr_name").bindTo { it.abbrName }

    /** 组织类型 */
    var orgTypeDictCode = varchar("org_type_dict_code").bindTo { it.orgTypeDictCode }

    /** 父id */
    var parentId = varchar("parent_id").bindTo { it.parentId }

    /** 在同父节点下的排序号 */
    var seqNo = int("seq_no").bindTo { it.seqNo }

    /** 子系统代码 */
    var subSysDictCode = varchar("sub_sys_dict_code").bindTo { it.subSysDictCode }

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId = varchar("owner_id").bindTo { it.ownerId }


    //region your codes 2

    //endregion your codes 2

}