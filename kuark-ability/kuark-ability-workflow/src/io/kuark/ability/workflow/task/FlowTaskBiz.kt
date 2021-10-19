package io.kuark.ability.workflow.task

import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.base.error.IllegalOperationException
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.lang.string.StringKit
import io.kuark.base.log.LogFactory
import io.kuark.base.query.sort.Order
import org.activiti.bpmn.model.FlowNode
import org.activiti.bpmn.model.SequenceFlow
import org.activiti.engine.HistoryService
import org.activiti.engine.RepositoryService
import org.activiti.engine.RuntimeService
import org.activiti.engine.TaskService
import org.activiti.engine.impl.identity.Authentication
import org.activiti.engine.task.Task
import org.activiti.engine.task.TaskInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional


/**
 * 流程任务相关业务
 *
 * @author K
 * @since 1.0.0
 */
open class FlowTaskBiz : IFlowTaskBiz {

    @Autowired
    private lateinit var taskService: TaskService

    @Autowired
    private lateinit var historyService: HistoryService

    @Autowired
    private lateinit var repositoryService: RepositoryService

    @Autowired
    private lateinit var runtimeService: RuntimeService


    private val log = LogFactory.getLog(this::class)

    override fun isExists(bizKey: String, taskDefinitionKey: String): Boolean {
        val count = taskService.createTaskQuery()
            .processInstanceBusinessKey(bizKey)
            .taskDefinitionKey(taskDefinitionKey)
            .count()
        return count > 0
    }

    override fun get(bizKey: String, taskDefinitionKey: String): FlowTask? {
        val task = getActivitiTask(bizKey, taskDefinitionKey)
        return if (task == null) null else FlowTask(task)
    }

    override fun search(
        searchParams: FlowTaskSearchParams,
        pageNum: Int,
        pageSize: Int,
        vararg orders: Order
    ): List<FlowTask> {
        val whereStr = StringBuilder("1=1")

        // 任务受理人id
        val assignee = searchParams.assignee
        if (StringKit.isNotBlank(assignee) && !assignee!!.contains("'")) {
            whereStr.append(" AND t.assignee_ = '$assignee'")
        }

        // 业务主键
        val bizKey = searchParams.bizKey
        if (StringKit.isNotBlank(bizKey) && !bizKey!!.contains("'")) {
            whereStr.append(" AND UPPER(t.business_key_) LIKE '%${bizKey.uppercase()}%'")
        }

        // 任务定义key(bpmn文件userTask元素的id)
        val taskDefinitionKey = searchParams.taskDefinitionKey
        if (StringKit.isNotBlank(taskDefinitionKey) && !taskDefinitionKey!!.contains("'")) {
            whereStr.append(" AND UPPER(t.task_def_key_) LIKE '%${taskDefinitionKey.uppercase()}%'")
        }

        // 任务名称
        val name = searchParams.name
        if (StringKit.isNotBlank(name) && !name!!.contains("'")) {
            whereStr.append(" AND UPPER(t.name_) LIKE '%${name.uppercase()}%'")
        }

        // 流程定义key(bpmn文件中process元素的id)
        val flowDefinitionKey = searchParams.flowDefinitionKey
        if (StringKit.isNotBlank(flowDefinitionKey) && !flowDefinitionKey!!.contains("'")) {
            whereStr.append(" AND UPPER(d.key_) LIKE '%${flowDefinitionKey.uppercase()}%'")
        }

        // 流程版本
        val flowVersion = searchParams.flowVersion
        if (flowVersion != null) {
            whereStr.append(" AND d.version_ = $flowVersion")
        }

        // 排序
        val orderStr = RdbKit.getOrderSql(*orders)

        // 查询
        val sql =
            "SELECT * FROM act_ru_task t LEFT JOIN act_re_procdef d ON t.proc_def_id_ = d.id_  WHERE $whereStr $orderStr"
        val query = taskService.createNativeTaskQuery().sql(sql)
        val tasks = if (pageSize < 1) {
            query.list()
        } else {
            val pageNo = if (pageNum < 1) 1 else pageNum
            query.listPage((pageNo - 1) * pageSize, pageSize)
        }

        return tasks.map { FlowTask(it) }
    }

