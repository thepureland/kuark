package io.kuark.ability.data.rdb.biz

import io.kuark.ability.data.rdb.support.IDbEntity
import io.kuark.base.query.Criteria
import io.kuark.base.query.sort.Order
import io.kuark.base.support.payload.ListSearchPayload
import io.kuark.base.support.payload.SearchPayload
import org.ktorm.schema.Column
import org.ktorm.schema.ColumnDeclaring
import kotlin.reflect.KClass

/**
 * 基于关系型数据库表的基础只读业务操作接口
 *
 * @param PK 实体主键类型
 * @param E 实体类型
 * @author K
 * @since 1.0.0
 */
interface IBaseReadOnlyBiz<PK : Any, E : IDbEntity<PK, E>> {

    //region Search

    /**
     * 查询指定主键值的实体
     *
     * @param id 主键值，类型必须为以下之一：String、Int、Long
     * @return 实体，找不到返回null
     * @author K
     * @since 1.0.0
     */
    fun get(id: PK): E?

    /**
     * 查询指定主键值的实体，可以指定返回的对象类型
     *
     * @param id 主键值，类型必须为以下之一：String、Int、Long
     * @return 结果对象，找不到返回null
     * @author K
     * @since 1.0.0
     */
    fun <R: Any> get(id: PK, returnType: KClass<R>): R?

