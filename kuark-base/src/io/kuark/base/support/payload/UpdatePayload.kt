package io.kuark.base.support.payload

import io.kuark.base.support.IIdEntity

/**
 * 更新数据项载体父类
 *
 * 更新规则:
 *    1. id属性用于查询待更新的记录, 不能为空, 本身不会被更新
 *    2. nullProperties的所有属性的值都更新为null
 *    3. 添加@Transient注解的属性不作更新
 *    4. 其它属性的值不为null时才更新该属性
 *
 * @author K
 * @since 1.0.0
 */
open class UpdatePayload<T> : IIdEntity<T> {

    override var id: T? = null

    /** 值要设置为null的属性的列表 */
    open var nullProperties: List<String>? = null

}