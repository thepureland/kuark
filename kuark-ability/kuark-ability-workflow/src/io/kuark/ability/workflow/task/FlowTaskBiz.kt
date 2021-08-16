package io.kuark.ability.workflow.task

import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.lang.string.StringKit
import io.kuark.base.log.LogFactory
import io.kuark.base.query.sort.Order
import org.activiti.engine.TaskService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.lang.StringBuilder

/**
 * 流程任务相关业务
 *
 * @author K
 * @since 1.0.0
 */
open class FlowTaskBiz : IFlowTaskBiz {

    @Autowired
    private lateinit var taskService: TaskService

    private val log = LogFactory.getLog(this::class)

    override fun isExists(bizKey: String, taskDefinitionKey: String): Boolean {
        val count = taskService.createTaskQuery()
            .processInstanceBusinessKey(bizKey)
            .taskDefinitionKey(taskDefinitionKey)
            .count()
        return count > 0
    }

    override fun get(bizKey: String, taskDefinitionKey: String): FlowTask? {
        val task = taskService.createTaskQuery()
            .processInstanceBusinessKey(bizKey)
            .taskDefinitionKey(taskDefinitionKey)
            .singleResult()
        return if (task == null) null else FlowTask(task)
    }

    override fun search(
        queryItems: FlowTaskQueryItems,
        pageNum: Int,
        pageSize: Int,
        vararg orders: Order
    ): List<FlowTask> {
        val whereStr = StringBuilder("1=1")

        // 任务受理人id
        val assignee = queryItems.assignee
        if (StringKit.isNotBlank(assignee) && !assignee!!.contains("'")) {
            whereStr.append(" AND t.assignee_ = '$assignee'")
        }

        // 业务主键
        val bizKey = queryItems.bizKey
        if (StringKit.isNotBlank(bizKey) && !bizKey!!.contains("'")) {
            whereStr.append(" AND UPPER(t.business_key_) LIKE '%${bizKey.uppercase()}%'")
        }

        // 任务定义key(bpmn文件userTask元素的id)
        val taskDefinitionKey = queryItems.taskDefinitionKey
        if (StringKit.isNotBlank(taskDefinitionKey) && !taskDefinitionKey!!.contains("'")) {
            whereStr.append(" AND UPPER(t.task_def_key_) LIKE '%${taskDefinitionKey!!.uppercase()}%'")
        }

        // 任务名称
        val name = queryItems.name
        if (StringKit.isNotBlank(name) && !name!!.contains("'")) {
            whereStr.append(" AND UPPER(t.name_) LIKE '%${name.uppercase()}%'")
        }

        // 流程定义key(bpmn文件中process元素的id)
        val flowDefinitionKey = queryItems.flowDefinitionKey
        if (StringKit.isNotBlank(flowDefinitionKey) && !flowDefinitionKey!!.contains("'")) {
            whereStr.append(" AND UPPER(d.key_) LIKE '%${flowDefinitionKey!!.uppercase()}%'")
        }

        // 流程版本
        val flowVersion = queryItems.flowVersion
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
    override fun complete(bizKey: String, taskDefinitionKey: String, userId: String, force: Boolean): Boolean {
        val userId = userId.trim()
        require(userId.isNotEmpty()) { "执行流程任务失败！【userId】参数不能为空！【bizKey：$bizKey, taskDefinitionKey：$taskDefinitionKey】" }
        val task = findTask(bizKey, taskDefinitionKey)
        if (force) {
            taskService.setAssignee(task._id, userId)
            taskService.complete(task._id)
            log.info("强制执行流程任务成功！【bizKey：$bizKey, taskDefinitionKey：$taskDefinitionKey, userId: $userId】")
        } else {
            if (userId == task.assignee) {
                taskService.complete(task._id)
                log.info("执行流程任务成功！【bizKey：$bizKey, taskDefinitionKey：$taskDefinitionKey, userId: $userId】")
            } else {
                log.error("执行流程任务失败，因签收的用户不是【$userId】！【bizKey：$bizKey, taskDefinitionKey：$taskDefinitionKey, userId: $userId】")
                return false
            }
        }
        return true
    }

    private fun findTask(bizKey: String, taskDefinitionKey: String): FlowTask {
        val task = get(bizKey, taskDefinitionKey)
        if (task == null) {
            val errMsg = "找不到流程任务！bizKey：$bizKey, taskDefinitionKey：$taskDefinitionKey"
            log.error(errMsg)
            throw ObjectNotFoundException(errMsg)
        }
        return task
    }

}