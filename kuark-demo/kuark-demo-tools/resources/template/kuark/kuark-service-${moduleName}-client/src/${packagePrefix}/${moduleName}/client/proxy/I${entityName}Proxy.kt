package ${packagePrefix}.${moduleName}.client.proxy

import ${packagePrefix}.${moduleName}.common.api.I${entityName}Api
import ${packagePrefix}.${moduleName}.client.fallback.${entityName}Fallback
import org.springframework.cloud.openfeign.FeignClient


<@generateClassComment table.comment+"客户端代理接口"/>
//region your codes 1
@FeignClient(name = "${moduleName}-${lowerShortEntityName}", fallback = ${entityName}Fallback::class)
interface I${entityName}Proxy : I${entityName}Api {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}