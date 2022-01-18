package io.kuark.service.sys.client.fallback

import io.kuark.service.sys.client.proxy.ISysDictClient
import io.kuark.service.sys.common.vo.dict.SysDictItemRecord
import org.springframework.stereotype.Component

@Component
class SysDictFallback: ISysDictClient {

    override fun getDictItems(module: String, type: String): List<SysDictItemRecord> {
        TODO("Not yet implemented")
    }

    override fun getDictItemMap(module: String, type: String): LinkedHashMap<String, String> {
        TODO("Not yet implemented")
    }

}