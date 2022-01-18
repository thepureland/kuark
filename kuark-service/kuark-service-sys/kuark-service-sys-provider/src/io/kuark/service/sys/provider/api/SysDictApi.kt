package io.kuark.service.sys.provider.api

import io.kuark.service.sys.common.api.ISysDictApi
import io.kuark.service.sys.common.vo.dict.SysDictItemRecord
import io.kuark.service.sys.provider.ibiz.ISysDictItemBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class SysDictApi: ISysDictApi {

    @Autowired
    private lateinit var sysDictItemBiz: ISysDictItemBiz

    override fun getDictItems(module: String, type: String): List<SysDictItemRecord> {
        return sysDictItemBiz.getItemsByModuleAndType(module, type)
    }

    override fun getDictItemMap(module: String, type: String): LinkedHashMap<String, String> {
        val items = sysDictItemBiz.getItemsByModuleAndType(module, type)
        val map = linkedMapOf<String, String>()
        items.forEach { map[it.itemCode] = it.itemName }
        return map
    }

}