package io.kuark.service.sys.provider.ibiz

import io.kuark.ability.data.rdb.biz.IBaseBiz
import io.kuark.service.sys.common.model.dict.SysDictPayload
import io.kuark.service.sys.common.model.dict.SysDictRecord
import io.kuark.service.sys.common.model.dict.SysDictSearchPayload
import io.kuark.service.sys.common.model.dict.SysDictTreeNode
import io.kuark.service.sys.provider.model.po.SysDict

/**
 * 字典主表业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface ISysDictBiz : IBaseBiz<String, SysDict> {
//endregion your codes 1

    //region your codes 2

    /**
     * 查询符合条件的字典项及字典
     *
     * @param searchPayload 查询参数
     * @return Pair(List(SysDictListModel), 总记录数)
     * @author K
     * @since 1.0.0
     */
    fun pagingSearch(searchPayload: SysDictSearchPayload): Pair<List<SysDictRecord>, Int>

    /**
     * 加载直接孩子结点(用于树)
     *
     * @param parent 父项标识，为null时加载模块列表
     * @param isModule 是否parent代表模块名
     * @param activeOnly 是否只加载启用状态的数据, 默认为是
     * @return List(SysDictTreeNode)
     * @author K
     * @since 1.0.0
     */
    fun loadDirectChildrenForTree(parent: String?, isModule: Boolean, activeOnly: Boolean = true): List<SysDictTreeNode>

    /**
     * 加载直接孩子结点(用于列表)
     *
     * @param searchPayload 查询参数
     * @return Pair(List(SysDictListModel), 总记录数)
     * @author K
     * @since 1.0.0
     */
    fun loadDirectChildrenForList(searchPayload: SysDictSearchPayload): Pair<List<SysDictRecord>, Int>

    /**
     * 返回指定id的字典信息
     *
     * @param id 字典或字典项id，由isDict参数决定
     * @param isDict true: 字典id，false：字典项id
     * @param fetchAllParentIds 是否要获取所有父项id，默认为false
     * @return SysDictRecord，找不到返回null
     * @author K
     * @since 1.0.0
     */
    fun get(id: String, isDict: Boolean?, fetchAllParentIds: Boolean = false): SysDictRecord?

    /**
     * 保存或更新字典或字典项
     *
     * @param payload 数据载体
     * @return 主键
     * @author K
     * @since 1.0.0
     */
    fun saveOrUpdate(payload: SysDictPayload): String

    /**
     * 删除字典或字典项
     *
     * @param id 主键
     * @param isDict true: 字典id，false：字典项id
     * @return 是否删除成功
     * @author K
     * @since 1.0.0
     */
    fun delete(id: String, isDict: Boolean): Boolean

    /**
     * 查询指定字典项的所有父项id
     *
     * @param itemId 字典项id
     * @return List(父项id)
     * @author K
     * @since 1.0.0
     */
    fun fetchAllParentIds(itemId: String): MutableList<String>

    //endregion your codes 2

}