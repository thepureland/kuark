package io.kuark.base.support

import java.io.Serializable

/**
 * 回调接口
 *
 * @author K
 * @since 1.0.0
 */
interface ICallback<P, R> : Serializable {
    /**
     * 回调行为
     *
     * @param p 参数
     * @return 返回值
     */
    fun execute(p: P): R
}