package io.kuark.ability.cache.support


/**
 * 缓存配置信息
 *
 * @author K
 * @since 1.0.0
 */
class CacheConfig {

    /** 名称 */
    var name: String = ""

    /** 缓存策略代码 */
    var strategyDictCode: String? = null

    /** 是否启动时写缓存 */
    var writeOnBoot: Boolean? = null

    /** 是否及时回写缓存 */
    var writeInTime: Boolean? = null

    /** 缓存生存时间(秒) */
    var ttl: Int? = null

}