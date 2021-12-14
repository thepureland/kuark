package io.kuark.ability.web.springmvc

import io.kuark.ability.web.common.WebResult
import io.kuark.base.lang.GenericKit
import io.kuark.base.support.Consts
import io.kuark.base.support.IIdEntity
import io.kuark.base.support.biz.IBaseReadOnlyBiz
import io.kuark.base.support.payload.ListSearchPayload
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.io.Serializable
import kotlin.reflect.KClass


/**
 * 基础的只读Controller
 *
 * @param PK 主键类型
 * @param B 业务处理类
 * @param S 列表查询条件载体类
 * @param R 记录类型
 * @param F 表单实体类
 * @author K
 * @since 1.0.0
 */
open class BaseReadOnlyController<PK: Any, B : IBaseReadOnlyBiz<PK, *>, S : ListSearchPayload, R : Serializable, F : IIdEntity<PK>>
    : BaseController<F>() {

    @Autowired
    protected lateinit var biz: B

    private var resultClass: KClass<R>? = null

    /**
     * 列表查询
     *
     * @param searchPayload 列表查询条件载体
     * @return WebResult(Pair(记录列表，总记录数))
     * @author K
     * @since 1.0.0
     */
    @PostMapping("/search")
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    open fun search(@RequestBody searchPayload: S): WebResult<Pair<List<R>, Int>> {
        val result = biz.pagingSearch(searchPayload) as Pair<List<R>, Int>
        return WebResult(result)
    }

    /**
     * 返回指定主键的记录
     *
     * @return WebResult(记录)
     * @author K
     * @since 1.0.0
     */
    @GetMapping("/get")
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    open fun get(id: PK): WebResult<R> {
        if (resultClass == null) {
            resultClass = GenericKit.getSuperClassGenricClass(this::class, 3) as KClass<R>
        }
        return WebResult(biz.get(id, resultClass!!))
    }

}