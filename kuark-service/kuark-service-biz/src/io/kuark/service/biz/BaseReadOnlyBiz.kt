package io.kuark.service.biz

import io.kuark.ability.data.rdb.support.BaseReadOnlyDao
import io.kuark.ability.data.rdb.support.IDbEntity
import io.kuark.base.query.Criteria
import io.kuark.base.query.sort.Order
import org.springframework.beans.factory.annotation.Autowired

/**
 * 基础的只读业务操作
 *
 * @param PK 实体主键类型
 * @param E 实体类型
 * @author K
 * @since 1.0.0
 */
open class BaseReadOnlyBiz<PK : Any, E : IDbEntity<PK, E>, DAO : BaseReadOnlyDao<PK, E, *>> : IBaseReadOnlyBiz<PK, E> {

    @Autowired
    protected lateinit var dao: DAO

    override fun getById(id: PK): E? = dao.getById(id)

    override fun getByIds(vararg ids: PK, countOfEachBatch: Int): List<E> =
        dao.getByIds(*ids, countOfEachBatch = countOfEachBatch)

    override fun oneSearch(property: String, value: Any?, vararg orders: Order): List<E> =
        dao.oneSearch(property, value, *orders)

    override fun oneSearchProperty(
        property: String, value: Any?, returnProperty: String, vararg orders: Order
    ): List<*> = dao.oneSearchProperty(property, value, returnProperty, *orders)

    override fun oneSearchProperties(
        property: String, value: Any?, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, *>> = dao.oneSearchProperties(property, value, returnProperties, *orders)

    override fun allSearch(vararg orders: Order): List<E> = dao.allSearch(*orders)

    override fun allSearchProperty(returnProperty: String, vararg orders: Order): List<*> =
        dao.allSearchProperty(returnProperty, *orders)

    override fun allSearchProperties(returnProperties: Collection<String>, vararg orders: Order): List<Map<String, *>> =
        dao.allSearchProperties(returnProperties, *orders)

    override fun andSearch(properties: Map<String, *>, vararg orders: Order): List<E> =
        dao.andSearch(properties, *orders)

    override fun andSearchProperty(properties: Map<String, *>, returnProperty: String, vararg orders: Order): List<*> =
        dao.andSearchProperty(properties, returnProperty, *orders)

    override fun andSearchProperties(
        properties: Map<String, *>, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, *>> = dao.andSearchProperties(properties, returnProperties, *orders)

    override fun orSearch(properties: Map<String, *>, vararg orders: Order): List<E> =
        dao.orSearch(properties, *orders)

    override fun orSearchProperty(properties: Map<String, *>, returnProperty: String, vararg orders: Order): List<*> =
        dao.orSearchProperty(properties, returnProperty, *orders)

    override fun orSearchProperties(
        properties: Map<String, *>, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, *>> = dao.orSearchProperties(properties, returnProperties, *orders)

    override fun inSearch(property: String, values: Collection<*>, vararg orders: Order): List<E> =
        dao.inSearch(property, values, *orders)

    override fun inSearchProperty(
        property: String, values: List<*>, returnProperty: String, vararg orders: Order
    ): List<*> = dao.inSearchProperty(property, values, returnProperty, *orders)

    override fun inSearchProperties(
        property: String, values: List<*>, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, *>> = dao.inSearchProperties(property, values, returnProperties, *orders)

    override fun inSearchById(values: List<PK>, vararg orders: Order): List<E> =
        dao.inSearchById(values, *orders)

    override fun inSearchPropertyById(values: List<PK>, returnProperty: String, vararg orders: Order): List<*> =
        dao.inSearchPropertyById(values, returnProperty, *orders)

    override fun inSearchPropertiesById(
        values: List<PK>, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, *>> = dao.inSearchPropertiesById(values, returnProperties, *orders)

    override fun search(criteria: Criteria, vararg orders: Order): List<E> =
        dao.search(criteria, *orders)

    override fun searchProperty(criteria: Criteria, returnProperty: String, vararg orders: Order): List<*> =
        dao.searchProperty(criteria, returnProperty, *orders)

    override fun searchProperties(
        criteria: Criteria, returnProperties: Collection<String>, vararg orders: Order
    ): List<Map<String, Any?>> = dao.searchProperties(criteria, returnProperties, *orders)

    override fun pagingSearch(criteria: Criteria, pageNo: Int, pageSize: Int, vararg orders: Order): List<E> =
        dao.pagingSearch(criteria, pageNo, pageSize, *orders)

    override fun pagingReturnProperty(
        criteria: Criteria, returnProperty: String, pageNo: Int, pageSize: Int, vararg orders: Order
    ): List<*> = dao.pagingReturnProperty(criteria, returnProperty, pageNo, pageSize, *orders)

    override fun pagingReturnProperties(
        criteria: Criteria, returnProperties: Collection<String>, pageNo: Int, pageSize: Int, vararg orders: Order
    ): List<Map<String, *>> = dao.pagingReturnProperties(criteria, returnProperties, pageNo, pageSize, *orders)

    override fun count(criteria: Criteria?): Int = dao.count(criteria)

    override fun sum(property: String, criteria: Criteria?): Number = dao.sum(property, criteria)

    override fun avg(property: String, criteria: Criteria?): Number = dao.avg(property, criteria)

    override fun max(property: String, criteria: Criteria?): Any = dao.avg(property, criteria)

    override fun min(property: String, criteria: Criteria?): Any = dao.avg(property, criteria)

}