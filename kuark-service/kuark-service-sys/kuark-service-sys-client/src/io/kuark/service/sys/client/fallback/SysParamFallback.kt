package io.kuark.service.sys.client.fallback

import io.kuark.service.sys.client.proxy.ISysParamProxy
import io.kuark.service.sys.common.vo.param.SysParamRecord
import org.springframework.stereotype.Component

@Component
class SysParamFallback: ISysParamProxy {

    override fun getParam(module: String, paramName: String): SysParamRecord? {
        TODO("Not yet implemented")
    }

}