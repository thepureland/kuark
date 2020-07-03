package org.kuark.base.collections

import org.apache.commons.collections.SetUtils
import org.apache.commons.collections.Transformer

// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
// 封装org.apache.commons.collections.SetUtils
// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

/**
 * 转换给定的集合
 * 每一个新添加到集合中的元素都将被传递给转换器进行转换. 更为重要的是, 在调用该方法后,
 * 不要使用原来的集合, 因为它是一个可以添加未转换的对象的后门.
 *
 * @param transformer 使用的转换器, 不能为null
 * @return 转换后的集合
 * @since 1.0.0
 */
fun Set<*>.transformedSet(transformer: Transformer): Set<*> = SetUtils.transformedSet(this, transformer)

// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// 封装org.apache.commons.collections.SetUtils
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^