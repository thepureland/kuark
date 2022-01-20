package io.kuark.base.support.payload

import io.kuark.base.support.IIdEntity


/**
 * 表单载体父类
 *
 * @param T ID类型
 * @author K
 * @since 1.0.0
 */
open class FormPayload<T>: IIdEntity<T> {

    /** 惟一标识 */
    override var id: T? = null

}