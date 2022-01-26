package ${packagePrefix}.${moduleName}.provider.biz.ibiz

import io.kuark.base.support.biz.IBaseCrudBiz
import ${packagePrefix}.${moduleName}.provider.model.po.${entityName}


<@generateClassComment table.comment+"业务接口"/>
//region your codes 1
interface I${entityName}Biz : IBaseCrudBiz<${pkColumn.kotlinTypeName}, ${entityName}> {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}