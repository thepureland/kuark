package io.kuark.service.sys.client.proxy

import io.kuark.service.sys.client.fallback.DictFallback
import io.kuark.service.sys.common.api.IParamApi
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "reg-param", fallback = DictFallback::class)
interface IParamClient: IParamApi {

    @GetMapping("/reg/param/getParam")
    override fun getParam(module: String, paramName: String): io.kuark.service.sys.common.vo.param.SysParamRecord?

}