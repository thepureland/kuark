package io.kuark.service.sys.provider.api.frontend

import io.kuark.service.sys.common.vo.domain.*
import io.kuark.service.sys.provider.biz.ibiz.ISysDomainBiz
import io.kuark.ability.web.springmvc.BaseCrudController
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping


/**
 * 域名前端控制器
 *
 * @author K
 * @since 1.0.0
 */
@RestController
//region your codes 1
@CrossOrigin
@RequestMapping("/sys/domain")
open class SysDomainController :
    BaseCrudController<String, ISysDomainBiz, SysDomainSearchPayload, SysDomainRecord, SysDomainDetail, SysDomainPayload>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}