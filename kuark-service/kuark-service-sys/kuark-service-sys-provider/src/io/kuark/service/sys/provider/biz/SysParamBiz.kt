package io.kuark.service.sys.provider.biz

import io.kuark.ability.cache.context.CacheNames
import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.ability.data.rdb.support.SqlWhereExpressionFactory
import io.kuark.base.query.enums.Operator
import io.kuark.base.support.Consts
import io.kuark.service.sys.provider.dao.SysParamDao
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
open class SysParamBiz : BaseCrudBiz<String, SysParam, SysParamDao>(), ISysParamBiz {
//endregion your codes 1

    //region your codes 2

    @Cacheable(value = [CacheNames.SYS_PARAM], key = "#module.concat(':').concat(#name)", unless = "#result == null")
    override fun getParamByModuleAndName(module: String, name: String): SysParam? {
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

    override fun pagingSearch(searchPayload: io.kuark.service.sys.common.vo.param.SysParamSearchPayload): Pair<List<io.kuark.service.sys.common.vo.param.SysParamRecord>, Int> {
        val records = dao.search(searchPayload) { column, value ->
            if (value != null && column.name in arrayOf(
                    SysParams.module.name,
                    SysParams.paramName.name,
                    SysParams.defaultValue.name
                )
            ) {
                SqlWhereExpressionFactory.create(column, Operator.ILIKE, value.toString().trim())
            } else if (column.name == SysParams.active.name && value == true) {
                SqlWhereExpressionFactory.create(column, Operator.EQ, true)
            } else null
        }
        val count = if (records.isEmpty()) 0 else dao.count(searchPayload)
        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        return Pair(records as List<io.kuark.service.sys.common.vo.param.SysParamRecord>, count)
    }

    //endregion your codes 2

}