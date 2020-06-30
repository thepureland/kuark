package ${basepackage}.${module}.po

import org.kuark.data.jdbc.support.IDbEntity

<@generateClassComment table.comment+"数据库实体"/>
//region your codes 1
interface ${className} : IDbEntity<${pkColumn.kotlinType}, ${className}> {
//endregion your codes 1

	//region properties
	<#list columns as column>
	/** ${column.comment} */
	var ${column.name}: ${column.kotlinType}
	</#list>
	//endregion

	//region your codes 2

	//endregion your codes 2

}