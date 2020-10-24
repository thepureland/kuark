package io.kuark.ability.data.rdb.support

import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.base.lang.GenericKit
import io.kuark.base.support.GroupExecutor
import org.ktorm.database.Database
import org.ktorm.dsl.QuerySource
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.inList
import org.ktorm.entity.*
import org.ktorm.schema.Column
import org.ktorm.schema.ColumnDeclaring
import org.ktorm.schema.Table
import kotlin.reflect.KClass

/**
 * 基础只读数据访问对象，封装某数据库表的通用查询操作
 *
 * @param PK 实体主键类型
 * @param E 实体类型
 * @param T 数据库表-实体关联对象的类型
 * @author K
 * @since 1.0.0
 */
open class BaseReadOnlyDao<PK : Any, E : IDbEntity<PK, E>, T : Table<E>> {

//    /** 数据库表-实体关联对象 */
//    private lateinit var table: T

    /**
     * 返回数据库表-实体关联对象
     *
     * @return 数据库表-实体关联对象
     * @author K
     * @since 1.0.0
     */
    fun table(): T {
        val tableClass = GenericKit.getSuperClassGenricClass(this::class, 2) as KClass<T>
        return tableClass.objectInstance!!
    }

    /**
     * 返回当前表所在的数据库对象
     *
     * @return 当前表所在的数据库对象
     * @author K
     * @since 1.0.0
     */
    fun database(): Database = RdbKit.getDatabase()

    /**
     * 返回T指定的表的查询源，基于该对象可以进行类似对数据库表的sql一样操作
     *
     * @return 查询源
     * @author K
     * @since 1.0.0
     */
    fun querySource(): QuerySource = database().from(table())

    /**
     * 返回T指定的表的实体序列，基于该序列可以进行类似对集合一样的操作
     *
     * @return 实体序列
     * @author K
     * @since 1.0.0
     */
    fun entitySequence(): EntitySequence<E, T> = database().sequenceOf(table())

    //region Search

    /**
     * 查询指定主键值的实体
     *
     * @param id 主键值，类型必须为以下之一：String、Int、Long
     * @return 实体
     * @throws NoSuchElementException 不存在指定主键对应的实体时
     * @author K
     * @since 1.0.0
     */
    fun getById(id: PK): E {
        return entitySequence().first { (table()["id"] as Column<PK>) eq id }
    }

    /**
     * 批量查询指定主键值的实体
     *
     * @param ids 主键值可变参数，元素类型必须为以下之一：String、Int、Long，为空时返回空列表
     * @param countOfEachBatch 每批大小，缺省为1000
     * @return 实体列表，ids为空时返回空列表
     * @author K
     * @since 1.0.0
     */
    fun getByIds(vararg ids: PK, countOfEachBatch: Int = 1000): List<E> {
        if (ids.isEmpty()) return listOf()
        val results = mutableListOf<E>()
        GroupExecutor(listOf(ids), countOfEachBatch) {
            val result = entitySequence().filter { (table()["id"] as ColumnDeclaring<PK>).inList(*ids) }.toList()
            results.addAll(result)
        }.execute()
        return results
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

    /**
     * 返回当前表的总记录数
     *
     * @return 当前表的总记录数
     * @author K
     * @since 1.0.0
     */
    fun countAll(): Int {
//        return querySource().select(count(table()["id"])).map { row -> row.getInt(1) }[0]
        return entitySequence().count()
    }

}