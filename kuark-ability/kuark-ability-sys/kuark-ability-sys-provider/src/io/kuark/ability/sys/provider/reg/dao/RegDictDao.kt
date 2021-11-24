package io.kuark.ability.sys.provider.reg.dao

import io.kuark.ability.data.rdb.support.BaseDao
import io.kuark.ability.data.rdb.support.ColumnHelper
import io.kuark.ability.sys.common.reg.dict.RegDictRecord
import io.kuark.ability.sys.common.reg.dict.RegDictSearchPayload
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.lang.collections.CollectionKit
import io.kuark.base.lang.string.StringKit
import io.kuark.base.query.enums.Operator
import io.kuark.base.support.Consts
import io.kuark.ability.sys.provider.reg.model.po.RegDict
import io.kuark.ability.sys.provider.reg.model.table.RegDictItems
import io.kuark.ability.sys.provider.reg.model.table.RegDicts
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
open class RegDictDao : BaseDao<String, RegDict, RegDicts>() {
//endregion your codes 1

    //region your codes 2
    @Suppress(Consts.SUPPRESS_UNCHECKED_CAST)
    fun getDictIdByModuleAndType(module: String, type: String): String? {
        val list = querySource()
            .select(RegDicts.id)
            .whereWithConditions {
                it += RegDicts.module eq module
                it += RegDicts.dictType eq type
            }
            .map { row -> row[RegDicts.id] }
            .toList() as List<String>
        return if(list.isEmpty()) null else list.first()
    }

    /**
     * 分页连接查询符合条件的字典项及字典
     *
     * @param searchPayload 查询项载体
     * @return List(RegDictListRecord)
     * @author K
     * @since 1.0.0
     */
    fun pagingSearch(searchPayload: RegDictSearchPayload): List<RegDictRecord> {
        var query = leftJoinSearch(searchPayload)
        val orders = searchPayload.orders
        if (CollectionKit.isEmpty(orders)) {
            query = query.orderBy(RegDicts.module.asc(), RegDicts.dictType.asc(), RegDictItems.seqNo.asc())
        } else {
            val orderExps = mutableListOf<OrderByExpression>()
            orders!!.forEach {
                var columns = try {
                    ColumnHelper.columnOf(RegDicts, it.property)
                } catch (e: IllegalStateException) {
                    emptyMap()
                }
                if (columns.isEmpty()) {
                    columns = ColumnHelper.columnOf(RegDictItems, it.property)
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
                RegDictRecord(
                    row[RegDicts.module],
                    row[RegDicts.id]!!,
                    row[RegDicts.dictType]!!,
                    row[RegDicts.dictName],
                    row[RegDictItems.id]!!,
                    row[RegDictItems.itemCode]!!,
                    row[RegDictItems.parentId],
                    row[RegDictItems.itemName],
                    row[RegDictItems.seqNo],
                    row[RegDictItems.active]!!,
                    row[RegDictItems.remark]
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
    fun count(searchPayload: RegDictSearchPayload): Int {
        return leftJoinSearch(searchPayload).totalRecords
    }

    /**
     * 构造RegDictItems左连接RegDicts的带有where查询条件的查询对象
     *
     * @param searchPayload 查询项载体
     * @return Query
     * @author K
     * @since 1.0.0
     */
    fun leftJoinSearch(searchPayload: RegDictSearchPayload): Query {
        return database()
            .from(RegDictItems).leftJoin(RegDicts, on = RegDictItems.dictId.eq(RegDicts.id))
            .select()
            .whereWithConditions {
                if (StringKit.isNotBlank(searchPayload.id)) {
                    it += RegDictItems.id.eq(searchPayload.id!!)
                }
                if (StringKit.isNotBlank(searchPayload.parentId)) {
                    it += RegDictItems.parentId.eq(searchPayload.parentId!!)
                }
                if (searchPayload.active != null) {
                    it += RegDictItems.active.eq(searchPayload.active!!)
                }
                if (StringKit.isNotBlank(searchPayload.module)) {
                    it += whereExpr(RegDicts.module, Operator.ILIKE, searchPayload.module!!.trim())!!
                }
                if (StringKit.isNotBlank(searchPayload.dictType)) {
                    it += whereExpr(RegDicts.dictType, Operator.ILIKE, searchPayload.dictType!!.trim())!!
                }
                if (StringKit.isNotBlank(searchPayload.dictName)) {
                    it += whereExpr(RegDicts.dictName, Operator.ILIKE, searchPayload.dictName!!.trim())!!
                }
                if (StringKit.isNotBlank(searchPayload.itemCode)) {
                    it += whereExpr(RegDictItems.itemCode, Operator.ILIKE, searchPayload.itemCode!!.trim())!!
                }
                if (StringKit.isNotBlank(searchPayload.parentCode)) {
                    it += whereExpr(RegDictItems.parentCode, Operator.ILIKE, searchPayload.parentCode!!.trim())!!
                }
                if (StringKit.isNotBlank(searchPayload.itemName)) {
                    it += whereExpr(RegDictItems.itemName, Operator.ILIKE, searchPayload.itemName!!.trim())!!
                }
            }
    }

    //endregion your codes 2

}