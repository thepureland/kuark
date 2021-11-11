package io.kuark.service.sys.provider.biz

import io.kuark.ability.data.rdb.biz.BaseBiz
import io.kuark.base.lang.string.StringKit
import io.kuark.base.query.Criteria
import io.kuark.base.query.enums.Operator
import io.kuark.base.query.sort.Direction
import io.kuark.base.support.Consts
import io.kuark.base.tree.TreeKit
import io.kuark.context.core.KuarkContextHolder
import io.kuark.service.sys.common.model.SysMenuTreeNode
import io.kuark.service.sys.common.model.dict.SysDictTreeNode
import io.kuark.service.sys.common.model.resource.SysResourceRecord
import io.kuark.service.sys.common.model.resource.SysResourceSearchPayload
import io.kuark.service.sys.common.model.resource.SysResourceTreeNode
import io.kuark.service.sys.provider.dao.SysResourceDao
import io.kuark.service.sys.provider.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.ibiz.ISysResourceBiz
import io.kuark.service.sys.provider.model.po.SysResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * 资源业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class SysResourceBiz : BaseBiz<String, SysResource, SysResourceDao>(), ISysResourceBiz {
//endregion your codes 1

    //region your codes 2

    @Autowired
    private lateinit var dictItemBiz: ISysDictItemBiz

    override fun getMenus(): List<SysMenuTreeNode> {
        //TODO 加入权限
        val criteria = Criteria.add(SysResource::active.name, Operator.EQ, true)
        val subSysCode = KuarkContextHolder.get().subSysCode
        if (StringKit.isNotBlank(subSysCode)) {
            criteria.addAnd(SysResource::subSysDictCode.name, Operator.EQ, subSysCode)
        }
        val origMenus = dao.search(criteria)
        val sysMenus = origMenus.map { SysMenuTreeNode(it.name, it.url, it.icon, it.id!!, it.parentId, it.seqNo) }
        return TreeKit.convertListToTree(sysMenus, Direction.ASC)
    }

    @Suppress(Consts.SUPPRESS_UNCHECKED_CAST)
    override fun loadDirectChildrenForTree(searchPayload: SysResourceSearchPayload): List<SysResourceTreeNode> {
        return when (if (searchPayload.level == null) Int.MAX_VALUE else searchPayload.level) {
            0 -> { // 资源类型
                val dictItems = dictItemBiz.getItemsByModuleAndType("kuark:sys", "resource_type")
                dictItems.map { SysResourceTreeNode().apply { this.id = it.itemCode;this.name = it.itemName } }
            }
            1 -> { // 子系统
                val dictItems = dictItemBiz.getItemsByModuleAndType("kuark:sys", "sub_sys")
                dictItems.map { SysResourceTreeNode().apply { this.id = it.itemCode;this.name = it.itemName } }
            }
            else -> { // 资源
                val resourceTypeDictCode = searchPayload.resourceTypeDictCode
                val subSysDictCode = searchPayload.subSysDictCode
                if (searchPayload.active == false) { // 非仅启用状态
                    searchPayload.active = null
                }
                if (searchPayload.level != null && searchPayload.level!! <= 2) { // 1层是资源类型，2层是子系统，从第3层开始才是SysResource
                    searchPayload.parentId = null
                }
                require(StringKit.isNotBlank(resourceTypeDictCode)) { "加载系统资源树时，资源类型参数不能为空！" }
                require(StringKit.isNotBlank(subSysDictCode)) { "加载系统资源树时，子系统参数不能为空！" }
                searchPayload.returnEntityClass = SysResourceTreeNode::class
                dao.search(searchPayload) as List<SysResourceTreeNode>
            }
        }
    }

    override fun loadDirectChildrenForList(searchPayload: SysResourceSearchPayload): Pair<List<SysResourceRecord>, Int> {
        val resourceTypeDictCode = searchPayload.resourceTypeDictCode
        val subSysDictCode = searchPayload.subSysDictCode
        if (searchPayload.active == false) { // 非仅启用状态
            searchPayload.active = null
        }
        if (searchPayload.level != null && searchPayload.level!! <= 2) { // 1层是资源类型，2层是子系统，从第3层开始才是SysResource
            searchPayload.parentId = null
        }
        require(StringKit.isNotBlank(resourceTypeDictCode)) { "加载系统资源列表时，资源类型参数不能为空！" }
        require(StringKit.isNotBlank(subSysDictCode)) { "加载系统资源列表时，子系统参数不能为空！" }
        return pagingSearch(searchPayload) as Pair<List<SysResourceRecord>, Int>
    }

    //endregion your codes 2

}