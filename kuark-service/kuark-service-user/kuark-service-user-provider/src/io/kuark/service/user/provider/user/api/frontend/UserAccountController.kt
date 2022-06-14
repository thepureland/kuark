package io.kuark.service.user.provider.user.api.frontend

import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.base.lang.string.StringKit
import io.kuark.base.security.DigestKit
import io.kuark.context.core.KuarkContextHolder
import io.kuark.service.sys.common.api.ISysParamApi
import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.sys.common.vo.resource.MenuTreeNode
import io.kuark.service.user.common.user.vo.account.UserAccountDetail
import io.kuark.service.user.common.user.vo.account.UserAccountPayload
import io.kuark.service.user.common.user.vo.account.UserAccountRecord
import io.kuark.service.user.common.user.vo.account.UserAccountSearchPayload
import io.kuark.service.user.provider.user.biz.ibiz.IUserAccountBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.ServletRequest
import javax.validation.Valid

@RestController
@RequestMapping("/user/account")
open class UserAccountController :
    BaseCrudController<String, IUserAccountBiz, UserAccountSearchPayload, UserAccountRecord, UserAccountDetail, UserAccountPayload>() {

    @Autowired
    private lateinit var paramApi: ISysParamApi

    @Autowired
    private lateinit var userAccountBiz: IUserAccountBiz


    @PostMapping("/saveOrUpdate")
    override fun saveOrUpdate(@RequestBody @Valid payload: UserAccountPayload): String {
        return if (payload.id == null || payload.id == "") {
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
            payload.id!!
        }
    }

    @GetMapping("/getAuthorisedMenus")
    fun getAuthorisedMenus(): List<MenuTreeNode> {
        val user = KuarkContextHolder.get().user!!
        return userAccountBiz.getAuthorisedMenus(user.id!!, MenuTreeNode::class)
    }

    @GetMapping("/getAuthorisedBaseMenus")
    fun getAuthorisedBaseMenus(): List<BaseMenuTreeNode> {
        val user = KuarkContextHolder.get().user!!
        return userAccountBiz.getAuthorisedMenus(user.id!!, BaseMenuTreeNode::class)
    }

}