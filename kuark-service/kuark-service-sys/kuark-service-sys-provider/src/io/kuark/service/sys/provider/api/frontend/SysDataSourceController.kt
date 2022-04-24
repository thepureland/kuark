package io.kuark.service.sys.provider.api.frontend

import io.kuark.service.sys.common.vo.datasource.*
import io.kuark.service.sys.provider.biz.ibiz.ISysDataSourceBiz
import io.kuark.ability.web.springmvc.BaseCrudController
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping


/**
 * 数据源前端控制器
 *
 * @author K
 * @since 1.0.0
 */
@RestController
//region your codes 1
@CrossOrigin
@RequestMapping("sys/dataSource")
open class SysDataSourceController :
    BaseCrudController<String, ISysDataSourceBiz, SysDataSourceSearchPayload, SysDataSourceRecord, SysDataSourceDetail, SysDataSourcePayload>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}