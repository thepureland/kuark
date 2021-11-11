package io.kuark.service.sys.provider.ibiz

import io.kuark.ability.data.rdb.biz.IBaseBiz
import io.kuark.service.sys.common.model.SysMenuTreeNode
import io.kuark.service.sys.common.model.dict.SysDictRecord
import io.kuark.service.sys.common.model.dict.SysDictSearchPayload
import io.kuark.service.sys.common.model.resource.SysResourceRecord
import io.kuark.service.sys.common.model.resource.SysResourceSearchPayload
import io.kuark.service.sys.common.model.resource.SysResourceTreeNode
import io.kuark.service.sys.provider.model.po.SysResource

/**
 * 资源业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface ISysResourceBiz: IBaseBiz<String, SysResource> {
//endregion your codes 1

    //region your codes 2

    fun getMenus(): List<SysMenuTreeNode>

    fun loadDirectChildrenForTree(searchPayload: SysResourceSearchPayload): List<SysResourceTreeNode>

    fun loadDirectChildrenForList(searchPayload: SysResourceSearchPayload): Pair<List<SysResourceRecord>, Int>

    //endregion your codes 2

}