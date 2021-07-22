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
            log.warn("找不到definitionKey：${definitionKey}对应的流程定义！")
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

    override fun getDefinitionsByDeploymentId(deploymentId: String): List<FlowDefinition> {
        val deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult()
        return if (deployment == null) {
            log.warn("找不到deploymentId：${deploymentId}对应的流程定义！")
            emptyList()
        } else {
            val definitions = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.id).list()
            definitions.map { FlowDefinition(deployment, it) }.toList()
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
                log.warn("definitionKey: ${definitionKey}对应的流程定义已经处于激活状态，忽略对其激活操作!")
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
            log.warn("definitionKey: ${definitionKey}对应的流程定义已经处于挂起状态，忽略对其挂起操作!")
        }
    }

    @Transactional
    override fun deleteDefinitions(deploymentId: String, cascade: Boolean) {
        try {
            repositoryService.deleteDeployment(deploymentId, cascade)
        } catch (e: ActivitiObjectNotFoundException) {
            log.error(e)
            throw IllegalArgumentException(e)
        }
    }

    override fun getFlowDiagram(definitionKey: String): InputStream? {
        val definitions = repositoryService.createProcessDefinitionQuery().processDefinitionKey(definitionKey).list()
        return if (definitions.isNotEmpty()) {
            if (definitions.size > 1) {
                log.warn("通过definitionKey：${definitionKey}, 找到多个流程定义！")
            }
            val definition = definitions.first()
            repositoryService.getResourceAsStream(definition.deploymentId, definition.diagramResourceName)
        } else {
            log.error("流程图获取失败！找不到definitionKey：${definitionKey}对应的流程定义！")
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
            val errMsg = "生成流程图失败！bpmn文件【${filePath}】不存在！"
            log.error(errMsg)
            throw FileNotFoundException(errMsg)
        }
    }

}