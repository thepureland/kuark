package io.kuark.service.sys.provider.api.frontend

import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.service.sys.common.vo.param.SysParamDetail
import io.kuark.service.sys.common.vo.param.SysParamPayload
import io.kuark.service.sys.common.vo.param.SysParamRecord
import io.kuark.service.sys.common.vo.param.SysParamSearchPayload
import io.kuark.service.sys.provider.biz.ibiz.ISysParamBiz
import io.kuark.service.sys.provider.model.po.SysDataSource
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sys/param")
open class SysParamController :
    BaseCrudController<String, ISysParamBiz, SysParamSearchPayload, SysParamRecord, SysParamDetail, SysParamPayload>() {

    @GetMapping("/updateActive")
    fun updateActive(id: String, active: Boolean): Boolean {
        val param = SysDataSource {
            this.id = id
            this.active = active
        }
        return biz.update(param)
    }

}