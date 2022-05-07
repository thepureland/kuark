package io.kuark.context.core


/**
 * 上下文初始化器
 *
 * @author K
 * @since 1.0.0
 */
interface IContextInitializer {

    fun init(context: KuarkContext)

}