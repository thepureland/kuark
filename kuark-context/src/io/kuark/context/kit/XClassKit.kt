package io.kuark.context.kit

import io.kuark.base.lang.ClassKit
import io.kuark.base.log.LogFactory
import org.springframework.aop.framework.AdvisedSupport
import org.springframework.aop.framework.AopProxy
import org.springframework.aop.support.AopUtils
import java.lang.reflect.Field

/**
 * XClass的扩展函数
 *
 * @author K
 * @since 1.0.0
 */

private val LOG = LogFactory.getLog(ClassKit::class)

/**
 * 取得JDK动态代理/CGLIB代理对象
 *
 * @param proxy JDK动态代理/CGLIB代理对象
 * @return 代理对象的真实类型, 如果传入的对象为null将返回null
 * @since 1.0.0
 */
fun ClassKit.getTargetClass(proxy: Any?): Class<*>? {
    if (proxy == null) {
        return null
    }
    if (!AopUtils.isAopProxy(proxy)) {
        return proxy.javaClass //不是代理对象
    }
    if (AopUtils.isJdkDynamicProxy(proxy)) {
        try {
            return getJdkDynamicProxyTargetObject(proxy).javaClass
        } catch (e: Exception) {
            LOG.error(e, "获取jdk动态代理对象出错！")
        }
    } else { //cglib
        try {
            return getCglibProxyTargetObject(proxy).javaClass
        } catch (e: Exception) {
            LOG.error(e, "获取CGLIB代理对象出错！")
        }
    }
    return null
}

private fun getCglibProxyTargetObject(proxy: Any): Any {
    val h = proxy.javaClass.getDeclaredField("CGLIB\$CALLBACK_0")
    h.isAccessible = true
    val dynamicAdvisedInterceptor = h[proxy]
    val advised = dynamicAdvisedInterceptor.javaClass.getDeclaredField("advised")
    advised.isAccessible = true
    return (advised[dynamicAdvisedInterceptor] as AdvisedSupport).targetSource.target!!
}

private fun getJdkDynamicProxyTargetObject(proxy: Any): Any {
    val h = proxy.javaClass.superclass.getDeclaredField("h")
    h.isAccessible = true
    val aopProxy: AopProxy = h[proxy] as AopProxy
    val advised: Field = aopProxy.javaClass.getDeclaredField("advised")
    advised.isAccessible = true
    return (advised[aopProxy] as AdvisedSupport).targetSource.target!!
}