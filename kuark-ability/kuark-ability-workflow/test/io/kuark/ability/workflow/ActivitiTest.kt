package io.kuark.ability.workflow

import io.kuark.test.SpringTest
import org.activiti.api.process.model.builders.ProcessPayloadBuilder
import org.activiti.api.process.runtime.ProcessRuntime
import org.activiti.api.runtime.shared.query.Page
import org.activiti.api.runtime.shared.query.Pageable
import org.activiti.api.task.model.builders.TaskPayloadBuilder
import org.activiti.api.task.runtime.TaskRuntime
import org.activiti.engine.RepositoryService
import org.activiti.engine.repository.Deployment
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.zip.ZipInputStream


/**
 *
 * @author https://cloud.tencent.com/developer/article/1584828
 * @author K
 * @since 1.0.0
 */
class ActivitiTest : SpringTest() {

    @Autowired
    private val processRuntime: ProcessRuntime? = null     // 实现流程定义相关操作

    @Autowired
    private val taskRuntime: TaskRuntime? = null   // 任务先关的操作类

    @Autowired
    private val securityUtil: SecurityUtil? = null

    @Autowired
    private val repositoryService: RepositoryService? = null

    @Test // 部署流程
    fun deploy() {
        securityUtil!!.logInAs("salaboy")
        val bpmnName = "test"
        val deploymentBuilder = repositoryService!!.createDeployment().name("请假流程")
        var deployment: Deployment? = null
        try {
            deployment = deploymentBuilder.addClasspathResource("processes/$bpmnName.bpmn")
                .addClasspathResource("processes/$bpmnName.png").deploy()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Test // 远程外部BPMN
    fun deploy2() {
        securityUtil!!.logInAs("salaboy")
        try {
            var deployment: Deployment? = null
            val `in`: InputStream =
                FileInputStream(File("C:\\Users\\飞牛\\git\\SpringBoot2_Activiti7\\src\\main\\resources\\processes\\leaveProcess.zip"))
            val zipInputStream = ZipInputStream(`in`)
            deployment = repositoryService!!.createDeployment().name("请假流程2") // 指定zip格式的文件完成部署
                .addZipInputStream(zipInputStream).deploy() // 完成部署
            zipInputStream.close()
        } catch (e: Exception) {
            // TODO 上线时删除
            e.printStackTrace()
        }
    }

    @Test // 查看流程
    fun contextLoads() {
        securityUtil!!.logInAs("salaboy")
        val processDefinitionPage: Page<*> = processRuntime!!.processDefinitions(Pageable.of(0, 10))
        System.err.println("已部署的流程个数：" + processDefinitionPage.totalItems)
        for (obj in processDefinitionPage.content) {
            System.err.println("流程定义：$obj")
        }
    }

    @Test // 启动流程
    fun startInstance() {
        securityUtil!!.logInAs("salaboy")
        val processInstance = processRuntime!!
            .start(ProcessPayloadBuilder.start().withProcessDefinitionKey("myProcess_1").build())
        System.err.println("流程实例ID：" + processInstance.id)
    }

    @Test // 执行流程
    fun testTask() {
        securityUtil!!.logInAs("salaboy")
        var page = taskRuntime!!.tasks(Pageable.of(0, 10))
        if (page.totalItems > 0) {
            for (task in page.content) {
                System.err.println("当前任务有1：$task")
                // 拾取
                taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.id).build())
                // 执行
                taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.id).build())
            }
        } else {
            System.err.println("没的任务1")
        }
        page = taskRuntime.tasks(Pageable.of(0, 10))
        if (page.totalItems > 0) {
            for (task in page.content) {
                System.err.println("当前任务有2：$task")
            }
        } else {
            System.err.println("没的任务2")
        }
    }

}