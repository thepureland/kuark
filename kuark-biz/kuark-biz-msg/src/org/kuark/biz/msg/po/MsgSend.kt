package org.kuark.biz.msg.po

import org.kuark.data.jdbc.support.IDbEntity
import java.time.LocalDateTime

/**
 * 消息发送数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface MsgSend: IDbEntity<String, MsgSend> {
//endregion your codes 1

	/** 接收者群组类型代码 */
	var receiverGroupTypeDictCode: String
	/** 接收者群组id */
	var receiverGroupId: String
	/** 消息实例id */
	var instanceId: String
	/** 消息类型代码 */
	var msgTypeDictCode: String
	/** 国家-语言代码 */
	var localeDictCode: String
	/** 发送状态代码 */
	var sendStatusDictCode: String
	/** 创建时间 */
	var createTime: LocalDateTime
	/** 更新时间 */
	var updateTime: LocalDateTime
	/** 发送成功数量 */
	var successCount: Int
	/** 发送失败数量 */
	var failCount: Int
	/** 定时任务id */
	var jobId: String
	/** 所有者id，依业务可以是店铺id、站点id、商户id等 */
	var ownerId: String

	//region your codes 2

	//endregion your codes 2

}