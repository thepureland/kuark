package io.kuark.ability.data.rdb.biz

import io.kuark.ability.data.rdb.support.BaseCrudDao
import io.kuark.ability.data.rdb.support.IDbEntity
import io.kuark.base.query.Criteria
import io.kuark.base.support.biz.IBaseCrudBiz
import io.kuark.base.support.payload.SearchPayload
import io.kuark.base.support.payload.UpdatePayload
import org.springframework.transaction.annotation.Transactional

/**
 * 基于关系型数据库表的基础业务操作
 *
 * @param PK 实体主键类型
 * @param E 实体类型
 * @author K
 * @since 1.0.0
 */
open class BaseCrudBiz<PK : Any, E : IDbEntity<PK, E>, DAO : BaseCrudDao<PK, E, *>>
    : BaseReadOnlyBiz<PK, E, DAO>(), IBaseCrudBiz<PK, E> {

    @Transactional
    override fun insert(any: Any): PK = dao.insert(any)

    @Transactional
    override fun insertOnly(entity: E, vararg propertyNames: String): PK = dao.insertOnly(entity, *propertyNames)

    @Transactional
    override fun insertExclude(entity: E, vararg excludePropertyNames: String): PK =
        dao.insertExclude(entity, *excludePropertyNames)

    @Transactional
    override fun batchInsert(objects: Collection<Any>, countOfEachBatch: Int): Int =
        dao.batchInsert(objects, countOfEachBatch)

    @Transactional
    override fun batchInsertOnly(entities: Collection<E>, countOfEachBatch: Int, vararg propertyNames: String): Int =
        dao.batchInsertOnly(entities, countOfEachBatch, *propertyNames)

    @Transactional
    override fun batchInsertExclude(
        entities: Collection<E>, countOfEachBatch: Int, vararg excludePropertyNames: String
    ): Int = dao.batchInsertExclude(entities, countOfEachBatch, *excludePropertyNames)

    @Transactional
    override fun update(any: Any): Boolean = dao.update(any)

    @Transactional
    override fun updateWhen(entity: E, criteria: Criteria): Boolean = dao.updateWhen(entity, criteria)

    @Transactional
    override fun updateProperties(id: PK, properties: Map<String, *>): Boolean =
        dao.updateProperties(id, properties)

    @Transactional
    override fun updatePropertiesWhen(id: PK, properties: Map<String, *>, criteria: Criteria): Boolean =
        dao.updatePropertiesWhen(id, properties, criteria)

    @Transactional
    override fun updateOnly(entity: E, vararg propertyNames: String): Boolean = dao.updateOnly(entity, *propertyNames)

    @Transactional
    override fun updateOnlyWhen(entity: E, criteria: Criteria, vararg propertyNames: String): Boolean =
        dao.updateOnlyWhen(entity, criteria, *propertyNames)

    @Transactional
    override fun updateExcludePropertiesWhen(
        entity: E, criteria: Criteria, vararg excludePropertyNames: String
    ): Boolean = dao.updateExcludePropertiesWhen(entity, criteria, *excludePropertyNames)

    @Transactional
    override fun batchUpdate(entities: Collection<E>, countOfEachBatch: Int): Int =
        dao.batchUpdate(entities, countOfEachBatch)

    @Transactional
    override fun batchUpdateWhen(entities: Collection<E>, criteria: Criteria, countOfEachBatch: Int): Int =
        dao.batchUpdateWhen(entities, criteria, countOfEachBatch)

    @Transactional
    override fun <S : SearchPayload> batchUpdateWhen(updatePayload: UpdatePayload<S>): Int =
        dao.batchUpdateWhen(updatePayload)

    @Transactional
    override fun updateExcludeProperties(entity: E, vararg excludePropertyNames: String): Boolean =
        dao.updateExcludeProperties(entity, *excludePropertyNames)

    @Transactional
    override fun batchUpdateProperties(criteria: Criteria, properties: Map<String, *>): Int =
        dao.batchUpdateProperties(criteria, properties)

    @Transactional
    override fun batchUpdateOnly(entities: Collection<E>, countOfEachBatch: Int, vararg propertyNames: String): Int =
        dao.batchUpdateOnly(entities, countOfEachBatch, *propertyNames)

    @Transactional
    override fun batchUpdateOnlyWhen(
        entities: Collection<E>, criteria: Criteria, countOfEachBatch: Int, vararg propertyNames: String
    ): Int = dao.batchUpdateOnlyWhen(entities, criteria, countOfEachBatch)

    @Transactional
    override fun batchUpdateExcludeProperties(
        entities: Collection<E>, countOfEachBatch: Int, vararg excludePropertyNames: String
    ): Int = dao.batchUpdateExcludeProperties(entities, countOfEachBatch, *excludePropertyNames)

    @Transactional
    override fun batchUpdateExcludePropertiesWhen(
        entities: Collection<E>, criteria: Criteria, countOfEachBatch: Int, vararg excludePropertyNames: String
    ): Int = dao.batchUpdateExcludePropertiesWhen(entities, criteria, countOfEachBatch, *excludePropertyNames)

    @Transactional
    override fun deleteById(id: PK): Boolean = dao.deleteById(id)

    @Transactional
    override fun batchDelete(ids: Collection<PK>): Int = dao.batchDelete(ids)

    @Transactional
    override fun batchDeleteCriteria(criteria: Criteria): Int = dao.batchDeleteCriteria(criteria)

    @Transactional
    override fun batchDeleteWhen(searchPayload: SearchPayload): Int = dao.batchDeleteWhen(searchPayload)

    @Transactional
    override fun delete(entity: E): Boolean = dao.delete(entity)

}