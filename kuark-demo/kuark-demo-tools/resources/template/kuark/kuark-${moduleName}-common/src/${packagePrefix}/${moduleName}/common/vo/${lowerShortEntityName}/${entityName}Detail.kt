package ${packagePrefix}.${moduleName}.common.vo.${lowerShortEntityName}

import io.kuark.base.support.result.IdJsonResult


<@generateClassComment table.comment+"查询记录"/>
//region your codes 1
open class ${shortEntityName}Detail : IdJsonResult<${pkColumn.kotlinTypeName}>() {
//endregion your codes 1

    //region your codes 2

    <#list detailItemColumns as column>
    /** ${column.comment!""} */
    var ${column.columnHumpName}: ${column.kotlinTypeName}? = null

    </#list>

    //endregion your codes 2

}