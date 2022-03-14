package io.kuark.service.sys.provider.api.local

import io.kuark.base.bean.validation.kit.ValidationKit
import io.kuark.service.sys.common.api.ISysDictApi
import io.kuark.service.sys.common.vo.dict.DictModuleAndTypePayload
import io.kuark.service.sys.common.vo.dict.SysDictItemRecord
import io.kuark.service.sys.provider.biz.ibiz.ISysDictItemBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class SysDictApi : ISysDictApi {

    @Autowired
    private lateinit var sysDictItemBiz: ISysDictItemBiz

    override fun getDictItems(payload: DictModuleAndTypePayload): List<SysDictItemRecord> {
        return sysDictItemBiz.getItemsByModuleAndType(payload.module ?: "", payload.dictType!!)
    }

    override fun getDictItemMap(payload: DictModuleAndTypePayload): LinkedHashMap<String, String> {
        val items = sysDictItemBiz.getItemsByModuleAndType(payload.module ?: "", payload.dictType!!)
        val map = linkedMapOf<String, String>()
        items.forEach { map[it.itemCode] = it.itemName }
        return map
    }

    override fun batchGetDictItems(payloads: List<DictModuleAndTypePayload>): Map<Pair<String, String>, List<SysDictItemRecord>> {
        val recordMap = mutableMapOf<Pair<String, String>, List<SysDictItemRecord>>()
        payloads.forEach { payload ->
            val errors = ValidationKit.validateBean(payload)
            if (errors.isEmpty()) {
                val module = payload.module ?: ""
                val items = sysDictItemBiz.getItemsByModuleAndType(module, payload.dictType!!)
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
                val items = sysDictItemBiz.getItemsByModuleAndType(module, payload.dictType!!)
                val map = linkedMapOf<String, String>()
                items.forEach { map[it.itemCode] = it.itemName }
                recordMap[Pair(module, payload.dictType!!)] = map
            } else {
                throw IllegalArgumentException(errors.first().message)
            }
        }
        return recordMap
    }


}