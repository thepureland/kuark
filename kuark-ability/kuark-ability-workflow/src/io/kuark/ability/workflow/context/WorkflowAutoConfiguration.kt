package io.kuark.ability.workflow.context

import io.kuark.ability.workflow.definition.FlowDefinitionBiz
import io.kuark.ability.workflow.definition.IFlowDefinitionBiz
import io.kuark.ability.workflow.event.GlobalFlowEventListener
import io.kuark.ability.workflow.instance.FlowInstanceBiz
import io.kuark.ability.workflow.instance.IFlowInstanceBiz
import io.kuark.ability.workflow.task.FlowTaskBiz
import io.kuark.ability.workflow.task.IFlowTaskBiz
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * 工作流自动配置
 *
 * @author K
 * @since 1.0.0
 */
@Configuration
open class WorkflowAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    open fun flowDefinitionBiz(): IFlowDefinitionBiz = FlowDefinitionBiz()

    @Bean
    @ConditionalOnMissingBean
    open fun flowInstanceBiz(): IFlowInstanceBiz = FlowInstanceBiz()

    @Bean
    @ConditionalOnMissingBean
    open fun flowTaskBiz(): IFlowTaskBiz = FlowTaskBiz()

    @Bean
    @ConditionalOnMissingBean
    open fun globalFlowEventListener(): GlobalFlowEventListener {
        // 要注册全局事件，可提供同名的bean，并加@Primary注解
        // 通过向构建器传入全局事件监听器的map
        return GlobalFlowEventListener(emptyMap())
    }

    @Bean
    @ConditionalOnMissingBean
    open fun workflowConfigurationConfigurer() = WorkflowConfigurationConfigurer()

}