package io.kuark.service.sys.provider.biz.impl

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.CacheNames
import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.ability.data.rdb.support.SqlWhereExpressionFactory
import io.kuark.base.bean.BeanKit
import io.kuark.base.log.LogFactory
import io.kuark.base.query.enums.Operator
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.vo.param.SysParamDetail
import io.kuark.service.sys.common.vo.param.SysParamRecord
import io.kuark.service.sys.common.vo.param.SysParamSearchPayload
import io.kuark.service.sys.provider.biz.ibiz.ISysParamBiz
import io.kuark.service.sys.provider.dao.SysParamDao
import io.kuark.service.sys.provider.model.po.SysParam
import io.kuark.service.sys.provider.model.table.SysParams
import org.ktorm.dsl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
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
    private lateinit var self: ISysParamBiz

    @Cacheable(value = [CacheNames.SYS_PARAM], key = "#module.concat(':').concat(#name)", unless = "#result == null")
    override fun getParamFromCache(module: String, name: String): SysParamDetail? {
        if (CacheKit.isCacheActive()) {
            log.debug("缓存中不存在模块为${module}且名称为${name}的参数，从数据库中加载...")
        }
        val paramList = RdbKit.getDatabase().from(SysParams)
            .select(SysParams.columns)
            .whereWithConditions {
                it += (SysParams.paramName eq name) and (SysParams.active eq true)
                if (module.isNotEmpty()) {
                    it += SysParams.module eq module
                }
            }
            .map { row ->
                val entity = SysParams.createEntity(row)
                BeanKit.copyProperties(entity, SysParamDetail())
            }
            .toList()
        return if (paramList.isEmpty()) {
            log.debug("数据库中不存在模块为${module}且名称为${name}的参数！")
            null
        } else paramList.first()
    }

    @Transactional
    override fun insert(any: Any): String {
        val id = super.insert(any)
        log.debug("新增id为${id}的参数。")
        // 同步缓存
        if (CacheKit.isCacheActive()) {
            log.debug("新增id为${id}的参数后，同步缓存...")
            val module = BeanKit.getProperty(any, SysParam::module.name) as String
            val paramName = BeanKit.getProperty(any, SysParam::paramName.name) as String
            self.getParamFromCache(module, paramName) // 缓存
            log.debug("缓存同步完成。")
        }
        return id
    }

    @Transactional
    override fun update(any: Any): Boolean {
        val success = super.update(any)
        val id = BeanKit.getProperty(any, SysParam::id.name) as String
        if (success) {
            log.debug("更新id为${id}的参数。")
            if (CacheKit.isCacheActive()) {
                log.debug("更新id为${id}的参数后，同步缓存...")
                val module = BeanKit.getProperty(any, SysParam::module.name) as String
                val paramName = BeanKit.getProperty(any, SysParam::paramName.name) as String
                val key = "${module}:${paramName}"
                CacheKit.evict(CacheNames.SYS_PARAM, key) // 踢除参数缓存
                self.getParamFromCache(module, paramName) // 重新缓存
                log.debug("缓存同步完成。")
            }
        } else {
            log.error("更新id为${id}的参数失败！")
        }
        return success
    }

    @Transactional
    override fun updateActive(id: String, active: Boolean): Boolean {
        val param = SysParam {
            this.id = id
            this.active = active
        }
        val success = dao.update(param)
        if (success) {
            log.debug("更新id为${id}的参数的启用状态为${active}。")
            if (CacheKit.isCacheActive()) {
                log.debug("更新id为${id}的参数的启用状态后，同步缓存...")
                val sysParam = dao.get(id)!!
                if (active) {
                    self.getParamFromCache(sysParam.module!!, sysParam.paramName)
                } else {
                    val key = "${sysParam.module}:${sysParam.paramName}"
                    CacheKit.evict(CacheNames.SYS_PARAM, key) // 踢除参数缓存
                }
                log.debug("缓存同步完成。")
            }
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
            if (CacheKit.isCacheActive()) {
                log.debug("删除id为${id}的参数后，同步从缓存中踢除...")
                val key = "${param.module}:${param.paramName}"
                CacheKit.evict(CacheNames.SYS_PARAM, key) // 踢除缓存
                log.debug("缓存同步完成。")
            }
        } else {
            log.error("删除id为${id}的参数失败！")
        }
        return success
    }

    @Transactional
    override fun batchDelete(ids: Collection<String>): Int {
        val count = super.batchDelete(ids)
        log.debug("批量删除参数，期望删除${ids.size}条，实际删除${count}条。")
        if (CacheKit.isCacheActive()) {
            log.debug("批量删除id为${ids}的参数后，同步从缓存中踢除...")
            val params = dao.inSearchById(ids)
            params.forEach {
                val key = "${it.module}:${it.paramName}"
                CacheKit.evict(CacheNames.SYS_PARAM, key) // 踢除缓存
            }
            log.debug("缓存同步完成。")
        }
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