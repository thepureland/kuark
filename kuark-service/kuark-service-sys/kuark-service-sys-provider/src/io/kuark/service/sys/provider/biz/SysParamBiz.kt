package io.kuark.service.sys.provider.biz

import io.kuark.ability.cache.context.CacheNames
import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.service.sys.provider.ibiz.ISysParamBiz
import io.kuark.service.sys.provider.model.po.SysParam
import io.kuark.service.sys.provider.model.table.SysParams
import org.ktorm.dsl.*
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

/**
 * 参数业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class SysParamBiz: ISysParamBiz {
//endregion your codes 1

    //region your codes 2

    /**
     * 根据模块和参数名称，取得对应参数信息(仅包括处于启用状态的)，并将结果缓存，查不到不缓存
     *
     * @param module 如果没有请传入空串，此时请保证参数名称的惟一性，否则结果将不确定是哪条记录
     * @param name 参数名称
     * @return 参数信息。如果module为空串，且存在多个同名参数，将任意返回一个name对应的参数信息。查无结果返回null。
     */
    @Cacheable(value = [CacheNames.SYS_PARAM], key = "#module.concat(':').concat(#name)", unless = "#result == null")
    open fun getParamByModuleAndName(module: String, name: String): SysParam? {
        val paramList = RdbKit.getDatabase().from(SysParams)
            .select(SysParams.columns)
            .whereWithConditions {
                it += (SysParams.paramName eq name) and (SysParams.active eq true)
                if (module.isNotEmpty()) {
                    it += SysParams.module eq module
                }
            }
            .map { row -> SysParams.createEntity(row) }
            .toList()
        return if (paramList.isEmpty()) null else paramList.first()
    }

    //endregion your codes 2

}