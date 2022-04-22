package io.kuark.service.sys.provider.api.frontend

import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.service.sys.common.api.ISysParamApi
import io.kuark.service.sys.common.vo.dict.SysParamCacheItem
import io.kuark.service.sys.common.vo.param.SysParamDetail
import io.kuark.service.sys.common.vo.param.SysParamPayload
import io.kuark.service.sys.common.vo.param.SysParamRecord
import io.kuark.service.sys.common.vo.param.SysParamSearchPayload
import io.kuark.service.sys.provider.biz.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.biz.ibiz.ISysParamBiz
import io.kuark.service.sys.provider.model.po.SysDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sys/param")
@CrossOrigin
open class SysParamController :
    BaseCrudController<String, ISysParamBiz, SysParamSearchPayload, SysParamRecord, SysParamDetail, SysParamPayload>() {

    @Autowired
    private lateinit var sysDictItemBiz: ISysDictItemBiz

    @Autowired
    private lateinit var paramApi: ISysParamApi

    @GetMapping("/updateActive")
    fun updateActive(id: String, active: Boolean): Boolean {
        val param = SysDataSource {
            this.id = id
            this.active = active
        }
        return biz.update(param)
    }

    @GetMapping("/getParam")
    fun getParam(
        @RequestParam("module") module: String,
        @RequestParam("paramName") paramName: String
    ): SysParamCacheItem? {
        return paramApi.getParam(module, paramName)
    }

}