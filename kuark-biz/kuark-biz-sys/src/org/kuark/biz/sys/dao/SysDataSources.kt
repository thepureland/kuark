package org.kuark.biz.sys.dao

import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import org.kuark.biz.sys.po.SysDataSource
import org.kuark.data.jdbc.support.MaintainableTable

object SysDataSources: MaintainableTable<SysDataSource>("sys_data_source") {

    val name by varchar("name").bindTo { it.name }
    val url by varchar("url").bindTo { it.url }
    val username by varchar("username").bindTo { it.username }
    val password by varchar("password").bindTo { it.password }
    val initialSize by int("initial_size").bindTo { it.initialSize }
    val maxActive by int("max_active").bindTo { it.maxActive }
    val minIdle by int("min_idle").bindTo { it.minIdle }
    val maxWait by int("max_wait").bindTo { it.maxWait }

}