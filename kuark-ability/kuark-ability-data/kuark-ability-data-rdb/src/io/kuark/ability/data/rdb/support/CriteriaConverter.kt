package io.kuark.ability.data.rdb.support

import io.kuark.base.query.Criteria
import io.kuark.base.query.Criterion
import io.kuark.base.query.enums.Operator
import org.ktorm.dsl.*
import org.ktorm.expression.ArgumentExpression
import org.ktorm.expression.BinaryExpression
import org.ktorm.expression.BinaryExpressionType
import org.ktorm.expression.FunctionExpression
import org.ktorm.schema.*

/**
 * Criteria转换器，可将Criteria转换为Ktorm查询条件表达式
 *
 * @author K
 * @since 1.0.0
 */
internal object CriteriaConverter {

    /**
     * 将Criteria转换为Ktorm的表达式
     *
     * @param criteria Criteria
     * @param table ktorm表对象
     * @return Ktorm查询条件的表达式
     * @author K
     * @since 1.0.0
     */
    fun convert(criteria: Criteria, table: Table<*>): ColumnDeclaring<Boolean> {
        val criterionGroups = criteria.getCriterionGroups()
        val andExpressions = mutableListOf<ColumnDeclaring<Boolean>>()
        criterionGroups.forEach { criterionGroup -> // 第一层元素间是AND关系
            when (criterionGroup) {
                is Array<*> -> { // 第二层元素间是OR关系
                    val orExpressions = mutableListOf<ColumnDeclaring<Boolean>>()
                    criterionGroup.forEach { groupElem ->
                        when (groupElem) {
                            is Criterion -> {
                                orExpressions.add(convertCriterion(groupElem, table))
                            }
                            is Criteria -> {
                                orExpressions.add(convert(groupElem, table))
                            }
                            else -> {
                                error("Criteria中的元素(数组中)不支持【${criterionGroup::class}】类型！")
                            }
                        }
                    }
                    var expression = orExpressions[0]
                    orExpressions.forEachIndexed { index, orExpression ->
                        if (index != 0) {
                            expression = expression.or(orExpression)
                        }
                    }
                    andExpressions.add(expression)
                }
                is Criterion -> {
                    andExpressions.add(convertCriterion(criterionGroup, table))
                }
                is Criteria -> {
                    andExpressions.add(convert(criterionGroup, table))
                }
                else -> {
                    error("Criteria中的元素不支持【${criterionGroup::class}】类型！")
                }
            }
        }
        var wholeExpression = andExpressions[0]
        andExpressions.forEachIndexed { index, andExpression ->
            if (index != 0) {
                wholeExpression = wholeExpression.and(andExpression)
            }
        }
        return wholeExpression
    }

