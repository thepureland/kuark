package ${packagePrefix}.${moduleName}.dao

import io.kuark.ability.data.rdb.support.BaseDao
import ${packagePrefix}.${moduleName}.model.po.${className}
import ${packagePrefix}.${moduleName}.model.table.${className}s
import org.ktorm.dsl.*
import org.springframework.stereotype.Repository

<@generateClassComment table.comment+"数据访问对象"/>
@Repository
//region your codes 1
class ${className}Dao: BaseDao<${pkColumn.kotlinTypeName}, ${className}, ${className}s>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}