package org.kuark.cache.core

import org.kuark.base.lang.string.RandomStringKit
import org.kuark.base.log.LogFactory
import org.kuark.cache.context.CacheNames
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
open class CacheTestService {

    @Cacheable(value = [CacheNames.TEST], key = "#id")
    open fun getFromDB(id: String): String {
        LogFactory.getLog(CacheTestService::class).info("模拟去db查询~~~$id")
        return RandomStringKit.uuid()
    }

}