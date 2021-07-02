package io.kuark.service.biz

import io.kuark.ability.data.rdb.support.IDbEntity
import io.kuark.base.query.Criteria
import io.kuark.base.query.sort.Order
import org.ktorm.schema.Table

/**
 * 基础的只读业务接口
 *
 * @param PK 实体主键类型
 * @param E 实体类型
 * @param T 数据库表-实体关联对象的类型
 * @author K
 * @since 1.0.0
 */
interface IBaseReadOnlyBiz<PK : Any, E : IDbEntity<PK, E>, T : Table<E>> {

    /**
     * 查询指定主键值的实体
     *
     * @param id 主键值，类型必须为以下之一：String、Int、Long
     * @return 实体
     */
    fun getById(id: PK): E

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

    //region oneSearch
    /**
     * 根据单个属性查询
     *
     * @param property 属性名
     * @param value    属性值
     * @param orders   排序规则
     * @return 指定类名对象的结果列表
     */
    fun oneSearch(property: String, value: Any?, vararg orders: Order): List<T>

    /**
     * 根据单个属性查询，只返回指定的单个属性的列表
     *
     * @param property       属性名
     * @param value          属性值
     * @param returnProperty 返回的属性名
     * @param orders         排序规则
     * @return List(属性值)
     */
    fun oneSearchProperty(property: String, value: Any?, returnProperty: String, vararg orders: Order): List<*>

    /**
     * 根据单个属性查询，只返回指定属性的列表
     *
     * @param property         属性名
     * @param value            属性值
     * @param returnProperties 返回的属性名称集合
     * @param orders           排序规则
     * @return List(Map(属性名, 属性值))
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
     */
    fun allSearch(vararg orders: Order): List<T>

    /**
     * 查询所有结果，只返回指定的单个属性的列表
     *
     * @param returnProperty 属性名
     * @param orders         排序规则
     * @return List(属性值)
     */
    fun allSearchProperty(returnProperty: String, vararg orders: Order): List<*>

    /**
     * 查询所有结果，只返回指定属性的列表
     *
     * @param returnProperties 属性名称集合
     * @param orders           排序规则
     * @return List(Map(属性名, 属性值))
     */
    fun allSearchProperties(returnProperties: Collection<String>, vararg orders: Order): List<Map<String, *>>

    //endregion allSearch


    //region andSearch
    /**
     * 根据多个属性进行and条件查询，返回实体类对象的列表
     *
     * @param properties Map(属性名，属性值)
     * @param orders     排序规则
     * @return 实体对象列表
     */
    fun andSearch(properties: Map<String, *>, vararg orders: Order): List<T>

