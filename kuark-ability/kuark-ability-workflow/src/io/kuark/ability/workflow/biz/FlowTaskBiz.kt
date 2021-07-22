package io.kuark.ability.workflow.biz

import io.kuark.ability.workflow.ibiz.IFlowTaskBiz
import org.activiti.engine.RuntimeService
import org.springframework.beans.factory.annotation.Autowired

/**
 * 流程任务相关业务
 *
 * @author K
 * @since 1.0.0
 */
open class FlowTaskBiz: IFlowTaskBiz {

    @Autowired
    private lateinit var taskService: RuntimeService

}