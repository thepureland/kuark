package ${packagePrefix}.${moduleName}.common.vo.${lowerShortEntityName}

import java.io.Serializable
<#if containsIdColumnInCacheItems>
import io.kuark.base.support.IIdEntity
</#if>
<#if containsLocalDateTimeColumnInCacheItems>
import java.time.LocalDateTime
</#if>
<#if containsLocalDateColumnInCacheItems>
import java.time.LocalDate
</#if>
<#if containsLocalTimeColumnInCacheItems>
import java.time.LocalTime
</#if>
<#if containsBlobColumnInCacheItems>
import java.sql.Blob
</#if>
<#if containsClobColumnInCacheItems>
import java.sql.Clob
</#if>
<#if containsBigDecimalColumnInCacheItems>
import java.math.BigDecimal
</#if>
<#if containsRefColumnInCacheItems>
import java.sql.Ref
</#if>
<#if containsRowIdColumnInCacheItems>
import java.sql.RowId
</#if>
<#if containsSQLXMLColumnInCacheItems>
import java.sql.SQLXML
</#if>


<@generateClassComment table.comment+"缓存项"/>
//region your codes 1
<#if containsIdColumnInCacheItems>
open class ${entityName}CacheItem : IIdEntity<${pkColumn.kotlinTypeName}>, Serializable {
</#if>
<#if containsIdColumnInCacheItems == false>
open class ${entityName}CacheItem : Serializable {
</#if>
//endregion your codes 1

    //region your codes 2

    companion object {
        private const val serialVersionUID = ${serialVersionUID}
    }

    //endregion your codes 2

    <#if containsIdColumnInCacheItems>
    /** ${pkColumn.comment!""} */
    override var id: ${pkColumn.kotlinTypeName}? = null
    </#if>

    <#list cacheItemColumns as column>
    <#if column.name != pkColumn.name>
    /** ${column.comment!""} */
    var ${column.columnHumpName}: ${column.kotlinTypeName}? = null

    </#if>
    </#list>
}