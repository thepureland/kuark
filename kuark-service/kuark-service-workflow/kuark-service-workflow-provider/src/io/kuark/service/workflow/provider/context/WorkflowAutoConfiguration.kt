package io.kuark.service.workflow.provider.context

import io.kuark.service.workflow.provider.biz.FlowDefinitionBiz
import io.kuark.service.workflow.provider.biz.FlowInstanceBiz
import io.kuark.service.workflow.provider.biz.FlowTaskBiz
import io.kuark.service.workflow.provider.event.GlobalFlowEventListener
import io.kuark.service.workflow.provider.ibiz.IFlowDefinitionBiz
import io.kuark.service.workflow.provider.ibiz.IFlowInstanceBiz
import io.kuark.service.workflow.provider.ibiz.IFlowTaskBiz
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
    open fun flowDefinitionBiz(): io.kuark.service.workflow.provider.ibiz.IFlowDefinitionBiz =
        io.kuark.service.workflow.provider.biz.FlowDefinitionBiz()

    @Bean
    @ConditionalOnMissingBean
    open fun flowInstanceBiz(): io.kuark.service.workflow.provider.ibiz.IFlowInstanceBiz =
        io.kuark.service.workflow.provider.biz.FlowInstanceBiz()

    @Bean
    @ConditionalOnMissingBean
    open fun flowTaskBiz(): io.kuark.service.workflow.provider.ibiz.IFlowTaskBiz =
        io.kuark.service.workflow.provider.biz.FlowTaskBiz()

    @Bean
    @ConditionalOnMissingBean
    open fun globalFlowEventListener(): io.kuark.service.workflow.provider.event.GlobalFlowEventListener {
        // 要注册全局事件，可提供同名的bean，并加@Primary注解
        // 通过向构建器传入全局事件监听器的map
        return io.kuark.service.workflow.provider.event.GlobalFlowEventListener(emptyMap())
    }

    @Bean
    @ConditionalOnMissingBean
    open fun workflowConfigurationConfigurer() =
        io.kuark.service.workflow.provider.context.WorkflowConfigurationConfigurer()

}