package ${packagePrefix}.${moduleName}.common.vo.${lowerShortEntityName}

import io.kuark.base.support.result.IdJsonResult
<#if containsLocalDateTimeColumnInListItems>
import java.time.LocalDateTime
</#if>
<#if containsLocalDateColumnInListItems>
import java.time.LocalDate
</#if>
<#if containsLocalTimeColumnInListItems>
import java.time.LocalTime
</#if>
<#if containsBlobColumnInListItems>
import java.sql.Blob
</#if>
<#if containsClobColumnInListItems>
import java.sql.Clob
</#if>
<#if containsBigDecimalColumnInListItems>
import java.math.BigDecimal
</#if>
<#if containsRefColumnInListItems>
import java.sql.Ref
</#if>
<#if containsRowIdColumnInListItems>
import java.sql.RowId
</#if>
<#if containsSQLXMLColumnInListItems>
import java.sql.SQLXML
</#if>


<@generateClassComment table.comment+"查询记录"/>
//region your codes 1
open class ${entityName}Record : IdJsonResult<${pkColumn.kotlinTypeName}>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

    <#list listItemColumns as column>
    <#if column.name != pkColumn.name>
    /** ${column.comment!""} */
    var ${column.columnHumpName}: ${column.kotlinTypeName}? = null

    </#if>
    </#list>
}