package io.kuark.service.sys.provider.reg.api

import io.kuark.base.bean.BeanKit
import io.kuark.service.sys.common.api.reg.IParamApi
import io.kuark.service.sys.common.vo.reg.param.RegParamRecord
import io.kuark.service.sys.provider.reg.ibiz.IRegParamBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class ParamApi: IParamApi {

    @Autowired
    private lateinit var regParamBiz: IRegParamBiz

    override fun getParam(module: String, paramName: String): RegParamRecord? {
        val param = regParamBiz.getParamByModuleAndName(module, paramName)
        return if (param == null) null else BeanKit.copyProperties(param, RegParamRecord())
    }

}