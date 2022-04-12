package io.kuark.service.sys.provider.api.local

import io.kuark.base.bean.validation.kit.ValidationKit
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.api.ISysDictApi
import io.kuark.service.sys.common.vo.dict.DictModuleAndTypePayload
import io.kuark.service.sys.common.vo.dict.SysDictItemCacheItem
import io.kuark.service.sys.provider.biz.ibiz.ISysDictItemBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class SysDictApi : ISysDictApi {

    @Autowired
    private lateinit var sysDictItemBiz: ISysDictItemBiz

    override fun getDictItems(payload: DictModuleAndTypePayload): List<SysDictItemCacheItem> {
        return sysDictItemBiz.getItemsFromCache(payload.module ?: "", payload.dictType!!)
    }

    override fun getDictItemMap(payload: DictModuleAndTypePayload): LinkedHashMap<String, String> {
        val items = sysDictItemBiz.getItemsFromCache(payload.module ?: "", payload.dictType!!)
        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        return items.associate { it.itemCode to it.itemName } as LinkedHashMap<String, String>
    }

    override fun batchGetDictItems(payloads: List<DictModuleAndTypePayload>): Map<Pair<String, String>, List<SysDictItemCacheItem>> {
        val recordMap = mutableMapOf<Pair<String, String>, List<SysDictItemCacheItem>>()
        payloads.forEach { payload ->
            val errors = ValidationKit.validateBean(payload)
            if (errors.isEmpty()) {
                val module = payload.module ?: ""
                val items = sysDictItemBiz.getItemsFromCache(module, payload.dictType!!)
                recordMap[Pair(module, payload.dictType!!)] = items
            } else {
                throw IllegalArgumentException(errors.first().message)
            }
        }
        return recordMap
    }

    override fun batchGetDictItemMap(payloads: List<DictModuleAndTypePayload>): Map<Pair<String, String>, LinkedHashMap<String, String>> {
        val recordMap = mutableMapOf<Pair<String, String>, LinkedHashMap<String, String>>()
        payloads.forEach { payload ->
            val errors = ValidationKit.validateBean(payload)
            if (errors.isEmpty()) {
                val module = payload.module ?: ""
                val items = sysDictItemBiz.getItemsFromCache(module, payload.dictType!!)

                @Suppress(Consts.Suppress.UNCHECKED_CAST)
                val map = items.associate { it.itemCode to it.itemName } as LinkedHashMap<String, String>
                recordMap[Pair(module, payload.dictType!!)] = map
            } else {
                throw IllegalArgumentException(errors.first().message)
            }
        }
        return recordMap
    }


}