package io.kuark.service.workflow.provider.biz

import io.kuark.ability.data.rdb.biz.BaseBiz
import io.kuark.base.lang.string.StringKit
import io.kuark.base.log.LogFactory
import io.kuark.base.query.Criteria
import io.kuark.base.query.enums.Operator
import io.kuark.base.query.sort.Order
import io.kuark.service.workflow.common.vo.form.FlowFormSearchParams
import io.kuark.service.workflow.provider.model.po.FlowForm
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * 工作流表单业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class FlowFormBiz : BaseBiz<String, io.kuark.service.workflow.provider.model.po.FlowForm, io.kuark.service.workflow.provider.dao.FlowFormDao>(),
    io.kuark.service.workflow.provider.ibiz.IFlowFormBiz {
//endregion your codes 1

    //region your codes 2

    protected val log = LogFactory.getLog(this::class)

    override fun get(key: String, version: Int?): io.kuark.service.workflow.provider.model.po.FlowForm? {
        require(key.isNotBlank()) { "获取流程表单失败！【key】参数不能为空！" }

        val criteria = Criteria.add(io.kuark.service.workflow.provider.model.po.FlowForm::key.name, Operator.EQ, key)
        var ver: Int? = version
        if (version == null) {
            ver = dao.max(io.kuark.service.workflow.provider.model.po.FlowForm::version.name, criteria) as Int?
        }
        return if (ver == null) {
            null
        } else {
            criteria.addAnd(io.kuark.service.workflow.provider.model.po.FlowForm::version.name, Operator.EQ, ver)
            val forms = dao.search(criteria)
            if (forms.isEmpty()) null else forms.first()
        }
    }

    override fun saveOrUpdate(flowForm: io.kuark.service.workflow.provider.model.po.FlowForm): Boolean {
        return if (StringKit.isBlank(flowForm.id)) {
            try {
                flowForm.version = 1
                flowForm.createTime = LocalDateTime.now()
//                flowForm.createUser =  //TODO
                dao.insert(flowForm)
                true
            } catch (e: Throwable) {
                log.error(e, "保存流程保单失败！【key：${flowForm.key}】")
                false
            }
        } else {
            dao.update(flowForm)
        }
    }

    override fun search(
        searchParams: FlowFormSearchParams, pageNum: Int, pageSize: Int, vararg orders: Order,
    ): List<io.kuark.service.workflow.provider.model.po.FlowForm> {
        val criteria = Criteria()
        if (StringKit.isNotBlank(searchParams.key)) {
            criteria.addAnd(io.kuark.service.workflow.provider.model.po.FlowForm::key.name, Operator.ILIKE, searchParams.key)
        }
        if (StringKit.isNotBlank(searchParams.name)) {
            criteria.addAnd(io.kuark.service.workflow.provider.model.po.FlowForm::name.name, Operator.ILIKE, searchParams.name)
        }
        if (StringKit.isNotBlank(searchParams.categoryDictCode)) {
            criteria.addAnd(io.kuark.service.workflow.provider.model.po.FlowForm::categoryDictCode.name, Operator.EQ, searchParams.categoryDictCode)
        }
        if (searchParams.version == null) {
            if (searchParams.latestOnly) {
                val latestVersion = dao.max(io.kuark.service.workflow.provider.model.po.FlowForm::version.name, criteria)
                latestVersion ?: return emptyList()
            }
        } else {
            criteria.addAnd(io.kuark.service.workflow.provider.model.po.FlowForm::version.name, Operator.EQ, searchParams.version)
        }
        val pageNo = if (pageNum < 1) 1 else pageNum
        return dao.pagingSearch(criteria, pageNo, pageSize, *orders)
    }

    //endregion your codes 2

}