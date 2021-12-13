package io.kuark.service.auth.provider.rbac.controller

import io.kuark.ability.web.common.WebResult
import io.kuark.ability.web.springmvc.BaseController
import io.kuark.base.lang.string.StringKit
import io.kuark.base.support.Consts
import io.kuark.service.auth.common.rbac.vo.role.AuthRolePayload
import io.kuark.service.auth.common.rbac.vo.role.AuthRoleRecord
import io.kuark.service.auth.common.rbac.vo.role.AuthRoleSearchPayload
import io.kuark.service.auth.provider.rbac.ibiz.IAuthRoleBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import javax.validation.Valid
import kotlin.reflect.KClass

class AuthRoleController: BaseController() {

    @Autowired
    private lateinit var authRoleBiz: IAuthRoleBiz

    @PostMapping("/search")
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    fun search(@RequestBody searchPayload: AuthRoleSearchPayload): WebResult<Pair<List<AuthRoleRecord>, Int>> {
        return WebResult(authRoleBiz.pagingSearch(searchPayload) as Pair<List<AuthRoleRecord>, Int>)
    }

    @GetMapping("/get")
    fun get(id: String): WebResult<AuthRoleRecord> {
        return WebResult(authRoleBiz.get(id, AuthRoleRecord::class))
    }

    @PostMapping("/saveOrUpdate")
    fun saveOrUpdate(@RequestBody @Valid payload: RegParamPayload, bindingResult: BindingResult): WebResult<String> {
        if (bindingResult.hasErrors()) error("数据校验失败！")
        val id = if (StringKit.isBlank(payload.id)) {
            authRoleBiz.insert(payload)
        } else {
            authRoleBiz.update(payload)
            payload.id
        }
        return WebResult(id)
    }

    @GetMapping("/updateActive")
    fun updateActive(id: String, active: Boolean): WebResult<Boolean> {
        val param = RegParam {
            this.id = id
            this.active = active
        }
        return WebResult(authRoleBiz.update(param))
    }

    @DeleteMapping("/delete")
    fun delete(id: String): WebResult<Boolean> {
        return WebResult(authRoleBiz.deleteById(id))
    }

    @PostMapping("/batchDelete")
    fun batchDelete(@RequestBody ids: List<String>): WebResult<Boolean> {
        return WebResult(authRoleBiz.batchDelete(ids) == ids.size)
    }

    override fun getFormModelClass(): KClass<*> = AuthRolePayload::class

}