package io.kuark.service.sys.client.proxy

import io.kuark.service.sys.client.fallback.SysDictFallback
import io.kuark.service.sys.common.api.ISysParamApi
import io.kuark.service.sys.common.vo.dict.SysParamCacheItem
import io.kuark.service.sys.common.vo.param.SysParamDetail
import io.kuark.service.sys.common.vo.param.SysParamRecord
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "sys-param", fallback = SysDictFallback::class)
interface ISysParamProxy: ISysParamApi {

    @GetMapping("/sys/param/getParam")
    override fun getParam(module: String, paramName: String): SysParamCacheItem?

}