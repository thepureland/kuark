package io.kuark.service.sys.provider.model.table

import io.kuark.ability.data.rdb.support.StringIdTable
import io.kuark.service.sys.provider.model.po.SysCache
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import org.ktorm.schema.varchar


/**
 * 缓存数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object SysCaches : StringIdTable<SysCache>("sys_cache") {
//endregion your codes 1

    /** 名称 */
    var name = varchar("name").bindTo { it.name }

    /** 子系统代码 */
    var subSysDictCode = varchar("sub_sys_dict_code").bindTo { it.subSysDictCode }

    /** 缓存策略代码 */
    var strategyDictCode = varchar("strategy_dict_code").bindTo { it.strategyDictCode }

    /** 是否启动时写缓存 */
    var writeOnBoot = boolean("write_on_boot").bindTo { it.writeOnBoot }

    /** 是否及时回写缓存 */
    var writeInTime = boolean("write_in_time").bindTo { it.writeInTime }

    /** 缓存生存时间(秒) */
    var ttl = int("ttl").bindTo { it.ttl }

    /** 备注，或其国际化key */
    var remark = varchar("remark").bindTo { it.remark }

    /** 是否启用 */
    var active = boolean("active").bindTo { it.active }

    /** 是否内置 */
    var builtIn = boolean("built_in").bindTo { it.builtIn }

    /** 创建用户 */
    var createUser = varchar("create_user").bindTo { it.createUser }

    /** 创建时间 */
    var createTime = datetime("create_time").bindTo { it.createTime }

    /** 更新用户 */
    var updateUser = varchar("update_user").bindTo { it.updateUser }

    /** 更新时间 */
    var updateTime = datetime("update_time").bindTo { it.updateTime }


    //region your codes 2

    //endregion your codes 2

}