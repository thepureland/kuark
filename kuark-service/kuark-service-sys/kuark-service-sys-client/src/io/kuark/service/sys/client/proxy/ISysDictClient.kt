package io.kuark.service.sys.client.proxy

import io.kuark.service.sys.client.fallback.SysDictFallback
import io.kuark.service.sys.common.api.ISysDictApi
import io.kuark.service.sys.common.vo.dict.SysDictItemRecord
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "sys-dict", fallback = SysDictFallback::class)
interface ISysDictClient: ISysDictApi {

    @GetMapping("/sys/dictItem/getDictItems")
    override fun getDictItems(module: String, type: String): List<SysDictItemRecord>

    @GetMapping("/sys/dictItem/getDictItemMap")
    override fun getDictItemMap(module: String, type: String): LinkedHashMap<String, String>

}