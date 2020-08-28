package io.kuark.base.support

/**
 * 注册器
 *
 * @author K
 * @since 1.0.0
 */
object Registry {

    /**
     * 所有注册的对象Map
     */
    private val map = mutableMapOf<String, MutableList<Any>>() //TODO 线程安全

    /**
     * 根据key查询所注册的对象
     *
     * @param key
     * @return
     */
    fun lookup(key: String): MutableList<Any> = map[key] ?: mutableListOf()

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
        if (objs.isEmpty()) {
            return
        }
        val resultList: MutableList<Any> = lookup(key)
        resultList.addAll(listOf(*objs))
        map[key] = resultList
    }
}