package io.kuark.ability.notify.provider.model.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IDbEntity
import java.time.LocalDateTime

/**
 * 消息实例数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface MsgInstance : IDbEntity<String, MsgInstance> {
//endregion your codes 1

    companion object : DbEntityFactory<MsgInstance>()

    /** 国家-语言代码 */
    var localeDictCode: String?

    /** 标题，可能还含有用户名等实际要发送时才能确定的模板变量 */
    var title: String?

    /** 通知内容，可能还含有用户名等实际要发送时才能确定的模板变量 */
    var content: String?

    /** 消息模板id，为null时表示没有依赖静态模板，可能是依赖动态模板或无模板 */
    var templateId: String?

    /** 发送类型代码 */
    var sendTypeDictCode: String?

    /** 事件类型代码 */
    var eventTypeDictCode: String?

    /** 消息类型代码 */
    var msgTypeDictCode: String?

    /** 有效期起 */
    var validTimeStart: LocalDateTime

    /** 有效期止 */
    var validTimeEnd: LocalDateTime

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId: String?


    //region your codes 2

    //endregion your codes 2

}