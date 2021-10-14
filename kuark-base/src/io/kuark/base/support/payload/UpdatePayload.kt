package io.kuark.base.support.payload

import io.kuark.base.support.IIdEntity

/**
 * 更新数据项载体父类
 *
 * 更新规则:
 *    1. id属性不会被更新
 *    2. nullProperties的所有属性的值都更新为null
 *    3. 其它属性的值不为null时才更新该属性
 *
 * @author K
 * @since 1.0.0
 */
open class UpdatePayload<S: SearchPayload> {

    /** 值要设置为null的属性的列表 */
    open var nullProperties: List<String>? = null

    /** 查询项载体，为null时，查询条件也可通过where条件表达式工厂函数自定义，此时各条件间的查询逻辑为AND */
    open var searchPayload: S? = null

}