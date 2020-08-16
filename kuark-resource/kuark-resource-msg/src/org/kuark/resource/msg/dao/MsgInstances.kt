package org.kuark.resource.msg.dao

import me.liuwj.ktorm.schema.*
import org.kuark.resource.msg.po.MsgInstance
import org.kuark.data.jdbc.support.StringIdTable

/**
 * 消息实例数据库实体DAO
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object MsgInstances: StringIdTable<MsgInstance>("msg_instance") {
//endregion your codes 1

    /** 国家-语言代码 */
    var localeDictCode = varchar("locale_dict_code").bindTo { it.localeDictCode }

    /** 标题，可能还含有用户名等实际要发送时才能确定的模板变量 */
    var title = varchar("title").bindTo { it.title }

    /** 通知内容，可能还含有用户名等实际要发送时才能确定的模板变量 */
    var content = varchar("content").bindTo { it.content }

    /** 消息模板id，为null时表示没有依赖静态模板，可能是依赖动态模板或无模板 */
    var templateId = varchar("template_id").bindTo { it.templateId }

    /** 发送类型代码 */
    var sendTypeDictCode = varchar("send_type_dict_code").bindTo { it.sendTypeDictCode }

    /** 事件类型代码 */
    var eventTypeDictCode = varchar("event_type_dict_code").bindTo { it.eventTypeDictCode }

    /** 消息类型代码 */
    var msgTypeDictCode = varchar("msg_type_dict_code").bindTo { it.msgTypeDictCode }

    /** 有效期起 */
    var validTimeStart = datetime("valid_time_start").bindTo { it.validTimeStart }

    /** 有效期止 */
    var validTimeEnd = datetime("valid_time_end").bindTo { it.validTimeEnd }

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId = varchar("owner_id").bindTo { it.ownerId }


    //region your codes 2

	//endregion your codes 2

}