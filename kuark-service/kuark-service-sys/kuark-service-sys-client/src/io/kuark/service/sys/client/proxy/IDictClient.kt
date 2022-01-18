package io.kuark.service.sys.client.proxy

import io.kuark.service.sys.client.fallback.DictFallback
import io.kuark.service.sys.common.api.IDictApi
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "reg-dict", fallback = DictFallback::class)
interface IDictClient: IDictApi {

    @GetMapping("/reg/dictItem/getDictItems")
    override fun getDictItems(module: String, type: String): List<io.kuark.service.sys.common.vo.dict.SysDictItemRecord>

    @GetMapping("/reg/dictItem/getDictItemMap")
    override fun getDictItemMap(module: String, type: String): LinkedHashMap<String, String>

}