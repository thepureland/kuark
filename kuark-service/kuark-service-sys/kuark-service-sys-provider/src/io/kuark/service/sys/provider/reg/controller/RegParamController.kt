package io.kuark.service.sys.provider.reg.controller

import io.kuark.ability.web.common.WebResult
import io.kuark.ability.web.springmvc.BaseController
import io.kuark.base.lang.string.StringKit
import io.kuark.service.sys.common.vo.reg.param.RegParamPayload
import io.kuark.service.sys.common.vo.reg.param.RegParamRecord
import io.kuark.service.sys.common.vo.reg.param.RegParamSearchPayload
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import kotlin.reflect.KClass

@RestController
@RequestMapping("/regParam")
@CrossOrigin
open class RegParamController : BaseController() {

    @Autowired
    private lateinit var regParamBiz: io.kuark.service.sys.provider.reg.ibiz.IRegParamBiz

    @Autowired
    private lateinit var regDictItemBiz: io.kuark.service.sys.provider.reg.ibiz.IRegDictItemBiz

    @PostMapping("/search")
    fun search(@RequestBody searchPayload: RegParamSearchPayload): WebResult<Pair<List<RegParamRecord>, Int>> {
        return WebResult(regParamBiz.pagingSearch(searchPayload))
    }

    @GetMapping("/get")
    fun get(id: String): WebResult<RegParamRecord> {
        return WebResult(regParamBiz.get(id, RegParamRecord::class))
    }

    @PostMapping("/saveOrUpdate")
    fun saveOrUpdate(@RequestBody @Valid payload: RegParamPayload, bindingResult: BindingResult): WebResult<String> {
        if (bindingResult.hasErrors()) error("数据校验失败！")
        val id = if (StringKit.isBlank(payload.id)) {
            regParamBiz.insert(payload)
        } else {
            regParamBiz.update(payload)
            payload.id
        }
        return WebResult(id)
    }

    @GetMapping("/updateActive")
    fun updateActive(id: String, active: Boolean): WebResult<Boolean> {
        val param = io.kuark.service.sys.provider.reg.model.po.RegParam {
            this.id = id
            this.active = active
        }
        return WebResult(regParamBiz.update(param))
    }

    @DeleteMapping("/delete")
    fun delete(id: String): WebResult<Boolean> {
        return WebResult(regParamBiz.deleteById(id))
    }

    @PostMapping("/batchDelete")
    fun batchDelete(@RequestBody ids: List<String>): WebResult<Boolean> {
        return WebResult(regParamBiz.batchDelete(ids) == ids.size)
    }

    @GetMapping("/loadModules")
    fun loadModules(): WebResult<List<String>> {
        val items = regDictItemBiz.getItemsByModuleAndType("kuark:sys", "module")
        val itemCodes = items.map { it.itemCode }
        return WebResult(itemCodes)
    }

    override fun getFormModelClass(): KClass<*> = RegParamPayload::class

}