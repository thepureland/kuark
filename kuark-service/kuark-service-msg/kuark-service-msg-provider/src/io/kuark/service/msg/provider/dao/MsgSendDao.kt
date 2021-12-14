package io.kuark.service.msg.provider.dao

import io.kuark.ability.data.rdb.support.BaseCrudDao
import io.kuark.service.msg.provider.model.po.MsgSend
import io.kuark.service.msg.provider.model.table.MsgSends
import org.springframework.stereotype.Repository

/**
 * 消息发送数据访问对象
 *
 * @author K
 * @since 1.0.0
 */
@Repository
//region your codes 1
open class MsgSendDao : BaseCrudDao<String, MsgSend, MsgSends>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}