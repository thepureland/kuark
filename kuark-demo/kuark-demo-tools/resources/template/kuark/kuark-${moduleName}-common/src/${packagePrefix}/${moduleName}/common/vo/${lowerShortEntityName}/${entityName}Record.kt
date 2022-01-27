package ${packagePrefix}.${moduleName}.common.vo.${lowerShortEntityName}

import io.kuark.base.support.result.IdJsonResult


<@generateClassComment table.comment+"查询记录"/>
//region your codes 1
open class ${shortEntityName}Record : IdJsonResult<${pkColumn.kotlinTypeName}>() {
//endregion your codes 1

    //region your codes 2

    <#list listItemColumns as column>
    /** ${column.comment!""} */
    var ${column.columnHumpName}: ${column.kotlinTypeName}? = null

    </#list>

    //endregion your codes 2

}