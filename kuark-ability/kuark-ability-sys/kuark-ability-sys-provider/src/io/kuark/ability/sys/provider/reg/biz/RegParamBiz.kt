package io.kuark.ability.sys.provider.reg.biz

import io.kuark.ability.cache.context.CacheNames
import io.kuark.ability.data.rdb.biz.BaseBiz
import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.ability.data.rdb.support.SqlWhereExpressionFactory
import io.kuark.ability.sys.common.reg.param.RegParamRecord
import io.kuark.ability.sys.common.reg.param.RegParamSearchPayload
import io.kuark.base.query.enums.Operator
import io.kuark.base.support.Consts
import io.kuark.ability.sys.provider.reg.dao.RegParamDao
import io.kuark.ability.sys.provider.reg.ibiz.IRegParamBiz
import io.kuark.ability.sys.provider.reg.model.po.RegParam
import io.kuark.ability.sys.provider.reg.model.table.RegParams
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
open class RegParamBiz : BaseBiz<String, RegParam, RegParamDao>(), IRegParamBiz {
//endregion your codes 1

    //region your codes 2

    @Cacheable(value = [CacheNames.REG_PARAM], key = "#module.concat(':').concat(#name)", unless = "#result == null")
    override fun getParamByModuleAndName(module: String, name: String): RegParam? {
        val paramList = RdbKit.getDatabase().from(RegParams)
            .select(RegParams.columns)
            .whereWithConditions {
                it += (RegParams.paramName eq name) and (RegParams.active eq true)
                if (module.isNotEmpty()) {
                    it += RegParams.module eq module
                }
            }
            .map { row -> RegParams.createEntity(row) }
            .toList()
        return if (paramList.isEmpty()) null else paramList.first()
    }

    override fun pagingSearch(searchPayload: RegParamSearchPayload): Pair<List<RegParamRecord>, Int> {
        val records = dao.search(searchPayload) { column, value ->
            if (value != null && column.name in arrayOf(RegParams.module.name, RegParams.paramName.name, RegParams.defaultValue.name)) {
                SqlWhereExpressionFactory.create(column, Operator.ILIKE, value.toString().trim())
            } else if (column.name == RegParams.active.name && value == true) {
                SqlWhereExpressionFactory.create(column, Operator.EQ, true)
            } else null
        }
        val count = if (records.isEmpty()) 0 else dao.count(searchPayload)
        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        return Pair(records as List<RegParamRecord>, count)
    }

    //endregion your codes 2

}