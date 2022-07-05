package io.kuark.service.sys.provider.api.backend

import io.kuark.service.sys.common.api.ISysParamApi
import io.kuark.service.sys.common.vo.dict.SysParamCacheItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam


/**
 * 参数后端控制器
 *
 * @author K
 * @since 1.0.0
 */
@RestController
//region your codes 1
@RequestMapping("/sys/param/api")
open class SysParamApiController {
//endregion your codes 1

    //region your codes 2

    @Autowired
    private lateinit var paramApi: ISysParamApi

    @GetMapping("/getParam")
    fun getParam(
        @RequestParam("module") module: String,
        @RequestParam("paramName") paramName: String
    ): SysParamCacheItem? {
        return paramApi.getParam(module, paramName)
    }

    //endregion your codes 2

}