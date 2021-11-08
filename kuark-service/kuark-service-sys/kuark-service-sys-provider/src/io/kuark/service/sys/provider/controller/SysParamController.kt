package io.kuark.service.sys.provider.controller

import io.kuark.ability.data.rdb.support.SqlWhereExpressionFactory
import io.kuark.ability.web.common.WebResult
import io.kuark.ability.web.springmvc.BaseController
import io.kuark.base.lang.string.StringKit
import io.kuark.base.query.enums.Operator
import io.kuark.service.sys.common.model.param.SysParamPayload
import io.kuark.service.sys.common.model.param.SysParamRecord
import io.kuark.service.sys.common.model.param.SysParamSearchPayload
import io.kuark.service.sys.provider.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.ibiz.ISysParamBiz
import io.kuark.service.sys.provider.model.po.SysParam
import io.kuark.service.sys.provider.model.table.SysParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import kotlin.reflect.KClass

@RestController
@RequestMapping("/sysParam")
@CrossOrigin
open class SysParamController : BaseController() {

    @Autowired
    private lateinit var sysParamBiz: ISysParamBiz

    @Autowired
    private lateinit var sysDictItemBiz: ISysDictItemBiz

    @PostMapping("/search")
    fun search(@RequestBody searchPayload: SysParamSearchPayload): WebResult<Pair<List<SysParamRecord>, Int>> {
        return WebResult(sysParamBiz.pagingSearch(searchPayload))
    }

    @GetMapping("/get")
    fun get(id: String): WebResult<SysParamRecord> {
        return WebResult(sysParamBiz.get(id, SysParamRecord::class))
    }

    @PostMapping("/saveOrUpdate")
    fun saveOrUpdate(@RequestBody @Valid payload: SysParamPayload, bindingResult: BindingResult): WebResult<String> {
        if (bindingResult.hasErrors()) error("数据校验失败！")
        val id = if (StringKit.isBlank(payload.id)) {
            sysParamBiz.insert(payload)
        } else {
            sysParamBiz.update(payload)
            payload.id
        }
        return WebResult(id)
    }

    @GetMapping("/updateActive")
    fun updateActive(id: String, active: Boolean): WebResult<Boolean> {
        val sysParam = SysParam {
            this.id = id
            this.active = active
        }
        return WebResult(sysParamBiz.update(sysParam))
    }

    @DeleteMapping("/delete")
    fun delete(id: String): WebResult<Boolean> {
        return WebResult(sysParamBiz.deleteById(id))
    }

    @GetMapping("/loadModules")
    fun loadModules(): WebResult<List<String>> {
        val items = sysDictItemBiz.getItemsByModuleAndType("kuark:sys", "module")
        val itemCodes = items.map { it.itemCode }
        return WebResult(itemCodes)
    }

    override fun getFormModelClass(): KClass<*> = SysParamPayload::class

}