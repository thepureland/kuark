package io.kuark.ability.sys.provider.reg.ibiz

import io.kuark.ability.cache.context.CacheNames
import io.kuark.ability.data.rdb.biz.IBaseBiz
import io.kuark.ability.sys.common.vo.reg.param.RegParamRecord
import io.kuark.ability.sys.common.vo.reg.param.RegParamSearchPayload
import io.kuark.ability.sys.provider.reg.model.po.RegParam
import org.springframework.cache.annotation.Cacheable

/**
 * 参数业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface IRegParamBiz: IBaseBiz<String, RegParam> {
//endregion your codes 1

    //region your codes 2

    /**
     * 根据模块和参数名称，取得对应参数信息(仅包括处于启用状态的)，并将结果缓存，查不到不缓存
     *
     * @param module 如果没有请传入空串，此时请保证参数名称的惟一性，否则结果将不确定是哪条记录
     * @param name 参数名称
     * @return 参数信息。如果module为空串，且存在多个同名参数，将任意返回一个name对应的参数信息。查无结果返回null。
     * @author K
     * @since 1.0.0
     */
    @Cacheable(value = [CacheNames.REG_PARAM], key = "#module.concat(':').concat(#name)", unless = "#result == null")
    fun getParamByModuleAndName(module: String, name: String): RegParam?

    /**
     * 分页查询
     *
     * @param searchPayload 查询载体
     * @return Pair(List(RegParamRecord)，总记录数)
     * @author K
     * @since 1.0.0
     */
    fun pagingSearch(searchPayload: RegParamSearchPayload): Pair<List<RegParamRecord>, Int>

    //endregion your codes 2

}