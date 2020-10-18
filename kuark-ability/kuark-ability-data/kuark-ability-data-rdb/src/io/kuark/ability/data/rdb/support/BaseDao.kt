package io.kuark.ability.data.rdb.support

import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.base.lang.GenericKit
import io.kuark.base.support.GroupExecutor
import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.ktorm.schema.Table
import kotlin.reflect.KClass

open class BaseDao<PK, E : IDbEntity<PK, E>, T : Table<E>> {

    protected val table: T

    constructor() {
        val tableClass = GenericKit.getSuperClassGenricClass(this::class, 2) as KClass<T>
        table = tableClass.objectInstance!!
    }

    fun querySource(): QuerySource = RdbKit.getDatabase().from(table)

    fun entitySequence(): EntitySequence<E, T> = RdbKit.getDatabase().sequenceOf(table)

    //region Search

    fun getById(id: PK): E {
        return when (id) {
            is String -> entitySequence().first { (table as StringIdTable<*>).id eq id }
            is Int -> entitySequence().first { (table as IntIdTable<*>).id eq id }
            is Long -> entitySequence().first { (table as LongIdTable<*>).id eq id }
            else -> error("不支持的主键类型【${id!!::class}】")
        }
    }

    fun searchAll(): List<E> = entitySequence().toList()

    //endregion Search


    //region Insert

    fun insert(entity: E): PK? {
        entitySequence().add(entity)
        return entity.id
    }

    fun batchInsert(entities: Collection<E>): Int {
        var totalCount = 0
        GroupExecutor(entities) {
            val counts = RdbKit.getDatabase().batchInsert(table) { it }
            totalCount += counts.sum()
        }.execute()
        return totalCount
    }

    //endregion Insert


    //region Update

    fun update(entity: E): Boolean {
        return RdbKit.getDatabase().update(table) { entity } == 1
    }

    fun batchUpdate(entities: Collection<E>): Int {
        var totalCount = 0
        GroupExecutor(entities) {
            val counts = RdbKit.getDatabase().batchUpdate(table) { it }
            totalCount += counts.sum()
        }.execute()
        return totalCount
    }

    //endregion Update


    //region Delete

    fun deleteById(id: PK): Boolean {
        val count = when (id) {
            is String -> entitySequence().removeIf { (table as StringIdTable<*>).id eq id }
            is Int -> entitySequence().removeIf { (table as IntIdTable<*>).id eq id }
            is Long -> entitySequence().removeIf { (table as LongIdTable<*>).id eq id }
            else -> error("不支持的主键类型【${id!!::class}】")
        }
        return count == 1
    }

    fun delete(entity: E): Boolean {
        val id = entity.id
        id ?: error("删除实体时，属性id不能为null")
        return deleteById(id)
    }

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