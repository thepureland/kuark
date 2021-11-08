package io.kuark.service.sys.provider.ibiz

import io.kuark.ability.cache.context.CacheNames
import io.kuark.ability.data.rdb.biz.IBaseBiz
import io.kuark.ability.web.common.WebResult
import io.kuark.service.sys.common.model.param.SysParamRecord
import io.kuark.service.sys.common.model.param.SysParamSearchPayload
import io.kuark.service.sys.provider.model.po.SysParam
import org.springframework.cache.annotation.Cacheable

/**
 * 参数业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface ISysParamBiz: IBaseBiz<String, SysParam> {
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
    @Cacheable(value = [CacheNames.SYS_PARAM], key = "#module.concat(':').concat(#name)", unless = "#result == null")
    fun getParamByModuleAndName(module: String, name: String): SysParam?

    /**
     * 分页查询
     *
     * @param searchPayload 查询载体
     * @return Pair(List(SysParamRecord)，总记录数)
     * @author K
     * @since 1.0.0
     */
    fun pagingSearch(searchPayload: SysParamSearchPayload): Pair<List<SysParamRecord>, Int>

    //endregion your codes 2

}