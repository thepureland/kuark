package io.kuark.service.sys.provider.api.local

import io.kuark.service.sys.common.api.ISysParamApi
import io.kuark.service.sys.common.vo.dict.SysParamCacheItem
import io.kuark.service.sys.provider.biz.ibiz.ISysParamBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class SysParamApi : ISysParamApi {

    @Autowired
    private lateinit var sysParamBiz: ISysParamBiz

    override fun getParam(module: String, paramName: String): SysParamCacheItem? {
        return sysParamBiz.getParamFromCache(module, paramName)
    }

}