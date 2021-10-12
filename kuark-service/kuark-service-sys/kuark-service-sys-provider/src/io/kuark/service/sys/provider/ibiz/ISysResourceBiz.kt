package io.kuark.service.sys.provider.ibiz

import io.kuark.service.sys.common.model.SysMenuTreeNode

/**
 * 资源业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface ISysResourceBiz {
//endregion your codes 1

    //region your codes 2

    fun getMenus(): List<SysMenuTreeNode>

    //endregion your codes 2

}