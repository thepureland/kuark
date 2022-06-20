package io.kuark.service.sys.client.proxy

import io.kuark.service.sys.client.fallback.SysDictFallback
import io.kuark.service.sys.common.api.ISysDictApi
import io.kuark.service.sys.common.vo.dict.DictModuleAndTypePayload
import io.kuark.service.sys.common.vo.dict.SysDictItemCacheItem
import io.kuark.service.sys.common.vo.dict.SysDictItemRecord
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "sys-dict", fallback = SysDictFallback::class)
interface ISysDictProxy: ISysDictApi {

    @GetMapping("/sys/dict/api/getDictItems")
    override fun getDictItems(payload: DictModuleAndTypePayload): List<SysDictItemCacheItem>

    @GetMapping("/sys/dict/api/getDictItemMap")
    override fun getDictItemMap(payload: DictModuleAndTypePayload): LinkedHashMap<String, String>

    @PostMapping("/sys/dict/api/batchGetDictItems")
    override fun batchGetDictItems(payloads: List<DictModuleAndTypePayload>): Map<Pair<String, String>, List<SysDictItemCacheItem>>

    @PostMapping("/sys/dict/api/batchGetDictItemMap")
    override fun batchGetDictItemMap(payloads: List<DictModuleAndTypePayload>): Map<Pair<String, String>, LinkedHashMap<String, String>>

}