    private fun convertCriterion(criterion: Criterion, table: Table<*>): ColumnDeclaring<Boolean> {
        val column = ColumnHelper.columnOf(table, criterion.property)[criterion.property] as Column<Any>
        val value = criterion.getValue()
        return when (criterion.operator) {
            Operator.EQ -> column.eq(value!!)
            Operator.NE, Operator.LG -> column.notEq(value!!)
            Operator.GT -> (column as Column<Comparable<Any>>).greater(value as Comparable<Any>)
            Operator.GE -> (column as Column<Comparable<Any>>).greaterEq(value as Comparable<Any>)
            Operator.LT -> (column as Column<Comparable<Any>>).less(value as Comparable<Any>)
            Operator.LE -> (column as Column<Comparable<Any>>).lessEq(value as Comparable<Any>)
            Operator.IEQ -> column.ieq(value.toString().uppercase())
            Operator.EQ_P -> columnEq(column, ColumnHelper.columnOf(table, value as String)[value] as Column<Any>)
            Operator.NE_P -> columnNotEq(column, ColumnHelper.columnOf(table, value as String)[value] as Column<Any>)
            Operator.GE_P -> column.columnGe(ColumnHelper.columnOf(table, value as String)[value] as Column<String>)
            Operator.LE_P -> column.columnLe(ColumnHelper.columnOf(table, value as String)[value] as Column<String>)
            Operator.GT_P -> column.columnGt(ColumnHelper.columnOf(table, value as String)[value] as Column<String>)
            Operator.LT_P -> column.columnLt(ColumnHelper.columnOf(table, value as String)[value] as Column<String>)
            Operator.LIKE -> column.like("%${value!!}%")
            Operator.LIKE_S -> column.like("${value!!}%")
            Operator.LIKE_E -> column.like("%${value!!}")
            Operator.ILIKE -> column.ilike("%${value!!.toString().uppercase()}%")
            Operator.ILIKE_S -> column.ilike("${value!!.toString().uppercase()}%")
            Operator.ILIKE_E -> column.ilike("%${value!!.toString().uppercase()}")
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
    private inline fun <T : Any> columnEq(
        column: ColumnDeclaring<T>, anotherColumn: Column<Any>
    ): ColumnDeclaring<Boolean> =
        column.eq(anotherColumn as Column<T>)

    // 为了解决 <T : Any> ColumnDeclaring<T>.notEq(expr: ColumnDeclaring<T>) 的泛型问题
    private inline fun <T : Any> columnNotEq(
        column: ColumnDeclaring<T>, anotherColumn: Column<*>
    ): ColumnDeclaring<Boolean> =
        column.notEq(anotherColumn as Column<T>)

    // 为了解决 <T : Any> ColumnDeclaring<T>.inList(list: Collection<T>) 的泛型问题
    private inline fun <T : Any> columnIn(column: ColumnDeclaring<T>, values: List<T>): ColumnDeclaring<Boolean> =
        column.inList(values)

    // 为了解决 <T : Any> ColumnDeclaring<T>.notInList(list: Collection<T>) 的泛型问题
    private inline fun <T : Any> columnNotIn(column: ColumnDeclaring<T>, values: List<T>): ColumnDeclaring<Boolean> =
        column.notInList(values)

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

    private infix fun ColumnDeclaring<*>.ilike(expr: ColumnDeclaring<String>): BinaryExpression<Boolean> {
        return BinaryExpression(
            BinaryExpressionType.LIKE,
            FunctionExpression("upper", listOf(asExpression()), VarcharSqlType),
            expr.asExpression(),
            BooleanSqlType
        )
    }

    private infix fun ColumnDeclaring<*>.ilike(value: String): BinaryExpression<Boolean> {
        return this ilike ArgumentExpression(value, VarcharSqlType)
    }

    private infix fun ColumnDeclaring<*>.ieq(expr: ColumnDeclaring<String>): BinaryExpression<Boolean> {
        return BinaryExpression(
            BinaryExpressionType.EQUAL,
            FunctionExpression("upper", listOf(asExpression()), VarcharSqlType),
            expr.asExpression(),
            BooleanSqlType
        )
    }

    private infix fun ColumnDeclaring<*>.ieq(value: String): BinaryExpression<Boolean> {
        return this ieq ArgumentExpression(value, VarcharSqlType)
    }

    private infix fun ColumnDeclaring<*>.columnGt(expr: ColumnDeclaring<String>): BinaryExpression<Boolean> {
        return BinaryExpression(
            BinaryExpressionType.GREATER_THAN,
            asExpression(),
            expr.asExpression(),
            BooleanSqlType
        )
    }

    private infix fun ColumnDeclaring<*>.columnGt(value: String): BinaryExpression<Boolean> {
        return this columnGt ArgumentExpression(value, VarcharSqlType)
    }

    private infix fun ColumnDeclaring<*>.columnLt(expr: ColumnDeclaring<String>): BinaryExpression<Boolean> {
        return BinaryExpression(
            BinaryExpressionType.LESS_THAN,
            asExpression(),
            expr.asExpression(),
            BooleanSqlType
        )
    }

    private infix fun ColumnDeclaring<*>.columnLt(value: String): BinaryExpression<Boolean> {
        return this columnLt ArgumentExpression(value, VarcharSqlType)
    }

    private infix fun ColumnDeclaring<*>.columnGe(expr: ColumnDeclaring<String>): BinaryExpression<Boolean> {
        return BinaryExpression(
            BinaryExpressionType.GREATER_THAN_OR_EQUAL,
            asExpression(),
            expr.asExpression(),
            BooleanSqlType
        )
    }

    private infix fun ColumnDeclaring<*>.columnGe(value: String): BinaryExpression<Boolean> {
        return this columnGe ArgumentExpression(value, VarcharSqlType)
    }

    private infix fun ColumnDeclaring<*>.columnLe(expr: ColumnDeclaring<String>): BinaryExpression<Boolean> {
        return BinaryExpression(
            BinaryExpressionType.LESS_THAN_OR_EQUAL,
            asExpression(),
            expr.asExpression(),
            BooleanSqlType
        )
    }

    private infix fun ColumnDeclaring<*>.columnLe(value: String): BinaryExpression<Boolean> {
        return this columnLe ArgumentExpression(value, VarcharSqlType)
    }

}