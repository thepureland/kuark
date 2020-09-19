package io.kuark.ability.cache.core

import io.kuark.ability.cache.context.CacheNames
import io.kuark.base.lang.string.RandomStringKit
import io.kuark.base.log.LogFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
open class CacheTestService {

    @Cacheable(value = [CacheNames.TEST], key = "#id")
    open fun getFromDB(id: String): String {
        LogFactory.getLog(CacheTestService::class).info("模拟去db查询~~~$id")
        return RandomStringKit.uuidWithoutDelimiter()
    }

}