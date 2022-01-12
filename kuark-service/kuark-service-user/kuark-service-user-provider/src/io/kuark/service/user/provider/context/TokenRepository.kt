package io.kuark.service.user.provider.context

import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.query.Criteria
import io.kuark.base.query.enums.Operator
import io.kuark.base.time.toDate
import io.kuark.base.time.toLocalDateTime
import io.kuark.service.user.provider.user.ibiz.IUserLoginPersistentBiz
import io.kuark.service.user.provider.user.model.po.UserLoginPersistent
import io.kuark.service.user.provider.user.model.table.UserLoginPersistents
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
import java.util.*

class TokenRepository: PersistentTokenRepository {

    @Autowired
    private lateinit var authPersistentLoginsBiz: IUserLoginPersistentBiz

    override fun createNewToken(t: PersistentRememberMeToken) {
        val authPersistentLogins = UserLoginPersistent {
            id = t.series
            username = t.username
            token = t.tokenValue
            lastUsed = t.date.toLocalDateTime()
        }
        authPersistentLoginsBiz.insert(authPersistentLogins)
    }

    override fun updateToken(series: String, tokenValue: String, lastUsed: Date) {
        val updateProperties = mapOf(
            UserLoginPersistents::token.name to tokenValue,
            UserLoginPersistents::lastUsed.name to lastUsed
        )
        authPersistentLoginsBiz.updateProperties(series, updateProperties)
    }

    override fun getTokenForSeries(seriesId: String): PersistentRememberMeToken {
        val t = authPersistentLoginsBiz.get(seriesId)
        t ?: throw ObjectNotFoundException("找不到持久化令牌令牌！【seriesId：${seriesId}】")
        return PersistentRememberMeToken(t.username, t.id, t.token, t.lastUsed!!.toDate())
    }

    override fun removeUserTokens(username: String) {
        val criteria = Criteria.add(UserLoginPersistents::username.name, Operator.EQ, username)
        authPersistentLoginsBiz.batchDeleteCriteria(criteria)
    }

}