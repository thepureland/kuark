package io.kuark.service.sys.provider.api.frontend

import io.kuark.ability.web.springmvc.BaseReadOnlyController
import io.kuark.base.query.sort.Order
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.api.ISysDictApi
import io.kuark.service.sys.common.vo.dict.*
import io.kuark.service.sys.provider.biz.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.model.table.SysDictItems
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * 字典项前端控制器
 *
 * @author K
 * @since 1.0.0
 */
@RestController
@RequestMapping("/sys/dictItem")
@CrossOrigin
class SysDictItemController :
    BaseReadOnlyController<String, ISysDictItemBiz, SysDictSearchPayload, SysDictRecord, SysDictItemDetail, SysDictPayload>() {

    @Autowired
    private lateinit var sysDictApi: ISysDictApi

    /**
     * 加载所有字典项编码
     *
     * @return List(字典项编码)
     * @author K
     * @since 1.0.0
     */
    @GetMapping("/loadDictItemCodes")
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    fun loadDictItemCodes(): List<String> { //TODO 改为懒加载
        val itemCodes = biz.allSearchProperty(SysDictItems.itemCode.name) as List<String>
        return itemCodes.distinct()
    }

    /**
     * 更新启用状态
     *
     * @param id 主键
     * @param active 是否启用（目标状态）
     * @return 是否更新成功
     * @author K
     * @since 1.0.0
     */
    @GetMapping("/updateActive")
    fun updateActive(id: String, active: Boolean): Boolean {
        return biz.updateActive(id, active)
    }

    /**
     * 返回字典id对应的所有字典项（包括未启用的）
     *
     * @param dictId 字典id
     * @return List(字典项详情对象)
     * @author K
     * @since 1.0.0
     */
    @GetMapping("/getDictItemsByDictId")
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    fun getDictItemsByDictId(@RequestParam("dictId") dictId: String): List<SysDictItemDetail> {
        val payload = SysDictSearchPayload().apply {
            this.dictId = dictId
            pageNo = null // 不分页
            returnEntityClass = SysDictItemDetail::class
            orders = listOf(Order.asc(SysDictItemDetail::seqNo.name))
        }
        return biz.search(payload) as List<SysDictItemDetail>
    }

    /**
     * 根据模块和字典类型，取得对应字典项(仅包括处于启用状态的)
     *
     * @param module 模块
     * @param dictType 字典类型
     * @return 字典项列表（自然排序）。如果模块未指定，且存在多个同名type，将任意返回一个type对应的字典项。查无结果返回空列表。
     * @throws IllegalArgumentException 参数校验不通过时
     * @author K
     * @since 1.0.0
     */
    @GetMapping("/getDictItems")
    fun getDictItems(
        @RequestParam("module") module: String,
        @RequestParam("dictType") dictType: String
    ): List<SysDictItemCacheItem> {
        return sysDictApi.getDictItems(DictModuleAndTypePayload(module, dictType))
    }

    /**
     * 根据模块和字典类型，取得对应字典项的编码和名称(仅包括处于启用状态的)
     *
     * @param module 模块
     * @param dictType 字典类型
     * @return LinkedHashMap(编码，名称)，自然排序。如果module为空串，且存在多个同名type，将任意返回一个type对应的字典项。查无结果返回空Map。
     * @throws IllegalArgumentException 参数校验不通过时
     * @author K
     * @since 1.0.0
     */
    @GetMapping("/getDictItemMap")
    fun getDictItemMap(
        @RequestParam("module") module: String,
        @RequestParam("dictType") dictType: String
    ): LinkedHashMap<String, String> {
        return sysDictApi.getDictItemMap(DictModuleAndTypePayload(module, dictType))
    }

    /**
     * 根据模块和字典类型的载体列表，取得对应字典项信息(仅包括处于启用状态的)
     *
     * @param payloads 模块和字典类型的载体列表（如果模块未指定，此时请保证type的惟一性，否则结果将不确定是哪条记录）
     * @return Map(Pair(模块，字典类型)，List(字典项信息对象))
     * @throws IllegalArgumentException 参数校验不通过时
     * @author K
     * @since 1.0.0
     */
    @PostMapping("/batchGetDictItems")
    fun batchGetDictItems(@RequestBody payloads: List<DictModuleAndTypePayload>): Map<Pair<String, String>, List<SysDictItemCacheItem>> {
        return sysDictApi.batchGetDictItems(payloads)
    }

    /**
     * 根据模块和字典类型的载体列表，取得对应字典项的编码和名称(仅包括处于启用状态的)
     *
     * @param payloads 模块和字典类型的载体列表（如果模块未指定，此时请保证type的惟一性，否则结果将不确定是哪条记录）
     * @return Map(Pair(模块，字典类型)，LinkedHashMap(编码，名称))
     * @throws IllegalArgumentException 参数校验不通过时
     * @author K
     * @since 1.0.0
     */
    @PostMapping("/batchGetDictItemMap")
    fun batchGetDictItemMap(@RequestBody payloads: List<DictModuleAndTypePayload>): Map<Pair<String, String>, LinkedHashMap<String, String>> {
        return sysDictApi.batchGetDictItemMap(payloads)
    }

}