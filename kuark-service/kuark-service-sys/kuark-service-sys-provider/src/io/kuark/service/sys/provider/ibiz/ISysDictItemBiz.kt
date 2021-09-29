package io.kuark.service.sys.provider.ibiz

import io.kuark.service.sys.provider.model.po.SysDictItem

/**
 * 字典子表业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface ISysDictItemBiz {
//endregion your codes 1

    //region your codes 2

    /**
     * 根据模块和字典类型，取得对应字典项(仅包括处于启用状态的)，并将结果缓存，查不到不缓存
     *
     * @param module 如果没有请传入空串，此时请保证type的惟一性，否则结果将不确定是哪条记录
     * @param type 字典类型
     * @return 字典项列表。如果module为空串，且存在多个同名type，将任意返回一个type对应的字典项。查无结果返回空列表。
     */
    fun getItemsByModuleAndType(module: String, type: String): List<SysDictItem>

    //endregion your codes 2

}