package io.kuark.service.sys.dao

import io.kuark.ability.data.jdbc.support.MaintainableTable
import io.kuark.service.sys.po.SysResource
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar

/**
 * 资源数据库实体DAO
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object SysResources: MaintainableTable<SysResource>("sys_resource") {
//endregion your codes 1

    /** 名称，或其国际化key */
    var name = varchar("name").bindTo { it.name }

    /** url */
    var url = varchar("url").bindTo { it.url }

    /** 资源类型字典代码 */
    var resourceTypeDictCode = varchar("resource_type_dict_code").bindTo { it.resourceTypeDictCode }

    /** 父id */
    var parentId = varchar("parent_id").bindTo { it.parentId }

    /** 在同父节点下的排序号 */
    var seqNo = int("seq_no").bindTo { it.seqNo }

    /** 子系统代码 */
    var subSysDictCode = varchar("sub_sys_dict_code").bindTo { it.subSysDictCode }

    /** 权限表达式 */
    var permission = varchar("permission").bindTo { it.permission }

    /** 图标url */
    var iconUrl = varchar("icon_url").bindTo { it.iconUrl }


    //region your codes 2

	//endregion your codes 2

}