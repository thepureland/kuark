package ${packagePrefix}.${moduleName}.common.vo.${lowerShortEntityName}

import io.kuark.base.support.payload.FormPayload


<@generateClassComment table.comment+"表单载体"/>
//region your codes 1
open class ${shortEntityName} : FormPayload<${pkColumn.kotlinTypeName}>() {
//endregion your codes 1

    //region your codes 2

    <#list editItemColumns as column>
    /** ${column.comment!""} */
    var ${column.columnHumpName}: ${column.kotlinTypeName}? = null

    </#list>

    //endregion your codes 2

}