package io.kuark.service.sys.client.proxy.reg

import io.kuark.service.sys.client.fallback.reg.DictFallback
import io.kuark.service.sys.common.api.reg.IDictApi
import io.kuark.service.sys.common.vo.reg.dict.RegDictItemRecord
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "reg-dict", fallback = DictFallback::class)
interface IDictClient: IDictApi {

    @GetMapping("/reg/dictItem/getDictItems")
    override fun getDictItems(module: String, type: String): List<RegDictItemRecord>

    @GetMapping("/reg/dictItem/getDictItemMap")
    override fun getDictItemMap(module: String, type: String): LinkedHashMap<String, String>

}