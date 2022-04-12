package io.kuark.service.user.provider.user.biz.impl

import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.base.lang.collections.CollectionKit
import io.kuark.base.lang.string.StringKit
import io.kuark.base.query.Criterion
import io.kuark.base.query.enums.Operator
import io.kuark.base.query.sort.Direction
import io.kuark.base.support.Consts
import io.kuark.base.tree.TreeKit
import io.kuark.service.sys.common.api.ISysTenantApi
import io.kuark.service.sys.common.vo.dict.SysTenantCacheItem
import io.kuark.service.sys.common.vo.tenant.SysTenantRecord
import io.kuark.service.user.common.user.vo.organization.BaseOrganizationTreeNode
import io.kuark.service.user.common.user.vo.organization.OrganizationTreeNode
import io.kuark.service.user.common.user.vo.organization.UserOrganizationSearchPayload
import io.kuark.service.user.provider.user.biz.ibiz.IUserOrganizationBiz
import io.kuark.service.user.provider.user.dao.UserOrganizationDao
import io.kuark.service.user.provider.user.model.po.UserOrganization
import io.kuark.service.user.provider.user.model.table.UserOrganizations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 组织机构业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class UserOrganizationBiz : BaseCrudBiz<String, UserOrganization, UserOrganizationDao>(), IUserOrganizationBiz {
//endregion your codes 1

    //region your codes 2

    @Autowired
    private lateinit var sysTenantApi: ISysTenantApi

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun searchTree(searchPayload: UserOrganizationSearchPayload): List<OrganizationTreeNode> {
        searchPayload.returnEntityClass = OrganizationTreeNode::class
        if (searchPayload.active == false) { // 这里的active应理解为activeOnly
            searchPayload.active = null
        }
        val organizations = dao.search(searchPayload) as List<OrganizationTreeNode>
        return TreeKit.convertListToTree(organizations, Direction.ASC)
    }

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun lazyLoadTree(searchPayload: UserOrganizationSearchPayload): List<BaseOrganizationTreeNode> {
        if (StringKit.isBlank(searchPayload.subSysDictCode)) {
            throw IllegalArgumentException("子系统未指定！")
        }

        val result = if (StringKit.isBlank(searchPayload.tenantId)) { // 加载租户
            val tenants = sysTenantApi.getTenants(searchPayload.subSysDictCode!!)
            if (tenants.isEmpty()) { // 该子系统没有租户，直接加载组织机构
                searchPayload.returnEntityClass = BaseOrganizationTreeNode::class
                searchPayload.active = true
                searchPayload.nullProperties = listOf(UserOrganizations.parentId.name)
                dao.search(searchPayload)
            } else { // 该子系统有租户，返回租户列表
                tenants.map {
                    BaseOrganizationTreeNode().apply {
                        this.id = it.id
                        this.name = it.name
                        this.organization = false
                    }
                }
            }
        } else { // 加载组织机构
            searchPayload.returnEntityClass = BaseOrganizationTreeNode::class
            searchPayload.active = true
            if (StringKit.isBlank(searchPayload.parentId)) { // 加载租户下的组织机构
                searchPayload.nullProperties = listOf(UserOrganization::parentId.name)
            } else { // 加载组织机构的下级机构
                // do nothing
            }
            dao.search(searchPayload)
        }
        return result as List<BaseOrganizationTreeNode>
    }

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun loadTree(subSysDictCode: String, tenantId: String?): List<BaseOrganizationTreeNode> {
        if (subSysDictCode.isBlank()) {
            throw IllegalArgumentException("子系统未指定！")
        }

        var tenants: List<SysTenantCacheItem>? = null
        val tenantIds = if (StringKit.isBlank(tenantId)) {  // 加载租户
            tenants = sysTenantApi.getTenants(subSysDictCode)
            tenants.map { it.id }
        } else {
            listOf(tenantId)
        }

        val payload = UserOrganizationSearchPayload().apply {
            returnEntityClass = BaseOrganizationTreeNode::class
            this.subSysDictCode = subSysDictCode
            this.active = true
            if (tenantIds.isNotEmpty()) {
                criterions = listOf(Criterion(UserOrganization::tenantId.name, Operator.IN, tenantIds))
            }
        }
        val nodes = (dao.search(payload) as List<BaseOrganizationTreeNode>).toMutableList()

        if (CollectionKit.isNotEmpty(tenants)) {
            val basicTenants = tenants!!.map {
                BaseOrganizationTreeNode().apply {
                    this.id = it.id
                    this.name = it.name
                    this.organization = false
                }
            }
            nodes.addAll(basicTenants)
        }

        return TreeKit.convertListToTree(nodes)
    }

    override fun fetchAllParentIds(orgId: String): List<String> {
        val results = mutableListOf<String>()
        recursionFindAllParentId(orgId, results)
        results.reverse()
        return results
    }

    @Transactional
    override fun cascadeDeleteChildren(id: String): Boolean {
        val childIds = mutableListOf<String>()
        recursionFindAllChildId(id, childIds)
        if (childIds.isNotEmpty()) {
            dao.batchDelete(childIds)
        }
        //TODO 从缓存中踢除
//        if (CacheKit.isCacheActive()) {
//            val dictIds = dao.oneSearchProperty(UserOrganization::id.name, id, UserOrganization::dictId.name)
//            val dict = sysDictBiz.get(dictIds.first() as String)!!
//            CacheKit.evict(CacheNames.SYS_DICT_ITEM, "${dict.module}:${dict.dictType}") // 字典的缓存粒度为字典类型
//        }
        return dao.deleteById(id)
    }

    @Transactional
    override fun deleteById(id: String): Boolean {
        val childIds = mutableListOf<String>()
        recursionFindAllChildId(id, childIds)
        if (childIds.isNotEmpty()) {
            dao.batchDelete(childIds)
        }
        //TODO 从缓存中踢除
//        if (CacheKit.isCacheActive()) {
//            val dictIds = dao.oneSearchProperty(SysDictItem::id.name, id, SysDictItem::dictId.name)
//            val dict = sysDictBiz.get(dictIds.first() as String)!!
//            CacheKit.evict(CacheNames.SYS_DICT_ITEM, "${dict.module}:${dict.dictType}") // 字典的缓存粒度为字典类型
//        }
        return dao.deleteById(id)
    }

    private fun recursionFindAllParentId(orgId: String, results: MutableList<String>) {
        val list = dao.oneSearchProperty(UserOrganization::id.name, orgId, UserOrganization::parentId.name) //TODO 从缓存中找
        if (list.isNotEmpty()) {
            val parentId = list.first()
            if(parentId != null) {
                results.add(parentId as String)
                recursionFindAllParentId(parentId, results)
            }
        }
    }

    private fun recursionFindAllChildId(itemId: String, results: MutableList<String>) {
        val itemIds = dao.oneSearchProperty(UserOrganization::parentId.name, itemId, UserOrganization::id.name)
        itemIds.forEach { id ->
            results.add(id as String)
            recursionFindAllChildId(id, results)
        }
    }

    //endregion your codes 2

}