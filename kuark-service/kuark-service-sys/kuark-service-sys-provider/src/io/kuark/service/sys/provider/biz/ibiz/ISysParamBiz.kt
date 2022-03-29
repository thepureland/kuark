package io.kuark.service.sys.provider.biz.ibiz

import io.kuark.base.support.biz.IBaseCrudBiz
import io.kuark.service.sys.common.vo.param.SysParamDetail
import io.kuark.service.sys.common.vo.param.SysParamRecord
import io.kuark.service.sys.common.vo.param.SysParamSearchPayload
import io.kuark.service.sys.provider.model.po.SysParam

/**
 * 参数业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface ISysParamBiz: IBaseCrudBiz<String, SysParam> {
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
    fun getParamFromCache(module: String, name: String): SysParamDetail?

    /**
     * 分页查询
     *
     * @param searchPayload 查询载体
     * @return Pair(List(RegParamRecord)，总记录数)
     * @author K
     * @since 1.0.0
     */
    fun pagingSearch(searchPayload: SysParamSearchPayload): Pair<List<SysParamRecord>, Int>

    /**
     * 更新启用状态，并同步缓存
     *
     * @param id 主键
     * @param active 是否启用
     * @return 是否更新成功
     * @author K
     * @since 1.0.0
     */
    fun updateActive(id: String, active: Boolean): Boolean

    //endregion your codes 2

}