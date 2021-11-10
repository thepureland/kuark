package io.kuark.service.sys.provider.ibiz

import io.kuark.ability.data.rdb.biz.IBaseBiz
import io.kuark.service.sys.common.model.SysMenuTreeNode
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

    //endregion your codes 2

}