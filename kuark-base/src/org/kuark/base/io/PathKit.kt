package org.kuark.base.io

import java.net.URLDecoder
import kotlin.reflect.KClass

/**
 * 路径工具类
 * @since 1.0.0
 */
object PathKit {

    /**
     * 获取指定类的类路径，包括包名部分的路径。无论指定的类是否在zip/jar中
     * @param clazz kotlin类
     * @return 类路径, 动态生成的类将返回空串
     * @since 1.0.0
     */
    fun getClasspathIncludePackage(clazz: KClass<*>): String {
        var c = clazz.java
        while (c.isMemberClass || c.isAnonymousClass) {
            c = c.enclosingClass // Get the actual enclosing file
        }
        if (c.protectionDomain.codeSource == null) {
            // This is a proxy or other dynamically generated class, and has no physical container,
            // so just return "".
            return ""
        }
        val path = try {
            val classNmae = """${c.simpleName}.class"""
            val thisClass = c.getResource(classNmae).path
            thisClass.replace(classNmae, "")
        } catch (e: Exception) {
            c.protectionDomain.codeSource.location.path
        }
        return URLDecoder.decode(path, "UTF-8")
    }

    /**
     *  获取指定类的类路径，不包括包名部分的路径。无论指定的类是否在zip/jar中
     *  @param clazz kotlin类
     *  @return 类路径, 动态生成的类将返回空串
     *  @since 1.0.0
     */
    fun getClasspath(clazz: KClass<*>): String {
        var c = clazz.java
        while (c.isMemberClass || c.isAnonymousClass) {
            c = c.enclosingClass // Get the actual enclosing file
        }
        if (c.protectionDomain.codeSource == null) {
            // This is a proxy or other dynamically generated class, and has no physical container,
            // so just return "".
            return ""
        }
        val path = c.protectionDomain.codeSource.location.path
        return URLDecoder.decode(path, "UTF-8")
    }

}