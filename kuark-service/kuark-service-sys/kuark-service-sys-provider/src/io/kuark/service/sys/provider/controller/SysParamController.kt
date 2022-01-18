package io.kuark.service.sys.provider.controller

import io.kuark.ability.web.common.WebResult
import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.service.sys.common.api.ISysParamApi
import io.kuark.service.sys.common.vo.param.SysParamDetail
import io.kuark.service.sys.common.vo.param.SysParamPayload
import io.kuark.service.sys.common.vo.param.SysParamRecord
import io.kuark.service.sys.common.vo.param.SysParamSearchPayload
import io.kuark.service.sys.provider.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.ibiz.ISysParamBiz
import io.kuark.service.sys.provider.model.po.SysParam
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
    fun updateActive(id: String, active: Boolean): WebResult<Boolean> {
        val param = SysParam {
            this.id = id
            this.active = active
        }
        return WebResult(biz.update(param))
    }

    @GetMapping("/loadModules")
    fun loadModules(): WebResult<List<String>> {
        val items = sysDictItemBiz.getItemsByModuleAndType("kuark:sys", "module")
        val itemCodes = items.map { it.itemCode }
        return WebResult(itemCodes)
    }

    @GetMapping("/getParam")
    fun getParam(
        @RequestParam("module") module: String,
        @RequestParam("paramName") paramName: String
    ): SysParamRecord? {
        return paramApi.getParam(module, paramName)
    }

}