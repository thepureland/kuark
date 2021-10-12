package io.kuark.ability.data.rdb.support

import org.ktorm.expression.ArgumentExpression
import org.ktorm.expression.BinaryExpression
import org.ktorm.expression.BinaryExpressionType
import org.ktorm.expression.FunctionExpression
import org.ktorm.schema.BooleanSqlType
import org.ktorm.schema.ColumnDeclaring
import org.ktorm.schema.VarcharSqlType

/**
 * ColumnDeclaring操作扩展
 *
 * @author K
 * @since 1.0.0
 */


infix fun ColumnDeclaring<*>.ilike(expr: ColumnDeclaring<String>): BinaryExpression<Boolean> {
    return BinaryExpression(
        BinaryExpressionType.LIKE,
        FunctionExpression("upper", listOf(asExpression()), VarcharSqlType),
        expr.asExpression(),
        BooleanSqlType
    )
}

infix fun ColumnDeclaring<*>.ilike(value: String): BinaryExpression<Boolean> {
    return this ilike ArgumentExpression(value.uppercase(), VarcharSqlType)
}

infix fun ColumnDeclaring<*>.ieq(expr: ColumnDeclaring<String>): BinaryExpression<Boolean> {
    return BinaryExpression(
        BinaryExpressionType.EQUAL,
        FunctionExpression("upper", listOf(asExpression()), VarcharSqlType),
        expr.asExpression(),
        BooleanSqlType
    )
}

infix fun ColumnDeclaring<*>.ieq(value: String): BinaryExpression<Boolean> {
    return this ieq ArgumentExpression(value.uppercase(), VarcharSqlType)
}

infix fun ColumnDeclaring<*>.columnGt(expr: ColumnDeclaring<String>): BinaryExpression<Boolean> {
    return BinaryExpression(
        BinaryExpressionType.GREATER_THAN,
        asExpression(),
        expr.asExpression(),
        BooleanSqlType
    )
}

infix fun ColumnDeclaring<*>.columnGt(value: String): BinaryExpression<Boolean> {
    return this columnGt ArgumentExpression(value, VarcharSqlType)
}

infix fun ColumnDeclaring<*>.columnLt(expr: ColumnDeclaring<String>): BinaryExpression<Boolean> {
    return BinaryExpression(
        BinaryExpressionType.LESS_THAN,
        asExpression(),
        expr.asExpression(),
        BooleanSqlType
    )
}

infix fun ColumnDeclaring<*>.columnLt(value: String): BinaryExpression<Boolean> {
    return this columnLt ArgumentExpression(value, VarcharSqlType)
}

infix fun ColumnDeclaring<*>.columnGe(expr: ColumnDeclaring<String>): BinaryExpression<Boolean> {
    return BinaryExpression(
        BinaryExpressionType.GREATER_THAN_OR_EQUAL,
        asExpression(),
        expr.asExpression(),
        BooleanSqlType
    )
}

infix fun ColumnDeclaring<*>.columnGe(value: String): BinaryExpression<Boolean> {
    return this columnGe ArgumentExpression(value, VarcharSqlType)
}

infix fun ColumnDeclaring<*>.columnLe(expr: ColumnDeclaring<String>): BinaryExpression<Boolean> {
    return BinaryExpression(
        BinaryExpressionType.LESS_THAN_OR_EQUAL,
        asExpression(),
        expr.asExpression(),
        BooleanSqlType
    )
}

infix fun ColumnDeclaring<*>.columnLe(value: String): BinaryExpression<Boolean> {
    return this columnLe ArgumentExpression(value, VarcharSqlType)
}