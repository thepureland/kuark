package io.kuark.service.provider.sys.model.table

import io.kuark.ability.data.rdb.support.MaintainableTable
import io.kuark.service.provider.sys.model.po.SysDataSource
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * 数据源数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object SysDataSources: MaintainableTable<SysDataSource>("sys_data_source") {
//endregion your codes 1

    /** 名称，或其国际化key */
    var name = varchar("name").bindTo { it.name }

    /** url */
    var url = varchar("url").bindTo { it.url }

    /** 用户名 */
    var username = varchar("username").bindTo { it.username }

    /** 密码，强烈建议加密 */
    var password = varchar("password").bindTo { it.password }

    /** 初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时 */
    var initialSize = int("initial_size").bindTo { it.initialSize }

    /** 最大连接池数量 */
    var maxActive = int("max_active").bindTo { it.maxActive }

    /** 最小连接池数量 */
    var minIdle = int("min_idle").bindTo { it.minIdle }

    /** 获取连接时最大等待时间，单位毫秒 */
    var maxWait = int("max_wait").bindTo { it.maxWait }


    //region your codes 2

	//endregion your codes 2

}