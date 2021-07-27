package io.kuark.ability.workflow.biz

import io.kuark.ability.workflow.ibiz.IFlowDefinitionBiz
import io.kuark.ability.workflow.vo.FlowDefinition
import io.kuark.base.image.ImageKit
import io.kuark.base.io.FileKit
import io.kuark.base.io.PathKit
import io.kuark.test.common.SpringTest
import org.activiti.engine.RepositoryService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.io.FileNotFoundException
import javax.imageio.ImageIO


internal open class FlowDefinitionBizTest : SpringTest() {

    @Autowired
    private lateinit var flowDefinitionBiz: IFlowDefinitionBiz

    @Autowired
    private lateinit var repositoryService: RepositoryService

    private val DEPLOYMENT_NAME = "请假申请(junit)"

    private val NO_EXISTS = "no exists"


    @Test
    @Transactional
    open fun deployWithBpmn() {
        assertThrows<FileNotFoundException> {
            flowDefinitionBiz.deployWithBpmn(DEPLOYMENT_NAME, NO_EXISTS, "test")
        }
        assertThrows<FileNotFoundException> {
            flowDefinitionBiz.deployWithBpmn(DEPLOYMENT_NAME, "test", NO_EXISTS)
        }
    }

    @Test
    @Transactional
    open fun deployWithZip() {
        val definitions = flowDefinitionBiz.deployWithZip(DEPLOYMENT_NAME, "bpmn")
        assertEquals(2, definitions.size)
        definitions.forEach {
            var d = repositoryService.createProcessDefinitionQuery().processDefinitionKey(it.key).singleResult()
            assert(d.diagramResourceName in arrayOf("test11.png", "test22.png"))
            d = repositoryService.createProcessDefinitionQuery().processDefinitionKey(it.key).singleResult()
            assert(d.diagramResourceName in arrayOf("test11.png", "test22.png"))
        }

        // 文件不存在
        assertThrows<FileNotFoundException> { flowDefinitionBiz.deployWithZip(DEPLOYMENT_NAME, "no exists") }
    }

    @Test
    @Transactional
    open fun getDefinitionByKey() {
        val definition = deploy()
        assert(flowDefinitionBiz.getDefinitionByKey(definition.key).isNotEmpty())
        assert(flowDefinitionBiz.getDefinitionByKey(NO_EXISTS).isEmpty())
    }

    @Test
    @Transactional
    open fun activateDefinition() {
        val definition = deploy()
        flowDefinitionBiz.activateDefinition(definition.key)
    }

    @Test
    @Transactional
    open fun suspendDefinition() {
        val definition = deploy()
        flowDefinitionBiz.suspendDefinition(definition.key)
    }

    @Test
    @Transactional
    open fun deleteDefinitions() {
        val definition = deploy()
        flowDefinitionBiz.deleteDefinitions(definition.key, true)
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.deleteDefinitions(NO_EXISTS, true) }
    }

    @Test
    @Transactional
    open fun getFlowDiagram() {
        System.setProperty("java.awt.headless", "false") //??? 不加会报java.awt.HeadlessException异常
        val definition = deploy()
        val inputStream = flowDefinitionBiz.getFlowDiagram(definition.key)
        val bufferedImage = ImageIO.read(inputStream)
        ImageKit.showImage(bufferedImage)
        Thread.sleep(2000)
    }

    @Test
    open fun genFlowDiagram() {
        val inputStream = flowDefinitionBiz.genFlowDiagram("test")
        val file = File("${PathKit.getUserDirectory()}/flow.png")
        inputStream.use {
            FileKit.copyInputStreamToFile(it!!, file)
        }

//        val bufferedImage = ImageIO.read(inputStream) //??? bufferedImage会为null
//        ImageKit.showImage(bufferedImage)
//        Thread.sleep(2000)

        assertThrows<FileNotFoundException> { flowDefinitionBiz.genFlowDiagram(NO_EXISTS) }
    }

    private fun deploy(): FlowDefinition {
        return flowDefinitionBiz.deployWithBpmn(DEPLOYMENT_NAME, "test", "test")
    }

}