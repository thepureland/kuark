package ${packagePrefix}.${moduleName}.client.fallback

import ${packagePrefix}.${moduleName}.client.proxy.I${entityName}Proxy
import org.springframework.stereotype.Component


<@generateClassComment table.comment+"容错处理"/>
@Component
//region your codes 1
interface ${entityName}Fallback : I${entityName}Proxy {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}