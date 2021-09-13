package io.kuark.ability.auth.context

import io.kuark.ability.auth.rbac.ibiz.IAuthPersistentLoginsBiz
import io.kuark.ability.auth.rbac.model.po.AuthPersistentLogins
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.query.Criteria
import io.kuark.base.query.enums.Operator
import io.kuark.base.time.toDate
import io.kuark.base.time.toLocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
import java.util.*

class TokenRepository: PersistentTokenRepository {

    @Autowired
    private lateinit var authPersistentLoginsBiz: IAuthPersistentLoginsBiz

    override fun createNewToken(t: PersistentRememberMeToken) {
        val authPersistentLogins = AuthPersistentLogins {
            id = t.series
            username = t.username
            token = t.tokenValue
            lastUsed = t.date.toLocalDateTime()
        }
        authPersistentLoginsBiz.insert(authPersistentLogins)
    }

    override fun updateToken(series: String, tokenValue: String, lastUsed: Date) {
        val updateProperties = mapOf(
            AuthPersistentLogins::token.name to tokenValue,
            AuthPersistentLogins::lastUsed.name to lastUsed
        )
        authPersistentLoginsBiz.updateProperties(series, updateProperties)
    }

    override fun getTokenForSeries(seriesId: String): PersistentRememberMeToken {
        val t = authPersistentLoginsBiz.getById(seriesId)
        t ?: throw ObjectNotFoundException("找不到持久化令牌令牌！【seriesId：${seriesId}】")
        return PersistentRememberMeToken(t.username, t.id, t.token, t.lastUsed!!.toDate())
    }

    override fun removeUserTokens(username: String) {
        val criteria = Criteria.add(AuthPersistentLogins::username.name, Operator.EQ, username)
        authPersistentLoginsBiz.batchDeleteCriteria(criteria)
    }

}