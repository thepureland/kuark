package io.kuark.ability.data.rdb.support

import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.base.bean.BeanKit
import io.kuark.base.lang.GenericKit
import io.kuark.base.lang.collections.CollectionKit
import io.kuark.base.lang.reflect.newInstance
import io.kuark.base.query.Criteria
import io.kuark.base.query.enums.Operator
import io.kuark.base.query.sort.Order
import io.kuark.base.support.Consts
import io.kuark.base.support.GroupExecutor
import io.kuark.base.support.logic.AndOr
import io.kuark.base.support.payload.ListSearchPayload
import io.kuark.base.support.payload.SearchPayload
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.ktorm.expression.InListExpression
import org.ktorm.expression.OrderByExpression
import org.ktorm.schema.Column
import org.ktorm.schema.ColumnDeclaring
import org.ktorm.schema.Table
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties


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
    private var table: T? = null

    private var entityClass: KClass<E>? = null

    /**
     * 返回数据库表-实体关联对象
     *
     * @return 数据库表-实体关联对象
     * @author K
     * @since 1.0.0
     */
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    protected fun table(): T {
        if (table == null) {
            val tableClass = GenericKit.getSuperClassGenricClass(this::class, 2) as KClass<T>
            table = tableClass.objectInstance!!
        }
        return table!!
    }

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    protected fun entityClass(): KClass<E> {
        if (entityClass == null) {
            return GenericKit.getSuperClassGenricClass(this::class, 1) as KClass<E>
        }
        return entityClass!!
    }

    /**
     * 返回当前表所在的数据库对象
     *
     * @return 当前表所在的数据库对象
     * @author K
     * @since 1.0.0
     */
    protected fun database(): Database = RdbKit.getDatabase()

    /**
     * 返回T指定的表的查询源，基于该对象可以进行类似对数据库表的sql一样操作
     *
     * @return 查询源
     * @author K
     * @since 1.0.0
     */
    protected fun querySource(): QuerySource = database().from(table())

    /**
     * 返回T指定的表的实体序列，基于该序列可以进行类似对集合一样的操作
     *
     * @return 实体序列
     * @author K
     * @since 1.0.0
     */
    protected fun entitySequence(): EntitySequence<E, T> = database().sequenceOf(table())

    /**
     * 返回主键的列(kuark数据库表规范，一个表有且仅有一个列名为id的主键)
     *
     * @return 主键列对象
     * @author K
     * @since 1.0.0
     */
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    protected fun getPkColumn(): Column<PK> {
        return table().primaryKeys[0] as Column<PK>
    }

    /**
     * 创建查询条件表达式
     *
     * @param column 列对象
     * @param operator 操作符
     * @param value 要查询的值
     * @return 列申明对象
     * @author K
     * @since 1.0.0
     */
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    fun <A : Any> whereExpr(column: Column<A>, operator: Operator, value: A?): ColumnDeclaring<Boolean>? {
        return SqlWhereExpressionFactory.create(column as Column<Any>, operator, value)
    }

    //region Search

    /**
     * 查询指定主键值的实体
     *
     * @param id 主键值，类型必须为以下之一：String、Int、Long
     * @return 实体，找不到返回null
     * @author K
     * @since 1.0.0
     */
    open fun get(id: PK): E? {
        return entitySequence().firstOrNull { getPkColumn() eq id }
    }

    /**
     * 查询指定主键值的实体，可以指定返回的对象类型
     *
     * @param id 主键值，类型必须为以下之一：String、Int、Long
     * @return 结果对象，找不到返回null
     * @author K
     * @since 1.0.0
     */
    open fun <R : Any> get(id: PK, returnType: KClass<R>): R? {
        val query = querySource()
            .select()
            .where { getPkColumn().eq(id) }
        val columnMap = getColumns(returnType)
        query.forEach { row ->
            val bean = returnType.newInstance()
            columnMap.forEach { (property, column) ->
                BeanKit.setProperty(bean, property, row[column])
            }
            return bean
        }
        return null
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
    open fun getByIds(vararg ids: PK, countOfEachBatch: Int = 1000): List<E> {
        if (ids.isEmpty()) return listOf()
        val results = mutableListOf<E>()
        GroupExecutor(listOf(ids), countOfEachBatch) {
            val result = entitySequence().filter { getPkColumn().inList(*ids) }.toList()
            results.addAll(result)
        }.execute()
        return results
    }

    //endregion Search


    //region oneSearch
    /**
     * 根据单个属性查询
     *
     * @param property 属性名
     * @param value    属性值
     * @param orders   排序规则
     * @return 指定类名对象的结果列表
     * @author K
     * @since 1.0.0
     */
    open fun oneSearch(property: String, value: Any?, vararg orders: Order): List<E> {
        return doSearchEntity(mapOf(property to value), null, null, *orders)
    }

    /**
     * 根据单个属性查询，只返回指定的单个属性的列表
     *
     * @param property       属性名
     * @param value          属性值
     * @param returnProperty 返回的属性名
     * @param orders         排序规则
     * @return List(属性值)
     * @author K
     * @since 1.0.0
     */
    open fun oneSearchProperty(property: String, value: Any?, returnProperty: String, vararg orders: Order): List<*> {
        val results = doSearchProperties(mapOf(property to value), null, listOf(returnProperty), null, *orders)
        return results.flatMap { it.values }
    }

    /**
     * 根据单个属性查询，只返回指定属性的列表
     *
     * @param property         属性名
     * @param value            属性值
     * @param returnProperties 返回的属性名称集合
     * @param orders           排序规则
     * @return List(Map(属性名, 属性值)), 一个Map封装一个记录
     * @author K
     * @since 1.0.0
     */
    open fun oneSearchProperties(
        property: String, value: Any?, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, *>> {
        return doSearchProperties(mapOf(property to value), null, returnProperties, null, *orders)
    }

    //endregion oneSearch


    //region allSearch
    /**
     * 查询所有结果
     *
     * @param orders 排序规则
     * @return 实体对象列表
     * @author K
     * @since 1.0.0
     */
    open fun allSearch(vararg orders: Order): List<E> {
        return entitySequence().toList()
    }

    /**
     * 查询所有结果，只返回指定的单个属性的列表
     *
     * @param returnProperty 属性名
     * @param orders         排序规则
     * @return List(属性值)
     * @author K
     * @since 1.0.0
     */
    open fun allSearchProperty(returnProperty: String, vararg orders: Order): List<*> {
        val results = doSearchProperties(null, null, listOf(returnProperty), null, *orders)
        return results.flatMap { it.values }
    }

    /**
     * 查询所有结果，只返回指定属性的列表
     *
     * @param returnProperties 属性名称集合
     * @param orders           排序规则
     * @return List(Map(属性名, 属性值))
     * @author K
     * @since 1.0.0
     */
    open fun allSearchProperties(returnProperties: Collection<String>, vararg orders: Order): List<Map<String, *>> {
        return doSearchProperties(null, null, returnProperties, null, *orders)
    }

    //endregion allSearch


    //region andSearch

    /**
     * 根据多个属性进行and条件查询，返回实体类对象的列表
     *
     * @param properties Map(属性名，属性值)
     * @param orders     排序规则
     * @param whereConditionFactory where条件表达式工厂函数，可对properties参数定义查询逻辑，也可完全自定义查询逻辑，函数返回null时将按“等于”操作处理properties参数中属性。参数默认为null
     * @return 实体对象列表
     * @author K
     * @since 1.0.0
     */
    open fun andSearch(
        properties: Map<String, *>,
        vararg orders: Order,
        whereConditionFactory: ((Column<Any>, Any?) -> ColumnDeclaring<Boolean>?)? = null
    ): List<E> {
        return doSearchEntity(properties, AndOr.AND, whereConditionFactory, *orders)
    }

    /**
     * 根据多个属性进行and条件查询，只返回指定的单个属性的列表
     *
     * @param properties     Map(属性名，属性值）
     * @param returnProperty 要返回的属性名
     * @param orders         排序规则
     * @param whereConditionFactory where条件表达式工厂函数，可对properties参数定义查询逻辑，也可完全自定义查询逻辑，函数返回null时将按“等于”操作处理properties参数中属性。参数默认为null
     * @return List(指定的属性的值)
     * @author K
     * @since 1.0.0
     */
    open fun andSearchProperty(
        properties: Map<String, *>,
        returnProperty: String,
        vararg orders: Order,
        whereConditionFactory: ((Column<Any>, Any?) -> ColumnDeclaring<Boolean>?)? = null
    ): List<*> {
        val results = doSearchProperties(properties, AndOr.AND, listOf(returnProperty), whereConditionFactory, *orders)
        return results.flatMap { it.values }
    }

    /**
     * 根据多个属性进行and条件查询，只返回指定属性的列表
     *
     * @param properties       Map(属性名，属性值)
     * @param returnProperties 要返回的属性名集合
     * @param orders           排序规则
     * @param whereConditionFactory where条件表达式工厂函数，可对properties参数定义查询逻辑，也可完全自定义查询逻辑，函数返回null时将按“等于”操作处理properties参数中属性。参数默认为null
     * @return List(Map(指定的属性名，属性值))
     * @author K
     * @since 1.0.0
     */
    open fun andSearchProperties(
        properties: Map<String, *>,
        returnProperties: Collection<String>,
        vararg orders: Order,
        whereConditionFactory: ((Column<Any>, Any?) -> ColumnDeclaring<Boolean>?)? = null
    ): List<Map<String, *>> {
        return doSearchProperties(properties, AndOr.AND, returnProperties, whereConditionFactory, *orders)
    }

    //endregion andSearch


    //region orSearch
    /**
     * 根据多个属性进行or条件查询，返回实体类对象的列表
     *
     * @param properties Map(属性名，属性值)
     * @param orders     排序规则
     * @param whereConditionFactory where条件表达式工厂函数，可对properties参数定义查询逻辑，也可完全自定义查询逻辑，函数返回null时将按“等于”操作处理properties参数中属性。参数默认为null
     * @return 实体对象列表
     * @author K
     * @since 1.0.0
     */
    open fun orSearch(
        properties: Map<String, *>,
        vararg orders: Order,
        whereConditionFactory: ((Column<Any>, Any?) -> ColumnDeclaring<Boolean>?)? = null
    ): List<E> {
        return doSearchEntity(properties, AndOr.OR, whereConditionFactory, *orders)
    }

    /**
     * 根据多个属性进行or条件查询，只返回指定的单个属性的列表
     *
     * @param properties     Map(属性名，属性值)
     * @param returnProperty 要返回的属性名
     * @param orders         排序规则
     * @param whereConditionFactory where条件表达式工厂函数，可对properties参数定义查询逻辑，也可完全自定义查询逻辑，函数返回null时将按“等于”操作处理properties参数中属性。参数默认为null
     * @return List(指定的属性的值)
     * @author K
     * @since 1.0.0
     */
    open fun orSearchProperty(
        properties: Map<String, *>,
        returnProperty: String,
        vararg orders: Order,
        whereConditionFactory: ((Column<Any>, Any?) -> ColumnDeclaring<Boolean>?)? = null
    ): List<*> {
        val results = doSearchProperties(properties, AndOr.OR, listOf(returnProperty), whereConditionFactory, *orders)
        return results.flatMap { it.values }
    }

    /**
     * 根据多个属性进行or条件查询，只返回指定的属性的列表
     *
     * @param properties       Map(属性名，属性值)
     * @param returnProperties 要返回的属性名集合
     * @param orders           排序规则
     * @param whereConditionFactory where条件表达式工厂函数，可对properties参数定义查询逻辑，也可完全自定义查询逻辑，函数返回null时将按“等于”操作处理properties参数中属性。参数默认为null
     * @return List(Map(指定的属性名，属性值))
     * @author K
     * @since 1.0.0
     */
    open fun orSearchProperties(
        properties: Map<String, *>,
        returnProperties: Collection<String>,
        vararg orders: Order,
        whereConditionFactory: ((Column<Any>, Any?) -> ColumnDeclaring<Boolean>?)? = null
    ): List<Map<String, *>> {
        return doSearchProperties(properties, AndOr.OR, returnProperties, whereConditionFactory, *orders)
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
     * @author K
     * @since 1.0.0
     */
    open fun inSearch(property: String, values: Collection<*>, vararg orders: Order): List<E> {
        val column = ColumnHelper.columnOf(table(), property)[property]!!
        var entitySequence = entitySequence().filter { column.inCollection(values) }
        entitySequence = entitySequence.sorted { sortOf(*orders) }
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
     * @author K
     * @since 1.0.0
     */
    open fun inSearchProperty(
        property: String, values: Collection<*>, returnProperty: String, vararg orders: Order
    ): List<*> {
        val results = doInSearchProperties(property, values, listOf(returnProperty), *orders)
        return results.flatMap { it.values }
    }

    /**
     * in查询，只返回指定属性的值
     *
     * @param property         属性名
     * @param values           in条件值集合
     * @param returnProperties 要返回的属性名集合
     * @param orders           排序规则
     * @return List(Map(指定的属性名，属性值))
     * @author K
     * @since 1.0.0
     */
    open fun inSearchProperties(
        property: String, values: Collection<*>, returnProperties: Collection<String>, vararg orders: Order
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
     * @author K
     * @since 1.0.0
     */
    open fun inSearchById(values: Collection<PK>, vararg orders: Order): List<E> {
        return inSearch(IDbEntity<PK, E>::id.name, values, *orders)
    }

    /**
     * 主键in查询，只返回指定的单个属性的值
     *
     * @param values         主键值集合
     * @param returnProperty 要返回的属性名
     * @param orders         排序规则
     * @return 指定属性的值列表
     * @author K
     * @since 1.0.0
     */
    open fun inSearchPropertyById(values: Collection<PK>, returnProperty: String, vararg orders: Order): List<*> {
        val results = doInSearchProperties(IDbEntity<PK, E>::id.name, values, listOf(returnProperty), *orders)
        return results.flatMap { it.values }
    }

    /**
     * 主键in查询，只返回指定属性的值
     *
     * @param values           主键值集合
     * @param returnProperties 要返回的属性名集合
     * @param orders           排序规则
     * @return List(Map(指定的属性名, 属性值))
     * @author K
     * @since 1.0.0
     */
    open fun inSearchPropertiesById(
        values: Collection<PK>, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, *>> {
        return doInSearchProperties(IDbEntity<PK, E>::id.name, values, returnProperties, *orders)
    }

    //endregion inSearch


    //region search Criteria
    /**
     * 复杂条件查询
     *
     * @param criteria 查询条件
     * @param orders   排序规则
     * @return 实体对象列表
     * @author K
     * @since 1.0.0
     */
    open fun search(criteria: Criteria, vararg orders: Order): List<E> {
        return searchEntityCriteria(criteria, 0, 0, *orders)
    }

    /**
     * 复杂条件查询，只返回指定单个属性的值
     *
     * @param criteria       查询条件
     * @param returnProperty 要返回的属性名
     * @param orders         排序规则
     * @return 指定属性的值列表
     * @author K
     * @since 1.0.0
     */
    open fun searchProperty(criteria: Criteria, returnProperty: String, vararg orders: Order): List<*> {
        val results = searchPropertiesCriteria(criteria, listOf(returnProperty), 0, 0, *orders)
        return results.flatMap { it.values }
    }

    /**
     * 复杂条件，只返回指定多个属性的值
     *
     * @param criteria         查询条件
     * @param returnProperties 要返回的属性名集合
     * @param orders           排序规则
     * @return List(Map(指定的属性名 属性值))
     * @author K
     * @since 1.0.0
     */
    open fun searchProperties(
        criteria: Criteria, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, Any?>> {
        return searchPropertiesCriteria(criteria, returnProperties, 0, 0, *orders)
    }

    //endregion search Criteria


    //region pagingSearch
    /**
     * 分页查询，返回对象列表
     *
     * @param criteria 查询条件
     * @param pageNo   当前页码(从1开始)
     * @param pageSize 每页条数
     * @param orders   排序规则
     * @return 实体对象列表
     * @author K
     * @since 1.0.0
     */
    open fun pagingSearch(criteria: Criteria, pageNo: Int, pageSize: Int, vararg orders: Order): List<E> {
        return searchEntityCriteria(criteria, pageNo, pageSize, *orders)
    }

    /**
     * 分页查询，返回对象列表,返回只包含指定属性
     *
     * @param criteria 查询条件
     * @param pageNo   当前页码(从1开始)
     * @param pageSize 每页条数
     * @param orders   排序规则
     * @return 实体对象列表
     * @author K
     * @since 1.0.0
     */
    open fun pagingReturnProperty(
        criteria: Criteria, returnProperty: String, pageNo: Int, pageSize: Int, vararg orders: Order
    ): List<*> {
        val results = searchPropertiesCriteria(criteria, listOf(returnProperty), pageNo, pageSize, *orders)
        return results.flatMap { it.values }
    }

    /**
     * 分页查询，返回对象列表,返回只包含指定属性
     *
     * @param criteria 查询条件
     * @param pageNo   当前页码(从1开始)
     * @param pageSize 每页条数
     * @param orders   排序规则
     * @return 实体对象列表
     * @author K
     * @since 1.0.0
     */
    open fun pagingReturnProperties(
        criteria: Criteria, returnProperties: Collection<String>, pageNo: Int, pageSize: Int, vararg orders: Order
    ): List<Map<String, *>> {
        return searchPropertiesCriteria(criteria, returnProperties, pageNo, pageSize, *orders)
    }

    //endregion pagingSearch


    //region payload search

    /**
     * 根据查询载体对象查询(包括分页), 具体规则见 @see SearchPayload
     *
     * 同一属性的查询逻辑在 listSearchPayload 和 whereConditionFactory 都有指定时，以 whereConditionFactory 为准！
     *
     * @param listSearchPayload 查询载体对象，默认为null,为null时返回的列表元素类型为PO实体类，此时，若whereConditionFactory有指定，各条件间的查询逻辑为AND
     * @param whereConditionFactory where条件表达式工厂函数，可对listSearchPayload参数定义查询逻辑，也可完全自定义查询逻辑，函数返回null时将按“等于”操作处理listSearchPayload中的属性。参数默认为null
     * @return 结果列表, 有三种类型可能, @see SearchPayload
     * @author K
     * @since 1.0.0
     */
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    open fun search(
        listSearchPayload: ListSearchPayload? = null,
        whereConditionFactory: ((Column<Any>, Any?) -> ColumnDeclaring<Boolean>?)? = null
    ): List<*> {
        // select, where
        val objects = searchByPayload(listSearchPayload, whereConditionFactory)
        var query = objects[0] as Query
        val returnProps = objects[1] as Set<String>
        val returnColumnMap = objects[2] as Map<String, Column<Any>>

        // order
        if (listSearchPayload != null) {
            val orders = listSearchPayload.orders
            if (CollectionKit.isNotEmpty(orders)) {
                val orderExps = sortOf(*orders!!.toTypedArray())
                query = query.orderBy(*orderExps.toTypedArray())
            }
        }

        // paging
        if (listSearchPayload != null) {
            val pageNo = listSearchPayload.pageNo
            if (pageNo != null) {
                val pageSize = listSearchPayload.pageSize ?: 10
                query = query.limit((pageNo - 1) * pageSize, pageSize)
            }
        }

        // result
        val returnProperties = listSearchPayload?.returnProperties ?: emptyList()
        val mapList = processResult(query, returnColumnMap)
        return when {
            CollectionKit.isEmpty(returnProperties) -> {
                val beanList = mutableListOf<Any>()
                mapList.forEach { map ->
                    val bean = if (listSearchPayload?.returnEntityClass != null) {
                        listSearchPayload.returnEntityClass!!.newInstance()
                    } else {
                        Entity.create(table().entityClass!!)
                    }
                    returnProps.forEach { prop ->
                        BeanKit.setProperty(bean, prop, map[prop])
                    }
                    beanList.add(bean)
                }
                beanList
            }
            returnProperties.size == 1 -> {
                mapList.flatMap { it.values }
            }
            else -> {
                mapList
            }
        }
    }

    //endregion payload search


    //region aggregate

    /**
     * 计算记录数
     *
     * @param criteria 查询条件，为null将计算所有记录
     * @return 记录数
     * @author K
     * @since 1.0.0
     */
    open fun count(criteria: Criteria? = null): Int {
        return if (criteria == null) {
            entitySequence().count()
        } else {
            entitySequence()
                .filter { CriteriaConverter.convert(criteria, table()) }
                .aggregateColumns { count(getPkColumn()) }!!
        }
    }

    /**
     * 计算记录数
     *
     * @param searchPayload 查询载体对象，默认为null,为null时返回的列表元素类型为PO实体类，此时，若whereConditionFactory有指定，各条件间的查询逻辑为AND
     * @param whereConditionFactory where条件表达式工厂函数，可对searchPayload参数定义查询逻辑，也可完全自定义查询逻辑，函数返回null时将按“等于”操作处理searchPayload中的属性。参数默认为null
     * @return 记录数
     * @author K
     * @since 1.0.0
     */
    open fun count(
        searchPayload: SearchPayload? = null,
        whereConditionFactory: ((Column<Any>, Any?) -> ColumnDeclaring<Boolean>?)? = null
    ): Int {
        val query = searchByPayload(searchPayload, whereConditionFactory)[0] as Query
        return query.totalRecords
    }

    /**
     * 求和. 对满足条件的记录根据指定属性进行求和
     *
     * @param property 待求和的属性
     * @param criteria 查询条件，为null将计算所有记录
     * @return 和
     * @author K
     * @since 1.0.0
     */
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    open fun sum(property: String, criteria: Criteria? = null): Number {
        var entitySequence = entitySequence()
        if (criteria != null) {
            entitySequence = entitySequence.filter { CriteriaConverter.convert(criteria, table()) }
        }
        return entitySequence.aggregateColumns {
            sum(ColumnHelper.columnOf(table(), property)[property] as Column<Number>)
        } as Number
    }

    /**
     * 求平均值. 对满足条件的记录根据指定属性进行求平均值
     *
     * @param property 待求平均值的属性
     * @param criteria 查询条件，为null将计算所有记录
     * @return 平均值
     * @author K
     * @since 1.0.0
     */
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    open fun avg(property: String, criteria: Criteria? = null): Number {
        var entitySequence = entitySequence()
        if (criteria != null) {
            entitySequence = entitySequence.filter { CriteriaConverter.convert(criteria, table()) }
        }
        return entitySequence.aggregateColumns {
            avg(ColumnHelper.columnOf(table(), property)[property] as Column<Number>)
        } as Number
    }

    /**
     * 求最大值. 对满足条件的记录根据指定属性进行求最大值
     *
     * @param property 待求最大值的属性
     * @param criteria 查询条件，为null将计算所有记录
     * @return 最大值
     * @author K
     * @since 1.0.0
     */
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    open fun max(property: String, criteria: Criteria? = null): Any? {
        var entitySequence = entitySequence()
        if (criteria != null) {
            entitySequence = entitySequence.filter { CriteriaConverter.convert(criteria, table()) }
        }
        return entitySequence.aggregateColumns {
            max(ColumnHelper.columnOf(table(), property)[property] as Column<Comparable<Any>>)
        }
    }

    /**
     * 求最小值. 对满足条件的记录根据指定属性进行求最小值
     *
     * @param property 待求最小值的属性
     * @param criteria 查询条件，为null将计算所有记录
     * @return 最小值
     * @author K
     * @since 1.0.0
     */
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    open fun min(property: String, criteria: Criteria? = null): Any? {
        var entitySequence = entitySequence()
        if (criteria != null) {
            entitySequence = entitySequence.filter { CriteriaConverter.convert(criteria, table()) }
        }
        return entitySequence.aggregateColumns {
            min(ColumnHelper.columnOf(table(), property)[property] as Column<Comparable<Any>>)
        }
    }

    //endregion aggregate

    private fun sortOf(vararg orders: Order): List<OrderByExpression> {
        return if (orders.isNotEmpty()) {
            val orderExpressions = mutableListOf<OrderByExpression>()
            orders.forEach {
                val column = ColumnHelper.columnOf(table(), it.property)[it.property]!!
                val orderByExpression = if (it.isAscending()) {
                    column.asc()
                } else column.desc()
                orderExpressions.add(orderByExpression)
            }
            orderExpressions
        } else {
            emptyList()
        }
    }

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    protected fun processWhere(
        propertyMap: Map<String, Pair<Operator, *>>,
        andOr: AndOr?,
        ignoreNull: Boolean = false,
        whereConditionFactory: ((Column<Any>, Any?) -> ColumnDeclaring<Boolean>?)? = null
    ): ColumnDeclaring<Boolean>? {
        val properties = propertyMap.keys.toTypedArray()
        val columns = ColumnHelper.columnOf(table(), *properties)
        val expressions = mutableListOf<ColumnDeclaring<Boolean>>()
        columns.forEach { (property, column) ->
            val operatorAndValue = propertyMap[property]
            val operator = operatorAndValue!!.first
            val value = operatorAndValue.second
            val expression = if (operator == Operator.IS_NULL) {
                column.isNull()
            } else if (operator == Operator.IS_NOT_NULL) {
                column.isNotNull()
            } else if (value == null || value == "") {
                if (ignoreNull) {
                    whereConditionFactory?.let { it(column, value) }
                } else {
                    column.isNull()
                }
            } else {
                if (whereConditionFactory == null) {
                    SqlWhereExpressionFactory.create(column, operator, value)
                } else {
                    whereConditionFactory(column, value) ?: column eq value
                }
            }
            if (expression != null) {
                expressions.add(expression)
            }
        }

        if (expressions.isEmpty()) {
            if (whereConditionFactory != null) {
                table().columns.forEach { column ->
                    val expression = whereConditionFactory(column as Column<Any>, null)
                    if (expression != null) {
                        expressions.add(expression)
                    }
                }
            }
        }

        return if (expressions.isEmpty()) {
            null
        } else {
            var fullExpression = expressions[0]
            expressions.forEachIndexed { index, expression ->
                if (index != 0) {
                    fullExpression = if (andOr == AndOr.AND) {
                        fullExpression.and(expression)
                    } else {
                        fullExpression.or(expression)
                    }
                }
            }
            fullExpression
        }
    }

    private fun doSearchEntity(
        propertyMap: Map<String, *>?,
        logic: AndOr?,
        whereConditionFactory: ((Column<Any>, Any?) -> ColumnDeclaring<Boolean>?)? = null,
        vararg orders: Order
    ): List<E> {
        var entitySequence = entitySequence()
        if (propertyMap != null) {
            val propMap = mutableMapOf<String, Pair<Operator, *>>()
            propertyMap.forEach { (prop, value) ->
                propMap[prop] = Pair(Operator.EQ, value)
            }
            val fullExpression = processWhere(propMap, logic, false, whereConditionFactory)
            entitySequence = entitySequence.filter { fullExpression!! }
        }

        entitySequence = entitySequence.sorted { sortOf(*orders) }
        return entitySequence.toList()
    }

    private fun doSearchProperties(
        propertyMap: Map<String, *>?,
        logic: AndOr?,
        returnProperties: Collection<String>,
        whereConditionFactory: ((Column<Any>, Any?) -> ColumnDeclaring<Boolean>?)? = null,
        vararg orders: Order
    ): List<Map<String, *>> {
        // select
        val returnColumnMap = ColumnHelper.columnOf(table(), *returnProperties.toTypedArray())
        var query = querySource().select(returnColumnMap.values)

        // where
        if (propertyMap != null) {
            val propMap = mutableMapOf<String, Pair<Operator, *>>()
            propertyMap.forEach { (prop, value) ->
                propMap[prop] = Pair(Operator.EQ, value)
            }
            val fullExpression = processWhere(propMap, logic, false, whereConditionFactory)
            query = query.where { fullExpression!! }
        }

        // order
        query = query.orderBy(*sortOf(*orders).toTypedArray())

        // result
        return processResult(query, returnColumnMap)
    }

    private fun processResult(query: Query, returnColumnMap: Map<String, Column<Any>>): List<Map<String, *>> {
        val returnValues = mutableListOf<Map<String, Any?>>()
        query.forEach { row ->
            val map = mutableMapOf<String, Any?>()
            returnColumnMap.forEach { (propertyName, column) ->
                map[propertyName] = row[column]
            }
            returnValues.add(map)
        }
        return returnValues
    }

    private fun doInSearchProperties(
        property: String, values: Collection<*>, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, *>> {
        val column = ColumnHelper.columnOf(table(), property)[property]!!
        val returnColumnMap = ColumnHelper.columnOf(table(), *returnProperties.toTypedArray())
        var query = querySource().select(returnColumnMap.values)
        query = query.where { column.inCollection(values) }
        query = query.orderBy(*sortOf(*orders).toTypedArray())
        return processResult(query, returnColumnMap)
    }

    private fun searchEntityCriteria(
        criteria: Criteria, pageNo: Int = 0, pageSize: Int = 0, vararg orders: Order
    ): List<E> {
        var entitySequence = entitySequence()

        // where
        entitySequence = entitySequence.filter { CriteriaConverter.convert(criteria, table()) }

        // sort
        entitySequence = entitySequence.sorted { sortOf(*orders) }

        // paging
        if (pageNo != 0 && pageSize != 0) {
            entitySequence = entitySequence.drop((pageNo - 1) * pageSize).take(pageSize)
        }

        return entitySequence.toList()
    }

    private fun searchPropertiesCriteria(
        criteria: Criteria, returnProperties: Collection<String>,
        pageNo: Int = 0, pageSize: Int = 0, vararg orders: Order
    ): List<Map<String, *>> {
        // select
        val returnColumnMap = ColumnHelper.columnOf(table(), *returnProperties.toTypedArray())
        var query = querySource().select(returnColumnMap.values)

        // where
        query = query.where { CriteriaConverter.convert(criteria, table()) }

        // order
        query = query.orderBy(*sortOf(*orders).toTypedArray())

        // paging
        if (pageNo != 0 && pageSize != 0) {
            query = query.limit((pageNo - 1) * pageSize, pageSize)
        }

        // result
        return processResult(query, returnColumnMap)
    }

    private fun searchByPayload(
        searchPayload: SearchPayload? = null,
        whereConditionFactory: ((Column<Any>, Any?) -> ColumnDeclaring<Boolean>?)? = null
    ): List<Any> {
        val entityProperties = getEntityProperties()

        // select
        val returnProperties = searchPayload?.returnProperties ?: emptyList()
        val props = if (CollectionKit.isEmpty(returnProperties)) {
            val returnEntityClass = searchPayload?.returnEntityClass
            returnEntityClass?.memberProperties?.map { it.name } ?: entityProperties
        } else {
            returnProperties
        }
        val returnProps = entityProperties.intersect(props) // 取交集,保证要查询的列一定存在
        val returnColumnMap = ColumnHelper.columnOf(table(), *returnProps.toTypedArray())
        var query = querySource().select(returnColumnMap.values)

        // where
        val propMap = if (searchPayload == null) {
            emptyMap()
        } else {
            getWherePropertyMap(searchPayload, entityProperties)
        }
        val andOr = searchPayload?.andOr ?: AndOr.AND
        val fullExpression = processWhere(propMap, andOr, true, whereConditionFactory)
        if (fullExpression != null) {
            query = query.where { fullExpression }
        }

        return listOf(query, returnProps, returnColumnMap)
    }

    protected fun getEntityProperties(): List<String> {
        return table().entityClass!!.memberProperties
            .filter { it.name != "entityClass" && it.name != "properties" }
            .map { it.name }
    }

    protected fun getWherePropertyMap(
        searchPayload: SearchPayload, entityProperties: List<String>
    ): Map<String, Pair<Operator, *>> {
        val propAndValueMap = BeanKit.extract(searchPayload).filter { entityProperties.contains(it.key) }
        val operatorMap = searchPayload.getOperators()
        val resultMap = mutableMapOf<String, Pair<Operator, *>>()
        propAndValueMap.forEach { (prop, value) ->
            var operator = operatorMap[prop]
            if (operator == null) {
                operator = Operator.EQ
            }
            resultMap[prop] = Pair(operator, value)
        }
        val nullProperties = searchPayload.nullProperties
        if (CollectionKit.isNotEmpty(nullProperties)) {
            nullProperties!!.forEach {
                resultMap[it] = Pair(Operator.IS_NULL, null)
            }
        }
        return resultMap
    }

    protected fun getColumns(returnType: KClass<*>): Map<String, Column<Any>> {
        val entityProperties = getEntityProperties()
        val properties = returnType.memberProperties.map { it.name }
        val returnProps = entityProperties.intersect(properties) // 取交集,保证要查询的列一定存在
        return ColumnHelper.columnOf(table(), *returnProps.toTypedArray())
    }

}

// 解决ktorm总是要调用到可变参数的inList方法的问题及泛型问题
@Suppress(Consts.Suppress.UNCHECKED_CAST)
private fun <T : Any> ColumnDeclaring<T>.inCollection(list: Collection<*>): InListExpression<T> {
    return InListExpression(left = asExpression(), values = list.map { wrapArgument(it as T?) })
}