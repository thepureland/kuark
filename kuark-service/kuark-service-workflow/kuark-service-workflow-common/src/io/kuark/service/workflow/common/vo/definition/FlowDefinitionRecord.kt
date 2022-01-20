package io.kuark.service.workflow.common.vo.definition

import io.kuark.base.support.result.IJsonResult
import java.util.*

class FlowDefinitionRecord : IJsonResult {

    /** 流程定义key(bpmn文件中process元素的id) */
    var key: String? = null

    /** 流程定义名称 */
    var name: String? = null

    /** 流程定义的版本 */
    var version: Int? = null

    /** 分类 */
    var category: String? = null

    /** 是否已部署 */
    var deployed: Boolean? = null

    /** 部署时间 */
    var deploymentTime: Date? = null

    /** 创建时间 */
    var createTime: Date? = null

    /** 最近更新时间 */
    var lastUpdateTime: Date? = null

    /** 租户(所属系统)id */
    var tenantId: String? = null

    /** 是否被挂起 */
    var suspend: Boolean? = null

}