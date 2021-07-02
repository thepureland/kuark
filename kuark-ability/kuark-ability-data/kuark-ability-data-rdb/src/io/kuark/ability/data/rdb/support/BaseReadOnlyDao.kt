package io.kuark.ability.data.rdb.support

import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.base.lang.GenericKit
import io.kuark.base.lang.string.humpToUnderscore
import io.kuark.base.query.Criteria
import io.kuark.base.query.Criterion
import io.kuark.base.query.sort.Order
import io.kuark.base.support.GroupExecutor
import io.kuark.base.support.logic.AndOr
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.ktorm.expression.OrderByExpression
import org.ktorm.schema.Column
import org.ktorm.schema.ColumnDeclaring
import org.ktorm.schema.Table
import java.util.*
import kotlin.reflect.KClass

/**
 * 基础只读数据访问对象，封装某数据库表的通用查询操作
 *
 * @param PK 实体主键类型
 * @param E 实体类型
 * @param T 数据库表-实体关联对象的类型
 * @author K
 * @since 1.0.0
 */
open class BaseReadOnlyDao<PK : Any, E : IDbEntity<PK, E>, T : Table<E>> {

    /** 数据库表-实体关联对象 */
    protected var table: T? = null

    /**
     * 返回数据库表-实体关联对象
     *
     * @return 数据库表-实体关联对象
     * @author K
     * @since 1.0.0
     */
    fun table(): T {
        if (table == null) {
            val tableClass = GenericKit.getSuperClassGenricClass(this::class, 2) as KClass<T>
            table = tableClass.objectInstance!!
        }
        return table!!
    }

    /**
     * 返回当前表所在的数据库对象
     *
     * @return 当前表所在的数据库对象
     * @author K
     * @since 1.0.0
     */
    fun database(): Database = RdbKit.getDatabase()

    /**
     * 返回T指定的表的查询源，基于该对象可以进行类似对数据库表的sql一样操作
     *
     * @return 查询源
     * @author K
     * @since 1.0.0
     */
    fun querySource(): QuerySource = database().from(table())

    /**
     * 返回T指定的表的实体序列，基于该序列可以进行类似对集合一样的操作
     *
     * @return 实体序列
     * @author K
     * @since 1.0.0
     */
    fun entitySequence(): EntitySequence<E, T> = database().sequenceOf(table())

    //region Search

    /**
     * 查询指定主键值的实体
     *
     * @param id 主键值，类型必须为以下之一：String、Int、Long
     * @return 实体
     * @throws NoSuchElementException 不存在指定主键对应的实体时
     * @author K
     * @since 1.0.0
     */
    fun getById(id: PK): E? {
        return entitySequence().firstOrNull { (table!!.primaryKeys[0] as Column<PK>) eq id }
    }

    /**
     * 批量查询指定主键值的实体
     *
     * @param ids 主键值可变参数，元素类型必须为以下之一：String、Int、Long，为空时返回空列表
     * @param countOfEachBatch 每批大小，缺省为1000
     * @return 实体列表，ids为空时返回空列表
     * @author K
     * @since 1.0.0
     */
    fun getByIds(vararg ids: PK, countOfEachBatch: Int = 1000): List<E> {
        if (ids.isEmpty()) return listOf()
        val results = mutableListOf<E>()
        GroupExecutor(listOf(ids), countOfEachBatch) {
            val result =
                entitySequence().filter { (table!!.primaryKeys[0] as ColumnDeclaring<PK>).inList(*ids) }.toList()
            results.addAll(result)
        }.execute()
        return results
    }

    /**
     * 查询当前表的实体。对于大表慎用，容易造成内存溢出！
     *
     * @return 实体列表
     * @author K
     * @since 1.0.0
     */
    fun searchAll(): List<E> = entitySequence().toList()

    //endregion Search

