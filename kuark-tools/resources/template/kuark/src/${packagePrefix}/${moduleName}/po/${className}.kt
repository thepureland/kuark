package ${packagePrefix}.${moduleName}.po

import org.kuark.data.jdbc.support.IDbEntity
<#if includeLocalDateTimeColumn>
import java.time.LocalDateTime
</#if>
<#if includeLocalDateColumn>
import java.time.LocalDate
</#if>
<#if includeLocalTimeColumn>
import java.time.LocalTime
</#if>
<#if includeBlobColumn>
import java.sql.Blob
</#if>
<#if includeClobColumn>
import java.sql.Clob
</#if>
<#if includeBigDecimalColumn>
import java.math.BigDecimal
</#if>
<#if includeRefColumn>
import java.sql.Ref
</#if>
<#if includeRowIdColumn>
import java.sql.RowId
</#if>
<#if includeSQLXMLColumn>
import java.sql.SQLXML
</#if>


<@generateClassComment table.comment+"数据库实体"/>
//region your codes 1
interface ${className} : IDbEntity<${pkColumn.kotlinTypeName}, ${className}> {
//endregion your codes 1

	<#list columns as column>
	/** ${column.comment!""} */
	var ${column.columnHumpName}: ${column.kotlinTypeName}
	</#list>

	//region your codes 2

	//endregion your codes 2

}