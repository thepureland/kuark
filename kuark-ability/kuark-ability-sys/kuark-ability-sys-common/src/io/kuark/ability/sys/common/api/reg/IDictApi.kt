package io.kuark.ability.sys.common.api.reg

import io.kuark.ability.sys.common.vo.reg.dict.RegDictItemRecord

interface IDictApi {

    /**
     * 根据模块和字典类型，取得对应字典项(仅包括处于启用状态的)
     *
     * @param module 如果没有请传入空串，此时请保证type的惟一性，否则结果将不确定是哪条记录
     * @param type 字典类型
     * @return 字典项列表（自然排序）。如果module为空串，且存在多个同名type，将任意返回一个type对应的字典项。查无结果返回空列表。
     * @author K
     * @since 1.0.0
     */
    fun getDictItems(module: String, type: String): List<RegDictItemRecord>

    /**
     * 根据模块和字典类型，取得对应字典项的编码和名称(仅包括处于启用状态的)
     *
     * @param module 如果没有请传入空串，此时请保证type的惟一性，否则结果将不确定是哪条记录
     * @param type 字典类型
     * @return Map(编码，名称)，自然排序。如果module为空串，且存在多个同名type，将任意返回一个type对应的字典项。查无结果返回空Map。
     * @author K
     * @since 1.0.0
     */
    fun getDictItemMap(module: String, type: String): LinkedHashMap<String, String>

}