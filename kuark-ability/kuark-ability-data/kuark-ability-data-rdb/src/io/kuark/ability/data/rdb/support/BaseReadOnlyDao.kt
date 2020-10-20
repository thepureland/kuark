package io.kuark.ability.data.rdb.support

import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.base.lang.GenericKit
import org.ktorm.dsl.QuerySource
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.first
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList
import org.ktorm.schema.Table
import kotlin.reflect.KClass

/**
 * 基础只读数据访问对象，封装某数据库实体的通用查询操作
 *
 * @param PK 实体主键类型
 * @param E 实体类型
 * @param T 数据库表-实体关联对象的类型
 * @author K
 * @since 1.0.0
 */
open class BaseReadOnlyDao<PK, E : IDbEntity<PK, E>, T : Table<E>> {

    /** 数据库表-实体关联对象 */
    protected val table: T

    constructor() {
        val tableClass = GenericKit.getSuperClassGenricClass(this::class, 2) as KClass<T>
        table = tableClass.objectInstance!!
    }

    /**
     * 返回T指定的表的查询源，基于该对象可以进行类似对数据库表的sql一样操作
     *
     * @return 查询源
     * @author K
     * @since 1.0.0
     */
    fun querySource(): QuerySource = RdbKit.getDatabase().from(table)

    /**
     * 返回T指定的表的实体序列，基于该序列可以进行类似对集合一样的操作
     *
     * @return 实体序列
     * @author K
     * @since 1.0.0
     */
    fun entitySequence(): EntitySequence<E, T> = RdbKit.getDatabase().sequenceOf(table)

    //region Search

    /**
     * 查询指定主键值的实体
     *
     * @param id 主键值，类型必须为以下之一：String、Int、Long
     * @return 实体
     * @author K
     * @since 1.0.0
     */
    fun getById(id: PK): E {
        return when (id) {
            is String -> entitySequence().first { (table as StringIdTable<*>).id eq id }
            is Int -> entitySequence().first { (table as IntIdTable<*>).id eq id }
            is Long -> entitySequence().first { (table as LongIdTable<*>).id eq id }
            else -> error("不支持的主键类型【${id!!::class}】")
        }
    }

    /**
     * 查询当前表的实体。对于大表慎用，容易造成内存溢出！
     *
     * @return 实体列表
     * @author K
     * @since 1.0.0
     */
    fun searchAll(): List<E> = entitySequence().toList()

    //endregion Search

}