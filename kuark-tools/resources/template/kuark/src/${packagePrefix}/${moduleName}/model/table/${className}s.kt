package ${packagePrefix}.${moduleName}.model.table

import org.ktorm.schema.*
import ${packagePrefix}.${moduleName}.model.po.${className}
<#if daoSuperClass == "MaintainableTable">
import io.kuark.ability.data.rdb.support.MaintainableTable
<#elseif daoSuperClass == "StringIdTable">
import io.kuark.ability.data.rdb.support.StringIdTable
<#elseif daoSuperClass == "IntIdTable">
import io.kuark.ability.data.rdb.support.IntIdTable
<#elseif daoSuperClass == "LongIdTable">
import io.kuark.ability.data.rdb.support.LongIdTable
</#if>

<@generateClassComment table.comment+"数据库表-实体关联对象"/>
//region your codes 1
object ${className}s : ${daoSuperClass}<${className}>("${table.name}") {
//endregion your codes 1

	<#list columns as column>
    /** ${column.comment!""} */
    var ${column.columnHumpName} = ${column.ktormSqlTypeFunName}("${column.name}").bindTo { it.${column.columnHumpName} }

	</#list>

    //region your codes 2

    //endregion your codes 2

}