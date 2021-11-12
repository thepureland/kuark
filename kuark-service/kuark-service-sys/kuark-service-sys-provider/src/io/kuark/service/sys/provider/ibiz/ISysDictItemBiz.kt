package io.kuark.service.sys.provider.ibiz

import io.kuark.ability.data.rdb.biz.IBaseBiz
import io.kuark.service.sys.common.model.dict.SysDictPayload
import io.kuark.service.sys.provider.model.po.SysDictItem
import org.springframework.transaction.annotation.Transactional

/**
 * 字典子表业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface ISysDictItemBiz: IBaseBiz<String, SysDictItem> {
//endregion your codes 1

    //region your codes 2

    /**
     * 根据模块和字典类型，取得对应字典项(仅包括处于启用状态的)，并将结果缓存，查不到不缓存
     *
     * @param module 如果没有请传入空串，此时请保证type的惟一性，否则结果将不确定是哪条记录
     * @param type 字典类型
     * @return 字典项列表。如果module为空串，且存在多个同名type，将任意返回一个type对应的字典项。查无结果返回空列表。
     * @author K
     * @since 1.0.0
     */
    fun getItemsByModuleAndType(module: String, type: String): List<SysDictItem>

    /**
     * 翻译字典项代码
     *
     * @param module 如果没有请传入空串，此时请保证type的惟一性，否则结果将不确定是哪条记录
     * @param type 字典类型
     * @param code 字典项代码
     * @return 字典项名称，找不到返回null
     * @author K
     * @since 1.0.0
     */
    fun transDictCode(module: String, type: String, code: String): String?

    /**
     * 保存或更新
     *
     * @param payload 载体对象
     * @return 主键
     * @author K
     * @since 1.0.0
     */
    fun saveOrUpdate(payload: SysDictPayload): String

    /**
     * 获取字典项的所有祖先id
     *
     * @param itemId 字典项id
     * @return List(祖先id)
     * @author K
     * @since 1.0.0
     */
    fun fetchAllParentIds(itemId: String): List<String>

    /**
     * 删除字典项并级联删除其所有孩子
     *
     * @param id 字典项id
     * @return 是否删除成功
     * @author K
     * @since 1.0.0
     */
    fun cascadeDeleteChildren(id: String): Boolean

    //endregion your codes 2

}