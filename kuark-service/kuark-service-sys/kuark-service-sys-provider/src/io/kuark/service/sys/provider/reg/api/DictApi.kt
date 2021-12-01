package io.kuark.service.sys.provider.reg.api

import io.kuark.service.sys.common.api.reg.IDictApi
import io.kuark.service.sys.common.vo.reg.dict.RegDictItemRecord
import io.kuark.service.sys.provider.reg.ibiz.IRegDictItemBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class DictApi: IDictApi {

    @Autowired
    private lateinit var regDictItemBiz: IRegDictItemBiz

    override fun getDictItems(module: String, type: String): List<RegDictItemRecord> {
        return regDictItemBiz.getItemsByModuleAndType(module, type)
    }

    override fun getDictItemMap(module: String, type: String): LinkedHashMap<String, String> {
        val items = regDictItemBiz.getItemsByModuleAndType(module, type)
        val map = linkedMapOf<String, String>()
        items.forEach { map[it.itemCode] = it.itemName }
        return map
    }

}