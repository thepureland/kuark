package io.kuark.service.sys.provider.context

import io.kuark.ability.web.springmvc.IContextInitFilter
import io.kuark.base.log.LogFactory
import io.kuark.context.core.KuarkContext
import io.kuark.context.core.KuarkContextHolder
import io.kuark.service.sys.provider.biz.ibiz.ISysDataSourceBiz
import io.kuark.service.sys.provider.biz.ibiz.ISysDomainBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest


/**
 * 上下文初始化web过滤器
 *
 * @author K
 * @since 1.0.0
 */
@Component
open class ContextInitFilter : IContextInitFilter {

    @Autowired
    private lateinit var sysDomainBiz: ISysDomainBiz

    @Autowired
    private lateinit var dataSourceBiz: ISysDataSourceBiz

    private val log = LogFactory.getLog(this::class)

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val builder = KuarkContext.Builder()

        val domainName = request.serverName
        // 域名 => 子系统和租户
        val domain = sysDomainBiz.getDomainByName(domainName)
        if (domain != null) {
            builder.subSysCode(domain.subSysDictCode)
            builder.tenantId(domain.tenantId)

            // 子系统和租户 => 数据源
            val dataSource = dataSourceBiz.getDataSource(domain.subSysDictCode!!, domain.tenantId)
            if (dataSource != null) {
                builder.dataSourceId(dataSource.id)
            }
        }

        // session
        val session = (request as HttpServletRequest).session
        session.attributeNames.asIterator().forEach { name ->
            val value = session.getAttribute(name)
            builder.addSessionAttributes(Pair(name, value))
        }

        // cookie
        request.cookies?.forEach { cookie ->
            builder.addCookieAttributes(Pair(cookie.name, cookie.value))
        }

        // header
        request.headerNames.asIterator().forEach { name ->
            val value = request.getHeader(name)
            builder.addHeaderAttributes(Pair(name, value))
        }

        // client info


        val context = builder.build()
        KuarkContextHolder.set(context)
        chain.doFilter(request, response)
    }

}