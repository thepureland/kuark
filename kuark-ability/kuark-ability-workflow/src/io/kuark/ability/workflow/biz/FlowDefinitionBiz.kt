package io.kuark.ability.workflow.biz

import io.kuark.ability.workflow.ibiz.IFlowDefinitionBiz
import io.kuark.ability.workflow.vo.FlowDefinition
import io.kuark.base.lang.string.appendIfMissing
import io.kuark.base.log.LogFactory
import org.activiti.bpmn.converter.BpmnXMLConverter
import org.activiti.engine.ActivitiException
import org.activiti.engine.ActivitiIllegalArgumentException
import org.activiti.engine.ActivitiObjectNotFoundException
import org.activiti.engine.RepositoryService
import org.activiti.image.impl.DefaultProcessDiagramGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.lang.IllegalArgumentException
import java.util.zip.ZipInputStream
import javax.xml.stream.XMLInputFactory

/**
 * 流程定义相关业务
 *
 * @author K
 * @since 1.0.0
 */
open class FlowDefinitionBiz : IFlowDefinitionBiz {

    @Autowired
    private lateinit var repositoryService: RepositoryService

    private val log = LogFactory.getLog(this::class)

    @Transactional
    override fun deployWithBpmn(
        deploymentName: String, bpmnFileName: String, diagramFileName: String?, prefixPath: String
    ): FlowDefinition {
        val bpmnFilePath = "${prefixPath}/${bpmnFileName}".appendIfMissing(".bpmn")
        val deploymentBuilder = repositoryService.createDeployment().name(deploymentName)
        try {
            deploymentBuilder.addClasspathResource(bpmnFilePath)
            if (diagramFileName != null && diagramFileName.isNotEmpty()) {
                val diagramFilePath = "${prefixPath}/${diagramFileName}".appendIfMissing(".png")
                deploymentBuilder.addClasspathResource(diagramFilePath)
            }
        } catch (e: ActivitiIllegalArgumentException) {
            throw FileNotFoundException(e.message)
        }
        val deployment = deploymentBuilder.deploy()
        val definition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.id).singleResult()
        return FlowDefinition(deployment, definition)
    }

    @Transactional
    override fun deployWithZip(deploymentName: String, zipFileName: String, prefixPath: String): List<FlowDefinition> {
        val zipPath = "${prefixPath}/${zipFileName}".appendIfMissing(".zip")
        val zipFile = File(zipPath)
        val inputStream = FileInputStream(zipFile)
        val zipInputStream = ZipInputStream(inputStream)
        val deployment = zipInputStream.use {
            repositoryService.createDeployment().name(deploymentName).addZipInputStream(it).deploy()
        }
        val definitions = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.id).list()
        return definitions.map { FlowDefinition(deployment, it) }.toList()
    }

    override fun getDefinitionByKey(definitionKey: String): List<FlowDefinition> {
        val definitions = repositoryService.createProcessDefinitionQuery().processDefinitionKey(definitionKey).list()
        return if (definitions.isEmpty()) {
            log.warn("找不到流程定义！definitionKey：$definitionKey")
            emptyList()
        } else {
            val results = mutableListOf<FlowDefinition>()
            definitions.forEach {
                val deployment = repositoryService.createDeploymentQuery().deploymentId(it.deploymentId).singleResult()
                results.add(FlowDefinition(deployment, it))
            }
            results
        }
    }

    @Transactional
    override fun activateDefinition(definitionKey: String) {
        try {
            repositoryService.activateProcessDefinitionByKey(definitionKey)
        } catch (e: ActivitiObjectNotFoundException) {
            log.error(e)
            throw IllegalArgumentException(e)
        } catch (e: ActivitiException) {
            if (e.message!!.contains("already in state")) {
                log.warn("忽略流程定义激活操作，因其已处于激活状态！definitionKey: $definitionKey")
            }
        }
    }

    @Transactional
    override fun suspendDefinition(definitionKey: String) {
        try {
            repositoryService.suspendProcessDefinitionByKey(definitionKey)
        } catch (e: ActivitiObjectNotFoundException) {
            log.error(e)
            throw IllegalArgumentException(e)
        } catch (e: ActivitiException) {
            log.warn("忽略流程定义挂起操作，因其已处于挂起状态！definitionKey: $definitionKey")
        }
    }

    @Transactional
    override fun deleteDefinitions(definitionKey: String, cascade: Boolean) {
        val definitions = getDefinitionByKey(definitionKey)
        if (definitions.isEmpty()) {
            val errMsg = "找不到流程定义！definitionKey：$definitionKey"
            log.error(errMsg)
            throw IllegalArgumentException(errMsg)
        }
        try {
            definitions.forEach {
                repositoryService.deleteDeployment(it._deploymentId, cascade)
            }
        } catch (e: ActivitiObjectNotFoundException) {
            log.error(e)
            throw IllegalArgumentException(e)
        }
    }

    override fun getFlowDiagram(definitionKey: String): InputStream? {
        val definitions = repositoryService.createProcessDefinitionQuery().processDefinitionKey(definitionKey).list()
        return if (definitions.isNotEmpty()) {
            if (definitions.size > 1) {
                log.warn("找到多个流程定义！definitionKey：$definitionKey")
            }
            val definition = definitions.first()
            repositoryService.getResourceAsStream(definition.deploymentId, definition.diagramResourceName)
        } else {
            log.error("流程图获取失败，因找不到流程定义！definitionKey：$definitionKey")
            null
        }
    }

    override fun genFlowDiagram(bpmnFileName: String, prefixPath: String): InputStream {
        val filePath = "${prefixPath}/${bpmnFileName}".appendIfMissing(".bpmn")
        val bpmnFile = File(filePath)
        return if (bpmnFile.exists()) {
            val inputStream = FileInputStream(bpmnFile)
            val bpmnModel = inputStream.use {
                val streamReader = XMLInputFactory.newInstance().createXMLStreamReader(it)
                BpmnXMLConverter().convertToBpmnModel(streamReader)
            }
            // generateDiagram(bpmnModel, "png",  "宋体", "微软雅黑", "黑体", null, 2.0)
            DefaultProcessDiagramGenerator().generateDiagram(bpmnModel, emptyList())
        } else {
            val errMsg = "生成流程图失败，因bpmn文件【${filePath}】不存在！"
            log.error(errMsg)
            throw FileNotFoundException(errMsg)
        }
    }

}