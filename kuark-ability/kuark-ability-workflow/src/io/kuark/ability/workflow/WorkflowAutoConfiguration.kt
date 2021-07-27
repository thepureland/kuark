package io.kuark.ability.workflow

import io.kuark.ability.workflow.biz.FlowDefinitionBiz
import io.kuark.ability.workflow.biz.FlowInstanceBiz
import io.kuark.ability.workflow.biz.FlowTaskBiz
import io.kuark.ability.workflow.ibiz.IFlowDefinitionBiz
import io.kuark.ability.workflow.ibiz.IFlowInstanceBiz
import io.kuark.ability.workflow.ibiz.IFlowTaskBiz
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

}