package io.kuark.service.sys.provider.api

import io.kuark.base.bean.BeanKit
import io.kuark.service.sys.common.api.IParamApi
import io.kuark.service.sys.provider.ibiz.ISysParamBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class ParamApi: IParamApi {

    @Autowired
    private lateinit var sysParamBiz: ISysParamBiz

    override fun getParam(module: String, paramName: String): io.kuark.service.sys.common.vo.param.SysParamRecord? {
        val param = sysParamBiz.getParamByModuleAndName(module, paramName)
        return if (param == null) null else BeanKit.copyProperties(param,
            io.kuark.service.sys.common.vo.param.SysParamRecord()
        )
    }

}