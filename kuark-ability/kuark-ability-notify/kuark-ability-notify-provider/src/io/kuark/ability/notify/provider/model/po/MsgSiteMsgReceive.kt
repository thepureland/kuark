package io.kuark.ability.notify.provider.model.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IDbEntity
import java.time.LocalDateTime

/**
 * 消息接收数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface MsgSiteMsgReceive : IDbEntity<String, MsgSiteMsgReceive> {
//endregion your codes 1

    companion object : DbEntityFactory<MsgSiteMsgReceive>()

    /** 接收者id */
    var receiverId: String

    /** 发送id */
    var sendId: String

    /** 接收状态代码 */
    var receiveStatusDictCode: String

    /** 创建时间 */
    var createTime: LocalDateTime

    /** 更新时间 */
    var updateTime: LocalDateTime?

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId: String?


    //region your codes 2

    //endregion your codes 2

}