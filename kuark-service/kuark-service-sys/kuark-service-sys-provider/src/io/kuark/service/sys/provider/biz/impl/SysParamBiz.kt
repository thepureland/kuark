package io.kuark.service.sys.provider.biz.impl

import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.ability.data.rdb.support.SqlWhereExpressionFactory
import io.kuark.base.bean.BeanKit
import io.kuark.base.log.LogFactory
import io.kuark.base.query.enums.Operator
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.vo.dict.SysParamCacheItem
import io.kuark.service.sys.common.vo.param.SysParamRecord
import io.kuark.service.sys.common.vo.param.SysParamSearchPayload
import io.kuark.service.sys.provider.biz.ibiz.ISysParamBiz
import io.kuark.service.sys.provider.cache.ParamByModuleAndNameCacheHandler
import io.kuark.service.sys.provider.dao.SysParamDao
import io.kuark.service.sys.provider.model.po.SysDataSource
import io.kuark.service.sys.provider.model.po.SysParam
import io.kuark.service.sys.provider.model.table.SysParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 参数业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class SysParamBiz : BaseCrudBiz<String, SysParam, SysParamDao>(), ISysParamBiz {
//endregion your codes 1

    //region your codes 2

    private val log = LogFactory.getLog(this::class)

    @Autowired
    private lateinit var paramCacheHandler: ParamByModuleAndNameCacheHandler

    override fun getParamFromCache(module: String, name: String): SysParamCacheItem? {
        return paramCacheHandler.getParamFromCache(module, name)
    }

    @Transactional
    override fun insert(any: Any): String {
        val id = super.insert(any)
        log.debug("新增id为${id}的参数。")
        paramCacheHandler.syncOnInsert(any, id) // 同步缓存
        return id
    }

    @Transactional
    override fun update(any: Any): Boolean {
        val success = super.update(any)
        val id = BeanKit.getProperty(any, SysDataSource::id.name) as String
        if (success) {
            log.debug("更新id为${id}的参数。")
            paramCacheHandler.syncOnUpdate(any, id)
        } else {
            log.error("更新id为${id}的参数失败！")
        }
        return success
    }

    @Transactional
    override fun updateActive(id: String, active: Boolean): Boolean {
        val param = SysDataSource {
            this.id = id
            this.active = active
        }
        val success = dao.update(param)
        if (success) {
            log.debug("更新id为${id}的参数的启用状态为${active}。")
            paramCacheHandler.syncOnUpdateActive(id, active)
        } else {
            log.error("更新id为${id}的参数的启用状态为${active}失败！")
        }
        return success
    }

    @Transactional
    override fun deleteById(id: String): Boolean {
        val param = dao.get(id)!!
        val success = super.deleteById(id)
        if (success) {
            log.debug("删除id为${id}的参数成功！")
            paramCacheHandler.syncOnDelete(param)
        } else {
            log.error("删除id为${id}的参数失败！")
        }
        return success
    }

    @Transactional
    override fun batchDelete(ids: Collection<String>): Int {
        val params = dao.inSearchById(ids)
        val count = super.batchDelete(ids)
        log.debug("批量删除参数，期望删除${ids.size}条，实际删除${count}条。")
        paramCacheHandler.synchOnBatchDelete(ids, params)
        return count
    }

    override fun pagingSearch(searchPayload: SysParamSearchPayload): Pair<List<SysParamRecord>, Int> {
        val records = dao.search(searchPayload) { column, value ->
            if (value != null && column.name in arrayOf(
                    SysParams.module.name,
                    SysParams.paramName.name,
                    SysParams.defaultValue.name
                )
            ) {
                SqlWhereExpressionFactory.create(column, Operator.ILIKE, value.toString().trim())
            } else if (column.name == SysParams.active.name && value == true) {
                SqlWhereExpressionFactory.create(column, Operator.EQ, true)
            } else null
        }
        val count = if (records.isEmpty()) 0 else dao.count(searchPayload)
        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        return Pair(records as List<SysParamRecord>, count)
    }

    //endregion your codes 2

}