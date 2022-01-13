package io.kuark.service.sys.client.proxy.param

import io.kuark.service.sys.client.fallback.reg.DictFallback
import io.kuark.service.sys.common.api.reg.IDictApi
import io.kuark.service.sys.common.api.reg.IParamApi
import io.kuark.service.sys.common.vo.reg.dict.RegDictItemRecord
import io.kuark.service.sys.common.vo.reg.param.RegParamRecord
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "reg-param", fallback = DictFallback::class)
interface IParamClient: IParamApi {

    @GetMapping("/reg/param/getParam")
    override fun getParam(module: String, paramName: String): RegParamRecord?

}