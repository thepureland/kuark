package io.kuark.cache.core

import java.lang.annotation.Inherited
import kotlin.reflect.KClass

/**
 * 可批量缓存
 *
 * 批量缓存实现的原则：缓存优先，批量查询，单个缓存。
 *
 * 约束：
 * 1.缓存key为String类型
 * 2.缓存名称一定要指定，并不会像spring的@Cacheable那样缺省取方法名
 * 3.方法的返回值必须是Map，其key为缓存的key，value通过@BatchCacheable的valueClass定义
 * 4.方法必须是open的
 * 5.组成缓存key的方法参数，如果是数组，其元素只能是基本类型
 *
 * 注意：由于参数组合的复杂性，批量缓存无法做到十分精确的避免查询出来的数据在缓存中一定是不存在的，只能尽量减少这种情况的发生。
 *      这种情况下，不会用查询出来的数据更新缓存中已存在的数据。参数个数或单个参数的元素越多，这种情况就越突出。
 *
 * @author K
 * @since 1.0.0
 */
@MustBeDocumented
@Inherited
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class BatchCacheable(

    /** 缓存名称，也可以通过@CacheConfig在类上指定 **/
    val cacheNames: Array<String> = [],

    /** 实现IKeysGenerator接口的spring bean的名称 */
    val keysGenerator: String = "defaultKeysGenerator",

    /** 单个key对应的缓存值的类型 */
    val valueClass: KClass<*>,

    /** 处理缓存key时忽略的参数的索引 */
    val ignoreParamIndexes: IntArray = []

)