    @Transactional
    override fun claim(bizKey: String, taskDefinitionKey: String, assignee: String): Boolean {
        val userId = assignee.trim()
        require(userId.isNotEmpty()) { "签收流程任务失败！【assignee】参数不能为空！【bizKey：$bizKey，taskDefinitionKey：$taskDefinitionKey】" }
        val task = findTask(bizKey, taskDefinitionKey)
        if (StringKit.isBlank(task.assignee)) {
            taskService.claim(task._id, userId)
            log.info("签收流程任务成功！【bizKey：$bizKey，taskDefinitionKey：$taskDefinitionKey，assignee: $userId】")
        } else {
            if (task.assignee == userId) {
                log.warn("忽略签收流程任务操作，因该用户已对此任务签收！【bizKey：$bizKey，taskDefinitionKey：$taskDefinitionKey，assignee: $userId】")
            } else {
                log.error("签收流程任务失败，因已被其他用户签收，要更改签收的用户，请先取消签收！【bizKey：$bizKey，taskDefinitionKey：$taskDefinitionKey，assignee: $userId】")
                return false
            }
        }
        return true
    }

    @Transactional
    override fun unclaim(bizKey: String, taskDefinitionKey: String): Boolean {
        val task = findTask(bizKey, taskDefinitionKey)
        return if (StringKit.isBlank(task.assignee)) {
            log.warn("忽略流程任务的取消签收操作，因该任务本就无用户签收！【bizKey：$bizKey，taskDefinitionKey：$taskDefinitionKey】")
            false
        } else {
            taskService.unclaim(task._id)
            log.info("流程任务取消签收成功！【bizKey：$bizKey，taskDefinitionKey：$taskDefinitionKey】")
            true
        }
    }

    @Transactional
    override fun delegate(bizKey: String, taskDefinitionKey: String, assignee: String): Boolean {
        val userId = assignee.trim()
        require(userId.isNotEmpty()) { "流程任务委托失败！【assignee】参数不能为空！【bizKey：$bizKey，taskDefinitionKey：$taskDefinitionKey】" }
        val task = findTask(bizKey, taskDefinitionKey)
        if (StringKit.isBlank(task.assignee)) {
            log.info("流程任务在进行委托操作时，发现该任务并无用户签收，直接进行签收操作！【bizKey：$bizKey，taskDefinitionKey：$taskDefinitionKey】")
            taskService.claim(task._id, userId)
            log.info("用户签收流程任务成功！【bizKey：$bizKey，taskDefinitionKey：$taskDefinitionKey，assignee: $userId】")
        } else {
            taskService.delegateTask(task._id, userId)
            log.info("流程任务委托成功！【bizKey：$bizKey，taskDefinitionKey：$taskDefinitionKey，assignee: $userId】")
        }
        return true
    }

    @Transactional
    override fun complete(
        bizKey: String,
        taskDefinitionKey: String,
        userId: String,
        comment: String?,
        variables: Map<String, *>?,
        force: Boolean
    ): Boolean {
        val uId = userId.trim()
        require(uId.isNotEmpty()) { "执行流程任务失败！【userId】参数不能为空！【bizKey：$bizKey, taskDefinitionKey：$taskDefinitionKey】" }
        val task = findTask(bizKey, taskDefinitionKey)
        if (force) {
            taskService.setAssignee(task._id, uId)
            if (StringKit.isNotBlank(comment)) {
                Authentication.setAuthenticatedUserId(uId) // addComment底层会调用Authentication.getAuthenticatedUserId()
                taskService.addComment(task._id, task._instanceId, comment)
            }
            taskService.complete(task._id, variables)
            log.info("强制执行流程任务成功！【bizKey：$bizKey, taskDefinitionKey：$taskDefinitionKey, userId: $uId】")
        } else {
            if (uId == task.assignee) {
                if (StringKit.isNotBlank(comment)) {
                    Authentication.setAuthenticatedUserId(uId) // addComment底层会调用Authentication.getAuthenticatedUserId()
                    taskService.addComment(task._id, task._instanceId, comment)
                }
                taskService.complete(task._id, variables)
                log.info("执行流程任务成功！【bizKey：$bizKey, taskDefinitionKey：$taskDefinitionKey, userId: $uId】")
            } else {
                log.error("执行流程任务失败，因签收的用户不是【$uId】！【bizKey：$bizKey, taskDefinitionKey：$taskDefinitionKey, userId: $uId】")
                return false
            }
        }
        return true
    }

