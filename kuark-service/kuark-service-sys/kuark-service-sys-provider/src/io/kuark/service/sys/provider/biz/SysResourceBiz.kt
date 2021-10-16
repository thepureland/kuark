package io.kuark.service.sys.provider.biz

import io.kuark.ability.data.rdb.biz.BaseBiz
import io.kuark.base.lang.string.StringKit
import io.kuark.base.query.Criteria
import io.kuark.base.query.enums.Operator
import io.kuark.base.query.sort.Direction
import io.kuark.base.tree.TreeKit
import io.kuark.context.core.KuarkContextHolder
import io.kuark.service.sys.common.model.SysMenuTreeNode
import io.kuark.service.sys.provider.dao.SysResourceDao
import io.kuark.service.sys.provider.ibiz.ISysResourceBiz
import io.kuark.service.sys.provider.model.po.SysResource
import org.springframework.stereotype.Service

/**
 * 资源业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class SysResourceBiz : BaseBiz<String, SysResource, SysResourceDao>(), ISysResourceBiz {
//endregion your codes 1

    //region your codes 2

    override fun getMenus(): List<SysMenuTreeNode> {
        //TODO 加入权限
        val criteria = Criteria.add(SysResource::active.name, Operator.EQ, true)
        val subSysCode = KuarkContextHolder.get().subSysCode
        if (StringKit.isNotBlank(subSysCode)) {
            criteria.addAnd(SysResource::subSysDictCode.name, Operator.EQ, subSysCode)
        }
        val origMenus = dao.search(criteria)
        val sysMenus = origMenus.map { SysMenuTreeNode(it.name, it.url, it.icon, it.id!!, it.parentId, it.seqNo) }
        return TreeKit.convertListToTree(sysMenus, Direction.ASC)
    }

    //endregion your codes 2

}