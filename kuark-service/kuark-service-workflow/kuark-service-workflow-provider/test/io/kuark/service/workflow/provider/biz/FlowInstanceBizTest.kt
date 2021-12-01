package io.kuark.service.workflow.provider.biz

import io.kuark.service.workflow.common.vo.instance.FlowInstanceSearchParams
import io.kuark.service.workflow.provider.ibiz.IFlowInstanceBiz
import io.kuark.service.workflow.provider.ibiz.IFlowTaskBiz
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.image.ImageKit
import io.kuark.base.io.IoKit
import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.imageio.ImageIO


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal open class FlowInstanceBizTest : SpringTest() {


    @Autowired
    private lateinit var flowInstanceBiz: IFlowInstanceBiz

    @Autowired
    private lateinit var flowTaskBiz: IFlowTaskBiz


    private val BIZ_KEY = FlowDefinitionBizTest.BIZ_KEY
    private val INSTANCE_NAME = FlowDefinitionBizTest.INSTANCE_NAME
    private val NO_EXISTS = "no exists"

    @BeforeAll
    fun setUp() {
        //??? 不加的话，运行getFlowDiagram()时会报java.awt.HeadlessException异常！
        System.setProperty("java.awt.headless", "false")
    }


    @Test
    @Transactional
    open fun isExists() {
        val instance = FlowDefinitionBizTest.deployThenStart()

        assert(flowInstanceBiz.isExists(BIZ_KEY, instance.definitionKey))
        assert(flowInstanceBiz.isExists(BIZ_KEY, instance.definitionKey, 1))
        assert(flowInstanceBiz.isExists(BIZ_KEY, instance.definitionKey, 1))
        assert(flowInstanceBiz.isExists(BIZ_KEY, " "))
        assertFalse(flowInstanceBiz.isExists(BIZ_KEY, NO_EXISTS, 1))
        assertFalse { flowInstanceBiz.isExists("", instance.definitionKey) }
    }

    @Test
    @Transactional
    open fun get() {
        val instance = FlowDefinitionBizTest.deployThenStart()

        // 正常结果，未指定版本号
        var flowInstance = flowInstanceBiz.get(BIZ_KEY, instance.definitionKey)
        assertNotNull(instance)
        assertNotNull(flowInstance)
        assertEquals(instance._id, flowInstance!!._id)

        // 正常结果，指定版本号
        flowInstance = flowInstanceBiz.get(BIZ_KEY, instance.definitionKey, 1)
        assertNotNull(instance)
        assertNotNull(flowInstance)
        assertEquals(instance._id, flowInstance!!._id)

        // 传不存在的key
        assertNull(flowInstanceBiz.get(BIZ_KEY, NO_EXISTS))
    }

    @Test
    @Transactional
    open fun search() {
        val instance = FlowDefinitionBizTest.deployThenStart()

        // 正常结果
        val builder = FlowInstanceSearchParams.Builder().definitionKey(instance.definitionKey).startTimeTo(Date())
        assert(flowInstanceBiz.search(builder.build()).isNotEmpty())
        assert(flowInstanceBiz.search(builder.bizKey(BIZ_KEY).build()).isNotEmpty())
        var criteria = builder.bizKey(BIZ_KEY).name(INSTANCE_NAME).build()
        assert(flowInstanceBiz.search(criteria).isNotEmpty())
        assert(flowInstanceBiz.search(criteria, 1, 10).isNotEmpty())
        criteria = builder.definitionKey("eaveAppl").bizKey("bizKe").name("INSTANCE").build()
        assert(flowInstanceBiz.search(criteria).isNotEmpty())

        // 传不存在的流程定义key
        criteria = builder.definitionKey(NO_EXISTS).build()
        assert(flowInstanceBiz.search(criteria).isEmpty())
    }

    @Test
    @Transactional
    open fun activate() {
        val instance = FlowDefinitionBizTest.deployThenStart()

        // 找不到流程实例
        assertThrows<ObjectNotFoundException> { flowInstanceBiz.activate(BIZ_KEY, NO_EXISTS) }

        // 成功激活
        flowInstanceBiz.activate(BIZ_KEY, instance.definitionKey)
        var flowInstance = flowInstanceBiz.get(BIZ_KEY, instance.definitionKey)!!
        assertFalse(flowInstance.suspend)

        // 重复激活
        flowInstanceBiz.activate(BIZ_KEY, instance.definitionKey)
        flowInstance = flowInstanceBiz.get(BIZ_KEY, instance.definitionKey)!!
        assertFalse(flowInstance.suspend)
    }

    @Test
    @Transactional
    open fun suspend() {
        val instance = FlowDefinitionBizTest.deployThenStart()

        // 找不到流程实例
        assertThrows<ObjectNotFoundException> { flowInstanceBiz.suspend(NO_EXISTS, instance.definitionKey) }

        // 成功挂起
        flowInstanceBiz.suspend(BIZ_KEY, instance.definitionKey)
        var flowInstance = flowInstanceBiz.get(BIZ_KEY, instance.definitionKey)!!
        assert(flowInstance.suspend)

        // 重复挂起
        flowInstanceBiz.suspend(BIZ_KEY, instance.definitionKey)
        flowInstance = flowInstanceBiz.get(BIZ_KEY, instance.definitionKey)!!
        assert(flowInstance.suspend)
    }

    @Test
    @Transactional
    open fun delete() {
        val instance = FlowDefinitionBizTest.deployThenStart()

        // 找不到流程实例
        assertThrows<ObjectNotFoundException> {
            flowInstanceBiz.delete(NO_EXISTS, instance.definitionKey, "test")
        }

        // 成功删除
        flowInstanceBiz.delete(BIZ_KEY, instance.definitionKey, "test")
        assertNull(flowInstanceBiz.get(BIZ_KEY, instance.definitionKey))
        val criteria = FlowInstanceSearchParams.Builder().definitionKey(instance.definitionKey).build()
        assert(flowInstanceBiz.search(criteria).isEmpty())
    }

    @Test
    @Transactional
    open fun updateBizKey() {
        val instance = FlowDefinitionBizTest.deployThenStart()
        val newBizKey = "new biz key"

        // 参数非法
        assertThrows<IllegalArgumentException> {
            flowInstanceBiz.updateBizKey(BIZ_KEY, "biz'key", instance.definitionKey)
        }

        // 流程实例找不到
        assertThrows<ObjectNotFoundException> {
            flowInstanceBiz.updateBizKey(NO_EXISTS, newBizKey, instance.definitionKey)
        }

        // 更新成功
        flowInstanceBiz.updateBizKey(BIZ_KEY, newBizKey, instance.definitionKey)
        assertNotNull(flowInstanceBiz.get(newBizKey, instance.definitionKey))
    }

    @Test
    @Transactional
    open fun getHighLightFlowDiagram() {
        val instance = FlowDefinitionBizTest.deployThenStart(
            mapOf(
                "applicantId" to FlowDefinitionBizTest.APPLICANT_ID,
                "deptManagerId" to FlowTaskBizTest.DEPT_MANAGER_ID
            )
        )

        flowTaskBiz.complete(
            instance.bizKey,
            FlowTaskBizTest.APPLICANT_TASK_DEFINITION_KEY,
            FlowTaskBizTest.APPLICANT_ID
        )
        val input = flowInstanceBiz.genHighLightFlowDiagram(instance.bizKey)!!
        val svgXml = input.use {
            IoKit.toString(input)
        }
        val bufferedImage = ImageKit.renderSvgToImage(svgXml, 600, 400)
        ImageKit.showImage(bufferedImage)
        Thread.sleep(3000)
    }

    @Test
    fun test() {
        val imageReadersByFormatName = ImageIO.getImageReadersByFormatName("png")
        val imageReadersBySuffix = ImageIO.getImageReadersBySuffix("png")
        println("")
    }

}