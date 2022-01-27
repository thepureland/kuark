package io.kuark.service.sys.client.fallback

import io.kuark.service.sys.client.proxy.ISysDictProxy
import io.kuark.service.sys.common.vo.dict.DictModuleAndTypePayload
import io.kuark.service.sys.common.vo.dict.SysDictItemRecord
import org.springframework.stereotype.Component

@Component
class SysDictFallback: ISysDictProxy {

    override fun getDictItems(payload: DictModuleAndTypePayload): List<SysDictItemRecord> {
        TODO("Not yet implemented")
    }

    override fun getDictItemMap(payload: DictModuleAndTypePayload): LinkedHashMap<String, String> {
        TODO("Not yet implemented")
    }

    override fun batchGetDictItems(payloads: List<DictModuleAndTypePayload>): Map<Pair<String, String>, List<SysDictItemRecord>> {
        TODO("Not yet implemented")
    }

    override fun batchGetDictItemMap(payloads: List<DictModuleAndTypePayload>): Map<Pair<String, String>, LinkedHashMap<String, String>> {
        TODO("Not yet implemented")
    }

}