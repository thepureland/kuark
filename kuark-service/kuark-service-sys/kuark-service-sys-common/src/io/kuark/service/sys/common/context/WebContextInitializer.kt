package io.kuark.service.sys.common.context

import io.kuark.context.core.IContextInitializer
import io.kuark.context.core.KuarkContext
import io.kuark.service.sys.common.api.ISysDataSourceApi
import io.kuark.service.sys.common.api.ISysDomainApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


/**
 * 上下文初始化web过滤器
 *
 * @author K
 * @since 1.0.0
 */
@Component
open class WebContextInitializer : IContextInitializer {

    @Autowired
    private lateinit var sysDomainApi: ISysDomainApi

    @Autowired
    private lateinit var dataSourceApi: ISysDataSourceApi

    override fun init(builder: KuarkContext.Builder, context: KuarkContext) {
        // 域名 => 子系统和租户
        val domainName = context.clientInfo?.domain!!
        val domain = sysDomainApi.getDomainByName(domainName)
        if (domain != null) {
            builder.subSysCode(domain.subSysDictCode)
            builder.tenantId(domain.tenantId)

            // 子系统和租户 => 数据源
            val dataSource = dataSourceApi.getDataSource(domain.subSysDictCode!!, domain.tenantId)
            if (dataSource != null) {
                builder.dataSourceId(dataSource.id)
            }
        }
    }

}