package io.kuark.service.sys.provider.biz

import io.kuark.ability.data.rdb.biz.BaseBiz
import io.kuark.service.sys.common.model.dict.SysDictListRecord
import io.kuark.service.sys.common.model.dict.SysDictSearchPayload
import io.kuark.service.sys.provider.dao.SysDictDao
import io.kuark.service.sys.provider.ibiz.ISysDictBiz
import io.kuark.service.sys.provider.model.po.SysDict
import org.springframework.stereotype.Service

/**
 * 字典主表业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class SysDictBiz: BaseBiz<String, SysDict, SysDictDao>(), ISysDictBiz {
//endregion your codes 1

    //region your codes 2

    override fun pagingSearch(searchPayload: SysDictSearchPayload): Pair<List<SysDictListRecord>, Int> {
        val dictItems = dao.pagingSearch(searchPayload)
        val totalCount = dao.count(searchPayload)
        return Pair(dictItems, totalCount)
    }

    //endregion your codes 2

}