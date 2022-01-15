package io.kuark.service.sys.provider.reg.controller

import io.kuark.ability.web.common.WebResult
import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.service.sys.common.api.reg.IParamApi
import io.kuark.service.sys.common.vo.reg.param.RegParamDetail
import io.kuark.service.sys.common.vo.reg.param.RegParamPayload
import io.kuark.service.sys.common.vo.reg.param.RegParamRecord
import io.kuark.service.sys.common.vo.reg.param.RegParamSearchPayload
import io.kuark.service.sys.provider.reg.ibiz.IRegDictItemBiz
import io.kuark.service.sys.provider.reg.ibiz.IRegParamBiz
import io.kuark.service.sys.provider.reg.model.po.RegParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/reg/param")
@CrossOrigin
open class RegParamController :
    BaseCrudController<String, IRegParamBiz, RegParamSearchPayload, RegParamRecord, RegParamDetail, RegParamPayload>() {

    @Autowired
    private lateinit var regDictItemBiz: IRegDictItemBiz

    @Autowired
    private lateinit var paramApi: IParamApi

    @GetMapping("/updateActive")
    fun updateActive(id: String, active: Boolean): WebResult<Boolean> {
        val param = RegParam {
            this.id = id
            this.active = active
        }
        return WebResult(biz.update(param))
    }

    @GetMapping("/loadModules")
    fun loadModules(): WebResult<List<String>> {
        val items = regDictItemBiz.getItemsByModuleAndType("kuark:sys", "module")
        val itemCodes = items.map { it.itemCode }
        return WebResult(itemCodes)
    }

    @GetMapping("/getParam")
    fun getParam(
        @RequestParam("module") module: String,
        @RequestParam("paramName") paramName: String
    ): RegParamRecord? {
        return paramApi.getParam(module, paramName)
    }

}