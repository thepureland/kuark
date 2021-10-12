package io.kuark.service.sys.provider.dao

import io.kuark.ability.data.rdb.support.BaseDao
import io.kuark.ability.data.rdb.support.ColumnHelper
import io.kuark.ability.data.rdb.support.ilike
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.lang.collections.CollectionKit
import io.kuark.base.lang.string.StringKit
import io.kuark.service.sys.common.model.dict.SysDictListRecord
import io.kuark.service.sys.common.model.dict.SysDictSearchPayload
import io.kuark.service.sys.provider.model.po.SysDict
import io.kuark.service.sys.provider.model.table.SysDictItems
import io.kuark.service.sys.provider.model.table.SysDicts
import org.ktorm.dsl.*
import org.ktorm.expression.OrderByExpression
import org.springframework.stereotype.Repository

/**
 * 字典主表数据访问对象
 *
 * @author K
 * @since 1.0.0
 */
@Repository
//region your codes 1
open class SysDictDao : BaseDao<String, SysDict, SysDicts>() {
//endregion your codes 1

    //region your codes 2
    @Suppress("UNCHECKED_CAST")
    fun searchIdsByModuleAndType(module: String, type: String): List<String> {
        return querySource()
            .select(SysDicts.id)
            .whereWithConditions {
                it += SysDicts.dictType eq type
                if (module.isNotEmpty()) {
                    it += SysDicts.module eq module
                }
            }
            .map { row -> row[SysDicts.id] }
            .toList() as List<String>
    }

    /**
     * 分页连接查询符合条件的字典项及字典
     *
     * @param searchPayload 查询参数
     * @return List(SysDictListRecord)
     * @author K
     * @since 1.0.0
     */
    fun pagingSearch(searchPayload: SysDictSearchPayload): List<SysDictListRecord> {
        var query = leftJoinSearch(searchPayload)
        val orders = searchPayload.orders
        if (CollectionKit.isEmpty(orders)) {
            query = query.orderBy(SysDicts.module.asc(), SysDicts.dictType.asc(), SysDictItems.seqNo.asc())
        } else {
            val orderExps = mutableListOf<OrderByExpression>()
            orders!!.forEach {
                var columns = try {
                    ColumnHelper.columnOf(SysDicts, it.property)
                } catch (e: IllegalStateException) {
                    emptyMap()
                }
                if (columns.isEmpty()) {
                    columns = ColumnHelper.columnOf(SysDictItems, it.property)
                }
                if (columns.isEmpty()) {
                    throw ObjectNotFoundException("根据属性【${it.property}】找不到对应的列!")
                }
                val column = columns[it.property]!!
                if (it.isAscending()) {
                    orderExps.add(column.asc())
                } else {
                    orderExps.add(column.desc())
                }
            }
            query = query.orderBy(*orderExps.toTypedArray())
        }
        return query.limit((searchPayload.pageNo - 1) * searchPayload.pageSize, searchPayload.pageSize)
            .map { row ->
                SysDictListRecord(
                    row[SysDicts.module],
                    row[SysDicts.dictType]!!,
                    row[SysDicts.dictName],
                    row[SysDictItems.dictId]!!,
                    row[SysDictItems.itemCode]!!,
                    row[SysDictItems.parentCode],
                    row[SysDictItems.itemName],
                    row[SysDictItems.seqNo],
                    row[SysDictItems.isActive]!!,
                )
            }
    }

    /**
     * 连接查询符合条件的字典项及字典的数量
     *
     * @param searchPayload 查询参数
     * @return 结果数
     * @author K
     * @since 1.0.0
     */
    fun count(searchPayload: SysDictSearchPayload): Int {
        return leftJoinSearch(searchPayload).totalRecords
    }

    private fun leftJoinSearch(searchPayload: SysDictSearchPayload): Query {
        return database()
            .from(SysDictItems).leftJoin(SysDicts, on = SysDictItems.dictId.eq(SysDicts.id))
            .select()
            .whereWithConditions {
                if (StringKit.isNotBlank(searchPayload.module)) {
                    it += SysDicts.module.ilike("%${searchPayload.module!!.trim()}%")
                }
                if (StringKit.isNotBlank(searchPayload.dictType)) {
                    it += SysDicts.dictType.ilike("%${searchPayload.dictType!!.trim()}%")
                }
                if (StringKit.isNotBlank(searchPayload.dictName)) {
                    it += SysDicts.dictName.ilike("%${searchPayload.dictName!!.trim()}%")
                }
                if (StringKit.isNotBlank(searchPayload.itemCode)) {
                    it += SysDictItems.itemCode.ilike("%${searchPayload.itemCode!!.trim()}%")
                }
                if (StringKit.isNotBlank(searchPayload.itemName)) {
                    it += SysDictItems.itemName.ilike("%${searchPayload.itemName!!.trim()}%")
                }
                if (searchPayload.isActive != null) {
                    it += SysDictItems.isActive.eq(searchPayload.isActive!!)
                }
            }
    }

    //endregion your codes 2

}