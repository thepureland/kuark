package io.kuark.service.sys.provider.api.frontend

import io.kuark.service.sys.common.vo.cache.*
import io.kuark.service.sys.provider.biz.ibiz.ISysCacheBiz
import io.kuark.ability.web.springmvc.BaseCrudController
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping


/**
 * 缓存前端控制器
 *
 * @author K
 * @since 1.0.0
 */
@RestController
//region your codes 1
@RequestMapping("SysCache")
open class SysCacheController :
    BaseCrudController<String, ISysCacheBiz, SysCacheSearchPayload, SysCacheRecord, SysCacheDetail, SysCachePayload>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}