    /**
     * 批量查询指定主键值的实体
     *
     * @param ids 主键值可变参数，元素类型必须为以下之一：String、Int、Long，为空时返回空列表
     * @param countOfEachBatch 每批大小，缺省为1000
     * @return 实体列表，ids为空时返回空列表
     * @author K
     * @since 1.0.0
     */
    fun getByIds(vararg ids: PK, countOfEachBatch: Int = 1000): List<E>

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
    fun oneSearch(property: String, value: Any?, vararg orders: Order): List<E>

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
    fun oneSearchProperty(property: String, value: Any?, returnProperty: String, vararg orders: Order): List<*>

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
    fun oneSearchProperties(
        property: String, value: Any?, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, *>>

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
    fun allSearch(vararg orders: Order): List<E>

    /**
     * 查询所有结果，只返回指定的单个属性的列表
     *
     * @param returnProperty 属性名
     * @param orders         排序规则
     * @return List(属性值)
     * @author K
     * @since 1.0.0
     */
    fun allSearchProperty(returnProperty: String, vararg orders: Order): List<*>

    /**
     * 查询所有结果，只返回指定属性的列表
     *
     * @param returnProperties 属性名称集合
     * @param orders           排序规则
     * @return List(Map(属性名, 属性值))
     * @author K
     * @since 1.0.0
     */
    fun allSearchProperties(returnProperties: Collection<String>, vararg orders: Order): List<Map<String, *>>

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
    fun andSearch(
        properties: Map<String, *>,
        vararg orders: Order,
        whereConditionFactory: ((Column<Any>, Any?) -> ColumnDeclaring<Boolean>?)? = null
    ): List<E>

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
    fun andSearchProperty(
        properties: Map<String, *>,
        returnProperty: String,
        vararg orders: Order,
        whereConditionFactory: ((Column<Any>, Any?) -> ColumnDeclaring<Boolean>?)? = null
    ): List<*>

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
    fun andSearchProperties(
        properties: Map<String, *>,
        returnProperties: Collection<String>,
        vararg orders: Order,
        whereConditionFactory: ((Column<Any>, Any?) -> ColumnDeclaring<Boolean>?)? = null
    ): List<Map<String, *>>

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
    fun orSearch(
        properties: Map<String, *>,
        vararg orders: Order,
        whereConditionFactory: ((Column<Any>, Any?) -> ColumnDeclaring<Boolean>?)? = null
    ): List<E>

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
    fun orSearchProperty(
        properties: Map<String, *>,
        returnProperty: String,
        vararg orders: Order,
        whereConditionFactory: ((Column<Any>, Any?) -> ColumnDeclaring<Boolean>?)? = null
    ): List<*>

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
    fun orSearchProperties(
        properties: Map<String, *>,
        returnProperties: Collection<String>,
        vararg orders: Order,
        whereConditionFactory: ((Column<Any>, Any?) -> ColumnDeclaring<Boolean>?)? = null
    ): List<Map<String, *>>

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
    fun inSearch(property: String, values: Collection<*>, vararg orders: Order): List<E>

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
    fun inSearchProperty(
        property: String, values: Collection<*>, returnProperty: String, vararg orders: Order
    ): List<*>

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
    fun inSearchProperties(
        property: String, values: Collection<*>, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, *>>

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
    fun inSearchById(values: Collection<PK>, vararg orders: Order): List<E>

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
    fun inSearchPropertyById(values: Collection<PK>, returnProperty: String, vararg orders: Order): List<*>

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
    fun inSearchPropertiesById(
        values: Collection<PK>, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, *>>

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
    fun search(criteria: Criteria, vararg orders: Order): List<E>

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
    fun searchProperty(criteria: Criteria, returnProperty: String, vararg orders: Order): List<*>

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
    fun searchProperties(
        criteria: Criteria, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, Any?>>

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
    fun pagingSearch(criteria: Criteria, pageNo: Int, pageSize: Int, vararg orders: Order): List<E>

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
    fun pagingReturnProperty(
        criteria: Criteria, returnProperty: String, pageNo: Int, pageSize: Int, vararg orders: Order
    ): List<*>

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
    fun pagingReturnProperties(
        criteria: Criteria, returnProperties: Collection<String>, pageNo: Int, pageSize: Int, vararg orders: Order
    ): List<Map<String, *>>

    //endregion pagingSearch


    //region payload search

    /**
     * 根据查询载体对象查询(包括分页), 具体规则见 @see SearchPayload
     *
     * @param listSearchPayload 查询载体对象，默认为null,为null时返回的列表元素类型为PO实体类，此时，若whereConditionFactory有指定，各条件间的查询逻辑为AND
     * @param whereConditionFactory where条件表达式工厂函数，可对listSearchPayload参数定义查询逻辑，也可完全自定义查询逻辑，函数返回null时将按“等于”操作处理listSearchPayload中的属性。参数默认为null
     * @return 结果列表, 有三种类型可能, @see SearchPayload
     * @author K
     * @since 1.0.0
     */
    fun search(
        listSearchPayload: ListSearchPayload? = null,
        whereConditionFactory: ((Column<Any>, Any?) -> ColumnDeclaring<Boolean>?)? = null
    ): List<*>

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
    fun count(criteria: Criteria? = null): Int

    /**
     * 计算记录数
     *
     * @param searchPayload 查询载体对象，默认为null,为null时返回的列表元素类型为PO实体类，此时，若whereConditionFactory有指定，各条件间的查询逻辑为AND
     * @param whereConditionFactory where条件表达式工厂函数，可对searchPayload参数定义查询逻辑，也可完全自定义查询逻辑，函数返回null时将按“等于”操作处理searchPayload中的属性。参数默认为null
     * @return 记录数
     * @author K
     * @since 1.0.0
     */
    fun count(
        searchPayload: SearchPayload? = null,
        whereConditionFactory: ((Column<Any>, Any?) -> ColumnDeclaring<Boolean>?)? = null
    ): Int

    /**
     * 求和. 对满足条件的记录根据指定属性进行求和
     *
     * @param property 待求和的属性
     * @param criteria 查询条件，为null将计算所有记录
     * @return 和
     * @author K
     * @since 1.0.0
     */
    fun sum(property: String, criteria: Criteria? = null): Number

    /**
     * 求平均值. 对满足条件的记录根据指定属性进行求平均值
     *
     * @param property 待求平均值的属性
     * @param criteria 查询条件，为null将计算所有记录
     * @return 平均值
     * @author K
     * @since 1.0.0
     */
    fun avg(property: String, criteria: Criteria? = null): Number

    /**
     * 求最大值. 对满足条件的记录根据指定属性进行求最大值
     *
     * @param property 待求最大值的属性
     * @param criteria 查询条件，为null将计算所有记录
     * @return 最大值
     * @author K
     * @since 1.0.0
     */
    fun max(property: String, criteria: Criteria? = null): Any?

    /**
     * 求最小值. 对满足条件的记录根据指定属性进行求最小值
     *
     * @param property 待求最小值的属性
     * @param criteria 查询条件，为null将计算所有记录
     * @return 最小值
     * @author K
     * @since 1.0.0
     */
    fun min(property: String, criteria: Criteria? = null): Any?

    //endregion aggregate

}