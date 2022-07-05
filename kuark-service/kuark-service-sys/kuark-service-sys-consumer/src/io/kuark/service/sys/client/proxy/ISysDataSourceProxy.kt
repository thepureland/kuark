package io.kuark.service.sys.client.proxy

import io.kuark.service.sys.common.api.ISysDataSourceApi
import io.kuark.service.sys.client.fallback.SysDataSourceFallback
import io.kuark.service.sys.common.vo.datasource.SysDataSourceCacheItem
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping


/**
 * 数据源客户端代理接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
@FeignClient(name = "sys-datasource", fallback = SysDataSourceFallback::class)
interface ISysDataSourceProxy : ISysDataSourceApi {
//endregion your codes 1

    //region your codes 2

    @GetMapping("/sys/dataSource/api/getDataSource")
    override fun getDataSource(subSysDictCode: String, tenantId: String?): SysDataSourceCacheItem?

    //endregion your codes 2

}