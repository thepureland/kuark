package io.kuark.ability.workflow.provider.context

import io.kuark.ability.workflow.provider.event.GlobalFlowEventListener
import org.activiti.spring.SpringProcessEngineConfiguration
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer
import org.springframework.beans.factory.annotation.Autowired


/**
 * 工作流配置配置器，可以做一些SpringProcessEngineConfiguration的额外配置，它将在引擎配置创建完并且所有默认值都被设置后被调用。
 *
 * @author K
 * @since 1.0.0
 */
open class WorkflowConfigurationConfigurer: ProcessEngineConfigurationConfigurer {

    @Autowired
    private lateinit var globalFlowEventListener: GlobalFlowEventListener

    override fun configure(processEngineConfiguration: SpringProcessEngineConfiguration) {
        processEngineConfiguration.eventListeners = listOf(globalFlowEventListener)
    }

}