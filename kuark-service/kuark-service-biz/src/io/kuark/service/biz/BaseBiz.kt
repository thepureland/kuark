package io.kuark.service.biz

import io.kuark.ability.data.rdb.support.BaseDao
import io.kuark.ability.data.rdb.support.IDbEntity
import io.kuark.base.query.Criteria

/**
 * 基础业务操作
 *
 * @param PK 实体主键类型
 * @param E 实体类型
 * @author K
 * @since 1.0.0
 */
open class BaseBiz<PK : Any, E : IDbEntity<PK, E>> : BaseReadOnlyBiz<PK, E, BaseDao<PK, E, *>>(), IBaseBiz<PK, E> {

    override fun insert(entity: E): PK = dao.insert(entity)

    override fun insertOnly(entity: E, vararg propertyNames: String): PK = dao.insertOnly(entity, *propertyNames)

    override fun insertExclude(entity: E, vararg excludePropertyNames: String): PK =
        dao.insertExclude(entity, *excludePropertyNames)

    override fun batchInsert(entities: Collection<E>, countOfEachBatch: Int): Int =
        dao.batchInsert(entities, countOfEachBatch)

    override fun batchInsertOnly(entities: Collection<E>, countOfEachBatch: Int, vararg propertyNames: String): Int =
        dao.batchInsertOnly(entities, countOfEachBatch, *propertyNames)

    override fun batchInsertExclude(
        entities: Collection<E>, countOfEachBatch: Int, vararg excludePropertyNames: String
    ): Int = dao.batchInsertExclude(entities, countOfEachBatch, *excludePropertyNames)

    override fun update(entity: E): Boolean = dao.update(entity)

    override fun updateWhen(entity: E, criteria: Criteria): Boolean = dao.updateWhen(entity, criteria)

    override fun updateProperties(id: PK, properties: Map<String, *>): Boolean =
        dao.updateProperties(id, properties)

    override fun updatePropertiesWhen(id: PK, properties: Map<String, *>, criteria: Criteria): Boolean =
        dao.updatePropertiesWhen(id, properties, criteria)

    override fun updateOnly(entity: E, vararg propertyNames: String): Boolean = dao.updateOnly(entity, *propertyNames)

    override fun updateOnlyWhen(entity: E, criteria: Criteria, vararg propertyNames: String): Boolean =
        dao.updateOnlyWhen(entity, criteria, *propertyNames)

    override fun updateExcludePropertiesWhen(
        entity: E, criteria: Criteria, vararg excludePropertyNames: String
    ): Boolean = dao.updateExcludePropertiesWhen(entity, criteria, *excludePropertyNames)

    override fun batchUpdate(entities: Collection<E>, countOfEachBatch: Int): Int =
        dao.batchUpdate(entities, countOfEachBatch)

    override fun batchUpdateWhen(entities: Collection<E>, criteria: Criteria, countOfEachBatch: Int): Int =
        dao.batchUpdateWhen(entities, criteria, countOfEachBatch)

    override fun updateExcludeProperties(entity: E, vararg excludePropertyNames: String): Boolean =
        dao.updateExcludeProperties(entity, *excludePropertyNames)

    override fun batchUpdateProperties(criteria: Criteria, properties: Map<String, *>): Int =
        dao.batchUpdateProperties(criteria, properties)

    override fun batchUpdateOnly(entities: Collection<E>, countOfEachBatch: Int, vararg propertyNames: String): Int =
        dao.batchUpdateOnly(entities, countOfEachBatch, *propertyNames)

    override fun batchUpdateOnlyWhen(
        entities: Collection<E>, criteria: Criteria, countOfEachBatch: Int, vararg propertyNames: String
    ): Int = dao.batchUpdateOnlyWhen(entities, criteria, countOfEachBatch)

    override fun batchUpdateExcludeProperties(
        entities: Collection<E>, countOfEachBatch: Int, vararg excludePropertyNames: String
    ): Int = dao.batchUpdateExcludeProperties(entities, countOfEachBatch, *excludePropertyNames)

    override fun batchUpdateExcludePropertiesWhen(
        entities: Collection<E>, criteria: Criteria, countOfEachBatch: Int, vararg excludePropertyNames: String
    ): Int = dao.batchUpdateExcludePropertiesWhen(entities, criteria, countOfEachBatch, *excludePropertyNames)

    override fun deleteById(id: PK): Boolean = dao.deleteById(id)

    override fun batchDelete(ids: Collection<PK>): Int = dao.batchDelete(ids)

    override fun batchDeleteCriteria(criteria: Criteria): Int = dao.batchDeleteCriteria(criteria)

    override fun delete(entity: E): Boolean = dao.delete(entity)

}