package io.kuark.service.sys.provider.api.frontend

import io.kuark.service.sys.common.vo.tenement.*
import io.kuark.service.sys.provider.biz.ibiz.ISysTenementBiz
import io.kuark.ability.web.springmvc.BaseCrudController
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping


/**
 * 租户前端控制器
 *
 * @author K
 * @since 1.0.0
 */
@RestController
//region your codes 1
@RequestMapping("SysTenement")
open class SysTenementController :
    BaseCrudController<String, ISysTenementBiz, SysTenementSearchPayload, SysTenementRecord, SysTenementDetail, SysTenementPayload>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}