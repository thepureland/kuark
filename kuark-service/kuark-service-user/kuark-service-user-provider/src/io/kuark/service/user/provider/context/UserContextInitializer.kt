package io.kuark.service.user.provider.context

import io.kuark.base.lang.string.StringKit
import io.kuark.base.log.LogFactory
import io.kuark.context.core.KuarkContext
import io.kuark.service.sys.common.context.SysContextInitializer
import io.kuark.service.user.provider.user.biz.ibiz.IUserAccountBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
open class UserContextInitializer: SysContextInitializer() {

    @Autowired
    private lateinit var userAccountBiz: IUserAccountBiz

    private val log = LogFactory.getLog(this::class)

    override fun init(context: KuarkContext) {
        super.init(context)
        val userId = context.sessionAttributes?.get(KuarkContext.SESSION_KEY_USER_ID)
        if (userId != null) {
            context.userId = userId as String

            // 用户 => 子系统和租户
            if (StringKit.isBlank(context.subSysCode) || StringKit.isBlank(context.tenantId)) {
                val user = userAccountBiz.getByUserId(userId)
                if (user != null) {
                    context.subSysCode = user.subSysDictCode
                    context.tenantId = user.tenantId
                } else {
                    log.error("不存在用户id为${userId}的用户！")
                }
            }
        }
    }

}