package io.kuark.ability.data.rdb.support

import io.kuark.base.query.enums.Operator
import org.ktorm.dsl.*
import org.ktorm.schema.Column
import org.ktorm.schema.ColumnDeclaring

/**
 * 查询条件表达式工厂
 *
 * @author K
 * @since 1.0.0
 */
object SqlWhereExpressionFactory {

    /**
     * 创建查询条件表达式
     *
     * @param column 列对象
     * @param operator 操作符
     * @param value 要查询的值
     * @return 列申明对象
     * @author K
     * @since 1.0.0
     */
    @Suppress("UNCHECKED_CAST")
    fun create(column: Column<Any>, operator: Operator, value: Any?): ColumnDeclaring<Boolean> {
        return when (operator) {
            Operator.EQ -> column.eq(value!!)
            Operator.NE, Operator.LG -> column.notEq(value!!)
            Operator.GT -> (column as Column<Comparable<Any>>).greater(value as Comparable<Any>)
            Operator.GE -> (column as Column<Comparable<Any>>).greaterEq(value as Comparable<Any>)
            Operator.LT -> (column as Column<Comparable<Any>>).less(value as Comparable<Any>)
            Operator.LE -> (column as Column<Comparable<Any>>).lessEq(value as Comparable<Any>)
            Operator.IEQ -> column.ieq(value.toString().uppercase())
            Operator.EQ_P -> columnEq(
                column, ColumnHelper.columnOf(column.table, value as String)[value] as Column<Any>
            )
            Operator.NE_P -> columnNotEq(
                column, ColumnHelper.columnOf(column.table, value as String)[value] as Column<Any>
            )
            Operator.GE_P -> column.columnGe(
                ColumnHelper.columnOf(column.table, value as String)[value] as Column<String>
            )
            Operator.LE_P -> column.columnLe(
                ColumnHelper.columnOf(column.table, value as String)[value] as Column<String>
            )
            Operator.GT_P -> column.columnGt(
                ColumnHelper.columnOf(column.table, value as String)[value] as Column<String>
            )
            Operator.LT_P -> column.columnLt(
                ColumnHelper.columnOf(column.table, value as String)[value] as Column<String>
            )
            Operator.LIKE -> column.like("%${value!!}%")
            Operator.LIKE_S -> column.like("${value!!}%")
            Operator.LIKE_E -> column.like("%${value!!}")
            Operator.ILIKE -> column.ilike("%${value!!}%")
            Operator.ILIKE_S -> column.ilike("${value!!}%")
            Operator.ILIKE_E -> column.ilike("%${value!!}")
            Operator.IN -> handleIn(true, value!!, column)
            Operator.NOT_IN -> handleIn(false, value!!, column)
            Operator.IS_NULL -> column.isNull()
            Operator.IS_NOT_NULL -> column.isNotNull()
            Operator.IS_EMPTY -> column.eq("")
            Operator.IS_NOT_EMPTY -> column.notEq("")
            Operator.BETWEEN -> (column as Column<Comparable<Any>>).between(value as ClosedRange<Comparable<Any>>)
            Operator.BETWEEN -> (column as Column<Comparable<Any>>).notBetween(value as ClosedRange<Comparable<Any>>)
            else -> error("未支持")
        }
    }

    // 为了解决 <T : Any> ColumnDeclaring<T>.eq(expr: ColumnDeclaring<T>) 的泛型问题
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> columnEq(
        column: ColumnDeclaring<T>, anotherColumn: Column<Any>
    ): ColumnDeclaring<Boolean> =
        column.eq(anotherColumn as Column<T>)

    // 为了解决 <T : Any> ColumnDeclaring<T>.notEq(expr: ColumnDeclaring<T>) 的泛型问题
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> columnNotEq(
        column: ColumnDeclaring<T>, anotherColumn: Column<*>
    ): ColumnDeclaring<Boolean> =
        column.notEq(anotherColumn as Column<T>)

    // 为了解决 <T : Any> ColumnDeclaring<T>.inList(list: Collection<T>) 的泛型问题
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> columnIn(column: ColumnDeclaring<T>, values: List<T>): ColumnDeclaring<Boolean> =
        column.inList(values)

    // 为了解决 <T : Any> ColumnDeclaring<T>.notInList(list: Collection<T>) 的泛型问题
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> columnNotIn(column: ColumnDeclaring<T>, values: List<T>): ColumnDeclaring<Boolean> =
        column.notInList(values)

    @Suppress("UNCHECKED_CAST")
    private fun handleIn(isIn: Boolean, value: Any, column: ColumnDeclaring<Any>): ColumnDeclaring<Boolean> {
        var values = value
        if (values !is List<*> && values !is Array<*>) {
            values = arrayOf(values)
        }
        if (values is Array<*>) {
            values = listOf(*values)
        }
        return if (isIn) {
            columnIn(column, values as List<Any>)
        } else {
            columnNotIn(column, values as List<Any>)
        }
    }

}