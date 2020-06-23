package org.kuark.base.support;

import java.io.Serializable;

/**
 * 回调接口
 *
 * @since 1.0.0
 */
public interface ICallback<P, R> extends Serializable{

    /**
     * 回调行为
     *
     * @param p 参数
     * @return 返回值
     */
    R execute(P p);

}
