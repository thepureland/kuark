package org.kuark.demo.tools.codegen.core

import io.kuark.ability.data.rdb.metadata.Column
import io.kuark.ability.data.rdb.metadata.RdbMetadataKit
import io.kuark.ability.data.rdb.support.*
import io.kuark.base.bean.BeanKit
import io.kuark.base.lang.string.*
import io.kuark.base.support.Consts
import org.kuark.demo.tools.codegen.model.vo.Config
import java.util.*

/**
 * 模板数据模型创建者，用户可继承此类自定义要填充模板的数据
 *
 * @author K
 * @since 1.0.0
 */
open class TemplateModelCreator {

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    fun create(): Map<String, Any?> {
        val tableName = CodeGeneratorContext.tableName
        val config = CodeGeneratorContext.config
        val columns = CodeGeneratorContext.columns
        val templateModel = mutableMapOf<String, Any?>()
        templateModel[Config.PROP_KEY_PACKAGE_PREFIX] = config.getPackagePrefix()
        templateModel[Config.PROP_KEY_MODULE_NAME] = config.getModuleName()
        val entityName = tableName.underscoreToHump().capitalizeString()
        templateModel["entityName"] = entityName
        val shortEntityName = entityName.replaceFirst(config.getModuleName(), "", true)
        templateModel["shortEntityName"] = shortEntityName
        templateModel["lowerShortEntityName"] = shortEntityName.lowercase()
        templateModel["table"] = RdbMetadataKit.getTableByName(tableName)
        val origColumns = RdbMetadataKit.getColumnsByTableName(tableName).values
        templateModel["columns"] = origColumns
        templateModel["pkColumn"] = origColumns.first { it.primaryKey }
        templateModel[Config.PROP_KEY_AUTHOR] = config.getAuthor()
        templateModel[Config.PROP_KEY_VERSION] = config.getVersion()
        val columnConfMap = columns.associateBy { it.getColumn() }

        // 查询项
        val searchItemColumns = mutableListOf<Column>()
        templateModel["searchItemColumns"] = searchItemColumns
        // 列表项
        val listItemColumns = mutableListOf<Column>()
        templateModel["listItemColumns"] = listItemColumns
        // 编辑项
        val editItemColumns = mutableListOf<Column>()
        templateModel["editItemColumns"] = editItemColumns
        // 详情项
        val detailItemColumns = mutableListOf<Column>()
        templateModel["detailItemColumns"] = detailItemColumns
        // 缓存项
        val cacheItemColumns = mutableListOf<Column>()
        templateModel["cacheItemColumns"] = cacheItemColumns

        for (origColumn in origColumns) {
            val columnName = origColumn.name.lowercase()
            val columnInfo = columnConfMap[columnName]
            if (columnInfo != null) {
                BeanKit.copyProperties(columnInfo, origColumn)
                origColumn.comment = columnInfo.getComment()

                if (columnInfo.getSearchItem()) {
                    searchItemColumns.add(origColumn)
                }
                if (columnInfo.getListItem()) {
                    listItemColumns.add(origColumn)
                }
                if (columnInfo.getEditItem()) {
                    editItemColumns.add(origColumn)
                }
                if (columnInfo.getDetailItem()) {
                    detailItemColumns.add(origColumn)
                }
                if (columnInfo.getCacheItem()) {
                    cacheItemColumns.add(origColumn)
                }
            }
        }

        determinePoDaoSuperClass(templateModel, origColumns)
        initOtherParameters(templateModel, templateModel["columns"] as Collection<Column>)
        return templateModel
    }

    open fun determinePoDaoSuperClass(templateModel: MutableMap<String, Any?>, origColumns: Collection<Column>) {
        val pkKotlinType = origColumns.first { it.primaryKey }.kotlinType
        var poSuperClass = IDbEntity::class.simpleName
        lateinit var daoSuperClass: String
        when (pkKotlinType) {
            String::class -> {
                val maintainColumns = listOf(
                    MaintainableTable<*>::id.name,
                    MaintainableTable<*>::createTime.name.humpToUnderscore(false),
                    MaintainableTable<*>::createUser.name.humpToUnderscore(false),
                    MaintainableTable<*>::updateTime.name.humpToUnderscore(false),
                    MaintainableTable<*>::updateUser.name.humpToUnderscore(false),
                    MaintainableTable<*>::active.name,
                    MaintainableTable<*>::builtIn.name.humpToUnderscore(false),
                    MaintainableTable<*>::remark.name,
                )
                if (origColumns.map { it.name }.containsAll(maintainColumns)) {
                    // 包括所有维护字段，po实现IMaintainableDbEntity，dao实现MaintainableTable
                    poSuperClass = IMaintainableDbEntity::class.simpleName
                    daoSuperClass = MaintainableTable::class.simpleName!!
                    // 过滤掉父类中已有的列
                    templateModel["columns"] = origColumns.filter { !maintainColumns.contains(it.name) }
                } else {
                    daoSuperClass = StringIdTable::class.simpleName!!
                    templateModel["columns"] =
                        origColumns.filter { it.name != IDbEntity<*, *>::id.name } // 过滤掉父类中已有的id列
                }
            }
            Int::class -> {
                daoSuperClass = IntIdTable::class.simpleName!!
                templateModel["columns"] = origColumns.filter { it.name != IDbEntity<*, *>::id.name } // 过滤掉父类中已有的id列
            }
            Long::class -> {
                daoSuperClass = LongIdTable::class.simpleName!!
                templateModel["columns"] = origColumns.filter { it.name != IDbEntity<*, *>::id.name } // 过滤掉父类中已有的id列
            }
            else -> daoSuperClass = "Table"
        }
        templateModel["poSuperClass"] = poSuperClass
        templateModel["daoSuperClass"] = daoSuperClass
    }

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    open fun initOtherParameters(templateModel: MutableMap<String, Any?>, origColumns: Collection<Column>) {
        // 为了模板中，非kotlin类型的import
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

        // po中
        for ((key, value) in kotlinTypeMap)
            templateModel[key] = origColumns.any { it.kotlinType == value }

        // 查询载体类中
        val searchItemColumns = templateModel["searchItemColumns"] as List<Column>
        for ((key, value) in kotlinTypeMap)
            templateModel[key + "InSearchItems"] = searchItemColumns.any { it.kotlinType == value }

        // 列表记录类中
        val listItemColumns = templateModel["listItemColumns"] as List<Column>
        for ((key, value) in kotlinTypeMap)
            templateModel[key + "InListItems"] = listItemColumns.any { it.kotlinType == value }

        // 编辑载体类中
        val editItemColumns = templateModel["editItemColumns"] as List<Column>
        for ((key, value) in kotlinTypeMap)
            templateModel[key + "InEditItems"] = editItemColumns.any { it.kotlinType == value }

        // 详情类中
        val detailItemColumns = templateModel["detailItemColumns"] as List<Column>
        for ((key, value) in kotlinTypeMap)
            templateModel[key + "InDetailItems"] = detailItemColumns.any { it.kotlinType == value }

        // 缓存项类中
        val cacheItemColumns = templateModel["cacheItemColumns"] as List<Column>
        for ((key, value) in kotlinTypeMap)
            templateModel[key + "InCacheItems"] = cacheItemColumns.any { it.kotlinType == value }

        // 缓存项中是否包含id
        templateModel["containsIdColumnInCacheItems"] = cacheItemColumns.any { it.name.equals("id", true) }

        // serialVersionUID
        templateModel["serialVersionUID"] = RandomStringKit.randomLong() + "L"
    }

}