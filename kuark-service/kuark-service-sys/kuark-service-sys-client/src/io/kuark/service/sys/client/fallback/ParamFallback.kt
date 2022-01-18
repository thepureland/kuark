package io.kuark.service.sys.client.fallback

import io.kuark.service.sys.client.proxy.IParamClient
import org.springframework.stereotype.Component

@Component
class ParamFallback: IParamClient {

    override fun getParam(module: String, paramName: String): io.kuark.service.sys.common.vo.param.SysParamRecord? {
        TODO("Not yet implemented")
    }

}