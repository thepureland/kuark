package io.kuark.service.sys.provider.dao

import io.kuark.service.sys.provider.model.po.SysTenement
import io.kuark.service.sys.provider.model.table.SysTenements
import org.springframework.stereotype.Repository
import io.kuark.ability.data.rdb.support.BaseCrudDao


/**
 * 租户数据访问对象
 *
 * @author K
 * @since 1.0.0
 */
@Repository
//region your codes 1
open class SysTenementDao : BaseCrudDao<String, SysTenement, SysTenements>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}