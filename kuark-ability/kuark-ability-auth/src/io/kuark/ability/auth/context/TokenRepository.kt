package io.kuark.ability.auth.context

import io.kuark.ability.auth.rbac.biz.AuthPersistentLoginsBiz
import io.kuark.ability.auth.rbac.ibiz.IAuthPersistentLoginsBiz
import io.kuark.ability.auth.rbac.ibiz.IAuthUserAccountBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
import java.util.*

class TokenRepository: PersistentTokenRepository {

    @Autowired
    private lateinit var authPersistentLoginsBiz: IAuthPersistentLoginsBiz

    override fun createNewToken(token: PersistentRememberMeToken) {
        authPersistentLoginsBiz.


        authUserAccountBiz.createNewTokenOnLogin(token.username, token.series, token.tokenValue, token.date)
        token.date
        TODO("Not yet implemented")
    }

    override fun updateToken(series: String?, tokenValue: String?, lastUsed: Date?) {
        TODO("Not yet implemented")
    }

    override fun getTokenForSeries(seriesId: String?): PersistentRememberMeToken {
        TODO("Not yet implemented")
    }

    override fun removeUserTokens(username: String?) {
        TODO("Not yet implemented")
    }

}