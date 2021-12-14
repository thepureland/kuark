package io.kuark.service.msg.provider.dao

import io.kuark.ability.data.rdb.support.BaseCrudDao
import io.kuark.service.msg.provider.model.po.MsgInstance
import io.kuark.service.msg.provider.model.table.MsgInstances
import org.springframework.stereotype.Repository

/**
 * 消息实例数据访问对象
 *
 * @author K
 * @since 1.0.0
 */
@Repository
//region your codes 1
open class MsgInstanceDao : BaseCrudDao<String, MsgInstance, MsgInstances>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}