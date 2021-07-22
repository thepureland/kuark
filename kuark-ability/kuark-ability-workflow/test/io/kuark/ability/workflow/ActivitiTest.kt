package io.kuark.ability.workflow

import io.kuark.test.common.SpringTest
import org.activiti.api.process.model.builders.ProcessPayloadBuilder
import org.activiti.api.process.runtime.ProcessRuntime
import org.activiti.api.runtime.shared.query.Page
import org.activiti.api.runtime.shared.query.Pageable
import org.activiti.api.task.model.builders.TaskPayloadBuilder
import org.activiti.api.task.runtime.TaskRuntime
import org.activiti.engine.ProcessEngineConfiguration
import org.activiti.engine.ProcessEngines
import org.activiti.engine.RepositoryService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipInputStream


/**
 *
 * @author https://cloud.tencent.com/developer/article/1584828
 * @author K
 * @since 1.0.0
 */
class ActivitiTest : SpringTest() {

    @Autowired
    private val processRuntime: ProcessRuntime? = null

    @Autowired
    private val taskRuntime: TaskRuntime? = null

    @Autowired
    private val securityUtil: SecurityUtil? = null

    @Autowired
    private val repositoryService: RepositoryService? = null

    @Test
    fun genTable() {
        println(ProcessEngines.getDefaultProcessEngine())
    }


    @Test
    fun deploy() {
        securityUtil!!.logInAs("salaboy")
        val bpmnName = "test"
        val deploymentBuilder = repositoryService!!.createDeployment().name("请假流程")
        val deploy = deploymentBuilder.addClasspathResource("bpmn/$bpmnName.bpmn")
            .addClasspathResource("bpmn/$bpmnName.png").deploy()
        println(deploy)
    }

    @Test
    fun deploy2() {
        securityUtil!!.logInAs("salaboy")
        val inputStream =
            FileInputStream(File("C:\\Users\\飞牛\\git\\SpringBoot2_Activiti7\\src\\main\\resources\\processes\\leaveProcess.zip"))
        val zipInputStream = ZipInputStream(inputStream)
        repositoryService!!.createDeployment().name("请假流程2") // 指定zip格式的文件完成部署
            .addZipInputStream(zipInputStream).deploy() // 完成部署
        zipInputStream.close()
    }

    @Test
    fun contextLoads() {
        securityUtil!!.logInAs("salaboy")
        val processDefinitionPage: Page<*> = processRuntime!!.processDefinitions(Pageable.of(0, 10))
        System.err.println("已部署的流程个数：" + processDefinitionPage.totalItems)
        for (obj in processDefinitionPage.content) {
            System.err.println("流程定义：$obj")
        }
    }

    @Test
    fun startInstance() {
        securityUtil!!.logInAs("salaboy")
        val processInstance = processRuntime!!
            .start(ProcessPayloadBuilder.start().withProcessDefinitionKey("myProcess_1").build())
        System.err.println("流程实例ID：" + processInstance.id)
    }

    @Test
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