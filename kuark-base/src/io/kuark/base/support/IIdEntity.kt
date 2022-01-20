package io.kuark.base.support

/**
 * id实体接口
 *
 * @param T 实体类型
 * @author K
 * @since 1.0.0
 */
interface IIdEntity<T> {

    /** 惟一标识 */
    var id: T?

}