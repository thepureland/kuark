package io.kuark.service.sys.client.fallback

import io.kuark.service.sys.client.proxy.IDictClient
import org.springframework.stereotype.Component

@Component
class DictFallback: IDictClient {

    override fun getDictItems(module: String, type: String): List<io.kuark.service.sys.common.vo.dict.SysDictItemRecord> {
        TODO("Not yet implemented")
    }

    override fun getDictItemMap(module: String, type: String): LinkedHashMap<String, String> {
        TODO("Not yet implemented")
    }

}