package io.kuark.service.sys.provider.api.local

import io.kuark.service.sys.common.api.ISysDataSourceApi
import io.kuark.service.sys.common.vo.datasource.SysDataSourceCacheItem
import io.kuark.service.sys.provider.biz.ibiz.ISysDataSourceBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


/**
 * 数据源 API本地实现
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
@Service
open class SysDataSourceApi : ISysDataSourceApi {
//endregion your codes 1

    //region your codes 2

    @Autowired
    private lateinit var sysDataSourceBiz: ISysDataSourceBiz

    override fun getDataSource(subSysDictCode: String, tenantId: String?): SysDataSourceCacheItem? {
        return sysDataSourceBiz.getDataSource(subSysDictCode, tenantId)
    }

    //endregion your codes 2

}