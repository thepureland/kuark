package org.kuark.biz.msg.po

import org.kuark.data.jdbc.support.IMaintainableDbEntity

/**
 * 消息接收者群组数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface MsgReceiverGroup: IMaintainableDbEntity<String, MsgReceiverGroup> {
//endregion your codes 1

	/** 接收者群组类型代码 */
	var receiverGroupTypeDictCode: String
	/** 群组定义的表 */
	var defineTable: String
	/** 群组名称在具体群组表中的字段名 */
	var nameColumn: String

	//region your codes 2

	//endregion your codes 2

}