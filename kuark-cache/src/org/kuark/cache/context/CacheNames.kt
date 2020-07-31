package org.kuark.cache.context

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
    const val DEMO = "c"

    /** 字典项 */
    const val SYS_DICT_ITEM = "sys_dict_item"

    /** 参数 */
    const val SYS_PARAM = "sys_param"

    /** Session */
    const val SESSION = "session"

}


