package io.kuark.base.support

import java.io.Serializable

/**
 * 回调接口
 *
 * @param P 参数类型
 * @param R 返回值类型
 * @author K
 * @since 1.0.0
 */
fun interface ICallback<P, R> : Serializable {

    /**
     * 回调行为
     *
     * @param p 参数
     * @return 返回值
     * @author K
     * @since 1.0.0
     */
    fun execute(p: P): R

}