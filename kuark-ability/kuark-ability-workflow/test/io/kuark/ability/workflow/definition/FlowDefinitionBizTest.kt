package io.kuark.ability.workflow.definition

import io.kuark.ability.workflow.event.FlowEvent
import io.kuark.ability.workflow.event.FlowEventType
import io.kuark.ability.workflow.event.IFlowEventListener
import io.kuark.ability.workflow.instance.FlowInstance
import io.kuark.base.error.ObjectAlreadyExistsException
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.image.ImageKit
import io.kuark.base.io.FileKit
import io.kuark.base.io.PathKit
import io.kuark.context.spring.SpringKit
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
    private lateinit var flowDefinitionBiz: IFlowDefinitionBiz

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
        createModel()

        assert(flowDefinitionBiz.isExists(KEY, 1))
        assert(flowDefinitionBiz.isExists(KEY))
        assertFalse(flowDefinitionBiz.isExists(KEY, 2))
        assertFalse(flowDefinitionBiz.isExists(NO_EXISTS, 1))
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.isExists(" ") }

        deployModel()

        assert(flowDefinitionBiz.isExists(KEY, 1))
        assert(flowDefinitionBiz.isExists(KEY))
        assertFalse(flowDefinitionBiz.isExists(KEY, 2))
        assertFalse(flowDefinitionBiz.isExists(NO_EXISTS, 1))
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.isExists(" ") }

    }

    @Test
    @Transactional
    open fun get() {
        createModel()

        assertNotNull(flowDefinitionBiz.get(KEY, 1))
        assertNull(flowDefinitionBiz.get(KEY, 2))
        assertNull(flowDefinitionBiz.get(NO_EXISTS, 1))
        assertEquals(1, flowDefinitionBiz.get(KEY)!!.version)
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.get(" ") }

        deployModel()

        assert(flowDefinitionBiz.get(KEY, 1)!!.isDeployed)
        assertNull(flowDefinitionBiz.get(KEY, 2))
        assertNull(flowDefinitionBiz.get(NO_EXISTS, 1))
        assertEquals(1, flowDefinitionBiz.get(KEY)!!.version)
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.get(" ") }
    }

    @Test
    @Transactional
    open fun search() {
        createThenDeploy()

        // 指定条件模糊搜索
        var criteria = FlowDefinitionCriteria.Builder()
            .key("leaveAp")
            .name("请假")
            .isDeployed(true)
            .latestOnly(true)
            .build()
        val definitions = flowDefinitionBiz.search(criteria, 1, 1)
        assertEquals(1, definitions.size)

        // 没有指定条件
        criteria = FlowDefinitionCriteria.Builder().build()
        assertEquals(1, flowDefinitionBiz.search(criteria).size)
    }

    @Test
    @Transactional
    open fun create() {
        // 正常新增
        val svgXml = FileKit.readFileToString(File(PathKit.getResourcePath("bpmn/test-svg.xml")))
        val flowJson = FileKit.readFileToString(File(PathKit.getResourcePath("bpmn/test-flow.json")))
        val definition = flowDefinitionBiz.create(KEY, NAME, CATEGORY, flowJson, svgXml)
        assertNotNull(definition._modelId)

        // 参数非法
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.create("", NAME, CATEGORY, flowJson, svgXml) }
        assertThrows<IllegalArgumentException> {
            flowDefinitionBiz.create(KEY, "", CATEGORY, flowJson, svgXml)
        }
        assertThrows<IllegalArgumentException> {
            flowDefinitionBiz.create(KEY, NAME, CATEGORY, flowJson, " ")
        }
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.create(KEY, NAME, CATEGORY, "", svgXml) }

        // 重复新增
        assertThrows<ObjectAlreadyExistsException> {
            flowDefinitionBiz.create(KEY, NAME, CATEGORY, flowJson, svgXml)
        }
    }

    @Test
    @Transactional
    open fun update() {
        var definition = createModel()

        // 更新未部署的模型
        val newSvgXml = FileKit.readFileToString(File(PathKit.getResourcePath("bpmn/test-svg.xml")))
        val newFlowJson = FileKit.readFileToString(File(PathKit.getResourcePath("bpmn/test-flow.json")))
        flowDefinitionBiz.update(definition.key, null, "newFlowName", CATEGORY, newSvgXml, newFlowJson)
        val newDefinition = flowDefinitionBiz.get(definition.key)!!
        assertEquals(1, newDefinition.version)

        definition = deployModel()

        // 更新已部署的模型
        flowDefinitionBiz.update(definition.key, null, "newFlowName2", CATEGORY, newSvgXml, newFlowJson)
        val criteria = FlowDefinitionCriteria.Builder().key(definition.key).latestOnly(false).build()
        val definitions = flowDefinitionBiz.search(criteria)
        assertEquals(2, definitions.size)
    }

    @Test
    @Transactional
    open fun deploy() {
        var model = createModel()

        // 非法参数
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.deploy("") }

        // 找不到模型
        assertThrows<ObjectNotFoundException> { flowDefinitionBiz.deploy(NO_EXISTS) }

        // 成功部署
        var definition = flowDefinitionBiz.deploy(model.key)
        assertNotNull(definition._modelId)
        assertNotNull(flowDefinitionBiz.get(definition.key))

        // 重复部署
        assertThrows<IllegalStateException> { flowDefinitionBiz.deploy(model.key) }
    }

    @Test
    @Transactional
    open fun deployWithBpmn() {
        assertThrows<ObjectNotFoundException> {
            flowDefinitionBiz.deployWithBpmn(DEPLOYMENT_NAME, NO_EXISTS, "test")
        }
        assertThrows<ObjectNotFoundException> {
            flowDefinitionBiz.deployWithBpmn(DEPLOYMENT_NAME, CATEGORY, NO_EXISTS)
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
        assertThrows<ObjectNotFoundException> { flowDefinitionBiz.deployWithZip(DEPLOYMENT_NAME, "no exists") }
    }

    @Test
    @Transactional
    open fun getFlowDiagram() {
        val definition = createThenDeploy()

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

        assertThrows<ObjectNotFoundException> { flowDefinitionBiz.genFlowDiagram(NO_EXISTS) }
    }

    @Test
    @Transactional
    open fun activate() {
        val definition = createThenDeploy()
        flowDefinitionBiz.activate(definition.key)
    }

    @Test
    @Transactional
    open fun suspend() {
        val definition = createThenDeploy()
        flowDefinitionBiz.suspend(definition.key)
    }

    @Test
    @Transactional
    open fun delete() {
        val definition = createThenDeploy()
        flowDefinitionBiz.delete(definition.key, null, true)
        assertThrows<ObjectNotFoundException> { flowDefinitionBiz.delete(NO_EXISTS, null, true) }
    }

    @Test
    @Transactional
    open fun start() {
        val definition = createThenDeploy()

        // 非法参数
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.start("", BIZ_KEY, INSTANCE_NAME) }
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.start(KEY, " ", INSTANCE_NAME) }
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.start(KEY, "biz'key", INSTANCE_NAME) }
        assertThrows<IllegalArgumentException> { flowDefinitionBiz.start(KEY, BIZ_KEY, "") }

        // 传不存在的流程定义key
        assertThrows<ObjectNotFoundException> { flowDefinitionBiz.start(NO_EXISTS, BIZ_KEY, INSTANCE_NAME) }

        // 成功启动
        var isEventFire = false
        val listener = object : IFlowEventListener {
            override fun onEvent(event: FlowEvent) {
                if (event.type == FlowEventType.ACTIVITY_STARTED) {
                    isEventFire = true
                }
            }
        }
        var variables = mapOf("applicantId" to APPLICANT_ID)
        val instance = flowDefinitionBiz.start(definition.key, BIZ_KEY, INSTANCE_NAME, variables, listener)
        assertNotNull(instance)
        assertNotNull(instance!!._id)
        assert(isEventFire)
        assertEquals(INSTANCE_NAME, instance.name)

        // 重复启动
        assertThrows<ObjectAlreadyExistsException> {
            flowDefinitionBiz.start(definition.key, BIZ_KEY, INSTANCE_NAME, variables, listener)
        }
    }


    companion object {

        internal val KEY = "leaveApplicationKey"
        internal val NAME = "请假流程"
        internal val CATEGORY = "test"
        internal val BIZ_KEY = "bizKey"
        internal val INSTANCE_NAME = "instanceName"
        internal val APPLICANT_ID = "applicantId"
        internal val DEPLOYMENT_NAME = "请假申请(junit)"

        internal fun createModel(): FlowDefinition {
            val svgXml = FileKit.readFileToString(File(PathKit.getResourcePath("bpmn/test-svg.xml")), "UTF-8")
            val flowJson = FileKit.readFileToString(File(PathKit.getResourcePath("bpmn/test-flow.json")), "UTF-8")
            val flowDefinitionBiz = SpringKit.getBean(IFlowDefinitionBiz::class)
            return flowDefinitionBiz.create(KEY, NAME, CATEGORY, flowJson, svgXml)
        }

        internal fun deployModel(): FlowDefinition {
            val flowDefinitionBiz = SpringKit.getBean(IFlowDefinitionBiz::class)
            return flowDefinitionBiz.deploy(KEY)
        }

        internal fun createThenDeploy(): FlowDefinition {
            createModel()
            return deployModel()
        }

        internal fun deployThenStart(variables: Map<String, *>? = mapOf("applicantId" to APPLICANT_ID)): FlowInstance {
            val flowDefinitionBiz = SpringKit.getBean(IFlowDefinitionBiz::class)
            createThenDeploy()
            return flowDefinitionBiz.start(KEY, BIZ_KEY, INSTANCE_NAME, variables)!!
        }

    }

}