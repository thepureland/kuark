package io.kuark.service.sys.provider.ibiz

import io.kuark.base.support.biz.IBaseCrudBiz
import io.kuark.service.sys.provider.model.po.SysResource
import kotlin.reflect.KClass

/**
 * 资源业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface ISysResourceBiz: IBaseCrudBiz<String, SysResource> {
//endregion your codes 1

    //region your codes 2

    fun getMenus(): List<io.kuark.service.sys.common.vo.resource.MenuTreeNode>

    fun loadDirectChildrenForTree(searchPayload: io.kuark.service.sys.common.vo.resource.SysResourceSearchPayload): List<io.kuark.service.sys.common.vo.resource.SysResourceTreeNode>

    fun loadDirectChildrenForList(searchPayload: io.kuark.service.sys.common.vo.resource.SysResourceSearchPayload): Pair<List<io.kuark.service.sys.common.vo.resource.SysResourceRecord>, Int>

    fun <R : Any> get(id: String, returnType: KClass<R>, fetchAllParentIds: Boolean = false): R?

    /**
     * 获取所有祖先id
     *
     * @param id 当前资源id
     * @return List(祖先id)
     * @author K
     * @since 1.0.0
     */
    fun fetchAllParentIds(id: String): List<String>

    /**
     * 删除资源并级联删除其所有孩子
     *
     * @param id 资源id
     * @return 是否删除成功
     * @author K
     * @since 1.0.0
     */
    fun cascadeDeleteChildren(id: String): Boolean

    //endregion your codes 2

}