    override fun revoke(bizKey: String, userId: String, comment: String?): Boolean {
        require(userId.isNotEmpty()) { "撤回流程任务失败！【userId】参数不能为空！【bizKey：$bizKey" }
        val curTask = taskService.createTaskQuery().processInstanceBusinessKey(bizKey).singleResult()

        val hisTaskInstances = historyService.createHistoricTaskInstanceQuery()
            .processInstanceBusinessKey(bizKey)
            .taskAssignee(userId)
            .orderByTaskCreateTime()
            .desc()
            .list()
        val myTask = if (hisTaskInstances.isEmpty()) {
            throw IllegalOperationException("撤回流程任务失败！该任务非当前用户提交！")
        } else {
            hisTaskInstances.first()
        }

        // 任务跳转
        jump(curTask, myTask, userId, comment)

        return true
    }

    @Transactional
    override fun reject(bizKey: String, taskDefinitionKey: String, userId: String, comment: String?): Boolean {
        require(userId.isNotEmpty()) { "驳回流程任务失败！【userId】参数不能为空！【bizKey：$bizKey, taskDefinitionKey：$taskDefinitionKey】" }
        val task = findTask(bizKey, taskDefinitionKey)
        if (userId != task.assignee) {
            log.error("驳回流程任务失败，因签收的用户不是【$userId】！【bizKey：$bizKey, taskDefinitionKey：$taskDefinitionKey, userId: $userId】")
            return false
        }

        // 取得所有历史任务按时间降序排序
        val htiList = historyService.createHistoricTaskInstanceQuery()
            .processInstanceId(task._instanceId)
            .orderByTaskCreateTime()
            .desc()
            .list()
        if (htiList.isEmpty() || htiList.size < 2) {
            return false
        }

        // 任务跳转
        jump(htiList[0], htiList[1], userId, comment)

        // 设置执行人
        val nextTask = taskService.createTaskQuery().processInstanceId(task._instanceId).singleResult()
        if (nextTask != null) {
            taskService.setAssignee(nextTask.id, htiList[1].assignee)
        }
        return true
    }

    private fun jump(srcTask: TaskInfo, destTask: TaskInfo, userId: String, comment: String?) {
        // 得到当前节点的信息
        val execution = runtimeService.createExecutionQuery().executionId(srcTask.executionId).singleResult()
        val bpmnModel = repositoryService.getBpmnModel(srcTask.processDefinitionId)
        val curFlowNode = bpmnModel.mainProcess.getFlowElement(execution.activityId) as FlowNode

        // 得到目标节点的信息
        val finishedInstanceList = historyService.createHistoricActivityInstanceQuery()
            .executionId(destTask.executionId)
            .finished()
            .list()
        val lastActivityId = finishedInstanceList.first { it.taskId == destTask.id }.activityId
        val lastFlowNode = bpmnModel.mainProcess.getFlowElement(lastActivityId) as FlowNode

        // 记录当前节点的原活动方向
        val oriSequenceFlows: MutableList<SequenceFlow> = ArrayList()
        oriSequenceFlows.addAll(curFlowNode.outgoingFlows)

        // 清理活动方向
        curFlowNode.outgoingFlows.clear()

        // 建立新方向
        val newSequenceFlow = SequenceFlow().apply {
            id = "newSequenceFlowId"
            sourceFlowElement = curFlowNode
            targetFlowElement = lastFlowNode
        }
        curFlowNode.outgoingFlows = listOf(newSequenceFlow)

        // 完成任务
        if (StringKit.isNotBlank(comment)) {
            Authentication.setAuthenticatedUserId(userId) // addComment底层会调用Authentication.getAuthenticatedUserId()
            taskService.addComment(srcTask.id, srcTask.processInstanceId, comment)
        }
        taskService.complete(srcTask.id)

        // 恢复原方向
        curFlowNode.outgoingFlows = oriSequenceFlows
    }

    private fun getActivitiTask(bizKey: String, taskDefinitionKey: String): Task? {
        return taskService.createTaskQuery()
            .processInstanceBusinessKey(bizKey)
            .taskDefinitionKey(taskDefinitionKey)
            .singleResult()
    }

    private fun findTask(bizKey: String, taskDefinitionKey: String): FlowTask {
        val activitiTask = findActivitiTask(bizKey, taskDefinitionKey)
        return FlowTask(activitiTask)
    }

    private fun findActivitiTask(bizKey: String, taskDefinitionKey: String): Task {
        val task = getActivitiTask(bizKey, taskDefinitionKey)
        if (task == null) {
            val errMsg = "找不到流程任务，可能流程未启动或已执行完成！bizKey：$bizKey, taskDefinitionKey：$taskDefinitionKey"
            log.error(errMsg)
            throw ObjectNotFoundException(errMsg)
        }
        return task
    }

}