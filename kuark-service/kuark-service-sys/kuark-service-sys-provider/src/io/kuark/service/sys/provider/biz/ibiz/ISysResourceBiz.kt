package io.kuark.service.sys.provider.biz.ibiz

import io.kuark.base.support.biz.IBaseCrudBiz
import io.kuark.service.sys.common.vo.resource.*
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

    fun getSimpleMenus(subSysDictCode: String): List<BaseMenuTreeNode>

    fun getMenus(): List<MenuTreeNode>

    fun loadDirectChildrenForTree(searchPayload: SysResourceSearchPayload): List<SysResourceTreeNode>

    fun loadDirectChildrenForList(searchPayload: SysResourceSearchPayload): Pair<List<SysResourceRecord>, Int>

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