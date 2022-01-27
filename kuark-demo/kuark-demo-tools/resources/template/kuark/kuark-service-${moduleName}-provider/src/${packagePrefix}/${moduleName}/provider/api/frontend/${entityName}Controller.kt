package ${packagePrefix}.${moduleName}.provider.api.frontend

import ${packagePrefix}.${moduleName}.common.vo.${lowerShortEntityName}.*
import ${packagePrefix}.${moduleName}.provider.biz.ibiz.I${entityName}Biz
<#if table.type.name() == "TABLE">
import io.kuark.ability.web.springmvc.BaseCrudController
<#assign superController = "BaseCrudController">
</#if>
<#if table.type.name() == "VIEW">
import io.kuark.ability.web.springmvc.BaseReadOnlyController
<#assign superController = "BaseReadOnlyController">
</#if>
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping


<@generateClassComment table.comment+"前端控制器"/>
@RestController
//region your codes 1
@RequestMapping("${entityName}")
open class ${entityName}Controller :
    ${superController}<String, I${entityName}Biz, ${entityName}SearchPayload, ${entityName}Record, ${entityName}Detail, ${entityName}Payload>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}