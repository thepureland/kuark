package io.kuark.service.workflow.common.vo.definition

import io.kuark.base.query.enums.Operator
import io.kuark.base.support.payload.ListSearchPayload

class FlowDefinitionSearchPayload: ListSearchPayload() {

    override var operators: Map<String, Operator>? = mapOf(
        this::key.name to Operator.ILIKE,
        this::name.name to Operator.ILIKE
    )

    /** 流程定义key(bpmn文件中process元素的id) */
    var key: String? = null

    /** 流程定义名称 */
    var name: String? = null

    /** 流程定义的版本 */
    var version: Int? = null

    /** 分类 */
    var category: String? = null

    /** 租户(所属系统)id */
    var tenantId: String? = null

    /** 是否已部署 */
    var deployed: Boolean? = null

    /** 是否只查询最新版本的 */
    var latestOnly: Boolean? = null

}