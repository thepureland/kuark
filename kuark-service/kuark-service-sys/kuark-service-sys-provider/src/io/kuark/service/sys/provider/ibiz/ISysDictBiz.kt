package io.kuark.service.sys.provider.ibiz

import io.kuark.ability.data.rdb.biz.IBaseBiz
import io.kuark.service.sys.common.model.dict.SysDictListRecord
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
    fun pagingSearch(searchPayload: SysDictSearchPayload): Pair<List<SysDictListRecord>, Int>

    /**
     * 加载直接孩子结点
     *
     * @param parentId 父主键，为null时加载SysDict
     * @param activeOnly 是否只加载启用状态的数据
     * @return List(SysDictTreeNode)
     * @author K
     * @since 1.0.0
     */
    fun loadDirectChildren(parentId: String?, activeOnly: Boolean = true): List<SysDictTreeNode>

    //endregion your codes 2

}