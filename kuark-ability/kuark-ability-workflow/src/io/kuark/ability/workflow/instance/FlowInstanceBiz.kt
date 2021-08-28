package io.kuark.ability.workflow.instance

import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.lang.string.StringKit
import io.kuark.base.log.LogFactory
import io.kuark.base.query.sort.Order
import org.activiti.bpmn.model.BpmnModel
import org.activiti.bpmn.model.FlowNode
import org.activiti.engine.*
import org.activiti.engine.history.HistoricActivityInstance
import org.activiti.image.impl.DefaultProcessDiagramGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.io.InputStream
import java.sql.Timestamp
import kotlin.collections.ArrayList

/**
 * 流程实例相关业务
 *
 * @author K
 * @since 1.0.0
 */
open class FlowInstanceBiz : IFlowInstanceBiz {

    @Autowired
    private lateinit var runtimeService: RuntimeService

    @Autowired
    private lateinit var repositoryService: RepositoryService

    @Autowired
    private lateinit var historyService: HistoryService

    private val log = LogFactory.getLog(this::class)

    override fun isExists(bizKey: String, key: String?, version: Int?): Boolean {
        val query = runtimeService.createProcessInstanceQuery()
            .processInstanceBusinessKey(bizKey)
        if (StringKit.isNotBlank(key)) {
            query.processDefinitionKey(key)
            if (version == null) {
                val definition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(key).latestVersion().singleResult()
                definition ?: return false
                query.processDefinitionVersion(definition.version)
            } else {
                query.processDefinitionVersion(version)
            }
        }
        return query.count() > 0
    }

    override fun get(bizKey: String, key: String?, version: Int?): FlowInstance? {
        val query = runtimeService.createProcessInstanceQuery()
            .processInstanceBusinessKey(bizKey)
        if (StringKit.isNotBlank(key)) {
            query.processDefinitionKey(key)
            if (version == null) {
                val definition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(key).latestVersion().singleResult()
                definition ?: return null
                query.processDefinitionVersion(definition.version)
            } else {
                query.processDefinitionVersion(version)
            }
        }
        query.orderBy { "start_time_" }.desc()
        val instances = query.list()
        return if (instances.isEmpty()) null else FlowInstance(instances.first())
    }

    override fun search(
        searchItems: FlowInstanceSearchItems, pageNum: Int, pageSize: Int, vararg orders: Order
    ): List<FlowInstance> {
        val whereStr = StringBuilder("e.proc_inst_id_ = e.id_") // 只查询流程实例

        // 流程定义key(bpmn文件中process元素的id)
        val key = searchItems.definitionKey
        if (StringKit.isNotBlank(key) && !key!!.contains("'")) {
            whereStr.append(" AND UPPER(d.key_) LIKE '%${key.uppercase()}%'")
        }

        // 业务主键
        val bizKey = searchItems.bizKey
        if (StringKit.isNotBlank(bizKey) && !bizKey!!.contains("'")) {
            whereStr.append(" AND UPPER(e.business_key_) LIKE '%${bizKey.uppercase()}%'")
        }

        // 实例名称
        val instanceName = searchItems.name
        if (StringKit.isNotBlank(instanceName) && !instanceName!!.contains("'")) {
            whereStr.append(" AND UPPER(e.name_) LIKE '%${instanceName.uppercase()}%'")
        }

        // 发起时间始
        val startTimeFrom = searchItems.startTimeFrom
        if (startTimeFrom != null) {
            whereStr.append(" AND e.start_time_ >= '${Timestamp(startTimeFrom.time)}'")
        }

        // 发起时间止
        val startTimeTo = searchItems.startTimeTo
        if (startTimeTo != null) {
            whereStr.append(" AND e.start_time_ <= '${Timestamp(startTimeTo.time)}'")
        }

        // 发起者id
        val startUserId = searchItems.startUserId
        if (StringKit.isNotBlank(startUserId) && !startUserId!!.contains("'")) {
            whereStr.append(" AND e.start_user_id_ = '${startUserId}'")
        }

        // 流程定义名称
        val definitionName = searchItems.definitionName
        if (StringKit.isNotBlank(definitionName) && !definitionName!!.contains("'")) {
            whereStr.append(" AND UPPER(d.name_) LIKE '%${definitionName.uppercase()}%'")
        }

        // 流程定义版本
        val definitionVersion = searchItems.definitionVersion
        if (definitionVersion != null) {
            whereStr.append(" AND d.version_ = $definitionVersion")
        }

        // 是否挂起
        val suspend = searchItems.isSuspend
        if (suspend != null) {
            if (suspend) {
                whereStr.append(" AND e.suspension_state_ = 2")
            } else {
                whereStr.append(" AND e.suspension_state_ != 2")
            }
        }


        // 排序
        val orderStr = RdbKit.getOrderSql(*orders)

        // 查询
        val sql =
            "SELECT e.*, d.key_ AS processDefinitionKey, d.name_ AS processDefinitionName, d.version_ AS processDefinitionVersion " +
                    "FROM act_ru_execution e LEFT JOIN act_re_procdef d " +
                    "ON e.proc_def_id_ = d.id_ " +
                    "WHERE $whereStr $orderStr"
        val query = runtimeService.createNativeProcessInstanceQuery().sql(sql)
        val instances = if (pageSize < 1) {
            query.list()
        } else {
            query.listPage((pageNum - 1) * pageSize, pageSize)
        }

        // 结果处理
        return instances.map { FlowInstance(it) }
    }

