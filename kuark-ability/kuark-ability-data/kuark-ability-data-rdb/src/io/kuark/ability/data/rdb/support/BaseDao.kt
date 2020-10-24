package io.kuark.ability.data.rdb.support

import io.kuark.base.lang.string.humpToUnderscore
import io.kuark.base.support.GroupExecutor
import org.ktorm.dsl.*
import org.ktorm.entity.add
import org.ktorm.entity.removeIf
import org.ktorm.schema.Column
import org.ktorm.schema.ColumnDeclaring
import org.ktorm.schema.Table

/**
 * 基础数据访问对象，封装某数据库表的通用操作
 *
 * @param PK 实体主键类型
 * @param E 实体类型
 * @param T 数据库表-实体关联对象的类型
 * @author K
 * @since 1.0.0
 */
open class BaseDao<PK : Any, E : IDbEntity<PK, E>, T : Table<E>> : BaseReadOnlyDao<PK, E, T>() {


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
     * ktorm底层该方法是基于原生 JDBC 提供的 executeBatch 函数实现
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
            val counts = database().batchInsert(table()) {
                it.forEach { entity ->
                    item {
                        for ((name, value) in entity.properties) {
                            set(table()[name.humpToUnderscore().toLowerCase()], value) //TODO 有没有办法直接取得列名？
                        }
                    }
                }
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
     * ktorm底层该方法是基于原生 JDBC 提供的 executeBatch 函数实现
     *
     * @param entities 实体集合
     * @param countOfEachBatch 每批大小，缺省为1000
     * @return 更新成功的记录数
     * @throws IllegalStateException 存在主键为null时
     * @author K
     * @since 1.0.0
     */
    fun batchUpdate(entities: Collection<E>, countOfEachBatch: Int = 1000): Int {
        if (entities.filter { it.id == null }.isNotEmpty()) {
            error("由于存在主键为null的实体，批量更新失败！")
        }

        var totalCount = 0
        GroupExecutor(entities, countOfEachBatch) {
            val counts = database().batchUpdate(table()) {
                for (entity in it) {
                    item {
                        entity.properties.filter { it.key != "id" }.forEach { (name, value) ->
                            set(table()[name.humpToUnderscore().toLowerCase()], value) //TODO 有没有办法直接取得列名？
                        }
                        where { (table()["id"] as Column<PK>) eq entity.id!! }
                    }
                }
            }
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
            is String -> entitySequence().removeIf { (table() as StringIdTable<*>).id eq id }
            is Int -> entitySequence().removeIf { (table() as IntIdTable<*>).id eq id }
            is Long -> entitySequence().removeIf { (table() as LongIdTable<*>).id eq id }
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


    //endregion Delete

}