package ${packagePrefix}.${moduleName}.provider.biz.impl

import ${packagePrefix}.${moduleName}.provider.biz.ibiz.I${entityName}Biz
import ${packagePrefix}.${moduleName}.provider.model.po.${entityName}
import ${packagePrefix}.${moduleName}.provider.dao.${entityName}Dao
<#if table.type.name() == "TABLE">
import io.kuark.ability.data.rdb.biz.BaseCrudBiz
<#assign superBiz = "BaseCrudBiz">
</#if>
<#if table.type.name() == "VIEW">
import io.kuark.ability.data.rdb.biz.BaseReadOnlyBiz
<#assign superBiz = "BaseReadOnlyBiz">
</#if>
import org.springframework.stereotype.Service


<@generateClassComment table.comment+"业务"/>
@Service
//region your codes 1
open class ${entityName}Biz : ${superBiz}<${pkColumn.kotlinTypeName}, ${entityName}, ${entityName}Dao>(), I${entityName}Biz {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}