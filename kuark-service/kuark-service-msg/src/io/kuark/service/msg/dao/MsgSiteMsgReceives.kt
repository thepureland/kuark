package io.kuark.service.msg.dao

import io.kuark.ability.data.jdbc.support.StringIdTable
import io.kuark.service.msg.po.MsgSiteMsgReceive
import me.liuwj.ktorm.schema.datetime
import me.liuwj.ktorm.schema.varchar

/**
 * 消息接收数据库实体DAO
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object MsgSiteMsgReceives: StringIdTable<MsgSiteMsgReceive>("msg_site_msg_receive") {
//endregion your codes 1

    /** 接收者id */
    var receiverId = varchar("receiver_id").bindTo { it.receiverId }

    /** 发送id */
    var sendId = varchar("send_id").bindTo { it.sendId }

    /** 接收状态代码 */
    var receiveStatusDictCode = varchar("receive_status_dict_code").bindTo { it.receiveStatusDictCode }

    /** 创建时间 */
    var createTime = datetime("create_time").bindTo { it.createTime }

    /** 更新时间 */
    var updateTime = datetime("update_time").bindTo { it.updateTime }

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId = varchar("owner_id").bindTo { it.ownerId }


    //region your codes 2

	//endregion your codes 2

}