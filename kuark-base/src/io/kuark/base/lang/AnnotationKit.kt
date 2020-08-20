package io.kuark.base.lang

import org.apache.commons.lang3.AnnotationUtils

/**
 * 注解工具类
 *
 * @author K
 * @since 1.0.0
 */
object AnnotationKit {




    /**
     * 返回在指定类的类体系(向上)中，匹配类注解的类
     *
     * @param clazz 要查找的源
     * @param annoClass 要找的注解类
     * @return 匹配类注解的类
     * @since 1.0.0
     */
    fun getClassUpHierarchy(
        clazz: Class<*>,
        annoClass: Class<out Annotation?>?
    ): Class<*>? {
        if (clazz == Any::class.java) {
            return null
        }
        val present = clazz.isAnnotationPresent(annoClass)
        return if (present) {
            clazz
        } else {
            getClassUpHierarchy(clazz.superclass, annoClass)
        }
    }
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.lang3.AnnotationUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    /**
     *
     *
     * 检查两个注解是否相等, 使用在[Annotation.equals]API文档描述中的标准.
     *
     *
     * @param a1 第一个注解, `null` 返回 `false` 除非两个参数都为 `null`
     * @param a2 第二个注解, `null` 返回 `false` 除非两个参数都为 `null`
     * @return `true` 如果两个注解相等或都为`null`
     * @since 1.0.0
     */
    fun equals(a1: Annotation?, a2: Annotation?): Boolean {
        return AnnotationUtils.equals(a1, a2)
    }

    /**
     *
     *
     * 生成注解对象的哈希值, 使用[Annotation.hashCode] API文档中描述的算法
     *
     *
     * @param a 注解, 不能为 `null`
     * @return 注解对象的哈希值
     * @throws RuntimeException 如果在注解成员访问时产生 `Exception` 异常
     * @throws IllegalStateException 如果注解方法调用返回 `null`
     * @since 1.0.0
     */
    fun hashCode(a: Annotation?): Int {
        return AnnotationUtils.hashCode(a)
    }

    /**
     *
     *
     * 生成注解的字符串表示, 使用[Annotation.toString]
     *
     *
     * @param a 注解
     * @return 注解的字符串表示, 不会为 `null`
     * @since 1.0.0
     */
    fun toString(a: Annotation?): String {
        return AnnotationUtils.toString(a)
    }

    /**
     *
     *
     * 检查指定的类是否为某个注解的成员
     *
     *
     *
     *
     * java语言规范只允许在注解内使用某些类型。这些类型包括： [String], [Class], 基本类型, [Annotation], [Enum], 和一维数组.
     *
     *
     * @param type 要检查的类, 可以为 `null`
     * @return `true` 如果指定的类为注解的成员
     * @since 1.0.0
     */
    fun isValidAnnotationMemberType(type: Class<*>?): Boolean {
        return AnnotationUtils.isValidAnnotationMemberType(type)
    } // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.lang3.AnnotationUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
}