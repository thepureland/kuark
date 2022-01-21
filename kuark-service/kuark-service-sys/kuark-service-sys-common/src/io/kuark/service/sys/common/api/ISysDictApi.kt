package io.kuark.service.sys.common.api

import io.kuark.service.sys.common.vo.dict.DictModuleAndTypePayload
import io.kuark.service.sys.common.vo.dict.SysDictItemRecord


/**
 * 系统字典服务对外的接口
 *
 * @author K
 * @since 1.0.0
 */
interface ISysDictApi {

    /**
     * 根据模块和字典类型，取得对应字典项(仅包括处于启用状态的)
     *
     * @param payload 模块和字典类型的载体（如果模块未指定，此时请保证type的惟一性，否则结果将不确定是哪条记录）
     * @return 字典项列表（自然排序）。如果模块未指定，且存在多个同名type，将任意返回一个type对应的字典项。查无结果返回空列表。
     * @throws IllegalArgumentException 参数校验不通过时
     * @author K
     * @since 1.0.0
     */
    fun getDictItems(payload: DictModuleAndTypePayload): List<SysDictItemRecord>

    /**
     * 根据模块和字典类型，取得对应字典项的编码和名称(仅包括处于启用状态的)
     *
     * @param payload 模块和字典类型的载体（如果模块未指定，此时请保证type的惟一性，否则结果将不确定是哪条记录）
     * @return Map(编码，名称)，自然排序。如果module为空串，且存在多个同名type，将任意返回一个type对应的字典项。查无结果返回空Map。
     * @throws IllegalArgumentException 参数校验不通过时
     * @author K
     * @since 1.0.0
     */
    fun getDictItemMap(payload: DictModuleAndTypePayload): LinkedHashMap<String, String>

    /**
     * 根据模块和字典类型的载体列表，取得对应字典项信息(仅包括处于启用状态的)
     *
     * @param payloads 模块和字典类型的载体列表（如果模块未指定，此时请保证type的惟一性，否则结果将不确定是哪条记录）
     * @return Map(Pair(模块，字典类型)，List(字典项信息对象))
     * @throws IllegalArgumentException 参数校验不通过时
     * @author K
     * @since 1.0.0
     */
    fun batchGetDictItems(payloads: List<DictModuleAndTypePayload>): Map<Pair<String, String>, List<SysDictItemRecord>>

    /**
     * 根据模块和字典类型的载体列表，取得对应字典项的编码和名称(仅包括处于启用状态的)
     * @param payloads 模块和字典类型的载体列表（如果模块未指定，此时请保证type的惟一性，否则结果将不确定是哪条记录）
     * @return Map(Pair(模块，字典类型)，LinkedHashMap(编码，名称))
     * @throws IllegalArgumentException 参数校验不通过时
     * @author K
     * @since 1.0.0
     */
    fun batchGetDictItemMap(payloads: List<DictModuleAndTypePayload>): Map<Pair<String, String>, LinkedHashMap<String, String>>

}