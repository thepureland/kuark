package io.kuark.service.sys.provider.api.frontend

import io.kuark.ability.web.springmvc.BaseReadOnlyController
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.vo.dict.*
import io.kuark.service.sys.provider.biz.ibiz.ISysDictBiz
import io.kuark.service.sys.provider.model.table.SysDicts
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


/**
 * 字典前端控制器
 *
 * @author K
 * @since 1.0.0
 */
@RestController
@RequestMapping("/sys/dict")
open class SysDictController :
    BaseReadOnlyController<String, ISysDictBiz, SysDictSearchPayload, SysDictRecord, SysDictDetail, SysDictPayload>() {

    /**
     * 懒加载字典树
     *
     * @param searchPayload 查询载体
     * @return List(字典树结点)
     * @author K
     * @since 1.0.0
     */
    @PostMapping("/loadTreeNodes")
    fun loadTreeNodes(@RequestBody searchPayload: SysDictSearchPayload): List<SysDictTreeNode> {
        val activeOnly = searchPayload.active ?: false
        return biz.loadDirectChildrenForTree(searchPayload.parentId, searchPayload.firstLevel ?: false, activeOnly)
    }

    /**
     * 加载直接孩子结点(用于列表)
     *
     * @param searchPayload 查询参数
     * @return Pair(List(RegDictListModel), 总记录数)
     * @author K
     * @since 1.0.0
     */
    @PostMapping("/searchByTree")
    fun searchByTree(@RequestBody searchPayload: SysDictSearchPayload): Pair<List<SysDictRecord>, Int> {
        return biz.loadDirectChildrenForList(searchPayload)
    }

    /**
     * 加载所有字典类型
     *
     * @return List(字典类型)
     * @author K
     * @since 1.0.0
     */
    @GetMapping("/loadDictTypes")
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    fun loadDictTypes(): List<String> {
        val dictTypes = biz.allSearchProperty(SysDicts.dictType.name) as List<String>
        return dictTypes.distinct()
    }

    /**
     * 返回指定id的字典或字典项
     *
     * @param id 字典或字典项id，由isDict参数决定
     * @param isDict true: 字典id，false：字典项id
     * @param fetchAllParentIds 是否要获取所有父项id，默认为false
     * @return SysDictRecord
     * @author K
     * @since 1.0.0
     */
    @GetMapping("/getDict")
    fun get(id: String, isDict: Boolean?, fetchAllParentIds: Boolean = false): SysDictRecord {
        val dict = biz.get(id, isDict, fetchAllParentIds)
        return if (dict == null) {
            throw ObjectNotFoundException("找不到对应的字典/字典项！")
        } else {
            if (isDict == true) {
                dict.dictId = id
            }
            dict
        }
    }

    /**
     * 保存或更新字典或字典项
     *
     * @param payload 数据载体
     * @param bindingResult 表单校验结果
     * @return 主键
     * @author K
     * @since 1.0.0
     */
    @PostMapping("/saveOrUpdate")
    fun saveOrUpdate(@RequestBody @Valid payload: SysDictPayload): String {
        return biz.saveOrUpdate(payload)
    }

    /**
     * 删除字典或字典项
     *
     * @param id 主键
     * @param isDict true: 字典id，false：字典项id
     * @return 是否删除成功
     * @author K
     * @since 1.0.0
     */
    @DeleteMapping("/delete")
    fun delete(id: String, isDict: Boolean): Boolean {
        return biz.delete(id, isDict)
    }

    /**
     * 批量删除字典或字典项
     *
     * @param map Map(字典或字典项id，是否字典项)
     * @return 是否删除成功
     * @author K
     * @since 1.0.0
     */
    @PostMapping("/batchDelete")
    fun batchDelete(@RequestBody map: Map<String, Boolean>): Boolean {
        map.forEach { (id, isDict) ->
            val success = biz.delete(id, isDict)
            if (!success) {
                return false
            }
        }
        return true
    }

}