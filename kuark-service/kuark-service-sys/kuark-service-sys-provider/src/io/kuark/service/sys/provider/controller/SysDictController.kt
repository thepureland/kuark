package io.kuark.service.sys.provider.controller

import io.kuark.ability.web.common.WebResult
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.model.dict.SysDictListRecord
import io.kuark.service.sys.common.model.dict.SysDictSearchPayload
import io.kuark.service.sys.provider.ibiz.ISysDictBiz
import io.kuark.service.sys.provider.model.table.SysDicts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sysDict")
@CrossOrigin
class SysDictController {

    @Autowired
    private lateinit var sysDictBiz: ISysDictBiz

    @PostMapping("/list")
    fun list(@RequestBody searchPayload: SysDictSearchPayload): WebResult<Pair<List<SysDictListRecord>, Int>> {
        return WebResult(sysDictBiz.pagingSearch(searchPayload))
    }

    @GetMapping("/loadModules")
    @Suppress(Consts.SUPPRESS_UNCHECKED_CAST)
    fun loadModules(): WebResult<List<String>> {
        val modules = sysDictBiz.allSearchProperty(SysDicts.module.name) as List<String>
        return WebResult(modules.distinct())
    }

    @GetMapping("/loadDictTypes")
    @Suppress(Consts.SUPPRESS_UNCHECKED_CAST)
    fun loadDictTypes(): WebResult<List<String>> {
        val modules = sysDictBiz.allSearchProperty(SysDicts.dictType.name) as List<String>
        return WebResult(modules.distinct())
    }

    @GetMapping("/get")
    fun get() {

    }

}