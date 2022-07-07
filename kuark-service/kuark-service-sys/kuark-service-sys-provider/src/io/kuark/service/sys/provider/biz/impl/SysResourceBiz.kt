package io.kuark.service.sys.provider.biz.impl

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.ability.data.rdb.support.SqlWhereExpressionFactory
import io.kuark.base.bean.BeanKit
import io.kuark.base.log.LogFactory
import io.kuark.base.query.enums.Operator
import io.kuark.base.query.sort.Direction
import io.kuark.base.support.Consts
import io.kuark.base.support.payload.ListSearchPayload
import io.kuark.base.tree.TreeKit
import io.kuark.service.sys.common.vo.dict.SysResourceCacheItem
import io.kuark.service.sys.common.vo.resource.*
import io.kuark.service.sys.provider.biz.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.biz.ibiz.ISysResourceBiz
import io.kuark.service.sys.provider.cache.ResourceByIdCacheHandler
import io.kuark.service.sys.provider.cache.ResourceIdsBySubSysAndTypeCacheHandler
import io.kuark.service.sys.provider.cache.ResourceIdBySubSysAndUrlCacheHandler
import io.kuark.service.sys.provider.dao.SysResourceDao
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

    private val log = LogFactory.getLog(this::class)

    @Autowired
    private lateinit var dictItemBiz: ISysDictItemBiz

    @Autowired
    private lateinit var resourceIdsBySubSysAndTypeCacheHandler: ResourceIdsBySubSysAndTypeCacheHandler

    @Autowired
    private lateinit var resourceIdBySubSysAndUrlCacheHandler: ResourceIdBySubSysAndUrlCacheHandler

    @Autowired
    private lateinit var resourceByIdCacheHandler: ResourceByIdCacheHandler


    override fun getResources(
        subSysDictCode: String,
        resourceTypeDictCode: String
    ): List<SysResourceCacheItem> {
        val ids = resourceIdsBySubSysAndTypeCacheHandler.getResourceIds(subSysDictCode, resourceTypeDictCode)
        return resourceByIdCacheHandler.getResourcesByIds(ids).values.toList()
    }

    override fun getResourceId(subSysDictCode: String, url: String): String? {
        return resourceIdBySubSysAndUrlCacheHandler.getResourceId(subSysDictCode, url)
    }

    @Transactional
    override fun insert(any: Any): String {
        val id = super.insert(any)
        log.debug("新增id为${id}的资源。")
        // 同步缓存
        resourceIdsBySubSysAndTypeCacheHandler.syncOnInsert(any, id)
        resourceByIdCacheHandler.syncOnInsert(id)
        return id
    }

    @Transactional
    override fun update(any: Any): Boolean {
        val id = BeanKit.getProperty(any, SysResource::id.name) as String
        val sysRes = getResource(id)
        val success = super.update(any)
        if (success) {
            // 同步缓存
            val oldUrl = sysRes!!.url
            val oldSubSysDictCode = sysRes.subSysDictCode!!
            val oldResourceTypeDictCode = sysRes.resourceTypeDictCode!!
            resourceIdsBySubSysAndTypeCacheHandler.syncOnUpdate(any, id, oldSubSysDictCode, oldResourceTypeDictCode)
            resourceIdBySubSysAndUrlCacheHandler.syncOnUpdate(any, id, oldUrl)
            resourceByIdCacheHandler.syncOnUpdate(id)
        } else {
            log.error("更新id为${id}的资源失败！")
        }
        return success
    }

    @Transactional
    override fun updateActive(id: String, active: Boolean): Boolean {
        val res = SysResource {
            this.id = id
            this.active = active
        }
        val success = dao.update(res)
        if (success) {
            log.debug("更新id为${id}的资源的启用状态为${active}。")
            resourceIdsBySubSysAndTypeCacheHandler.syncOnUpdateActive(id, active)
        } else {
            log.error("更新id为${id}的资源的启用状态为${active}失败！")
        }
        return success
    }

    override fun getSimpleMenus(subSysDictCode: String): List<BaseMenuTreeNode> {
        val origMenus = getResources(subSysDictCode, ResourceType.MENU.code)
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

    override fun getMenus(subSysDictCode: String, predicate: ((SysResourceCacheItem) -> Boolean)?): List<MenuTreeNode> {
        var origMenus = getResources(subSysDictCode, ResourceType.MENU.code)
        if (predicate != null) {
            origMenus = origMenus.filter(predicate)
        }
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

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun loadDirectChildrenForTree(searchPayload: SysResourceSearchPayload): List<SysResourceTreeNode> {
        return when (if (searchPayload.level == null) Int.MAX_VALUE else searchPayload.level) {
            0 -> { // 资源类型
                val dictItems = dictItemBiz.getItemsFromCache("kuark:sys", "resource_type")
                dictItems.map {
                    SysResourceTreeNode().apply { this.id = it.itemCode;this.name = it.itemName }
                }
            }
            1 -> { // 子系统
                val dictItems = dictItemBiz.getItemsFromCache("kuark:sys", "sub_sys")
                dictItems.map {
                    SysResourceTreeNode().apply { this.id = it.itemCode;this.name = it.itemName }
                }
            }
            else -> { // 资源
                searchPayload.resourceTypeDictCode = ResourceType.MENU.code

                if (searchPayload.active == false) { // 非仅启用状态
                    searchPayload.active = null
                }
                searchPayload.returnEntityClass = SysResourceTreeNode::class
                searchPayload.pageNo = null // 不分页
                dao.search(searchPayload) { column, _ ->
                    if (column.name == SysResources.parentId.name && searchPayload.level == 2) { // 1层是资源类型，2层是子系统，从第3层开始才是SysResource
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
        val whereConditionFactory: (Column<Any>, Any?) -> ColumnDeclaring<Boolean>? = { column, _ ->
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
        val count = dao.count(listSearchPayload, whereConditionFactory)
        return Pair(result, count)
    }

    override fun <R : Any> get(id: String, returnType: KClass<R>, fetchAllParentIds: Boolean): R? {
        val result = super.get(id, returnType)
        if (result is SysResourceRecord) {
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
        // 找出组成缓存key的子系统代码和资源类型代码
        val resource = getResource(id)
        if (resource == null) {
            log.error("找不到主键为${id}的资源记录！")
            return false
        }
        val subSysDictCode = resource.subSysDictCode!!
        val resourceTypeDictCode = resource.resourceTypeDictCode!!

        // 级联删除所有孩子记录
        val childItemIds = mutableListOf<String>()
        recursionFindAllChildId(id, childItemIds)
        if (childItemIds.isNotEmpty()) {
            val success = dao.batchDelete(childItemIds) == childItemIds.size
            if (!success) {
                log.error("级联删除主键为${id}的资源记录的所有孩子记录失败！")
                return false
            } else {
                val ids = childItemIds.toMutableList()
                ids.add(id)

                ids.forEach {
                    val res = getResource(it)!!
                    resourceIdBySubSysAndUrlCacheHandler.syncOnDelete(id, res.subSysDictCode!!, res.url)
                }

                // 同步清除role_ids_by_resource_id缓存
                // 该缓存的维护在user-service服务，当前sys-service服务不依赖于user-service服务，所以这里直接用CacheKit进行操作
                val cacheName = "rbac_role_ids_by_resource_id"
                if (CacheKit.isCacheActive(cacheName)) {
                    ids.forEach {
                        log.debug("删除id为${it}的资源后，同步从${cacheName}缓存中踢除...")
                        CacheKit.evict(cacheName, it)
                        log.debug("${cacheName}缓存同步完成。")
                    }
                }

                resourceByIdCacheHandler.syncOnBatchDelete(ids)
            }
        }

        // 删除主记录
        val success = dao.deleteById(id)
        if (!success) {
            log.error("删除主键为${id}的资源记录失败！")
            return false
        }

        // 同步缓存
        resourceIdsBySubSysAndTypeCacheHandler.syncOnDelete(id, subSysDictCode, resourceTypeDictCode)

        return true
    }

    override fun getResource(id: String): SysResourceCacheItem? {
        return resourceByIdCacheHandler.getResourceById(id)
    }

    override fun getResources(ids: Collection<String>): Map<String, SysResourceCacheItem> {
        return resourceByIdCacheHandler.getResourcesByIds(ids)
    }

    override fun getResources(
        subSysDictCode: String, resourceType: ResourceType, vararg resourceIds: String
    ): List<SysResourceCacheItem> {
        val resources = getResources(subSysDictCode, resourceType.code)
        return resources.filter { it.id in resourceIds }
    }

    override fun getDirectChildrenResources(subSysDictCode: String, resourceType: ResourceType, parentId: String?): List<SysResourceCacheItem> {
        val resources = getResources(subSysDictCode, resourceType.code)
        val resList = resources.filter { it.parentId == parentId }
        return resList.sortedBy { it.seqNo }
    }

    override fun getChildrenResources(
        subSysDictCode: String,
        resourceType: ResourceType,
        parentId: String
    ): List<SysResourceCacheItem> {
        val resourceIds = resourceIdsBySubSysAndTypeCacheHandler.getResourceIds(subSysDictCode, resourceType.code)
        val resources = resourceByIdCacheHandler.getResourcesByIds(resourceIds).values
        val children = mutableListOf<SysResourceCacheItem>()
        filterChildrenRecursively(parentId, children, resources)
        return children
    }

    /**
     * 递归地过滤孩子资源
     */
    private fun filterChildrenRecursively(parentId: String, children: MutableList<SysResourceCacheItem>, resources: Collection<SysResourceCacheItem>) {
        val filteredChildren = resources.filter { it.parentId == parentId }
        children.addAll(filteredChildren)
        filteredChildren.forEach { filterChildrenRecursively(it.parentId!!, children, resources) }
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

    //endregion your codes 2

}