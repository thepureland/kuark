package org.kuark.base.support

import java.util.*

/**
 * 注册器
 *
 * @author K
 * @since 1.0.0
 */
object Registry {

    /**
     * 国际化信息类型key, value类型为List<String>
    </String> */
    const val I18N_TYPE_KEY = "I18N_TYPE"

    /**
     * 国际化支持的语言
     */
    const val I18N_KEY_LOCALES = "I18N_LOCALES"

    /**
     * 所有注册的对象Map
     */
    private val map: MutableMap<String, MutableList<Any>> =
        HashMap()

    /**
     * 根据key查询所注册的对象
     *
     * @param key
     * @return
     */
    fun lookup(key: String): MutableList<Any> {
        val resultList = map[key]
        return resultList ?: mutableListOf()
    }

    /**
     * 注册
     *
     * @param key
     * @param obj
     */
    fun register(key: String, obj: Any) {
        val resultList: MutableList<Any> = lookup(key)
        if (!resultList.contains(obj)) {
            resultList.add(obj)
        }
        map[key] = resultList
    }

    /**
     * 批量注册
     *
     * @param key
     * @param objs
     */
    fun register(key: String, vararg objs: Any) {
        if (objs == null || objs.isEmpty()) {
            return
        }
        val resultList: MutableList<Any> = lookup(key)
        resultList.addAll(listOf(*objs))
        map[key] = resultList
    }
}