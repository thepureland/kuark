package ${packagePrefix}.${moduleName}.common.vo.${lowerShortEntityName}

import io.kuark.base.support.payload.ListSearchPayload
import kotlin.reflect.KClass


<@generateClassComment table.comment+"查询条件载体"/>
//region your codes 1
open class ${shortEntityName} : ListSearchPayload() {
//endregion your codes 1

    //region your codes 2

    override var returnEntityClass: KClass<*>? = ${entityName}Record::class

    <#list searchItemColumns as column>
    /** ${column.comment!""} */
    var ${column.columnHumpName}: ${column.kotlinTypeName}? = null

    </#list>

    //endregion your codes 2

}