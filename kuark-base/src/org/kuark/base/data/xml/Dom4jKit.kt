package org.kuark.base.data.xml

import org.dom4j.Document
import org.dom4j.DocumentException
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.dom4j.io.OutputFormat
import org.dom4j.io.SAXReader
import org.dom4j.io.XMLWriter
import org.kuark.base.log.LogFactory
import java.io.*

/**
 * DOM4J操作工具类
 *
 * @author K
 * @since 1.0.0
 */
object Dom4jKit {

    private val LOG = LogFactory.getLog(Dom4jKit::class)

    /**
     * 从输入流读取xml文档，读完后输入流将被关闭
     *
     * @param in 输入流，为null将返回null
     * @return Document对象，参数为null将返回null
     * @since 1.0.0
     */
    fun readXml(`in`: InputStream): Document {
        `in`.use {
            val saxReader = SAXReader()
            return saxReader.read(`in`)
        }
    }

    /**
     * 读取指定路径的xml文档
     *
     *
     * @param path 文件路径，为null或找不到将返回null
     * @return Document对象，若找不到文件路径或路径为null将返回null
     * @since 1.0.0
     */
    fun readXml(path: String): Document? {
        var document: Document? = null
        if (path.isNotBlank()) {
            val file = File(path)
            if (file.exists()) {
                try {
                    val saxReader = SAXReader()
                    document = saxReader.read(File(path))
                } catch (e: DocumentException) {
                    LOG.error(e, "读取xml文件[{0}]失败：{1}", path, e.message)
                }
            } else {
                LOG.error("调用Dom4jTool.readXml方法时传入的path参数对应的文件路径找不到：$path")
            }
        } else {
            LOG.error("调用Dom4jTool.readXml方法时传入的path参数为空！")
        }
        return document
    }

    /**
     * 将Document对象保存为一个xml文件到本地,默认UTF-8编码，Xml格式为压缩格式
     *
     *
     * @param document 需要保存的document对象，为null将什么也不做
     * @param path 文件路径，为null或找不到将什么也不做
     * @since 1.0.0
     */
    fun writeXml(document: Document?, path: String?) {
        writeXml(document, path, "UTF-8", OutputFormat.createCompactFormat())
    }

    /**
     * 将Document对象保存为一个xml文件到本地
     *
     *
     * @param document 需要保存的document对象，为null将什么也不做
     * @param path 文件路径，为null或找不到将什么也不做
     * @param encoding 编码，为空将什么也不做
     * @param format xml文档格式，为null将什么也不做
     * @since 1.0.0
     */
    fun writeXml(document: Document?, path: String?, encoding: String?, format: OutputFormat?) {
        if (document == null) {
            LOG.error("调用Dom4jTool.writeXml方法时传入的document参数为null！")
            return
        }
        if (path == null || path.isBlank()) {
            LOG.error("调用Dom4jTool.writeXml方法时传入的path参数为空！")
            return
        }
        if (encoding == null || encoding.isBlank()) {
            LOG.error("调用Dom4jTool.writeXml方法时传入的encoding参数为空！")
            return
        }
        if (format == null) {
            LOG.error("调用Dom4jTool.writeXml方法时传入的format参数为null！")
            return
        }
        val file = File(path)
        if (!file.exists()) {
            LOG.error("调用Dom4jTool.writeXml方法时传入的path参数对应的文件路径找不到：$path")
            return
        }
        try {
            format.encoding = encoding
            val writer = XMLWriter(FileWriter(file), format)
            writer.write(document)
            writer.close()
        } catch (e: IOException) {
            LOG.error(e, "往[$path]写入xml失败！")
        }
    }

    /**
     * 将xml格式的字符串保存为一个xml文件到本地,默认UTF-8编码，Xml格式为压缩格式
     *
     *
     * @param xmlStr xml格式的字符串，为空将什么也不做
     * @param path 文件路径，为null或找不到将什么也不做
     * @since 1.0.0
     */
    fun writeXml(xmlStr: String, path: String?) {
        if (xmlStr.isBlank()) {
            LOG.error("调用Dom4jTool.writeXml方法时传入的xmlStr参数为空！")
            return
        }
        val document: Document? = stringToDocument(xmlStr)
        document ?: writeXml(document, path, "UTF-8", OutputFormat.createCompactFormat())
    }

    /**
     * 将xml格式的字符串保存为一个xml文件到本地
     *
     *
     * @param xmlStr xml格式的字符串，为空将什么也不做
     * @param path 文件路径，为null或找不到将什么也不做
     * @param encoding 编码，为空将什么也不做
     * @param format xml文档格式，为null将什么也不做
     * @since 1.0.0
     */
    fun writeXml(xmlStr: String, path: String?, encoding: String?, format: OutputFormat?) {
        if (xmlStr.isBlank()) {
            LOG.error("调用Dom4jTool.writeXml方法时传入的xmlStr参数为空！")
            return
        }
        val document = stringToDocument(xmlStr)
        document ?: writeXml(document, path, encoding, format)
    }

    /**
     * 将xml格式的字符串转为Document对象
     *
     *
     * @param xmlStr xml格式的字符串，为空将返回null
     * @return Document对象，为参数为空或不能解析为xml都将返回null
     * @since 1.0.0
     */
    fun stringToDocument(xmlStr: String): Document? {
        var doc: Document? = null
        if (xmlStr.isNotBlank()) {
            try {
                doc = DocumentHelper.parseText(xmlStr)
            } catch (e: DocumentException) {
                LOG.error(e, "不能将指定的字符串解析为xml：\n$xmlStr")
            }
        } else {
            LOG.error("调用Dom4jTool.string2Document方法时传入的xmlStr参数为空！")
        }
        return doc
    }

