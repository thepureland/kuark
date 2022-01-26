package ${packagePrefix}.${moduleName}.provider.api.frontend

import io.kuark.ability.web.springmvc.BaseReadOnlyController
import ${packagePrefix}.${moduleName}.common.vo.${lowerShortEntityName}.*
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping


<@generateClassComment table.comment+"前端控制器"/>
@RestController
//region your codes 1
@RequestMapping("${entityName}")
open class ${entityName}Controller: BaseCrudController<String, I${entityName}Biz, ${entityName}SearchPayload, ${entityName}Record, ${entityName}Detail, ${entityName}Payload>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}