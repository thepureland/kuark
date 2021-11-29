package io.kuark.service.sys.client.proxy

import io.kuark.service.sys.client.fallback.LoginFallback
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@FeignClient(value = "login-biz", fallback = LoginFallback::class)
interface ILoginBizProxy {

    @GetMapping("/thirdPartyLogin/github")
    fun githubLogin(@RequestParam code: String, @RequestParam state: String)

}