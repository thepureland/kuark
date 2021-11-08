package io.kuark.service.sys.provider.dao

import io.kuark.ability.data.rdb.support.BaseDao
import io.kuark.ability.data.rdb.support.ColumnHelper
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.lang.collections.CollectionKit
import io.kuark.base.lang.string.StringKit
import io.kuark.base.query.enums.Operator
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.model.dict.SysDictRecord
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
    @Suppress(Consts.SUPPRESS_UNCHECKED_CAST)
    fun getDictIdByModuleAndType(module: String, type: String): String? {
        val list = querySource()
            .select(SysDicts.id)
            .whereWithConditions {
                it += SysDicts.module eq module
                it += SysDicts.dictType eq type
            }
            .map { row -> row[SysDicts.id] }
            .toList() as List<String>
        return if(list.isEmpty()) null else list.first()
    }

    /**
     * 分页连接查询符合条件的字典项及字典
     *
     * @param searchPayload 查询项载体
     * @return List(SysDictListRecord)
     * @author K
     * @since 1.0.0
     */
    fun pagingSearch(searchPayload: SysDictSearchPayload): List<SysDictRecord> {
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
        val pageNo = searchPayload.pageNo ?: 1
        val pageSize = searchPayload.pageSize ?: 10
        return query.limit((pageNo - 1) * pageSize, pageSize)
            .map { row ->
                SysDictRecord(
                    row[SysDicts.module],
                    row[SysDicts.id]!!,
                    row[SysDicts.dictType]!!,
                    row[SysDicts.dictName],
                    row[SysDictItems.id]!!,
                    row[SysDictItems.itemCode]!!,
                    row[SysDictItems.parentId],
                    row[SysDictItems.itemName],
                    row[SysDictItems.seqNo],
                    row[SysDictItems.active]!!,
                    row[SysDictItems.remark]
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

    /**
     * 构造SysDictItems左连接SysDicts的带有where查询条件的查询对象
     *
     * @param searchPayload 查询项载体
     * @return Query
     * @author K
     * @since 1.0.0
     */
    fun leftJoinSearch(searchPayload: SysDictSearchPayload): Query {
        return database()
            .from(SysDictItems).leftJoin(SysDicts, on = SysDictItems.dictId.eq(SysDicts.id))
            .select()
            .whereWithConditions {
                if (StringKit.isNotBlank(searchPayload.id)) {
                    it += SysDictItems.id.eq(searchPayload.id!!)
                }
                if (StringKit.isNotBlank(searchPayload.parentId)) {
                    it += SysDictItems.parentId.eq(searchPayload.parentId!!)
                }
                if (searchPayload.active != null) {
                    it += SysDictItems.active.eq(searchPayload.active!!)
                }
                if (StringKit.isNotBlank(searchPayload.module)) {
                    it += whereExpr(SysDicts.module, Operator.ILIKE, searchPayload.module!!.trim())
                }
                if (StringKit.isNotBlank(searchPayload.dictType)) {
                    it += whereExpr(SysDicts.dictType, Operator.ILIKE, searchPayload.dictType!!.trim())
                }
                if (StringKit.isNotBlank(searchPayload.dictName)) {
                    it += whereExpr(SysDicts.dictName, Operator.ILIKE, searchPayload.dictName!!.trim())
                }
                if (StringKit.isNotBlank(searchPayload.itemCode)) {
                    it += whereExpr(SysDictItems.itemCode, Operator.ILIKE, searchPayload.itemCode!!.trim())
                }
                if (StringKit.isNotBlank(searchPayload.parentCode)) {
                    it += whereExpr(SysDictItems.parentCode, Operator.ILIKE, searchPayload.parentCode!!.trim())
                }
                if (StringKit.isNotBlank(searchPayload.itemName)) {
                    it += whereExpr(SysDictItems.itemName, Operator.ILIKE, searchPayload.itemName!!.trim())
                }
            }
    }

    //endregion your codes 2

}