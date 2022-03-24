package io.kuark.service.sys.common.api

import io.kuark.service.sys.common.vo.param.SysParamDetail
import io.kuark.service.sys.common.vo.param.SysParamRecord


/**
 * 系统参数服务对外的接口
 *
 * @author K
 * @since 1.0.0
 */
interface ISysParamApi {

    /**
     * 根据模块和字典类型，取得对应字典项(仅包括处于启用状态的)
     *
     * @param module 如果没有请传入空串，此时请保证paramName的惟一性，否则结果将不确定是哪条记录
     * @param paramName 参数名
     * @return 参数信息对象。如果module为空串，且存在多个同名paramName，将任意返回一个paramName对应的参数。查无结果返回null。
     * @author K
     * @since 1.0.0
     */
    fun getParam(module: String, paramName: String): SysParamDetail?

}