    /**
     * 返回当前表的总记录数
     *
     * @return 当前表的总记录数
     * @author K
     * @since 1.0.0
     */
    fun countAll(): Int {
//        return querySource().select(count(table()["id"])).map { row -> row.getInt(1) }[0]
        return entitySequence().count()
    }


    //region oneSearch
    /**
     * 根据单个属性查询
     *
     * @param property 属性名
     * @param value    属性值
     * @param orders   排序规则
     * @return 指定类名对象的结果列表
     */
    fun oneSearch(property: String, value: Any?, vararg orders: Order): List<E> {
        return doSearchEntity(mapOf(property to value), null, *orders)
    }

    /**
     * 根据单个属性查询，只返回指定的单个属性的列表
     *
     * @param property       属性名
     * @param value          属性值
     * @param returnProperty 返回的属性名
     * @param orders         排序规则
     * @return List(属性值)
     */
    fun oneSearchProperty(property: String, value: Any?, returnProperty: String, vararg orders: Order): List<*> {
        return doSearchProperties(mapOf(property to value), null, listOf(returnProperty), *orders)
    }

    /**
     * 根据单个属性查询，只返回指定属性的列表
     *
     * @param property         属性名
     * @param value            属性值
     * @param returnProperties 返回的属性名称集合
     * @param orders           排序规则
     * @return List(Map(属性名, 属性值)), 一个Map封装一个记录
     */
    fun oneSearchProperties(
        property: String, value: Any?, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, *>> {
        return doSearchProperties(mapOf(property to value), null, returnProperties, *orders)
    }

    //endregion oneSearch


    //region allSearch
    /**
     * 查询所有结果
     *
     * @param orders 排序规则
     * @return 实体对象列表
     */
    fun allSearch(vararg orders: Order): List<E> {
        return entitySequence().toList()
    }

    /**
     * 查询所有结果，只返回指定的单个属性的列表
     *
     * @param returnProperty 属性名
     * @param orders         排序规则
     * @return List(属性值)
     */
    fun allSearchProperty(returnProperty: String, vararg orders: Order): List<*> {
        return doSearchProperties(null, null, listOf(returnProperty), *orders)
    }

    /**
     * 查询所有结果，只返回指定属性的列表
     *
     * @param returnProperties 属性名称集合
     * @param orders           排序规则
     * @return List(Map(属性名, 属性值))
     */
    fun allSearchProperties(returnProperties: Collection<String>, vararg orders: Order): List<Map<String, *>> {
        return doSearchProperties(null, null, returnProperties, *orders)
    }

    //endregion allSearch


    //region andSearch
    /**
     * 根据多个属性进行and条件查询，返回实体类对象的列表
     *
     * @param properties Map(属性名，属性值)
     * @param orders     排序规则
     * @return 实体对象列表
     */
    fun andSearch(properties: Map<String, *>, vararg orders: Order): List<E> {
        return doSearchEntity(properties, AndOr.AND, *orders)
    }

    /**
     * 根据多个属性进行and条件查询，只返回指定的单个属性的列表
     *
     * @param properties     Map(属性名，属性值）
     * @param returnProperty 要返回的属性名
     * @param orders         排序规则
     * @return List(指定的属性的值)
     */
    fun andSearchProperty(properties: Map<String, *>, returnProperty: String, vararg orders: Order): List<*> {
        return doSearchProperties(properties, AndOr.AND, listOf(returnProperty), *orders)
    }

    /**
     * 根据多个属性进行and条件查询，只返回指定属性的列表
     *
     * @param properties       Map(属性名，属性值)
     * @param returnProperties 要返回的属性名集合
     * @param orders           排序规则
     * @return List(Map(指定的属性名，属性值))
     */
    fun andSearchProperties(
        properties: Map<String, *>, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, *>> {
        return doSearchProperties(properties, AndOr.AND, returnProperties, *orders)
    }

    //endregion andSearch


    //region orSearch
    /**
     * 根据多个属性进行or条件查询，返回实体类对象的列表
     *
     * @param properties Map(属性名，属性值)
     * @param orders     排序规则
     * @return 实体对象列表
     */
    fun orSearch(properties: Map<String, *>, vararg orders: Order): List<E> {
        return doSearchEntity(properties, AndOr.OR, *orders)
    }

    /**
     * 根据多个属性进行or条件查询，只返回指定的单个属性的列表
     *
     * @param properties     Map(属性名，属性值)
     * @param returnProperty 要返回的属性名
     * @param orders         排序规则
     * @return List(指定的属性的值)
     */
    fun orSearchProperty(properties: Map<String, *>, returnProperty: String, vararg orders: Order): List<*> {
        return doSearchProperties(properties, AndOr.OR, listOf(returnProperty), *orders)
    }

    /**
     * 根据多个属性进行or条件查询，只返回指定的属性的列表
     *
     * @param properties       Map(属性名，属性值)
     * @param returnProperties 要返回的属性名集合
     * @param orders           排序规则
     * @return List(Map(指定的属性名，属性值))
     */
    fun orSearchProperties(
        properties: Map<String, *>, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, *>> {
        return doSearchProperties(properties, AndOr.OR, returnProperties, *orders)
    }

    //endregion orSearch


    //region inSearch
    /**
     * in查询，返回实体类对象列表
     *
     * @param property 属性名
     * @param values   in条件值集合
     * @param orders   排序规则
     * @return 指定类名对象的结果列表
     */
    fun inSearch(property: String, values: List<*>, vararg orders: Order): List<E> {
        val column = columnOf(property)[0]
        val entitySequence = entitySequence().filter { column.inList(values) }
        entitySequence.sorted { sortOf(*orders) }
        return entitySequence.toList()
    }

    /**
     * in查询，只返回指定的单个属性的值
     *
     * @param property       属性名
     * @param values         in条件值集合
     * @param returnProperty 要返回的属性名
     * @param orders         排序规则
     * @return 指定属性的值列表
     */
    fun inSearchProperty(property: String, values: List<*>, returnProperty: String, vararg orders: Order): List<*> {
        return doInSearchProperties(property, values, listOf(returnProperty), *orders)
    }

    /**
     * in查询，只返回指定属性的值
     *
     * @param property         属性名
     * @param values           in条件值集合
     * @param returnProperties 要返回的属性名集合
     * @param orders           排序规则
     * @return List(Map(指定的属性名，属性值))
     */
    fun inSearchProperties(
        property: String, values: List<*>, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, *>> {
        return doInSearchProperties(property, values, returnProperties, *orders)
    }

    /**
     * 主键in查询，返回实体类对象列表
     *
     * @param values 主键值集合
     * @param orders 排序规则
     * @param orders 排序规则
     * @return 实体对象列表
     */
    fun inSearchById(values: List<PK>, vararg orders: Order): List<E> {
        return inSearch("id", values, *orders)
    }

    /**
     * 主键in查询，只返回指定的单个属性的值
     *
     * @param values         主键值集合
     * @param returnProperty 要返回的属性名
     * @param orders         排序规则
     * @return 指定属性的值列表
     */
    fun inSearchPropertyById(values: List<PK>, returnProperty: String, vararg orders: Order): List<*> {
        return doInSearchProperties("id", values, listOf(returnProperty), *orders)
    }

    /**
     * 主键in查询，只返回指定属性的值
     *
     * @param values           主键值集合
     * @param returnProperties 要返回的属性名集合
     * @param orders           排序规则
     * @return List(Map(指定的属性名, 属性值))
     */
    fun inSearchPropertiesById(
        values: List<PK>, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, *>> {
        return doInSearchProperties("id", values, returnProperties, *orders)
    }

    //endregion inSearch
//
//
//    //region search Criteria
//    /**
//     * 复杂条件查询
//     *
//     * @param criteria 查询条件
//     * @param orders   排序规则
//     * @return 实体对象列表
//     */
//    fun search(criteria: Criteria, vararg orders: Order): List<T>
//
//    /**
//     * 复杂条件查询，只返回指定单个属性的值
//     *
//     * @param criteria       查询条件
//     * @param returnProperty 要返回的属性名
//     * @param orders         排序规则
//     * @return 指定属性的值列表
//     */
//    fun searchProperty(criteria: Criteria, returnProperty: String, vararg orders: Order): List<*>
//
//    /**
//     * 复杂条件，只返回指定多个属性的值
//     *
//     * @param criteria         查询条件
//     * @param returnProperties 要返回的属性名集合
//     * @param orders           排序规则
//     * @return List(Map(指定的属性名 属性值))
//     */
//    fun searchProperties(
//        criteria: Criteria, returnProperties: Collection<String>, vararg orders: Order
//    ): List<Map<String, Any?>>
//
//    //endregion search Criteria
//
//
//    //region pagingSearch
//    /**
//     * 分页查询，返回对象列表
//     *
//     * @param criteria 查询条件
//     * @param pageNo   当前页码(从1开始)
//     * @param pageSize 每页条数
//     * @param orders   排序规则
//     * @return 实体对象列表
//     */
//    fun pagingSearch(criteria: Criteria, pageNo: Int, pageSize: Int, vararg orders: Order): List<T>
//
//    /**
//     * 分页查询，返回对象列表,返回只包含指定属性
//     *
//     * @param criteria 查询条件
//     * @param pageNo   当前页码(从1开始)
//     * @param pageSize 每页条数
//     * @param orders   排序规则
//     * @return 实体对象列表
//     */
//    fun pagingReturnProperty(
//        criteria: Criteria, returnProperty: String, pageNo: Int, pageSize: Int, vararg orders: Order
//    ): List<*>
//
//    /**
//     * 分页查询，返回对象列表,返回只包含指定属性
//     *
//     * @param criteria 查询条件
//     * @param pageNo   当前页码(从1开始)
//     * @param pageSize 每页条数
//     * @param orders   排序规则
//     * @return 实体对象列表
//     */
//    fun pagingReturnProperties(
//        criteria: Criteria, returnProperties: Collection<String>, pageNo: Int, pageSize: Int, vararg orders: Order
//    ): List<Map<String?, Any?>>
//
//    //endregion pagingSearch
//
//
//    /**
//     * 计算记录数
//     *
//     * @param criteria 查询条件
//     * @return 记录数
//     */
//    fun count(criteria: Criteria): Long
//
//    /**
//     * 求和. 对满足条件的记录根据指定属性进行求和
//     *
//     * @param criteria 查询条件
//     * @param property 待求和的属性
//     * @return 和
//     */
//    fun sum(criteria: Criteria, property: String): Number
//
//    /**
//     * 求平均值. 对满足条件的记录根据指定属性进行求平均值
//     *
//     * @param criteria 查询条件
//     * @param property 待求平均值的属性
//     * @return 平均值
//     */
//    fun avg(criteria: Criteria, property: String): Number
//
//    /**
//     * 求最大值. 对满足条件的记录根据指定属性进行求最大值
//     *
//     * @param criteria 查询条件
//     * @param property 待求最大值的属性
//     * @return 最大值
//     */
//    fun max(criteria: Criteria, property: String): Any?
//
//    /**
//     * 求最小值. 对满足条件的记录根据指定属性进行求最小值
//     *
//     * @param criteria 查询条件
//     * @param property 待求最小值的属性
//     * @return 最小值
//     */
//    fun min(criteria: Criteria, property: String): Any?

    companion object {

        /**
         * 列信息缓存 Map(表名，Map(属性名, 列对象))
         */
        private val columnCache: MutableMap<String, MutableMap<String, Column<Any>>> = mutableMapOf()

    }


    /**
     * 根据属性名得到列对象
     *
     * @param propertyNames 属性名可变数组
     * @return 列对象数组
     */
    private fun columnOf(vararg propertyNames: String): Array<Column<Any>> { //TODO 是否ktorm能从列绑定关系直接取?
        if (propertyNames.isEmpty()) return emptyArray()

        val tableName = table!!.tableName
        var columnMap = columnCache[tableName]
        if (columnMap == null) {
            columnMap = mutableMapOf()
            columnCache[tableName] = columnMap
        }

        val columns = mutableListOf<Column<Any>>()
        propertyNames.forEach { propertyName ->
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
            } else columns.add(column as Column<Any>)
        }
        return columns.toTypedArray()
    }

    private fun sortOf(vararg orders: Order): List<OrderByExpression> {
        return if (orders.isNotEmpty()) {
            val orderExpressions = mutableListOf<OrderByExpression>()
            orders.forEach {
                val column = columnOf(it.property)[0]
                val orderByExpression = if (it.isAscending) {
                    column.asc()
                } else column.desc()
                orderExpressions.add(orderByExpression)
            }
            orderExpressions
        } else {
            emptyList()
        }
    }

    private fun processWhere(propertyMap: Map<String, *>, logic: AndOr?): ColumnDeclaring<Boolean> {
        val properties = propertyMap.keys.toTypedArray()
        val columns = columnOf(*properties)
        val expressions = mutableListOf<ColumnDeclaring<Boolean>>()
        columns.forEachIndexed { index, column ->
            val value = propertyMap[properties[index]]
            val expression = if (value == null) {
                column.isNull()
            } else {
                column eq value
            }
            expressions.add(expression)
        }
        var fullExpression = expressions[0]
        expressions.forEachIndexed { index, expression ->
            if (index != 0) {
                fullExpression = if (logic == AndOr.AND) {
                    fullExpression.and(expression)
                } else {
                    fullExpression.or(expression)
                }
            }
        }
        return fullExpression
    }

    private fun doSearchEntity(propertyMap: Map<String, *>?, logic: AndOr?, vararg orders: Order): List<E> {
        val entitySequence = entitySequence()
        if (propertyMap != null) {
            val fullExpression = processWhere(propertyMap!!, logic)
            entitySequence.filter { fullExpression }
        }

        entitySequence.sorted { sortOf(*orders) }
        return entitySequence.toList()
    }

    private fun doSearchProperties(
        propertyMap: Map<String, *>?, logic: AndOr?, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, *>> {
        // select
        val returnColumns = columnOf(*returnProperties.toTypedArray())
        val query = querySource().select(*returnColumns)

        // where
        if (propertyMap != null) {
            val fullExpression = processWhere(propertyMap!!, logic)
            query.where { fullExpression }
        }

        // order
        query.orderBy(*sortOf(*orders).toTypedArray())

        // result
        return processResult(query, returnProperties, returnColumns)
    }

    private fun processResult(
        query: Query, returnProperties: Collection<String>, returnColumns: Array<Column<Any>>
    ): List<Map<String, *>> {
        val columnMap = mutableMapOf<String, Column<Any>>()
        returnProperties.forEachIndexed { index, returnProperty ->
            columnMap[returnProperty] = returnColumns[index]
        }
        val returnValues = mutableListOf<Map<String, Any?>>()
        query.forEach { row ->
            val map = mutableMapOf<String, Any?>()
            columnMap.forEach { (propertyName, column) ->
                map[propertyName] = row[column]
            }
            returnValues.add(map)
        }
        return returnValues
    }

    private fun doInSearchProperties(
        property: String, values: List<*>, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, *>> {
        val returnColumns = columnOf(property)
        val query = querySource().select(returnColumns[0].inList(values))
        query.orderBy(*sortOf(*orders).toTypedArray())
        return processResult(query, returnProperties, returnColumns)
    }


}