    @Transactional
    override fun activate(bizKey: String, key: String?) {
        val instanceId = findInstanceId(bizKey, key)
        try {
            runtimeService.activateProcessInstanceById(instanceId)
            log.info("激活流程实例成功！【key：$key，bizKey：$bizKey】")
        } catch (e: ActivitiException) {
            log.warn("忽略流程实例激活操作，因其已处于激活状态！【key：$key，bizKey：$bizKey】")
        }
    }

    @Transactional
    override fun suspend(bizKey: String, key: String?) {
        val instanceId = findInstanceId(bizKey, key)
        try {
            runtimeService.suspendProcessInstanceById(instanceId)
            log.info("挂起流程实例成功！【key：$key，bizKey：$bizKey】")
        } catch (e: ActivitiException) {
            log.warn("忽略流程实例挂起操作，因其已处于挂起状态！【key：$key，bizKey：$bizKey】")
        }
    }

    @Transactional
    override fun delete(bizKey: String, key: String?, reason: String?) {
        val instanceId = findInstanceId(bizKey, key)
        runtimeService.deleteProcessInstance(instanceId, reason)
        log.info("删除流程实例成功！【bizKey: $bizKey，definitionKey：$key】")
    }

    @Transactional
    override fun updateBizKey(oldBizKey: String, newBizKey: String, key: String?) {
        require(newBizKey.isNotBlank()) { "更新流程实例业务主键失败！指定的新业务主键不能为空！【key：$key，oldBizKey: $oldBizKey】" }
        require(!newBizKey.contains("'")) { "更新流程实例业务主键失败！指定的新业务主键不能包含单引号！【key：$key，oldBizKey: $oldBizKey，newBizKey：$newBizKey】" }

        val instanceId = findInstanceId(oldBizKey, key)
        try {
            runtimeService.updateBusinessKey(instanceId, newBizKey)
            log.info("更新流程实例的业务主键成功！【key：$key，oldBizKey: $oldBizKey，newBizKey：$newBizKey】")
        } catch (e: Throwable) {
            val errMsg = "更新流程实例的业务主键失败！【key：$key，oldBizKey: $oldBizKey，newBizKey：$newBizKey】"
            log.error(e, errMsg)
            when (e) {
                is ActivitiIllegalArgumentException, is ActivitiObjectNotFoundException -> {
                    throw ObjectNotFoundException(errMsg, e)
                }
            }
        }
    }

    override fun genHighLightFlowDiagram(bizKey: String, key: String?): InputStream {
        require(StringKit.isNotBlank(bizKey)) { "生成高亮流程图时失败！【bizKey】参数不能为空！" }

        // 根据业务id，查询流程实例
        val query = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(bizKey)
        if (StringKit.isNotBlank(key)) {
            query.processDefinitionKey(key)
        }
        val processInstance = query.singleResult()

        // 根据流程对象获取流程对象模型
        val bpmnModel = repositoryService.getBpmnModel(processInstance.processDefinitionId)

        // 获取已触发（包括未完成）的流程节点集合
        val historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
            .processInstanceId(processInstance.id)
            .orderByHistoricActivityInstanceStartTime()
            .asc()
            .list()
        if (historicActivityInstances.isEmpty()) {
            return DefaultProcessDiagramGenerator().generateDiagram(
                bpmnModel, null, null, null, null, "宋体", "微软雅黑", "黑体", true, "png"
            )
        }

        // 获取流程走过的线
        val flowIds = getHighLightedFlows(bpmnModel, historicActivityInstances)

        val executedActivityIds = historicActivityInstances.filter { it.endTime != null }.map { it.activityId }
        return DefaultProcessDiagramGenerator().generateDiagram(
            bpmnModel, executedActivityIds, flowIds, emptyList(), emptyList(), "宋体", "微软雅黑", "黑体", true, "png"
        )
    }

