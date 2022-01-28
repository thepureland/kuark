package io.kuark.service.msg.provider.model.table

import io.kuark.ability.data.rdb.support.StringIdTable
import io.kuark.service.msg.provider.model.po.MsgSiteMsgReceive
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar

/**
 * 消息接收数据库表-实体关联对象
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

    /** 租户id */
    var tenantId = varchar("tenant_id").bindTo { it.tenantId }


    //region your codes 2

	//endregion your codes 2

}