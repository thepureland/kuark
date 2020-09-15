package ${packagePrefix}.${moduleName}.po

import io.kuark.data.jdbc.support.DbEntityFactory
<#if poSuperClass == "IDbEntity">
<#elseif poSuperClass == "IMaintainableDbEntity">
</#if>
<#if containsLocalDateTimeColumn>
</#if>
<#if containsLocalDateColumn>
</#if>
<#if containsLocalTimeColumn>
</#if>
<#if containsBlobColumn>
</#if>
<#if containsClobColumn>
</#if>
<#if containsBigDecimalColumn>
</#if>
<#if containsRefColumn>
</#if>
<#if containsRowIdColumn>
</#if>
<#if containsSQLXMLColumn>
</#if>

<@generateClassComment table.comment+"数据库实体"/>
//region your codes 1
interface ${className} : ${poSuperClass}<${pkColumn.kotlinTypeName}, ${className}> {
//endregion your codes 1

    companion object : DbEntityFactory<${className}>()

	<#list columns as column>
    /** ${column.comment!""} */
    var ${column.columnHumpName}: ${column.kotlinTypeName}

	</#list>

    //region your codes 2

    //endregion your codes 2

}