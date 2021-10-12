package io.kuark.ability.data.rdb.biz

import io.kuark.ability.data.rdb.support.IDbEntity
import io.kuark.base.query.Criteria
import io.kuark.base.support.payload.UpdatePayload
import java.lang.IllegalArgumentException

/**
 * 基于关系型数据库表的基础业务操作接口
 *
 * @param PK 实体主键类型
 * @param E 实体类型
 * @author K
 * @since 1.0.0
 */
interface IBaseBiz<PK : Any, E : IDbEntity<PK, E>>: IBaseReadOnlyBiz<PK, E> {

    //region Insert

    /**
     * 插入指定实体到当前表
     *
     * @param entity 实体
     * @return 主键值
     * @author K
     * @since 1.0.0
     */
    fun insert(entity: E): PK

    /**
     * 保存实体对象，只保存指定的属性
     *
     * @param entity 实体对象
     * @param propertyNames 要保存的属性的可变数组
     * @return 主键值
     */
    fun insertOnly(entity: E, vararg propertyNames: String): PK

    /**
     * 保存实体对象，不保存指定的属性
     *
     * @param entity 实体对象
     * @param excludePropertyNames 不保存的属性的可变数组
     * @return 主键值
     */
    fun insertExclude(entity: E, vararg excludePropertyNames: String): PK

    /**
     * 批量插入指定实体到当前表。
     *
     * ktorm底层该方法是基于原生 JDBC 提供的 executeBatch 函数实现
     *
     * @param entities 实体集合
     * @param countOfEachBatch 每批大小，缺省为1000
     * @return 成功插入的记录数
     * @author K
     * @since 1.0.0
     */
    fun batchInsert(entities: Collection<E>, countOfEachBatch: Int = 1000): Int

    /**
     * 批量保存实体对象，只保存指定的属性
     *
     * @param entities 实体对象列表
     * @param countOfEachBatch 每批大小，缺省为1000
     * @param propertyNames 要保存的属性的可变数组
     * @return 保存的记录数
     */
    fun batchInsertOnly(entities: Collection<E>, countOfEachBatch: Int = 1000, vararg propertyNames: String): Int

    /**
     * 批量保存实体对象，不保存指定的属性
     *
     * @param entities 实体对象列表
     * @param countOfEachBatch 每批大小，缺省为1000
     * @param excludePropertyNames 不保存的属性的可变数组
     * @return 保存的记录数
     */
    fun batchInsertExclude(
        entities: Collection<E>, countOfEachBatch: Int = 1000, vararg excludePropertyNames: String
    ): Int

    //endregion Insert


    //region Update

    /**
     * 更新指定实体对应的记录
     *
     * @param entity 实体
     * @return 是否更新成功
     * @author K
     * @since 1.0.0
     */
    fun update(entity: E): Boolean

    /**
     * 用任意对象更新指定id的记录.
     * 更新规则见 @see UpdatePayload 类
     *
     * @param id 主键
     * @param updatePayload 更新项载体
     * @return 是否更新成功
     * @throws IllegalArgumentException 主键值为空时
     * @author K
     * @since 1.0.0
     */
    fun update(updatePayload: UpdatePayload<PK>): Boolean

    /**
     * 有条件的更新实体对象（仅当满足给定的附加查询条件时）
     *
     * @param entity 实体对象
     * @param criteria 附加查询条件
     * @return 记录是否有更新
     */
    fun updateWhen(entity: E, criteria: Criteria): Boolean

    /**
     * 只更新实体的某几个属性
     *
     * @param id         主键值
     * @param properties Map(属性名，属性值)
     * @return 是否更新成功
     */
    fun updateProperties(id: PK, properties: Map<String, *>): Boolean

    /**
     * 有条件的只更新实体的某几个属性（仅当满足给定的附加查询条件时）
     * 注：id属性永远不会被更新
     *
     * @param id         主键值
     * @param properties Map(属性名，属性值)
     * @param criteria 附加查询条件
     * @return 记录是否有更新
     */
    fun updatePropertiesWhen(id: PK, properties: Map<String, *>, criteria: Criteria): Boolean

    /**
     * 只更新实体的某几个属性
     *
     * @param entity     实体对象
     * @param propertyNames 更新的属性的可变数组
     * @return 是否更新成功
     */
    fun updateOnly(entity: E, vararg propertyNames: String): Boolean

    /**
     * 有条件的只更新实体的某几个属性（仅当满足给定的附加查询条件时）
     * 注：id属性永远不会被更新
     *
     * @param entity     实体对象
     * @param criteria 附加查询条件
     * @param propertyNames 更新的属性的可变数组
     * @return 记录是否有更新
     */
    fun updateOnlyWhen(entity: E, criteria: Criteria, vararg propertyNames: String): Boolean

