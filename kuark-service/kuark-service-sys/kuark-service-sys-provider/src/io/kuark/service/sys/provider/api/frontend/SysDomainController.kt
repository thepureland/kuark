package io.kuark.service.sys.provider.api.frontend

import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.service.sys.common.vo.domain.SysDomainDetail
import io.kuark.service.sys.common.vo.domain.SysDomainPayload
import io.kuark.service.sys.common.vo.domain.SysDomainRecord
import io.kuark.service.sys.common.vo.domain.SysDomainSearchPayload
import io.kuark.service.sys.provider.biz.ibiz.ISysDomainBiz
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 * 域名前端控制器
 *
 * @author K
 * @since 1.0.0
 */
@RestController
//region your codes 1
@RequestMapping("/sys/domain")
open class SysDomainController :
    BaseCrudController<String, ISysDomainBiz, SysDomainSearchPayload, SysDomainRecord, SysDomainDetail, SysDomainPayload>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}