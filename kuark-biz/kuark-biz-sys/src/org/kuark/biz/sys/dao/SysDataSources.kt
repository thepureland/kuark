package org.kuark.biz.sys.dao

import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import org.kuark.biz.sys.po.SysDataSource
import org.kuark.data.jdbc.support.MaintainableTable

object SysDataSources: MaintainableTable<SysDataSource>("sys_data_source") {

    val name = varchar("name").bindTo { it.name }
    val url = varchar("url").bindTo { it.url }
    val username = varchar("username").bindTo { it.username }
    val password = varchar("password").bindTo { it.password }
    val initialSize = int("initial_size").bindTo { it.initialSize }
    val maxActive = int("max_active").bindTo { it.maxActive }
    val minIdle = int("min_idle").bindTo { it.minIdle }
    val maxWait = int("max_wait").bindTo { it.maxWait }

}