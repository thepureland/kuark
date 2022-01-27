package ${packagePrefix}.${moduleName}.provider.dao

import ${packagePrefix}.${moduleName}.provider.model.po.${entityName}
import ${packagePrefix}.${moduleName}.provider.model.table.${entityName}s
import org.springframework.stereotype.Repository
<#if table.type.name() == "TABLE">
import io.kuark.ability.data.rdb.support.BaseCrudDao
<#assign superDao = "BaseCrudDao">
</#if>
<#if table.type.name() == "VIEW">
import io.kuark.ability.data.rdb.support.BaseReadOnlyDao
<#assign superDao = "BaseReadOnlyDao">
</#if>


<@generateClassComment table.comment+"数据访问对象"/>
@Repository
//region your codes 1
open class ${entityName}Dao : ${superDao}<${pkColumn.kotlinTypeName}, ${entityName}, ${entityName}s>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}