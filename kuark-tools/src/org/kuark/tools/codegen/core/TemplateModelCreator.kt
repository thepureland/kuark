package org.kuark.tools.codegen.core

import org.kuark.base.bean.BeanKit
import org.kuark.base.lang.string.underscoreToHump
import org.kuark.data.jdbc.metadata.Column
import org.kuark.data.jdbc.metadata.RdbMetadataKit
import org.kuark.tools.codegen.vo.ColumnInfo
import org.kuark.tools.codegen.vo.Config

class TemplateModelCreator(
    private val config: Config,
    private val tableName: String,
    private val columns: List<ColumnInfo>
) {

    fun create(): Map<String, Any?> {
        val templateModel = mutableMapOf<String, Any?>()
        templateModel[Config.PROP_KEY_PACKAGE_PREFIX] = config.getPackagePrefix()
        templateModel[Config.PROP_KEY_MODULE_NAME] = config.getModuleName()
        templateModel[Config.CLASS_NAME] = tableName.underscoreToHump().capitalize()
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
        initOtherParameters(templateModel, origColumns)
        return templateModel
    }

    private fun initOtherParameters(templateModel: MutableMap<String, Any?>, origColumns: Collection<Column>) {
        val map = mapOf(
            "includeLocalDateTimeColumn" to java.time.LocalDateTime::class,
            "includeLocalDateColumn" to java.time.LocalDate::class,
            "includeLocalTimeColumn" to java.time.LocalTime::class,
            "includeBlobColumn" to java.sql.Blob::class,
            "includeClobColumn" to java.sql.Clob::class,
            "includeBigDecimalColumn" to java.math.BigDecimal::class,
            "includeRefColumn" to java.sql.Ref::class,
            "includeRowIdColumn" to java.sql.RowId::class,
            "includeSQLXMLColumn" to java.sql.SQLXML::class
        )
        for ((key, value) in map) templateModel[key] = origColumns.any { it.kotlinType == value }
    }

}