package io.kuark.service.user.provider.context

import io.kuark.base.lang.string.StringKit
import io.kuark.base.log.LogFactory
import io.kuark.base.support.Consts
import io.kuark.base.support.IIdEntity
import io.kuark.context.core.KuarkContext
import io.kuark.service.sys.common.context.SysContextInitializer
import io.kuark.service.user.common.user.vo.account.UserAccountCacheItem
import io.kuark.service.user.provider.user.biz.ibiz.IUserAccountBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component


@Component
open class UserContextInitializer: SysContextInitializer() {

    @Autowired
    private lateinit var userAccountBiz: IUserAccountBiz

    private val log = LogFactory.getLog(this::class)

    override fun init(context: KuarkContext) {
        super.init(context)
        val user = context.sessionAttributes?.get(KuarkContext.SESSION_KEY_USER)
        if (user != null) {
            @Suppress(Consts.Suppress.UNCHECKED_CAST)
            context.user = user as UserAccountCacheItem

            // 用户 => 子系统和租户
            if (StringKit.isBlank(context.subSysCode) || StringKit.isBlank(context.tenantId)) {
                context.subSysCode = user.subSysDictCode
                context.tenantId = user.tenantId
            }
        }
    }

}