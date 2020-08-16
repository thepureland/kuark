package org.kuark.resource.msg.dao

import me.liuwj.ktorm.schema.*
import org.kuark.resource.msg.po.MsgTemplate
import org.kuark.data.jdbc.support.StringIdTable

/**
 * 消息模板数据库实体DAO
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object MsgTemplates: StringIdTable<MsgTemplate>("msg_template") {
//endregion your codes 1

    /**  */
    var sendTypeDictCode = varchar("send_type_dict_code").bindTo { it.sendTypeDictCode }

    /** 事件类型代码。send_type_dict_code为auto时，字典类型为auto_event_type;为manual时，则为manual_event_type */
    var eventTypeDictCode = varchar("event_type_dict_code").bindTo { it.eventTypeDictCode }

    /** 消息类型代码 */
    var msgTypeDictCode = varchar("msg_type_dict_code").bindTo { it.msgTypeDictCode }

    /** 模板分组编码,uuid,用于区分同一事件下不同操作原因的多套模板 */
    var groupCode = varchar("group_code").bindTo { it.groupCode }

    /** 国家-语言代码 */
    var localeDictCode = varchar("locale_dict_code").bindTo { it.localeDictCode }

    /** 模板标题 */
    var title = varchar("title").bindTo { it.title }

    /** 模板内容 */
    var content = varchar("content").bindTo { it.content }

    /** 是否启用默认值 */
    var isDefaultActive = boolean("is_default_active").bindTo { it.isDefaultActive }

    /** 模板标题默认值 */
    var defaultTitle = varchar("default_title").bindTo { it.defaultTitle }

    /** 模板内容默认值 */
    var defaultContent = varchar("default_content").bindTo { it.defaultContent }

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId = varchar("owner_id").bindTo { it.ownerId }


    //region your codes 2

	//endregion your codes 2

}