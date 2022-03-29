package io.kuark.ability.cache.support

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
    const val SYS_DICT = "sys_dict"

    /** 字典项 */
    const val SYS_DICT_ITEM = "sys_dict_item"

    /** 参数 */
    const val SYS_PARAM = "sys_param"

    /** 资源 */
    const val SYS_RESOURCE = "sys_resource"

    /** 租户 */
    const val SYS_TENANT = "sys_tenant"

    /** 角色 */
    const val RBAC_ROLE = "rbac_role"

    /** 角色id */
    const val RBAC_ROLE_ID = "rbac_role_id"

    /** Session */
    const val SESSION = "session"

}


