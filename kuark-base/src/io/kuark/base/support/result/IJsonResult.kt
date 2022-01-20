package io.kuark.base.support.result

import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serializable

/**
 * 要以json返回的结果对象接口，会自动去除值为null的属性
 *
 * @author K
 * @since 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
interface IJsonResult: Serializable