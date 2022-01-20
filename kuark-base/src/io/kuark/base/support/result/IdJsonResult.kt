package io.kuark.base.support.result

import io.kuark.base.support.IIdEntity


/**
 * 要以json返回的带有惟一标识的结果对象，会自动去除值为null的属性
 *
 * @param T ID类型
 * @author K
 * @since 1.0.0
 */
open class IdJsonResult<T>: IJsonResult, IIdEntity<T> {

    /** 惟一标识 */
    override var id: T? = null

}