package io.kuark.ability.workflow.model

import io.kuark.ability.workflow.definition.FlowDefinition
import io.kuark.base.error.ObjectAlreadyExistsException
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.image.ImageKit
import io.kuark.base.io.FileKit
import io.kuark.base.io.PathKit
import io.kuark.context.spring.SpringKit
import io.kuark.test.common.SpringTest
import org.activiti.engine.RepositoryService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.io.File
import javax.imageio.ImageIO

internal open class FlowModelBizTest : SpringTest() {

    @Autowired
    private lateinit var repositoryService: RepositoryService

    @Autowired
    private lateinit var flowModelBiz: IFlowModelBiz

    private val DEPLOYMENT_NAME = "请假申请(junit)"
    private val NO_EXISTS = "no exists"

    @Test
    @Transactional
    open fun isExists() {
        createModel()

        assert(flowModelBiz.isExists(KEY, 1))
        assert(flowModelBiz.isExists(KEY))
        assertFalse(flowModelBiz.isExists(KEY, 2))
        assertFalse(flowModelBiz.isExists(NO_EXISTS, 1))
        assertThrows<IllegalArgumentException> { flowModelBiz.isExists(" ") }
    }

    @Test
    @Transactional
    open fun get() {
        createModel()

        assertNotNull(flowModelBiz.get(KEY, 1))
        assertNull(flowModelBiz.get(KEY, 2))
        assertNull(flowModelBiz.get(NO_EXISTS, 1))
        assertEquals(1, flowModelBiz.get(KEY)!!.version)
        assertThrows<IllegalArgumentException> { flowModelBiz.get(" ") }
    }

    @Test
    @Transactional
    open fun search() {
        val model = createModel()

        // 指定条件模糊搜索
        var criteria = FlowModelCriteria.Builder().key("leaveApp").name("请假").build()
        var models = flowModelBiz.search(criteria, 1, 10)
        assertEquals(1, models.size)

        deployModel()
        flowModelBiz.update(model.key, null, "newName", CATEGORY, null, null)

        // 更新已部署的模型后
        criteria = FlowModelCriteria.Builder().key("leaveApp").latestOnly(false).build()
        models = flowModelBiz.search(criteria, 1, 10)
        assertEquals(2, models.size)
        criteria = FlowModelCriteria.Builder().key("leaveApp").latestOnly(true).build()
        models = flowModelBiz.search(criteria, 1, -1)
        assertEquals(1, models.size)
        assertEquals(2, models.first().version)
    }

    @Test
    @Transactional
    open fun create() {
        // 正常新增
        val svgXml = FileKit.readFileToString(File(PathKit.getResourcePath("bpmn/test-svg.xml")))
        val flowJson = FileKit.readFileToString(File(PathKit.getResourcePath("bpmn/test-flow.json")))
        val definition = flowModelBiz.create(KEY, NAME, CATEGORY, flowJson, svgXml)
        assertNotNull(definition._id)

        // 参数非法
        assertThrows<IllegalArgumentException> { flowModelBiz.create("", NAME, CATEGORY, flowJson, svgXml) }
        assertThrows<IllegalArgumentException> {
            flowModelBiz.create(KEY, "", CATEGORY, flowJson, svgXml)
        }
        assertThrows<IllegalArgumentException> {
            flowModelBiz.create(KEY, NAME, CATEGORY, flowJson, " ")
        }
        assertThrows<IllegalArgumentException> { flowModelBiz.create(KEY, NAME, CATEGORY, "", svgXml) }

        // 重复新增
        assertThrows<ObjectAlreadyExistsException> {
            flowModelBiz.create(KEY, NAME, CATEGORY, flowJson, svgXml)
        }
    }

