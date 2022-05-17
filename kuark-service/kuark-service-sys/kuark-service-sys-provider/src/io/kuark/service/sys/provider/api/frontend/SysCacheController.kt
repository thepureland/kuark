package io.kuark.service.sys.provider.api.frontend

import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.service.sys.common.vo.cache.SysCacheDetail
import io.kuark.service.sys.common.vo.cache.SysCachePayload
import io.kuark.service.sys.common.vo.cache.SysCacheRecord
import io.kuark.service.sys.common.vo.cache.SysCacheSearchPayload
import io.kuark.service.sys.provider.biz.ibiz.ISysCacheBiz
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 * 缓存前端控制器
 *
 * @author K
 * @since 1.0.0
 */
@RestController
//region your codes 1
@RequestMapping("/sys/cache")
open class SysCacheController :
    BaseCrudController<String, ISysCacheBiz, SysCacheSearchPayload, SysCacheRecord, SysCacheDetail, SysCachePayload>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}