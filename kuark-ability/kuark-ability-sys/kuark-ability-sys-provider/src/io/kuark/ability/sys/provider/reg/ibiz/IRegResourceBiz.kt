package io.kuark.ability.sys.provider.reg.ibiz

import io.kuark.ability.data.rdb.biz.IBaseBiz
import io.kuark.ability.sys.common.vo.reg.resource.MenuTreeNode
import io.kuark.ability.sys.common.vo.reg.resource.RegResourceRecord
import io.kuark.ability.sys.common.vo.reg.resource.RegResourceSearchPayload
import io.kuark.ability.sys.common.vo.reg.resource.RegResourceTreeNode
import io.kuark.ability.sys.provider.reg.model.po.RegResource
import kotlin.reflect.KClass

/**
 * 资源业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface IRegResourceBiz: IBaseBiz<String, RegResource> {
//endregion your codes 1

    //region your codes 2

    fun getMenus(): List<MenuTreeNode>

    fun loadDirectChildrenForTree(searchPayload: RegResourceSearchPayload): List<RegResourceTreeNode>

    fun loadDirectChildrenForList(searchPayload: RegResourceSearchPayload): Pair<List<RegResourceRecord>, Int>

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