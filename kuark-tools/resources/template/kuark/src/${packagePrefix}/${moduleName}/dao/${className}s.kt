package ${packagePrefix}.${moduleName}.dao

import ${packagePrefix}.${moduleName}.po.${className}
<#if daoSuperClass == "MaintainableTable">
<#elseif daoSuperClass == "StringIdTable">
<#elseif daoSuperClass == "IntIdTable">
<#elseif daoSuperClass == "LongIdTable">
</#if>

<@generateClassComment table.comment+"数据库实体DAO"/>
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