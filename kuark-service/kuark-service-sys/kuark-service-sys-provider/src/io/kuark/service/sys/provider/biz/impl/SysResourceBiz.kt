package io.kuark.service.sys.provider.biz.impl

import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.ability.data.rdb.support.SqlWhereExpressionFactory
import io.kuark.base.lang.string.StringKit
import io.kuark.base.query.Criteria
import io.kuark.base.query.enums.Operator
import io.kuark.base.query.sort.Direction
import io.kuark.base.support.Consts
import io.kuark.base.support.payload.ListSearchPayload
import io.kuark.base.tree.TreeKit
import io.kuark.context.core.KuarkContextHolder
import io.kuark.service.sys.common.vo.resource.*
import io.kuark.service.sys.provider.dao.SysResourceDao
import io.kuark.service.sys.provider.biz.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.biz.ibiz.ISysResourceBiz
import io.kuark.service.sys.provider.model.po.SysResource
import io.kuark.service.sys.provider.model.table.SysResources
import org.ktorm.dsl.isNull
import org.ktorm.schema.Column
import org.ktorm.schema.ColumnDeclaring
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.reflect.KClass

/**
 * 资源业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class SysResourceBiz : BaseCrudBiz<String, SysResource, SysResourceDao>(), ISysResourceBiz {
//endregion your codes 1

    //region your codes 2

    @Autowired
    private lateinit var dictItemBiz: ISysDictItemBiz

    override fun getSimpleMenus(subSysDictCode: String): List<BaseMenuTreeNode> {
        val origMenus = searchMenus(subSysDictCode)
        val menus = origMenus.map {
            BaseMenuTreeNode().apply {
                title = it.name
                id = it.id!!
                parentId = it.parentId
                seqNo = it.seqNo
            }
        }
        return TreeKit.convertListToTree(menus, Direction.ASC)
    }

    override fun getMenus(): List<MenuTreeNode> {
        //TODO 加入权限
        val subSysCode = KuarkContextHolder.get().subSysCode
        val origMenus = searchMenus(subSysCode)
        val menus = origMenus.map {
            MenuTreeNode().apply {
                title = it.name
                index = it.url
                icon = it.icon
                id = it.id!!
                parentId = it.parentId
                seqNo = it.seqNo
            }
        }
        return TreeKit.convertListToTree(menus, Direction.ASC)
    }

    private fun searchMenus(subSysCode: String?): List<SysResource> {
        val criteria = Criteria.add(SysResource::active.name, Operator.EQ, true)
        if (StringKit.isNotBlank(subSysCode)) {
            criteria.addAnd(SysResource::subSysDictCode.name, Operator.EQ, subSysCode)
        }
        return dao.search(criteria)
    }

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun loadDirectChildrenForTree(searchPayload: SysResourceSearchPayload): List<SysResourceTreeNode> {
        return when (if (searchPayload.level == null) Int.MAX_VALUE else searchPayload.level) {
            0 -> { // 资源类型
                val dictItems = dictItemBiz.getItemsByModuleAndType("kuark:sys", "resource_type")
                dictItems.map { SysResourceTreeNode()
                    .apply { this.id = it.itemCode;this.name = it.itemName } }
            }
            1 -> { // 子系统
                val dictItems = dictItemBiz.getItemsByModuleAndType("kuark:sys", "sub_sys")
                dictItems.map { SysResourceTreeNode()
                    .apply { this.id = it.itemCode;this.name = it.itemName } }
            }
            else -> { // 资源
                if (searchPayload.active == false) { // 非仅启用状态
                    searchPayload.active = null
                }
                searchPayload.returnEntityClass = SysResourceTreeNode::class
                searchPayload.pageNo = null // 不分页
                dao.search(searchPayload) { column, _ ->
                    if (column.name == SysResources.parentId.name && searchPayload.level == 2) { // 1层是资源类型，2层是子系统，从第3层开始才是RegResource
                        column.isNull()
                    } else null
                } as List<SysResourceTreeNode>
            }
        }
    }

    override fun loadDirectChildrenForList(searchPayload: SysResourceSearchPayload): Pair<List<SysResourceRecord>, Int> {
        if (searchPayload.active == false) { // 非仅启用状态
            searchPayload.active = null
        }
        val whereConditionFactory: (Column<Any>, Any?) -> ColumnDeclaring<Boolean>? = { column, value ->
            if (column.name == SysResources.parentId.name && searchPayload.level == 2) { // 1层是资源类型，2层是子系统，从第3层开始才是RegResource
                column.isNull()
            } else null
        }
        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val result = dao.search(searchPayload, whereConditionFactory) as List<SysResourceRecord>
        val count = dao.count(searchPayload, whereConditionFactory)
        return Pair(result, count)
    }

    override fun pagingSearch(listSearchPayload: ListSearchPayload): Pair<List<*>, Int> {
        val whereConditionFactory: (Column<Any>, Any?) -> ColumnDeclaring<Boolean>? = { column, value ->
            if (column.name == SysResources.name.name) {
                SqlWhereExpressionFactory.create(column, Operator.ILIKE, value)
            } else null
        }
        val result = dao.search(listSearchPayload, whereConditionFactory)
        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        (result as List<SysResourceRecord>).forEach { transCode(it) }
        val count = dao.count(listSearchPayload, whereConditionFactory)
        return Pair(result, count)
    }

    override fun <R : Any> get(id: String, returnType: KClass<R>, fetchAllParentIds: Boolean): R? {
        val result = super.get(id, returnType)
        if (result is SysResourceRecord) {
            transCode(result)
            if (fetchAllParentIds) {
                val realParentIds = fetchAllParentIds(id)
                val parentIds = mutableListOf(result.resourceTypeDictCode!!, result.subSysDictCode!!)
                parentIds.addAll(realParentIds)
                result.parentIds = parentIds
            }
        }
        return result
    }

    override fun fetchAllParentIds(id: String): List<String> {
        val results = mutableListOf<String>()
        recursionFindAllParentId(id, results)
        results.reverse()
        return results
    }

    @Transactional
    override fun cascadeDeleteChildren(id: String): Boolean {
        val childItemIds = mutableListOf<String>()
        recursionFindAllChildId(id, childItemIds)
        if (childItemIds.isNotEmpty()) {
            dao.batchDelete(childItemIds)
        }
        return dao.deleteById(id)
    }

    private fun recursionFindAllParentId(itemId: String, results: MutableList<String>) {
        val list = dao.oneSearchProperty(
            SysResources.id.name,
            itemId,
            SysResources.parentId.name
        )
        if (list.isNotEmpty()) {
            val parentId = list.first() as String?
            if (parentId != null) {
                results.add(parentId)
                recursionFindAllParentId(parentId, results)
            }
        }
    }

    private fun recursionFindAllChildId(resId: String, results: MutableList<String>) {
        val itemIds = dao.oneSearchProperty(
            SysResources.parentId.name,
            resId,
            SysResources.id.name
        )
        itemIds.forEach { id ->
            results.add(id as String)
            recursionFindAllChildId(id, results)
        }
    }

    private fun transCode(record: SysResourceRecord) {
        record.resourceTypeName = dictItemBiz.transDictCode("kuark:sys", "resource_type", record.resourceTypeDictCode!!)
        record.subSysName = dictItemBiz.transDictCode("kuark:sys", "sub_sys", record.subSysDictCode!!)
    }

    //endregion your codes 2

}