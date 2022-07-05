package io.kuark.service.sys.provider.api.backend

import io.kuark.service.sys.common.api.ISysDataSourceApi
import io.kuark.service.sys.common.api.ISysDictApi
import io.kuark.service.sys.common.vo.datasource.SysDataSourceCacheItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping


/**
 * 数据源后端控制器
 *
 * @author K
 * @since 1.0.0
 */
@RestController
//region your codes 1
@RequestMapping("/sys/dataSource/api")
open class SysDataSourceApiController {
//endregion your codes 1

    //region your codes 2

    @Autowired
    private lateinit var sysDataSourceApi: ISysDataSourceApi

    @GetMapping("/getDataSource")
    fun getDataSource(subSysDictCode: String, tenantId: String?): SysDataSourceCacheItem? {
        return sysDataSourceApi.getDataSource(subSysDictCode, tenantId)
    }

    //endregion your codes 2

}