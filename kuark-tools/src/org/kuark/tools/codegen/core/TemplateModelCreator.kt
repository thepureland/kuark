package org.kuark.tools.codegen.core

import org.kuark.base.bean.BeanKit
import org.kuark.base.lang.string.underscoreToHump
import org.kuark.data.jdbc.metadata.Column
import org.kuark.data.jdbc.metadata.RdbMetadataKit
import org.kuark.tools.codegen.vo.Config

/**
 * 模板数据模型创建者，用户可继承此类自定义要填充模板的数据
 *
 * @author K
 * @since 1.0.0
 */
open class TemplateModelCreator {

    fun create(): Map<String, Any?> {
        val tableName = CodeGeneratorContext.tableName
        val config = CodeGeneratorContext.config
        val columns = CodeGeneratorContext.columns
        val templateModel = mutableMapOf<String, Any?>()
        templateModel[Config.PROP_KEY_PACKAGE_PREFIX] = config.getPackagePrefix()
        templateModel[Config.PROP_KEY_MODULE_NAME] = config.getModuleName()
        templateModel["className"] = tableName.underscoreToHump().capitalize()
        templateModel["table"] = RdbMetadataKit.getTableByName(tableName)
        val origColumns = RdbMetadataKit.getColumnsByTableName(tableName).values
        templateModel["columns"] = origColumns
        templateModel["pkColumn"] = origColumns.first { it.isPrimaryKey }
        templateModel[Config.PROP_KEY_AUTHOR] = config.getAuthor()
        templateModel[Config.PROP_KEY_VERSION] = config.getVersion()
        val columnConfMap = columns.map { it.getColumn() to it }.toMap()
        for (origColumn in origColumns) {
            val columnName = origColumn.name.toLowerCase()
            val columnInfo = columnConfMap[columnName]
            if (columnInfo != null) {
                BeanKit.copyProperties(columnInfo, origColumn)
                origColumn.comment = columnInfo.getComment()
            }
        }
        determinPoDaoSuperClass(templateModel, origColumns)
        initOtherParameters(templateModel, templateModel["columns"] as Collection<Column>)
        return templateModel
    }

    open fun determinPoDaoSuperClass(templateModel: MutableMap<String, Any?>, origColumns: Collection<Column>) {
        val pkKotylinType = origColumns.first { it.isPrimaryKey }.kotlinType
        var poSuperClass = "IDbEntity"
        lateinit var daoSuperClass: String
        when (pkKotylinType) {
            String::class -> {
                val maintainColumns = listOf(
                    "id", "create_time", "create_user", "update_time", "update_user",
                    "is_active", "is_built_in", "remark"
                )
                if (origColumns.map { it.name }.containsAll(maintainColumns)) {
                    // 包括所有维护字段，po实现IMaintainableDbEntity，dao实现MaintainableTable
                    poSuperClass = "IMaintainableDbEntity"
                    daoSuperClass = "MaintainableTable"
                    // 过滤掉父类中已有的列
                    templateModel["columns"] = origColumns.filter { !maintainColumns.contains(it.name) }
                } else {
                    daoSuperClass = "StringIdTable"
                    templateModel["columns"] = origColumns.filter { it.name != "id" } // 过滤掉父类中已有的id列
                }
            }
            Int::class -> {
                daoSuperClass = "IntIdTable"
                templateModel["columns"] = origColumns.filter { it.name != "id" } // 过滤掉父类中已有的id列
            }
            Long::class -> {
                daoSuperClass = "LongIdTable"
                templateModel["columns"] = origColumns.filter { it.name != "id" } // 过滤掉父类中已有的id列
            }
            else -> daoSuperClass = "Table"
        }
        templateModel["poSuperClass"] = poSuperClass
        templateModel["daoSuperClass"] = daoSuperClass
    }

    open fun initOtherParameters(templateModel: MutableMap<String, Any?>, origColumns: Collection<Column>) {
        // 为了PO模板中，非kotlin类型的import
        val kotlinTypeMap = mapOf(
            "containsLocalDateTimeColumn" to java.time.LocalDateTime::class,
            "containsLocalDateColumn" to java.time.LocalDate::class,
            "containsLocalTimeColumn" to java.time.LocalTime::class,
            "containsBlobColumn" to java.sql.Blob::class,
            "containsClobColumn" to java.sql.Clob::class,
            "containsBigDecimalColumn" to java.math.BigDecimal::class,
            "containsRefColumn" to java.sql.Ref::class,
            "containsRowIdColumn" to java.sql.RowId::class,
            "containsSQLXMLColumn" to java.sql.SQLXML::class
        )
        for ((key, value) in kotlinTypeMap) templateModel[key] = origColumns.any { it.kotlinType == value }
    }

}