    /**
     * 获得节点的属性值,如果为空,则返回默认值.
     *
     *
     * @param node 节点，为null将返回null
     * @param attributeName 属性名 为空将返回null
     * @param defaultValue 默认值，可以为null
     * @return 指定属性名的值
     * @since 1.0.0
     */
    fun getAttributeValue(node: Element?, attributeName: String?, defaultValue: String?): String? {
        if (node == null) {
            LOG.error("调用Dom4jTool.getAttributeValue方法时传入的node参数为null！")
            return null
        }
        if (attributeName == null || attributeName.isBlank()) {
            LOG.error("调用Dom4jTool.getAttributeValue方法时传入的attributeName参数为空！")
            return null
        }
        val result: String = node.attributeValue(attributeName)
        return if (result.isNotBlank()) {
            result
        } else defaultValue
    }

    /**
     * 获得节点的属性值,并以整型的类型输出.为空返回defaultValue指定的默认值。
     *
     *
     * @param node 节点，为null将返回null
     * @param attributeName 属性名 为空将返回null
     * @param defaultValue 默认值
     * @return 指定属性名的整型值，如果node或attributeName为空，或属性值不是整型值都将返回null
     * @since 1.0.0
     */
    fun getIntegerAttribute(node: Element?, attributeName: String, defaultValue: Int): Int? {
        if (node == null) {
            LOG.error("调用Dom4jTool.getIntegerAttribute方法时传入的node参数为null！")
            return null
        }
        if (attributeName.isBlank()) {
            LOG.error("调用Dom4jTool.getIntegerAttribute方法时传入的attributeName参数为空！")
            return null
        }
        val strValue: String = node.attributeValue(attributeName)
        return if (strValue.isNotBlank()) {
            val value: Int = try {
                strValue.toInt()
            } catch (e: NumberFormatException) {
                LOG.error(
                    "XML中的[" + node.getPath().toString() + "]节点的[" + attributeName + "]属性值不是整型类型."
                )
                return null
            }
            value
        } else {
            defaultValue
        }
    }

    /**
     * 获得节点的属性值,并以布尔类型输出.为空返回defaultValue指定的默认值。
     *
     *
     * @param node 节点，为null将返回null
     * @param attributeName 属性名 为空将返回null
     * @param defaultValue 默认值
     * @return 指定属性名的布尔值，如果node或attributeName为空，或属性值不是布尔值都将返回null
     * @return
     * @since 1.0.0
     */
    fun getBooleanAttribute(node: Element?, attributeName: String, defaultValue: Boolean): Boolean? {
        if (node == null) {
            LOG.error("调用Dom4jTool.getBooleanAttribute方法时传入的node参数为null！")
            return null
        }
        if (attributeName.isBlank()) {
            LOG.error("调用Dom4jTool.getBooleanAttribute方法时传入的attributeName参数为空！")
            return null
        }
        val strValue: String = node.attributeValue(attributeName)
        return if (strValue.isNotBlank()) {
            if ("false" == strValue) {
                false
            } else if ("true" == strValue) {
                true
            } else {
                LOG.error(
                    "XML中的[" + node.getPath()
                        .toString() + "]节点的[" + attributeName + "]属性值必须为false或者true."
                )
                null
            }
        } else {
            defaultValue
        }
    }

    /**
     * 将xml文档转化成xml字符串(默认UTF-8)
     *
     *
     * @param document 待转化的Document文档，为null将返回null
     * @return Document文档的xml字符串表示, document参数为null将返回null
     * @since 1.0.0
     */
    fun asXml(document: Document?): String? {
        if (document == null) {
            LOG.error("调用Dom4jTool.asXml方法时传入的document参数为null！")
            return null
        }
        val root: Element = document.getRootElement()
        return asXml(root)
    }

    /**
     * 将xml文档元素转化成xml字符串(默认UTF-8)
     *
     *
     * @param elem 待转化的Document文档Element对象，为null将返回null
     * @return Element对象的xml字符串表示, elem参数为null将返回null
     * @since 1.0.0
     */
    fun asXml(elem: Element?): String? {
        return asXml(elem, "UTF-8")
    }

    /**
     * 将xml文档元素转化成指定编码的xml字符串
     *
     *
     * @param elem 待转化的Document文档Element对象，为null将返回null
     * @param charset 编码，为空将当作UTF-8
     * @return Element对象的xml字符串表示, elem参数为null将返回null
     * @since 1.0.0
     */
    fun asXml(elem: Element?, charset: String): String? {
        var charset = charset
        if (elem == null) {
            LOG.error("调用Dom4jTool.asXml方法时传入的elem参数为null！")
            return null
        }
        if (charset.isBlank()) {
            charset = "UTF-8"
        }
        return """<?xml version="1.0" encoding="$charset"?>
            ${elementToString(elem)}"""
    }

    /**
     * 将Element元素转化为xml字符串
     *
     *
     * @param elem 待转化的Document文档Element对象，为null将返回null
     * @return Element对象的xml字符串表示, elem参数为null或转化失败都将返回null
     * @since 1.0.0
     */
    fun elementToString(elem: Element?): String? {
        if (elem == null) {
            LOG.error("调用Dom4jTool.elementToString方法时传入的elem参数为null！")
            return null
        }
        val out = StringWriter()
        val format: OutputFormat = OutputFormat.createPrettyPrint()
        val writer = XMLWriter(out, format)
        try {
            writer.write(elem)
        } catch (e: IOException) {
            LOG.error(e, "将Element元素转化为xml字符串出错！")
            return null
        } finally {
            try {
                writer.close()
            } catch (e: IOException) {
                LOG.error(e, "关闭XMLWriter出错！")
            }
        }
        return out.toString()
    }

}