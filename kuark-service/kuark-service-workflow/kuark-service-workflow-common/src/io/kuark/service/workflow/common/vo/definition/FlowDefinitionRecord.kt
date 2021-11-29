package io.kuark.service.workflow.common.vo.definition

import java.util.*

class FlowDefinitionRecord {

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

    /** 租户(所属系统)id */
    var tenantId: String? = null

    /** 是否被挂起 */
    var suspend: Boolean? = null

}