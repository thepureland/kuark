package io.kuark.service.sys.provider.api.backend

import io.kuark.service.sys.common.api.ISysDictApi
import io.kuark.service.sys.common.vo.dict.DictModuleAndTypePayload
import io.kuark.service.sys.common.vo.dict.SysDictItemRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sys/dict/api")
class SysDictApiController {

    @Autowired
    private lateinit var sysDictApi: ISysDictApi

    @GetMapping("/getDictItems")
    fun getDictItems(payload: DictModuleAndTypePayload): List<SysDictItemRecord> {
        return sysDictApi.getDictItems(payload)
    }

    @GetMapping("/getDictItemMap")
    fun getDictItemMap(payload: DictModuleAndTypePayload): LinkedHashMap<String, String> {
        return sysDictApi.getDictItemMap(payload)
    }

    @PostMapping("/batchGetDictItems")
    fun batchGetDictItems(payloads: List<DictModuleAndTypePayload>): Map<Pair<String, String>, List<SysDictItemRecord>> {
        return sysDictApi.batchGetDictItems(payloads)
    }

    @PostMapping("/batchGetDictItemMap")
    fun batchGetDictItemMap(payloads: List<DictModuleAndTypePayload>): Map<Pair<String, String>, LinkedHashMap<String, String>> {
         return sysDictApi.batchGetDictItemMap(payloads)
    }

}