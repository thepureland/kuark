package io.kuark.ability.data.rdb.support

import io.kuark.base.lang.string.humpToUnderscore
import org.ktorm.schema.Column
import org.ktorm.schema.Table
import java.util.*

object ColumnHelper {

    /**
     * 列信息缓存 Map(表名，Map(属性名, 列对象))
     */
    private val columnCache: MutableMap<String, MutableMap<String, Column<Any>>> = mutableMapOf()

    /**
     * 根据属性名得到列对象
     *
     * @param table ktorm表对象
     * @param propertyNames 属性名可变数组
     * @return 列对象数组
     */
    fun columnOf(table: Table<*>, vararg propertyNames: String): Map<String, Column<Any>> { //TODO 是否ktorm能从列绑定关系直接取?
        if (propertyNames.isEmpty()) return emptyMap()

        val tableName = table!!.tableName
        var columnMap = columnCache[tableName]
        if (columnMap == null) {
            columnMap = mutableMapOf()
            columnCache[tableName] = columnMap
        }

        val resultMap = linkedMapOf<String, Column<Any>>()
        propertyNames.forEach { propertyName ->
            if (columnMap.containsKey(propertyName)) {
                resultMap[propertyName] = columnMap[propertyName]!!
            } else {
                var columnName = propertyName.humpToUnderscore(false)
                var column: Column<*>?
                try {
                    column = table!![columnName] // 1.先尝试以小写字段名获取
                } catch (e: NoSuchElementException) {
                    columnName = columnName.uppercase(Locale.getDefault())
                    column = try {
                        table!![columnName] // 2.再尝试以大写字段名获取
                    } catch (e: NoSuchElementException) {
                        // 3.最后忽略大小写的分别比较下划线分割的列名、属性名
                        table!!.columns.firstOrNull {
                            it.name.equals(columnName, true) || it.name.equals(
                                propertyName,
                                true
                            )
                        }
                    }
                }
                if (column == null) {
                    error("无法推测属性【${propertyName}】在表【${tableName}】中的字段名！")
                } else {
                    resultMap[propertyName] = column as Column<Any>
                    columnMap[propertyName] = resultMap[propertyName]!!
                }
            }

        }
        return resultMap
    }

}