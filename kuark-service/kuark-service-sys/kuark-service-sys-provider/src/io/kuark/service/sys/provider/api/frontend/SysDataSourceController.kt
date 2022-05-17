package io.kuark.service.sys.provider.api.frontend

import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.service.sys.common.vo.datasource.SysDataSourceDetail
import io.kuark.service.sys.common.vo.datasource.SysDataSourcePayload
import io.kuark.service.sys.common.vo.datasource.SysDataSourceRecord
import io.kuark.service.sys.common.vo.datasource.SysDataSourceSearchPayload
import io.kuark.service.sys.provider.biz.ibiz.ISysDataSourceBiz
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 * 数据源前端控制器
 *
 * @author K
 * @since 1.0.0
 */
@RestController
//region your codes 1
@RequestMapping("sys/dataSource")
open class SysDataSourceController :
    BaseCrudController<String, ISysDataSourceBiz, SysDataSourceSearchPayload, SysDataSourceRecord, SysDataSourceDetail, SysDataSourcePayload>() {
//endregion your codes 1

    //region your codes 2

    @GetMapping("/resetPassword")
    open fun resetPassword(id: String, password: String) {
        biz.resetPassword(id, password)
    }

    //endregion your codes 2

}