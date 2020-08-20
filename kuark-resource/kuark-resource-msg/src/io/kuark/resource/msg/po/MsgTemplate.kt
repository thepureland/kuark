package io.kuark.resource.msg.po

import io.kuark.data.jdbc.support.DbEntityFactory
import io.kuark.data.jdbc.support.IDbEntity

/**
 * 消息模板数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface MsgTemplate: IDbEntity<String, MsgTemplate> {
//endregion your codes 1

    companion object : DbEntityFactory<MsgTemplate>()

    /**  */
    var sendTypeDictCode: String

    /** 事件类型代码。send_type_dict_code为auto时，字典类型为auto_event_type;为manual时，则为manual_event_type */
    var eventTypeDictCode: String

    /** 消息类型代码 */
    var msgTypeDictCode: String

    /** 模板分组编码,uuid,用于区分同一事件下不同操作原因的多套模板 */
    var groupCode: String

    /** 国家-语言代码 */
    var localeDictCode: String

    /** 模板标题 */
    var title: String

    /** 模板内容 */
    var content: String

    /** 是否启用默认值 */
    var isDefaultActive: Boolean

    /** 模板标题默认值 */
    var defaultTitle: String

    /** 模板内容默认值 */
    var defaultContent: String

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId: String


    //region your codes 2

	//endregion your codes 2

}