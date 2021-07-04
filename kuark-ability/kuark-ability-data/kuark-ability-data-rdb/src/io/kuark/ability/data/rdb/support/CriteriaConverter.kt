package io.kuark.ability.data.rdb.support

import io.kuark.base.query.Criteria
import io.kuark.base.query.Criterion
import io.kuark.base.query.enums.Operator
import org.ktorm.dsl.*
import org.ktorm.schema.Column
import org.ktorm.schema.ColumnDeclaring
import org.ktorm.schema.Table

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
                    orExpressions.forEachIndexed { index, expression ->
                        if (index != 0) {
                            expression.or(expression)
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
        var expression = andExpressions[0]
        andExpressions.forEachIndexed { index, expression ->
            if (index != 0) {
                expression.and(expression)
            }
        }
        return expression
    }

    private fun convertCriterion(criterion: Criterion, table: Table<*>): ColumnDeclaring<Boolean> {
        val column = ColumnHelper.columnOf(table, criterion.property) as ColumnDeclaring<Any>
        val value = criterion.getValue()
        return when (criterion.operator) {
            Operator.EQ -> column.eq(value!!)
//            Operator.IEQ ->  error("未支持") //TODO ktorm怎么支持sql函数？
            Operator.EQ_P -> columnEq(column, ColumnHelper.columnOf(table, value as String) as Column<Any>)
            Operator.NE_P, Operator.LG_P -> columnNotEq(column, ColumnHelper.columnOf(table, value as String) as Column<Any>)
//            Operator.GE_P, Operator.LE_P, Operator.GT_P, Operator.LT_P -> error("未支持")
            Operator.LIKE -> column.like("%${value!!}%")
            Operator.LIKE_S -> column.like("${value!!}%")
            Operator.LIKE_E -> column.like("%${value!!}")
//            Operator.ILIKE:
//            Operator.ILIKE_S:
//            Operator.ILIKE_E:
//                return "LOWER(" + column + ") LIKE LOWER(" + valueTmpl + ")";
            Operator.IN -> handleIn(true, value!!, column)
            Operator.NOT_IN -> handleIn(false, value!!, column)
            Operator.IS_NULL -> column.isNull()
            Operator.IS_NOT_NULL -> column.isNotNull()
            Operator.IS_EMPTY -> column.eq("")
            Operator.IS_NOT_EMPTY -> column.notEq("")
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

}