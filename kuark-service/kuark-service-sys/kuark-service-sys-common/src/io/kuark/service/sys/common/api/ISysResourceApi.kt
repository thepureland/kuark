package io.kuark.service.sys.common.api

import io.kuark.service.sys.common.vo.resource.ResourceType
import io.kuark.service.sys.common.vo.resource.SysResourceRecord


/**
 * 系统资源服务对外的接口
 *
 * @author K
 * @since 1.0.0
 */
interface ISysResourceApi {

    /**
     * 根据子系统和资源类型，返回对应的资源
     *
     * @param subSysDictCode 子系统代码
     * @param resourceType 资源类型枚举
     * @return List(资源对象)
     * @author K
     * @since 1.0.0
     */
    fun getResources(subSysDictCode: String, resourceType: ResourceType): List<SysResourceRecord>

    /**
     * 根据资源id返回对应的资源
     *
     * @param resourceIds 资源id可变数组
     * @return List(资源对象)
     * @author K
     * @since 1.0.0
     */
    fun getResources(vararg resourceIds: String): List<SysResourceRecord>

}