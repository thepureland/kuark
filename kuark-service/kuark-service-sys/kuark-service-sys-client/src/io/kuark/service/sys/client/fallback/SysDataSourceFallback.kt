package io.kuark.service.sys.client.fallback

import io.kuark.service.sys.client.proxy.ISysDataSourceProxy
import io.kuark.service.sys.common.vo.datasource.SysDataSourceCacheItem
import org.springframework.stereotype.Component


/**
 * 数据源容错处理
 *
 * @author K
 * @since 1.0.0
 */
@Component
//region your codes 1
interface SysDataSourceFallback : ISysDataSourceProxy {
//endregion your codes 1

    //region your codes 2

    override fun getDataSource(subSysDictCode: String, tenantId: String?): SysDataSourceCacheItem? {
        TODO("Not yet implemented")
    }

    //endregion your codes 2

}