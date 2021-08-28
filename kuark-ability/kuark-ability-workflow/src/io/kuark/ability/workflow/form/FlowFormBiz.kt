package io.kuark.ability.workflow.form

import io.kuark.ability.data.rdb.biz.BaseBiz
import io.kuark.base.lang.string.StringKit
import io.kuark.base.log.LogFactory
import io.kuark.base.query.Criteria
import io.kuark.base.query.enums.Operator
import io.kuark.base.query.sort.Order
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
open class FlowFormBiz : BaseBiz<String, FlowForm, FlowFormDao>(), IFlowFormBiz {
//endregion your codes 1

    //region your codes 2

    protected val log = LogFactory.getLog(this::class)

    override fun get(key: String, version: Int?): FlowForm? {
        require(key.isNotBlank()) { "获取流程表单失败！【key】参数不能为空！" }

        val criteria = Criteria.add(FlowForm::key.name, Operator.EQ, key)
        var ver: Int? = version
        if (version == null) {
            ver = dao.max(FlowForm::version.name, criteria) as Int?
        }
        return if (ver == null) {
            null
        } else {
            criteria.addAnd(FlowForm::version.name, Operator.EQ, ver)
            val forms = dao.search(criteria)
            if (forms.isEmpty()) null else forms.first()
        }
    }

    override fun saveOrUpdate(flowForm: FlowForm): Boolean {
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
        searchItems: FlowFormSearchItems, pageNum: Int, pageSize: Int, vararg orders: Order,
    ): List<FlowForm> {
        val criteria = Criteria()
        if (StringKit.isNotBlank(searchItems.key)) {
            criteria.addAnd(FlowForm::key.name, Operator.ILIKE, searchItems.key)
        }
        if (StringKit.isNotBlank(searchItems.name)) {
            criteria.addAnd(FlowForm::name.name, Operator.ILIKE, searchItems.name)
        }
        if (StringKit.isNotBlank(searchItems.categoryDictCode)) {
            criteria.addAnd(FlowForm::categoryDictCode.name, Operator.EQ, searchItems.categoryDictCode)
        }
        if (searchItems.version == null) {
            if (searchItems.latestOnly) {
                val latestVersion = dao.max(FlowForm::version.name, criteria)
                latestVersion ?: return emptyList()
            }
        } else {
            criteria.addAnd(FlowForm::version.name, Operator.EQ, searchItems.version)
        }
        val pageNo = if (pageNum < 1) 1 else pageNum
        return dao.pagingSearch(criteria, pageNo, pageSize, *orders)
    }

    //endregion your codes 2

}