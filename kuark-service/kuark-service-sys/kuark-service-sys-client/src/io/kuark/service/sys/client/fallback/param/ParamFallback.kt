package io.kuark.service.sys.client.fallback.param

import io.kuark.service.sys.client.proxy.param.IParamClient
import io.kuark.service.sys.client.proxy.reg.IDictClient
import io.kuark.service.sys.common.vo.reg.dict.RegDictItemRecord
import io.kuark.service.sys.common.vo.reg.param.RegParamRecord
import org.springframework.stereotype.Component

@Component
class ParamFallback: IParamClient {

    override fun getParam(module: String, paramName: String): RegParamRecord? {
        TODO("Not yet implemented")
    }

}