package io.kuark.service.sys.provider.controller

import io.kuark.ability.web.common.WebResult
import io.kuark.ability.web.springmvc.BaseController
import io.kuark.base.bean.validation.teminal.TeminalConstraintsCreator
import io.kuark.base.lang.string.StringKit
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.model.dict.SysDictAddPayload
import io.kuark.service.sys.common.model.dict.SysDictListRecord
import io.kuark.service.sys.common.model.dict.SysDictSearchPayload
import io.kuark.service.sys.common.model.dict.SysDictTreeNode
import io.kuark.service.sys.provider.ibiz.ISysDictBiz
import io.kuark.service.sys.provider.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.model.po.SysDict
import io.kuark.service.sys.provider.model.po.SysDictItem
import io.kuark.service.sys.provider.model.table.SysDicts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid
import kotlin.reflect.KClass

@RestController
@RequestMapping("/sysDict")
@CrossOrigin
open class SysDictController : BaseController() {

    @Autowired
    private lateinit var sysDictBiz: ISysDictBiz

    @Autowired
    private lateinit var sysDictItemBiz: ISysDictItemBiz

    @PostMapping("/laodTreeNodes")
    fun laodTreeNodes(@RequestBody searchPayload: SysDictSearchPayload): WebResult<List<SysDictTreeNode>> {
        val activeOnly = searchPayload.active ?: false
        return WebResult(
            sysDictBiz.loadDirectChildrenForTree(
                searchPayload.parentId, searchPayload.firstLevel ?: false, activeOnly
            )
        )
    }

    @PostMapping("/listByTree")
    fun listByTree(@RequestBody searchPayload: SysDictSearchPayload): WebResult<Pair<List<SysDictListRecord>, Int>> {
        return WebResult(sysDictBiz.loadDirectChildrenForList(searchPayload))
    }

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
    fun get(id: String, isDict: Boolean?): WebResult<SysDictListRecord> {
        val dict = sysDictBiz.get(id, isDict)
        return if (dict == null) {
            WebResult("找不到对应的字典/字典项！")
        } else {
            WebResult(dict)
        }
    }

    @PostMapping("/add")
    fun add(@RequestBody @Valid addPayload: SysDictAddPayload, bindingResult: BindingResult): WebResult<String> {
        if (bindingResult.hasErrors()) error("数据校验失败！")
        val id = if (StringKit.isBlank(addPayload.parentId)) { // 添加SysDict
            val sysDict = SysDict().apply {
                module = addPayload.module
                dictType = addPayload.code!!
                dictName = addPayload.name
                remark = addPayload.remark
            }
            sysDictBiz.insert(sysDict)
        } else { // 添加SysDictItem
            val sysDictItem = SysDictItem().apply {
                dictId = addPayload.dictId!!
                parentId = addPayload.parentId
                itemCode = addPayload.code!!
                itemName = addPayload.name!!
                seqNo = addPayload.seqNo
                remark = addPayload.remark
            }
            sysDictItemBiz.insert(sysDictItem)
        }
        return WebResult(id)
    }

    override fun getFormModelClass(): KClass<SysDictAddPayload> = SysDictAddPayload::class

}