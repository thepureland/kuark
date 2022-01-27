package ${packagePrefix}.${moduleName}.common.vo.${lowerShortEntityName}

import io.kuark.base.support.payload.FormPayload
<#if containsLocalDateTimeColumnInEditItems>
import java.time.LocalDateTime
</#if>
<#if containsLocalDateColumnInEditItems>
import java.time.LocalDate
</#if>
<#if containsLocalTimeColumnInEditItems>
import java.time.LocalTime
</#if>
<#if containsBlobColumnInEditItems>
import java.sql.Blob
</#if>
<#if containsClobColumnInEditItems>
import java.sql.Clob
</#if>
<#if containsBigDecimalColumnInEditItems>
import java.math.BigDecimal
</#if>
<#if containsRefColumnInEditItems>
import java.sql.Ref
</#if>
<#if containsRowIdColumnInEditItems>
import java.sql.RowId
</#if>
<#if containsSQLXMLColumnInEditItems>
import java.sql.SQLXML
</#if>


<@generateClassComment table.comment+"表单载体"/>
//region your codes 1
open class ${entityName}Payload : FormPayload<${pkColumn.kotlinTypeName}>() {
//endregion your codes 1

    //region your codes 2

    <#list editItemColumns as column>
    <#if column.name != pkColumn.name>
    /** ${column.comment!""} */
    var ${column.columnHumpName}: ${column.kotlinTypeName}? = null

    </#if>
    </#list>
    //endregion your codes 2

}