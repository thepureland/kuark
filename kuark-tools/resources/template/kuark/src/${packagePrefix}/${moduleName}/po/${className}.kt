package ${packagePrefix}.${moduleName}.po

<#if poSuperClass == "IDbEntity">
import org.kuark.data.jdbc.support.IDbEntity
<#elseif poSuperClass == "IMaintainableDbEntity">
import org.kuark.data.jdbc.support.IMaintainableDbEntity
</#if>
<#if containsLocalDateTimeColumn>
import java.time.LocalDateTime
</#if>
<#if containsLocalDateColumn>
import java.time.LocalDate
</#if>
<#if containsLocalTimeColumn>
import java.time.LocalTime
</#if>
<#if containsBlobColumn>
import java.sql.Blob
</#if>
<#if containsClobColumn>
import java.sql.Clob
</#if>
<#if containsBigDecimalColumn>
import java.math.BigDecimal
</#if>
<#if containsRefColumn>
import java.sql.Ref
</#if>
<#if containsRowIdColumn>
import java.sql.RowId
</#if>
<#if containsSQLXMLColumn>
import java.sql.SQLXML
</#if>

<@generateClassComment table.comment+"数据库实体"/>
//region your codes 1
interface ${className}: ${poSuperClass}<${pkColumn.kotlinTypeName}, ${className}> {
//endregion your codes 1

	<#list columns as column>
	/** ${column.comment!""} */
	var ${column.columnHumpName}: ${column.kotlinTypeName}
	</#list>

	//region your codes 2

	//endregion your codes 2

}