package $

import org.ktorm.dsl.*
import org.springframework.stereotype.Repository

{packagePrefix}.${moduleName}.dao

<#if table.type.name() == "TABLE">
<#assign superDao = "BaseDao">
</#if>
<#if table.type.name() == "VIEW">
<#assign superDao = "BaseReadOnlyDao">
</#if>
import ${packagePrefix}.${moduleName}.model.po.${className}
import ${packagePrefix}.${moduleName}.model.table.${className}s

<@generateClassComment table.comment+"数据访问对象"/>
@Repository
//region your codes 1
open class ${className}Dao : ${superDao}<${pkColumn.kotlinTypeName}, ${className}, ${className}s>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}