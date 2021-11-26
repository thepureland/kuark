package io.kuark.ability.sys.client.fallback.reg

import io.kuark.ability.sys.client.proxy.reg.IDictClient
import io.kuark.ability.sys.common.vo.reg.dict.RegDictItemRecord
import org.springframework.stereotype.Component

@Component
class DictFallback: IDictClient {

    override fun getDictItems(module: String, type: String): List<RegDictItemRecord> {
        TODO("Not yet implemented")
    }

    override fun getDictItemMap(module: String, type: String): LinkedHashMap<String, String> {
        TODO("Not yet implemented")
    }

}