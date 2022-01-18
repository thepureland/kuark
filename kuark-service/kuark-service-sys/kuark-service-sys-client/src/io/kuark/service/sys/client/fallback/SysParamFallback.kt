package io.kuark.service.sys.client.fallback

import io.kuark.service.sys.client.proxy.ISysParamClient
import io.kuark.service.sys.common.vo.param.SysParamRecord
import org.springframework.stereotype.Component

@Component
class SysParamFallback: ISysParamClient {

    override fun getParam(module: String, paramName: String): SysParamRecord? {
        TODO("Not yet implemented")
    }

}