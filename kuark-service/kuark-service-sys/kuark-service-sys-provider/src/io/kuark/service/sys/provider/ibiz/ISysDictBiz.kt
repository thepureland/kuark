package io.kuark.service.sys.provider.ibiz

import io.kuark.ability.data.rdb.biz.IBaseBiz
import io.kuark.service.sys.common.model.dict.SysDictListRecord
import io.kuark.service.sys.common.model.dict.SysDictSearchPayload
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

    //endregion your codes 2

}