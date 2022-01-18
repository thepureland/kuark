package io.kuark.service.sys.provider.api

import io.kuark.base.support.Consts
import io.kuark.service.sys.common.api.ISysResourceApi
import io.kuark.service.sys.common.vo.resource.ResourceType
import io.kuark.service.sys.common.vo.resource.SysResourceRecord
import io.kuark.service.sys.common.vo.resource.SysResourceSearchPayload
import io.kuark.service.sys.provider.ibiz.ISysResourceBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
open class SysResourceApi: ISysResourceApi {

    @Autowired
    private lateinit var sysResourceBiz: ISysResourceBiz

    @SuppressWarnings(Consts.Suppress.UNCHECKED_CAST)
    override fun getResources(subSysDictCode: String, resourceType: ResourceType): List<SysResourceRecord> {
        val searchPayload = SysResourceSearchPayload().apply {
            pageNo = null
            this.subSysDictCode = subSysDictCode
            resourceTypeDictCode = resourceType.code
        }
        return sysResourceBiz.search(searchPayload) as List<SysResourceRecord>
    }

}