    /**
     * 根据多个属性进行and条件查询，只返回指定的单个属性的列表
     *
     * @param properties     Map(属性名，属性值）
     * @param returnProperty 要返回的属性名
     * @param orders         排序规则
     * @return List(指定的属性的值)
     */
    fun andSearchProperty(properties: Map<String, *>, returnProperty: String, vararg orders: Order): List<*>

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
    ): List<Map<String, *>>

    //endregion andSearch


    //region orSearch
    /**
     * 根据多个属性进行or条件查询，返回实体类对象的列表
     *
     * @param properties Map(属性名，属性值)
     * @param orders     排序规则
     * @return 实体对象列表
     */
    fun orSearch(properties: Map<String, *>, vararg orders: Order): List<T>

    /**
     * 根据多个属性进行or条件查询，只返回指定的单个属性的列表
     *
     * @param properties     Map(属性名，属性值)
     * @param returnProperty 要返回的属性名
     * @param orders         排序规则
     * @return List(指定的属性的值)
     */
    fun orSearchProperty(properties: Map<String, *>, returnProperty: String, vararg orders: Order): List<*>

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
     */
    fun inSearch(property: String, values: List<*>, vararg orders: Order): List<T>

    /**
     * in查询，只返回指定的单个属性的值
     *
     * @param property       属性名
     * @param values         in条件值集合
     * @param returnProperty 要返回的属性名
     * @param orders         排序规则
     * @return 指定属性的值列表
     */
    fun inSearchProperty(property: String, values: List<*>, returnProperty: String, vararg orders: Order): List<*>

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
    ): List<Map<String, *>>

    /**
     * 主键in查询，返回实体类对象列表
     *
     * @param values 主键值集合
     * @param orders 排序规则
     * @param orders 排序规则
     * @return 实体对象列表
     */
    fun inSearchById(values: List<PK>, vararg orders: Order): List<T>

    /**
     * 主键in查询，只返回指定的单个属性的值
     *
     * @param values         主键值集合
     * @param returnProperty 要返回的属性名
     * @param orders         排序规则
     * @return 指定属性的值列表
     */
    fun inSearchPropertyById(values: List<PK>, returnProperty: String, vararg orders: Order): List<*>

    /**
     * 主键in查询，只返回指定属性的值
     *
     * @param values           主键值集合
     * @param returnProperties 要返回的属性名集合
     * @param orders           排序规则
     * @return List<Map></Map><指定的属性名></指定的属性名>, 属性值>>
     */
    fun inSearchPropertiesById(
        values: List<PK>, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, *>>

    //endregion inSearch


    //region search Criteria
    /**
     * 复杂条件查询
     *
     * @param criteria 查询条件
     * @param orders   排序规则
     * @return 实体对象列表
     */
    fun search(criteria: Criteria, vararg orders: Order): List<T>

    /**
     * 复杂条件查询，只返回指定单个属性的值
     *
     * @param criteria       查询条件
     * @param returnProperty 要返回的属性名
     * @param orders         排序规则
     * @return 指定属性的值列表
     */
    fun searchProperty(criteria: Criteria, returnProperty: String, vararg orders: Order): List<*>

    /**
     * 复杂条件，只返回指定多个属性的值
     *
     * @param criteria         查询条件
     * @param returnProperties 要返回的属性名集合
     * @param orders           排序规则
     * @return List(Map(指定的属性名 属性值))
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
     */
    fun pagingSearch(criteria: Criteria, pageNo: Int, pageSize: Int, vararg orders: Order): List<T>

    /**
     * 分页查询，返回对象列表,返回只包含指定属性
     *
     * @param criteria 查询条件
     * @param pageNo   当前页码(从1开始)
     * @param pageSize 每页条数
     * @param orders   排序规则
     * @return 实体对象列表
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
     */
    fun pagingReturnProperties(
        criteria: Criteria, returnProperties: Collection<String>, pageNo: Int, pageSize: Int, vararg orders: Order
    ): List<Map<String?, Any?>>

    //endregion pagingSearch


    /**
     * 计算记录数
     *
     * @param criteria 查询条件
     * @return 记录数
     */
    fun count(criteria: Criteria): Long

    /**
     * 求和. 对满足条件的记录根据指定属性进行求和
     *
     * @param criteria 查询条件
     * @param property 待求和的属性
     * @return 和
     */
    fun sum(criteria: Criteria, property: String): Number

    /**
     * 求平均值. 对满足条件的记录根据指定属性进行求平均值
     *
     * @param criteria 查询条件
     * @param property 待求平均值的属性
     * @return 平均值
     */
    fun avg(criteria: Criteria, property: String): Number

    /**
     * 求最大值. 对满足条件的记录根据指定属性进行求最大值
     *
     * @param criteria 查询条件
     * @param property 待求最大值的属性
     * @return 最大值
     */
    fun max(criteria: Criteria, property: String): Any?

    /**
     * 求最小值. 对满足条件的记录根据指定属性进行求最小值
     *
     * @param criteria 查询条件
     * @param property 待求最小值的属性
     * @return 最小值
     */
    fun min(criteria: Criteria, property: String): Any?

}