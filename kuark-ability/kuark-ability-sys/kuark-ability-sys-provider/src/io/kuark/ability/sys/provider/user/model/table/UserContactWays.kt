package io.kuark.ability.sys.provider.user.model.table

import io.kuark.ability.data.rdb.support.MaintainableTable
import io.kuark.ability.sys.provider.user.model.po.UserContactWay
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * 用户联系方式数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object UserContactWays: MaintainableTable<io.kuark.ability.sys.provider.user.model.po.UserContactWay>("user_contact_way") {
//endregion your codes 1

    /** 外键，用户账号id，user_account表主键 */
    var userId = varchar("user_id").bindTo { it.userId }

    /** 联系方式代码 */
    var contactWayDictCode = varchar("contact_way_dict_code").bindTo { it.contactWayDictCode }

    /** 联系方式值 */
    var contactWayValue = varchar("contact_way_value").bindTo { it.contactWayValue }

    /** 联系方式状态代码 */
    var contactWayStatusDictCode = varchar("contact_way_status_dict_code").bindTo { it.contactWayStatusDictCode }

    /** 优先级 */
    var priority = int("priority").bindTo { it.priority }


    //region your codes 2

	//endregion your codes 2

}