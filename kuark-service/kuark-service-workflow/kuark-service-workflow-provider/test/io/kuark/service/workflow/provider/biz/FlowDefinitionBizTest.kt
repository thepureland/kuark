package io.kuark.service.workflow.provider.biz

import io.kuark.base.error.ObjectAlreadyExistsException
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.image.ImageKit
import io.kuark.base.io.FileKit
import io.kuark.base.io.IoKit
import io.kuark.base.io.PathKit
import io.kuark.context.kit.SpringKit
import io.kuark.service.workflow.common.vo.definition.FlowDefinitionSearchPayload
import io.kuark.test.common.SpringTest
import org.activiti.engine.RepositoryService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.io.File
import javax.imageio.ImageIO


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal open class FlowDefinitionBizTest : SpringTest() {

    @Autowired
    private lateinit var flowDefinitionBiz: io.kuark.service.workflow.provider.ibiz.IFlowDefinitionBiz

    @Autowired
    private lateinit var repositoryService: RepositoryService

    private val NO_EXISTS = "no exists"

    @BeforeAll
    fun setUp() {
        //??? 不加的话，运行getFlowDiagram()时会报java.awt.HeadlessException异常！
        System.setProperty("java.awt.headless", "false")
    }

    @Test
    @Transactional
    open fun isExists() {
        io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.createModel()

        assert(flowDefinitionBiz.isExists(io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY, 1))
        assert(flowDefinitionBiz.isExists(io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY))
        assertFalse(flowDefinitionBiz.isExists(io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY, 2))
        assertFalse(flowDefinitionBiz.isExists(NO_EXISTS, 1))
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.isExists(" ") }

        io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.deployModel()

        assert(flowDefinitionBiz.isExists(io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY, 1))
        assert(flowDefinitionBiz.isExists(io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY))
        assertFalse(flowDefinitionBiz.isExists(io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY, 2))
        assertFalse(flowDefinitionBiz.isExists(NO_EXISTS, 1))
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.isExists(" ") }

    }

    @Test
    @Transactional
    open fun get() {
        io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.createModel()

        assertNotNull(flowDefinitionBiz.get(io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY, 1))
        assertNull(flowDefinitionBiz.get(io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY, 2))
        assertNull(flowDefinitionBiz.get(NO_EXISTS, 1))
        assertEquals(1, flowDefinitionBiz.get(io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY)!!.version)
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.get(" ") }

        io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.deployModel()

        assert(flowDefinitionBiz.get(io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY, 1)!!.deployed)
        assertNull(flowDefinitionBiz.get(io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY, 2))
        assertNull(flowDefinitionBiz.get(NO_EXISTS, 1))
        assertEquals(1, flowDefinitionBiz.get(io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY)!!.version)
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.get(" ") }
    }

    @Test
    @Transactional
    open fun search() {
        io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.createThenDeploy()

        // 指定条件模糊搜索
        var searchPayload = FlowDefinitionSearchPayload().apply {
            key = "leaveAp"
            name = "请假"
            deployed = true
            latestOnly = true
            pageSize = 1
        }
        val definitions = flowDefinitionBiz.search(searchPayload)
        assertEquals(1, definitions.size)

        // 没有指定条件
        searchPayload = FlowDefinitionSearchPayload()
        assertEquals(1, flowDefinitionBiz.search(searchPayload).size)
    }

    @Test
    @Transactional
    open fun create() {
        // 正常新增
        val svgXml = FileKit.readFileToString(File(PathKit.getResourcePath("bpmn/test-svg.xml")))
        val flowJson = FileKit.readFileToString(File(PathKit.getResourcePath("bpmn/test-flow.json")))
        val definition = flowDefinitionBiz.create(
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY,
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.NAME,
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.CATEGORY, flowJson, svgXml)
        assertNotNull(definition._modelId)

        // 参数非法
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.create("",
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.NAME,
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.CATEGORY, flowJson, svgXml) }
        assertThrows<IllegalArgumentException> {
            flowDefinitionBiz.create(
                io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY, "",
                io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.CATEGORY, flowJson, svgXml)
        }
        assertThrows<IllegalArgumentException> {
            flowDefinitionBiz.create(
                io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY,
                io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.NAME,
                io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.CATEGORY, flowJson, " ")
        }
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.create(
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY,
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.NAME,
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.CATEGORY, "", svgXml) }

        // 重复新增
        assertThrows<ObjectAlreadyExistsException> {
            flowDefinitionBiz.create(
                io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY,
                io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.NAME,
                io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.CATEGORY, flowJson, svgXml)
        }
    }

    @Test
    @Transactional
    open fun deploy() {
        val model = io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.createModel()

        // 非法参数
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.deploy("") }

        // 找不到模型
        assertThrows<ObjectNotFoundException> { flowDefinitionBiz.deploy(NO_EXISTS) }

        // 成功部署
        val definition = flowDefinitionBiz.deploy(model.key)
        assertNotNull(definition._modelId)
        assertNotNull(flowDefinitionBiz.get(definition.key))

        // 重复部署
        assertThrows<IllegalStateException> { flowDefinitionBiz.deploy(model.key) }
    }

    @Test
    @Transactional
    open fun deployWithBpmn() {
        assertThrows<ObjectNotFoundException> {
            flowDefinitionBiz.deployWithBpmn(io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.DEPLOYMENT_NAME, NO_EXISTS, "test")
        }
        assertThrows<ObjectNotFoundException> {
            flowDefinitionBiz.deployWithBpmn(
                io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.DEPLOYMENT_NAME,
                io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.CATEGORY, NO_EXISTS)
        }
    }

    @Test
    @Transactional
    open fun deployWithZip() {
        val definitions = flowDefinitionBiz.deployWithZip(io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.DEPLOYMENT_NAME, "bpmn")
        assertEquals(2, definitions.size)
        definitions.forEach {
            var d = repositoryService.createProcessDefinitionQuery().processDefinitionKey(it.key).singleResult()
            assert(d.diagramResourceName in arrayOf("test11.png", "test22.png"))
            d = repositoryService.createProcessDefinitionQuery().processDefinitionKey(it.key).singleResult()
            assert(d.diagramResourceName in arrayOf("test11.png", "test22.png"))
        }

        // 文件不存在
        assertThrows<ObjectNotFoundException> { flowDefinitionBiz.deployWithZip(io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.DEPLOYMENT_NAME, "no exists") }
    }

    @Test
    @Transactional
    open fun getFlowDiagram() {
        val definition = io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.createThenDeploy()

        val inputStream = flowDefinitionBiz.getFlowDiagram(definition.key)
        val bufferedImage = ImageIO.read(inputStream)
        ImageKit.showImage(bufferedImage)
        Thread.sleep(2000)
    }

    @Test
    open fun genFlowDiagram() {
        assertThrows<ObjectNotFoundException> { flowDefinitionBiz.genFlowDiagram(NO_EXISTS) }

        val inputStream = flowDefinitionBiz.genFlowDiagram("test")
        val svgXml = inputStream.use {
            IoKit.toString(inputStream, "utf-8")
        }
        val bufferedImage = ImageKit.renderSvgToImage(svgXml, 600, 400)
        ImageKit.showImage(bufferedImage)
        Thread.sleep(3000)
    }

    @Test
    @Transactional
    open fun activate() {
        val definition = io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.createThenDeploy()
        flowDefinitionBiz.activate(definition.key)
    }

    @Test
    @Transactional
    open fun suspend() {
        val definition = io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.createThenDeploy()
        flowDefinitionBiz.suspend(definition.key)
    }

    @Test
    @Transactional
    open fun delete() {
        val definition = io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.createThenDeploy()
        flowDefinitionBiz.delete(definition.key, null, true)
        assertThrows<ObjectNotFoundException> { flowDefinitionBiz.delete(NO_EXISTS, null, true) }
    }

    @Test
    @Transactional
    open fun start() {
        val definition = io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.createThenDeploy()

        // 非法参数
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.start("",
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.BIZ_KEY,
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.INSTANCE_NAME
        ) }
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.start(
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY, " ",
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.INSTANCE_NAME
        ) }
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.start(
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY, "biz'key",
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.INSTANCE_NAME
        ) }
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.start(
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY,
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.BIZ_KEY, "") }

        // 传不存在的流程定义key
        assertThrows<ObjectNotFoundException> { flowDefinitionBiz.start(NO_EXISTS,
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.BIZ_KEY,
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.INSTANCE_NAME
        ) }

        // 成功启动
        var isEventFire = false
        val listener = object : io.kuark.service.workflow.provider.event.IFlowEventListener {
            override fun onEvent(event: io.kuark.service.workflow.provider.event.FlowEvent) {
                if (event.type == io.kuark.service.workflow.provider.event.FlowEventType.ACTIVITY_STARTED) {
                    isEventFire = true
                }
            }
        }
        var variables = mapOf("applicantId" to io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.APPLICANT_ID)
        val instance = flowDefinitionBiz.start(definition.key,
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.BIZ_KEY,
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.INSTANCE_NAME, variables, listener)
        assertNotNull(instance)
        assertNotNull(instance!!._id)
        assert(isEventFire)
        assertEquals(io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.INSTANCE_NAME, instance.name)

        // 重复启动
        assertThrows<ObjectAlreadyExistsException> {
            flowDefinitionBiz.start(definition.key,
                io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.BIZ_KEY,
                io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.INSTANCE_NAME, variables, listener)
        }
    }


    internal companion object {

        const val KEY = "leaveApplicationKey"
        const val NAME = "请假流程"
        const val CATEGORY = "test"
        const val BIZ_KEY = "bizKey"
        const val INSTANCE_NAME = "instanceName"
        const val APPLICANT_ID = "applicantId"
        const val DEPLOYMENT_NAME = "请假申请(junit)"

        internal fun createModel(): io.kuark.service.workflow.provider.model.vo.FlowDefinition {
            val svgXml = FileKit.readFileToString(File(PathKit.getResourcePath("bpmn/test-svg.xml")), "UTF-8")
            val flowJson = FileKit.readFileToString(File(PathKit.getResourcePath("bpmn/test-flow.json")), "UTF-8")
            val flowDefinitionBiz = SpringKit.getBean(io.kuark.service.workflow.provider.ibiz.IFlowDefinitionBiz::class)
            return flowDefinitionBiz.create(
                io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY,
                io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.NAME,
                io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.CATEGORY, flowJson, svgXml)
        }

        internal fun deployModel(): io.kuark.service.workflow.provider.model.vo.FlowDefinition {
            val flowDefinitionBiz = SpringKit.getBean(io.kuark.service.workflow.provider.ibiz.IFlowDefinitionBiz::class)
            return flowDefinitionBiz.deploy(io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY)
        }

        internal fun createThenDeploy(): io.kuark.service.workflow.provider.model.vo.FlowDefinition {
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.createModel()
            return io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.deployModel()
        }

        internal fun deployThenStart(variables: Map<String, *>? = mapOf("applicantId" to io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.APPLICANT_ID)): io.kuark.service.workflow.provider.model.vo.FlowInstance {
            val flowDefinitionBiz = SpringKit.getBean(io.kuark.service.workflow.provider.ibiz.IFlowDefinitionBiz::class)
            io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.createThenDeploy()
            return flowDefinitionBiz.start(
                io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.KEY,
                io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.BIZ_KEY,
                io.kuark.service.workflow.provider.biz.FlowDefinitionBizTest.Companion.INSTANCE_NAME, variables)!!
        }

    }

}