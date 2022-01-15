package io.kuark.service.user.provider.user.controller

import io.kuark.ability.web.common.WebResult
import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.base.lang.string.StringKit
import io.kuark.base.security.DigestKit
import io.kuark.service.sys.common.api.reg.IParamApi
import io.kuark.service.user.common.user.vo.account.UserAccountDetail
import io.kuark.service.user.common.user.vo.account.UserAccountPayload
import io.kuark.service.user.common.user.vo.account.UserAccountRecord
import io.kuark.service.user.common.user.vo.account.UserAccountSearchPayload
import io.kuark.service.user.provider.user.ibiz.IUserAccountBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/user/account")
@CrossOrigin
open class UserAccountController :
    BaseCrudController<String, IUserAccountBiz, UserAccountSearchPayload, UserAccountRecord, UserAccountDetail, UserAccountPayload>() {

    @Autowired
    private lateinit var paramApi: IParamApi


    @PostMapping("/saveOrUpdate")
    override fun saveOrUpdate(
        @RequestBody @Valid payload: UserAccountPayload, bindingResult: BindingResult
    ): WebResult<String> {
        if (bindingResult.hasErrors()) error("数据校验失败！")
        val id = if (payload.id == null || payload.id == "") {
            // 设置默认密码
            val param = paramApi.getParam("kuark:sys", "account_default_password")
            var password = param?.paramValue
            if (StringKit.isBlank(password)) {
                password = "123456"
            }
            password = DigestKit.getMD5(password!!, payload.username!!)
            payload.password = password
            biz.insert(payload)
        } else {
            biz.update(payload)
            payload.id
        }
        return WebResult(id)
    }

}