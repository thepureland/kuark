package ${packagePrefix}.${moduleName}.provider.api.backend

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping


<@generateClassComment table.comment+"后端控制器"/>
@RestController
//region your codes 1
@RequestMapping("${entityName}/api")
open class ${entityName}ApiController {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}