    /**
     * 有条件的更新实体除指定的几个属性外的所有属性（仅当满足给定的附加查询条件时）
     * 注：id属性永远不会被更新
     *
     * @param entity            实体对象
     * @param criteria 附加查询条件
     * @param excludePropertyNames 不更新的属性的可变数组
     * @return 记录是否有更新
     */
    fun updateExcludePropertiesWhen(entity: E, criteria: Criteria, vararg excludePropertyNames: String): Boolean

    /**
     * 批量更新实体对应的记录
     *
     * ktorm底层该方法是基于原生 JDBC 提供的 executeBatch 函数实现
     *
     * @param entities 实体对象集合
     * @param countOfEachBatch 每批大小，缺省为1000
     * @return 更新成功的记录数
     * @throws IllegalStateException 存在主键为null时
     * @author K
     * @since 1.0.0
     */
    fun batchUpdate(entities: Collection<E>, countOfEachBatch: Int = 1000): Int

    /**
     * 有条件的批量更新实体对象（仅当满足给定的附加查询条件时）
     *
     * @param entities 实体对象集合
     * @param criteria 附加查询条件
     * @param countOfEachBatch 每批大小，缺省为1000
     * @return 更新的记录数
     */
    fun batchUpdateWhen(entities: Collection<E>, criteria: Criteria, countOfEachBatch: Int = 1000): Int

    /**
     * 更新实体除指定的几个属性外的所有属性
     * 注：id属性永远不会被更新
     *
     * @param entity            实体对象
     * @param excludePropertyNames 不更新的属性的可变数组
     * @return 是否更新成功
     */
    fun updateExcludeProperties(entity: E, vararg excludePropertyNames: String): Boolean

    /**
     * 批量更新实体对象, 只更新实体的某几个属性
     *
     * @param criteria   查询条件
     * @param properties Map(属性名，属性值)
     * @return 是否更新成功
     */
    fun batchUpdateProperties(criteria: Criteria, properties: Map<String, *>): Int

    /**
     * 批量更新实体对象指定的几个属性
     *
     * @param entities   实体对象列表
     * @param countOfEachBatch 每批大小，缺省为1000
     * @param propertyNames 更新的属性的可变数组
     * @return 更新的记录数
     */
    fun batchUpdateOnly(entities: Collection<E>, countOfEachBatch: Int = 1000, vararg propertyNames: String): Int

    /**
     * 有条件的批量更新实体对象指定的几个属性（仅当满足给定的附加查询条件时）
     * 注：id属性永远不会被更新
     *
     * @param entities   实体对象列表
     * @param criteria 附加查询条件
     * @param countOfEachBatch 每批大小，缺省为1000
     * @param propertyNames 更新的属性的可变数组
     * @return 更新的记录数
     */
    fun batchUpdateOnlyWhen(
        entities: Collection<E>, criteria: Criteria, countOfEachBatch: Int = 1000, vararg propertyNames: String
    ): Int

    /**
     * 批量更新实体除了指定几个属性外的所有属性
     * 注：id属性永远不会被更新
     *
     * @param entities   实体对象列表
     * @param countOfEachBatch 每批大小，缺省为1000
     * @param excludePropertyNames 不更新的属性的可变数组
     * @return 是否更新成功
     */
    fun batchUpdateExcludeProperties(
        entities: Collection<E>, countOfEachBatch: Int = 1000, vararg excludePropertyNames: String
    ): Int

    /**
     * 有条件的批量更新实体除了指定几个属性外的所有属性（仅当满足给定的附加查询条件时）
     * 注：id属性永远不会被更新
     *
     * @param entities   实体对象列表
     * @param criteria 附加查询条件
     * @param countOfEachBatch 每批大小，缺省为1000
     * @param excludePropertyNames 不更新的属性的可变数组
     * @return 是否更新成功
     */
    fun batchUpdateExcludePropertiesWhen(
        entities: Collection<E>, criteria: Criteria, countOfEachBatch: Int = 1000, vararg excludePropertyNames: String
    ): Int

    //endregion Update


    //region Delete

    /**
     * 删除指定主键值对应的记录
     *
     * @param id 主键值，类型必须为以下之一：String、Int、Long
     * @return 是否删除成功
     * @author K
     * @since 1.0.0
     */
    fun deleteById(id: PK): Boolean

    /**
     * 批量删除指定主键对应的实体对象
     *
     * @param ids 主键列表
     * @return 删除的记录数
     */
    fun batchDelete(ids: Collection<PK>): Int

    /**
     * 批量删除指定主键对应的实体对象
     *
     * @param criteria 查询条件
     * @return 删除的记录数
     */
    fun batchDeleteCriteria(criteria: Criteria): Int

    /**
     * 删除实体对应的记录
     *
     * @param entity 实体
     * @return 是否删除成功
     * @author K
     * @since 1.0.0
     */
    fun delete(entity: E): Boolean

    //endregion Delete

}