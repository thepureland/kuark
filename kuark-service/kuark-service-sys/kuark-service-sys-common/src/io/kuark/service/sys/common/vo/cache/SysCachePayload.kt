package io.kuark.service.sys.common.vo.cache

import io.kuark.base.bean.validation.constraint.annotaions.DictCode
import io.kuark.base.support.payload.FormPayload
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive


/**
 * 缓存表单载体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
open class SysCachePayload : FormPayload<String>() {
//endregion your codes 1

    //region your codes 2

    /** 名称 */
    @get:NotBlank(message = "名称不能为空！")
    var name: String? = null

    /** 子系统代码 */
    @get:NotBlank(message = "子系统不能为空！")
    @get:DictCode("sub_sys", "kuark:sys", "子系统不在取值范围内！")
    var subSysDictCode: String? = null

    /** 缓存策略代码 */
    @get:NotBlank(message = "缓存策略不能为空！")
    @get:DictCode("cache_strategy", "kuark:sys", "缓存策略不在取值范围内！")
    var strategyDictCode: String? = null

    /** 是否启动时写缓存 */
    var writeOnBoot: Boolean? = null

    /** 是否及时回写缓存 */
    var writeInTime: Boolean? = null

    /** 缓存管理Bean的名称 */
    @get:NotBlank(message = "缓存管理Bean的名称不能为空！")
    var managementBeanName: String? = null

    /** 缓存生存时间(秒) */
    @get:Positive(message = "TTL必须为正整数！")
    var ttl: Int? = null

    /** 备注，或其国际化key */
    var remark: String? = null

    //endregion your codes 2

}