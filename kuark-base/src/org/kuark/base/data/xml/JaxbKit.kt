package org.kuark.base.data.xml

import org.apache.commons.lang3.StringUtils
import org.kuark.base.lang.ClassKit
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import java.io.StringReader
import java.io.StringWriter
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.xml.bind.*
import javax.xml.bind.annotation.XmlAnyElement
import javax.xml.namespace.QName
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParserFactory
import javax.xml.transform.Source
import javax.xml.transform.sax.SAXSource
import kotlin.reflect.KClass

/**
 * jaxb操作的工具类
 * JAXB = Java Architecture for XML Binding
 * 使用Jaxb2.0实现XML和Java的相互转化, OXM(Object XML Mapping), JAXB2在底层是用StAX(JSR
 * 173)来处理XML文档的
 *
 * @author K
 * @since 1.0.0
 */
object JaxbKit {

    private val jaxbContexts = ConcurrentHashMap<KClass<*>, JAXBContext>()

    /**
     * 序列化，将bean转为xml
     *
     * @param root 待序列化的根对象
     * @return 序列化后的xml字符串
     * @since 1.0.0
     */
    fun toXml(root: Any): String {
//        val clazz: Class<*> = ClassKit.getTargetClass(root)
        return toXml(root, root::class, null)
    }

    /**
     * 序列化，按指定编码将bean转为xml
     *
     * @param root 待序列化的根对象
     * @param encoding 编码名称
     * @return 序列化后的xml字符串
     * @since 1.0.0
     */
    fun toXml(root: Any, encoding: String): String {
//        val clazz: Class<*> = ClassKit.getTargetClass(root)
        return toXml(root, root::class, encoding)
    }

    /**
     * 序列化，按指定编码将bean转为xml
     *
     * @param root 待序列化的根对象
     * @param clazz 类
     * @param encoding 编码名称
     * @return 序列化后的xml字符串
     * @since 1.0.0
     */
    fun toXml(root: Any?, clazz: KClass<*>, encoding: String?): String {
        val writer = StringWriter()
        createMarshaller(clazz, encoding).marshal(root, writer)
        return writer.toString()
    }
    /**
     * 序列化，按指定编码将bean转为xml, 特别支持Root Element是Collection的情形.
     *
     * @param root 待序列化的根容器对象
     * @param rootName 根的名称
     * @param clazz 类
     * @param encoding 编码名称
     * @return 序列化后的xml字符串
     * @since 1.0.0
     */
    /**
     * 序列化，特别支持Root Element是Collection的情形.
     *
     * @param root 待序列化的根容器对象
     * @param rootName 根的名称
     * @param clazz 类
     * @return 序列化后的xml字符串
     * @since 1.0.0
     */
    @JvmOverloads
    fun toXml(
        root: Collection<*>?,
        rootName: String?,
        clazz: KClass<*>,
        encoding: String? = null
    ): String {
        val wrapper = CollectionWrapper()
        wrapper.collection = root
        val wrapperElement: JAXBElement<CollectionWrapper> = JAXBElement<CollectionWrapper>(
            QName(rootName),
            CollectionWrapper::class.java, wrapper
        )
        val writer = StringWriter()
        createMarshaller(clazz, encoding).marshal(wrapperElement, writer)
        return writer.toString()
    }

    /**
     *
     *
     * 反序列化，将xml转为指定类的实例
     *
     *
     * @param xml xml字符串
     * @param clazz 实例的类型
     * @return 指定类的实例
     * @since 1.0.0
     */
    fun <T: Any> fromXml(xml: String?, clazz: KClass<T>): Any? {
        val reader = StringReader(xml)
        return createUnmarshaller(clazz).unmarshal(reader)
    }

    /**
     *
     *
     * 反序列化，将xml转为指定类的实例，忽略命名空间
     *
     *
     * @param xml
     * @param clazz
     * @param <T>
     * @return
    </T> */
    fun <T: Any> fromXmlIgnoreNameSpace(xml: String, clazz: KClass<T>): Any? {
        val reader = StringReader(xml)
        val sax = SAXParserFactory.newInstance()
        sax.isNamespaceAware = false
        val xmlReader = sax.newSAXParser().xmlReader
        val source: Source = SAXSource(xmlReader, InputSource(reader))
        return createUnmarshaller(clazz).unmarshal(source)
    }

    /**
     *
     *
     * 创建Marshaller并设定encoding. 线程不安全，需要每次创建或pooling。
     *
     *
     * @param clazz 类
     * @param encoding 可为null
     * @return Marshaller
     * @since 1.0.0
     */
    fun createMarshaller(clazz: KClass<*>, encoding: String?): Marshaller {
        val jaxbContext: JAXBContext = getJaxbContext(clazz)
        val marshaller: Marshaller = jaxbContext.createMarshaller()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        if (StringUtils.isNotBlank(encoding)) {
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding)
        }
        return marshaller
    }

    /**
     *
     *
     * 创建UnMarshaller. 线程不安全，需要每次创建或pooling。
     *
     *
     * @param clazz 类
     * @return UnMarshaller
     * @since 1.0.0
     */
    fun createUnmarshaller(clazz: KClass<*>): Unmarshaller {
        val jaxbContext: JAXBContext = getJaxbContext(clazz)
        return jaxbContext.createUnmarshaller()
    }

    private fun getJaxbContext(clazz: KClass<*>): JAXBContext {
        var jaxbContext = JAXBContext.newInstance(clazz.java, CollectionWrapper::class.java)
        jaxbContexts.putIfAbsent(clazz, jaxbContext)
        return jaxbContext
    }

    /**
     * 封装Root Element 是 Collection的情况.
     */
    class CollectionWrapper {
        @XmlAnyElement
        var collection: Collection<*>? = null
    }
}