    protected fun findInstanceId(bizKey: String, key: String?): String {
        val instance = get(bizKey, key)
        if (instance == null) {
            val errMsg = "找不到流程实例！【key：$key，bizKey：$bizKey】"
            log.error(errMsg)
            throw ObjectNotFoundException(errMsg)
        }
        return instance._id
    }

    protected fun getHighLightedFlows(
        bpmnModel: BpmnModel, historicActivityInstances: List<HistoricActivityInstance>
    ): List<String> {
        // 用以保存高亮的线flowId
        val highFlows = mutableListOf<String>()
        if (historicActivityInstances.isEmpty()) return highFlows

        // 遍历历史节点
        for (i in 0 until historicActivityInstances.size - 1) {
            // 用以保存后续开始时间相同的节点
            val sameStartTimeNodes: MutableList<FlowNode> = ArrayList()

            // 获取下一个节点（用于连线）
            val sameActivityImpl =
                getNextFlowNode(bpmnModel, historicActivityInstances, i, historicActivityInstances[i])

            // 将后面第一个节点放在时间相同节点的集合里
            if (sameActivityImpl != null) sameStartTimeNodes.add(sameActivityImpl)

            // 循环后面节点，看是否有与此后继节点开始时间相同的节点，有则添加到后继节点集合
            for (j in i + 1 until historicActivityInstances.size - 1) {
                val activityImpl1 = historicActivityInstances[j] // 后续第一个节点
                val activityImpl2 = historicActivityInstances[j + 1] // 后续第二个节点
                if (activityImpl1.startTime.time != activityImpl2.startTime.time) break

                // 如果第一个节点和第二个节点开始时间相同保存
                val sameActivityImpl2 = bpmnModel.mainProcess.getFlowElement(activityImpl2.activityId) as FlowNode
                sameStartTimeNodes.add(sameActivityImpl2)
            }

            // 得到节点定义的详细信息
            val activityImpl = bpmnModel.mainProcess.getFlowElement(historicActivityInstances[i].activityId) as FlowNode
            // 取出节点的所有出去的线，对所有的线进行遍历
            val pvmTransitions = activityImpl.outgoingFlows
            for (pvmTransition in pvmTransitions) {
                // 获取节点
                val pvmActivityImpl = bpmnModel.mainProcess.getFlowElement(pvmTransition.targetRef) as FlowNode

                // 不是后继节点
                if (!sameStartTimeNodes.contains(pvmActivityImpl)) continue

                // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                highFlows.add(pvmTransition.id)
            }
        }

        // 返回高亮的线
        return highFlows
    }

    protected fun getNextFlowNode(
        bpmnModel: BpmnModel,
        historicActivityInstances: List<HistoricActivityInstance>,
        i: Int,
        activityImpl_: HistoricActivityInstance
    ): FlowNode? {
        // 保存后一个节点
        var sameActivityImpl: FlowNode? = null

        // 如果当前节点不是用户任务节点，则取排序的下一个节点为后续节点
        if ("userTask" != activityImpl_.activityType) {
            // 是最后一个节点，没有下一个节点
            if (i == historicActivityInstances.size) return sameActivityImpl
            // 不是最后一个节点，取下一个节点为后继节点
            sameActivityImpl =
                bpmnModel.mainProcess.getFlowElement(historicActivityInstances[i + 1].activityId) as FlowNode // 找到紧跟在后面的一个节点
            // 返回
            return sameActivityImpl
        }

        // 遍历后续节点，获取当前节点后续节点
        for (k in i + 1 until historicActivityInstances.size) {
            // 后续节点
            val activity = historicActivityInstances[k]
            // 都是userTask，且主节点与后续节点的开始时间相同，说明不是真实的后继节点
            if ("userTask" == activity.activityType && activityImpl_.startTime.time == activity.startTime.time) continue
            // 找到紧跟在后面的一个节点
            sameActivityImpl = bpmnModel.mainProcess.getFlowElement(historicActivityInstances[k].activityId) as FlowNode
            break
        }
        return sameActivityImpl
    }

}