package io.kuark.ability.sys.client.proxy.reg

import io.kuark.ability.sys.client.fallback.reg.DictFallback
import io.kuark.ability.sys.common.api.reg.IDictApi
import io.kuark.ability.sys.common.vo.reg.dict.RegDictItemRecord
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "reg-dict", fallback = DictFallback::class)
interface IDictClient: IDictApi {

    @GetMapping("/regDictItem/getDictItems")
    override fun getDictItems(module: String, type: String): List<RegDictItemRecord>

    @GetMapping("/regDictItem/getDictItemMap")
    override fun getDictItemMap(module: String, type: String): LinkedHashMap<String, String>

}