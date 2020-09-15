package io.kuark.web.session

/**
 * web会话
 *
 * @author K
 * @since 1.0.0
 */
class WebSession {

    val attributeMap = mutableMapOf<String, Any?>() // 访问权限不能是private，序列化时会取不到该字段

    fun getAttributeNames(): Set<String> = attributeMap.keys

    fun getAttribute(attributeName: String): Any? = attributeMap[attributeName]

    fun setAttribute(attributeName: String, value: Any?) {
        attributeMap[attributeName] = value
    }

    fun removeAttribute(attributeName: String) = attributeMap.remove(attributeName)

}