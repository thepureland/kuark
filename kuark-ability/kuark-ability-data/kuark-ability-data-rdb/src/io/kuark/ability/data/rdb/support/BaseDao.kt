package io.kuark.ability.data.rdb.support

import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.base.support.GroupExecutor
import org.ktorm.dsl.batchInsert
import org.ktorm.dsl.batchUpdate
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import org.ktorm.entity.add
import org.ktorm.entity.removeIf
import org.ktorm.schema.Table

/**
 * 基础数据访问对象，封装某数据库实体的通用操作
 *
 * @param PK 实体主键类型
 * @param E 实体类型
 * @param T 数据库表-实体关联对象的类型
 * @author K
 * @since 1.0.0
 */
open class BaseDao<PK, E : IDbEntity<PK, E>, T : Table<E>> : BaseReadOnlyDao<PK, E, T>() {


    //region Insert

    /**
     * 插入指定实体到当前表
     *
     * @param entity 实体
     * @return 主键值
     * @author K
     * @since 1.0.0
     */
    fun insert(entity: E): PK? {
        entitySequence().add(entity)
        return entity.id
    }

    /**
     * 批量插入指定实体到当前表。
     *
     * @param entities 实体集合
     * @param countOfEachBatch 每批大小，缺省为1000
     * @return 成功插入的记录数
     * @author K
     * @since 1.0.0
     */
    fun batchInsert(entities: Collection<E>, countOfEachBatch: Int = 1000): Int {
        var totalCount = 0
        GroupExecutor(entities, countOfEachBatch) {
            val counts = RdbKit.getDatabase().batchInsert(table) {
                it.forEach { _ -> item { entity -> entity } }
            }
            totalCount += counts.sum()
        }.execute()
        return totalCount
    }

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
    fun update(entity: E): Boolean = entity.flushChanges() == 1

    /**
     * 批量更新实体对应的记录
     *
     * @param entities 实体集合
     * @param countOfEachBatch 每批大小，缺省为1000
     * @return 更新成功的记录数
     * @author K
     * @since 1.0.0
     */
    fun batchUpdate(entities: Collection<E>, countOfEachBatch: Int = 1000): Int {
        var totalCount = 0
        GroupExecutor(entities) {
            val counts = RdbKit.getDatabase().batchUpdate(table) { it }
            totalCount += counts.sum()
        }.execute()
        return totalCount
    }

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
    fun deleteById(id: PK): Boolean {
        val count = when (id) {
            is String -> entitySequence().removeIf { (table as StringIdTable<*>).id eq id }
            is Int -> entitySequence().removeIf { (table as IntIdTable<*>).id eq id }
            is Long -> entitySequence().removeIf { (table as LongIdTable<*>).id eq id }
            else -> error("不支持的主键类型【${id!!::class}】")
        }
        return count == 1
    }

    /**
     * 删除实体对应的记录
     *
     * @param entity 实体
     * @return 是否删除成功
     * @author K
     * @since 1.0.0
     */
    fun delete(entity: E): Boolean {
        val id = entity.id
        id ?: error("删除实体时，属性id不能为null")
        return deleteById(id)
    }

    /**
     * 批量删除实体对应的记录
     *
     * @param entities 实体集合
     * @return 删除成功的记录数
     * @author K
     * @since 1.0.0
     */
    fun batchDelete(entities: Collection<E>): Int {
        var totalCount = 0
        GroupExecutor(entities) {
            //TODO
//            val counts = RdbKit.getDatabase().dele(table) { it }
//            totalCount += counts.sum()
        }.execute()
        return totalCount
    }

    //endregion Delete

}