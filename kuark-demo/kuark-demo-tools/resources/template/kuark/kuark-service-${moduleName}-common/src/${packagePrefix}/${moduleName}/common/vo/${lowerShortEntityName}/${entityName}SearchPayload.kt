package ${packagePrefix}.${moduleName}.common.vo.${lowerShortEntityName}

import io.kuark.base.support.payload.ListSearchPayload
import kotlin.reflect.KClass
<#if containsLocalDateTimeColumnInSearchItems>
import java.time.LocalDateTime
</#if>
<#if containsLocalDateColumnInSearchItems>
import java.time.LocalDate
</#if>
<#if containsLocalTimeColumnInSearchItems>
import java.time.LocalTime
</#if>
<#if containsBlobColumnInSearchItems>
import java.sql.Blob
</#if>
<#if containsClobColumnInSearchItems>
import java.sql.Clob
</#if>
<#if containsBigDecimalColumnInSearchItems>
import java.math.BigDecimal
</#if>
<#if containsRefColumnInSearchItems>
import java.sql.Ref
</#if>
<#if containsRowIdColumnInSearchItems>
import java.sql.RowId
</#if>
<#if containsSQLXMLColumnInSearchItems>
import java.sql.SQLXML
</#if>


<@generateClassComment table.comment+"查询条件载体"/>
//region your codes 1
open class ${entityName}SearchPayload : ListSearchPayload() {
//endregion your codes 1

    //region your codes 2

    override var returnEntityClass: KClass<*>? = ${entityName}Record::class

    <#list searchItemColumns as column>
    /** ${column.comment!""} */
    var ${column.columnHumpName}: ${column.kotlinTypeName}? = null

    </#list>
    //endregion your codes 2

}