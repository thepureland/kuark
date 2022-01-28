package io.kuark.service.msg.provider.model.table

import io.kuark.ability.data.rdb.support.StringIdTable
import io.kuark.service.msg.provider.model.po.MsgTemplate
import org.ktorm.schema.boolean
import org.ktorm.schema.varchar

/**
 * 消息模板数据库表-实体关联对象
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
    var defaultActive = boolean("default_active").bindTo { it.defaultActive }

    /** 模板标题默认值 */
    var defaultTitle = varchar("default_title").bindTo { it.defaultTitle }

    /** 模板内容默认值 */
    var defaultContent = varchar("default_content").bindTo { it.defaultContent }

    /** 租户id */
    var tenantId = varchar("tenant_id").bindTo { it.tenantId }


    //region your codes 2

	//endregion your codes 2

}