    @Test
    @Transactional
    open fun update() {
        val definition = createModel()

        // 更新未部署的模型
        val newName = "newFlowName"
        val newSvgXml = FileKit.readFileToString(File(PathKit.getResourcePath("bpmn/test-svg.xml")))
        val newFlowJson = FileKit.readFileToString(File(PathKit.getResourcePath("bpmn/test-flow.json")))
        flowModelBiz.update(definition.key, null, newName, CATEGORY, newSvgXml, newFlowJson)
        val newDefinition = flowModelBiz.get(definition.key)!!
        assertEquals(1, newDefinition.version)

        deployModel()

        // 更新已部署的模型
        flowModelBiz.update(definition.key, null, newName, CATEGORY, newSvgXml, newFlowJson)
        val criteria = FlowModelCriteria.Builder().key(definition.key).latestOnly(false).build()
        val models = flowModelBiz.search(criteria)
        assertEquals(2, models.size)
    }

    @Test
    @Transactional
    open fun deploy() {
        var model = createModel()

        // 非法参数
        assertThrows<IllegalArgumentException> { flowModelBiz.deploy("") }

        // 找不到模型
        assertThrows<ObjectNotFoundException> { flowModelBiz.deploy(NO_EXISTS) }

        // 成功部署
        var definition = flowModelBiz.deploy(model.key)
        assertNotNull(definition._modelId)
        assertNotNull(flowModelBiz.get(definition.key))

        // 重复部署
        assertThrows<IllegalStateException> { flowModelBiz.deploy(model.key) }
    }

    @Test
    @Transactional
    open fun deployWithBpmn() {
        assertThrows<ObjectNotFoundException> {
            flowModelBiz.deployWithBpmn(DEPLOYMENT_NAME, NO_EXISTS, "test")
        }
        assertThrows<ObjectNotFoundException> {
            flowModelBiz.deployWithBpmn(DEPLOYMENT_NAME, CATEGORY, NO_EXISTS)
        }
    }

    @Test
    @Transactional
    open fun deployWithZip() {
        val definitions = flowModelBiz.deployWithZip(DEPLOYMENT_NAME, "bpmn")
        assertEquals(2, definitions.size)
        definitions.forEach {
            var d = repositoryService.createProcessDefinitionQuery().processDefinitionKey(it.key).singleResult()
            assert(d.diagramResourceName in arrayOf("test11.png", "test22.png"))
            d = repositoryService.createProcessDefinitionQuery().processDefinitionKey(it.key).singleResult()
            assert(d.diagramResourceName in arrayOf("test11.png", "test22.png"))
        }

        // 文件不存在
        assertThrows<ObjectNotFoundException> { flowModelBiz.deployWithZip(DEPLOYMENT_NAME, "no exists") }
    }

    @Test
    @Transactional
    open fun getFlowDiagram() {
        System.setProperty("java.awt.headless", "false") //??? 不加会报java.awt.HeadlessException异常
        createModel()
        val definition = deployModel()

        val inputStream = flowModelBiz.getFlowDiagram(definition.key)
        val bufferedImage = ImageIO.read(inputStream)
        ImageKit.showImage(bufferedImage)
        Thread.sleep(2000)
    }

    @Test
    open fun genFlowDiagram() {
        val inputStream = flowModelBiz.genFlowDiagram("test")
        val file = File("${PathKit.getUserDirectory()}/flow.png")
        inputStream.use {
            FileKit.copyInputStreamToFile(it!!, file)
        }

//        val bufferedImage = ImageIO.read(inputStream) //??? bufferedImage会为null
//        ImageKit.showImage(bufferedImage)
//        Thread.sleep(2000)

        assertThrows<ObjectNotFoundException> { flowModelBiz.genFlowDiagram(NO_EXISTS) }
    }

    companion object {

        internal val KEY = "leaveApplicationKey"
        internal val NAME = "请假流程"
        internal val CATEGORY = ""

        internal fun createModel(): FlowModel {
            val svgXml = FileKit.readFileToString(File(PathKit.getResourcePath("bpmn/test-svg.xml")), "UTF-8")
            val flowJson = FileKit.readFileToString(File(PathKit.getResourcePath("bpmn/test-flow.json")), "UTF-8")
            val flowModelBiz = SpringKit.getBean(IFlowModelBiz::class)
            return flowModelBiz.create(KEY, NAME, CATEGORY, flowJson, svgXml)
        }

        internal fun deployModel(): FlowDefinition {
            val flowModelBiz = SpringKit.getBean(IFlowModelBiz::class)
            return flowModelBiz.deploy(KEY)
        }

    }

}