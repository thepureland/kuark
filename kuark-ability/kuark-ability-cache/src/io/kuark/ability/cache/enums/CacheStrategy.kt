package io.kuark.ability.cache.enums

/**
 * 缓存策略枚举
 *
 * @author K
 * @since 1.0.0
 */
enum class CacheStrategy {

    /** 单节点本地缓存 */
    SINGLE_LOCAL,
    /** 远程缓存 */
    REMOTE,
    /** 本地-远程两级联动缓存 */
    LOCAL_REMOTE

}