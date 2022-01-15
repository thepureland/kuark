package io.kuark.ability.web.springmvc

import io.kuark.ability.web.common.WebResult
import io.kuark.base.support.IIdEntity
import io.kuark.base.support.biz.IBaseCrudBiz
import io.kuark.base.support.payload.ListSearchPayload
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.io.Serializable
import javax.validation.Valid

/**
 * 基础的增删改查Controller
 *
 * @param PK 主键类型
 * @param B 业务处理类
 * @param S 列表查询载体类
 * @param R 记录类型
 * @param F 表单实体类
 * @author K
 * @since 1.0.0
 */
open class BaseCrudController<PK : Any, B : IBaseCrudBiz<PK, *>, S : ListSearchPayload, R : Serializable, D : Serializable, F : IIdEntity<PK>> :
    BaseReadOnlyController<PK, B, S, R, D, F>() {

    /**
     * 保存或更新记录
     *
     * @param payload 表单实体
     * @param bindingResult spring mvc 绑定结果对象 (表单校验用)
     * @return WebResult(主键)
     * @author K
     * @since 1.0.0
     */
    @PostMapping("/saveOrUpdate")
    open fun saveOrUpdate(@RequestBody @Valid payload: F, bindingResult: BindingResult): WebResult<PK> {
        if (bindingResult.hasErrors()) error("数据校验失败！")
        val id = if (payload.id == null || payload.id == "") {
            biz.insert(payload)
        } else {
            biz.update(payload)
            payload.id
        }
        return WebResult(id)
    }

    /**
     * 删除指定主键的记录
     *
     * @param id 主键
     * @return WebResult(是否删除成功)
     * @author K
     * @since 1.0.0
     */
    @DeleteMapping("/delete")
    open fun delete(id: PK): WebResult<Boolean> {
        return WebResult(biz.deleteById(id))
    }

    /**
     * 批量删除指定主键的记录
     *
     * @param ids 主键列表
     * @return WebResult(是否删除成功)
     * @author K
     * @since 1.0.0
     */
    @PostMapping("/batchDelete")
    open fun batchDelete(@RequestBody ids: List<PK>): WebResult<Boolean> {
        return WebResult(biz.batchDelete(ids) == ids.size)
    }

}