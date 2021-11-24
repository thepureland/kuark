package io.kuark.ability.cache.context

import org.springframework.stereotype.Component

/**
 * 缓存名称定义
 *
 * @author K
 * @since 1.0.0
 */
@Component
object CacheNames: ICacheNames {

    /** 测试用 */
    const val TEST = "test"

    /** 字典项 */
    const val REG_DICT_ITEM = "reg_dict_item"

    /** 参数 */
    const val REG_PARAM = "reg_param"

    /** Session */
    const val SESSION = "session"

}


