package io.kuark.service.sys.provider.api.frontend

import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.ability.web.springmvc.FrontEndApi
import io.kuark.service.sys.common.api.ISysParamApi
import io.kuark.service.sys.common.vo.param.SysParamDetail
import io.kuark.service.sys.common.vo.param.SysParamPayload
import io.kuark.service.sys.common.vo.param.SysParamRecord
import io.kuark.service.sys.common.vo.param.SysParamSearchPayload
import io.kuark.service.sys.provider.biz.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.biz.ibiz.ISysParamBiz
import io.kuark.service.sys.provider.model.po.SysParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sys/param")
@FrontEndApi
@CrossOrigin
open class SysParamController :
    BaseCrudController<String, ISysParamBiz, SysParamSearchPayload, SysParamRecord, SysParamDetail, SysParamPayload>() {

    @Autowired
    private lateinit var sysDictItemBiz: ISysDictItemBiz

    @Autowired
    private lateinit var paramApi: ISysParamApi

    @GetMapping("/updateActive")
    fun updateActive(id: String, active: Boolean): Boolean {
        val param = SysParam {
            this.id = id
            this.active = active
        }
        return biz.update(param)
    }

    @GetMapping("/loadModules")
    fun loadModules(): List<String> {
        val items = sysDictItemBiz.getItemsByModuleAndType("kuark:sys", "module")
        return items.map { it.itemCode }
    }

    @GetMapping("/getParam")
    fun getParam(
        @RequestParam("module") module: String,
        @RequestParam("paramName") paramName: String
    ): SysParamRecord? {
        return paramApi.getParam(module, paramName)
    }

}