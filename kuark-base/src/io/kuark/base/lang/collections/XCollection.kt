package io.kuark.base.lang.collections

import io.kuark.base.support.Consts
import org.apache.commons.collections.CollectionUtils

/**
 * kotlin.Collection扩展函数
 *
 * @author K
 * @since 1.0.0
 */


/**
 * 转换Collection所有元素(通过toString())为String, 每个元素的前面加入prefix，后面加入postfix，如<div>mymessage</div>。
 *
 * @param prefix 要添加的前缀，默认为空串
 * @param postfix 要添加的后缀，默认为空串
 * @param seperator 每个元素间的分隔符，默认为空串
 * @return 加上前缀和后缀的每个元素的toString值的连接串
 * @author K
 * @since 1.0.0
 */
fun Collection<*>.joinEachToString(prefix: String = "", postfix: String = "", seperator: String = ""): String {
    if (this.isEmpty()) return ""
    val builder = StringBuilder()
    this.forEachIndexed { index, elem ->
        builder.append(prefix).append(elem).append(postfix)
        if (index != this.size - 1) {
            builder.append(seperator)
        }
    }
    return builder.toString()
}

// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
// 封装org.apache.commons.collections.CollectionUtils
// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

/**
 * 返回容器中每一个相同的元素出现的次数
 *
 * @return Map(容器中的元素, 出现的次数)
 * @author K
 * @since 1.0.0
 */
@Suppress(Consts.Suppress.UNCHECKED_CAST)
fun <T> Collection<T>.getCardinalityMap(): Map<T, Int> = CollectionUtils.getCardinalityMap(this) as Map<T, Int>

/**
 * 检测两个容器的大小及其包含的元素是否全部相等
 *
 * @param b 第二个容器, 不能为null
 * @return `true` 如果两个容器的大小及其包含的元素全部相等
 * @author K
 * @since 1.0.0
 */
fun Collection<*>.isEqualCollection(b: Collection<*>?): Boolean = CollectionUtils.isEqualCollection(this, b)

// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// 封装org.apache.commons.collections.CollectionUtils
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^