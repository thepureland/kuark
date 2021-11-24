package io.kuark.base.data.xml

import io.kuark.base.support.Consts
import org.apache.commons.lang3.StringUtils
import org.xml.sax.InputSource
import java.io.StringReader
import java.io.StringWriter
import java.util.concurrent.ConcurrentHashMap
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBElement
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller
import javax.xml.bind.annotation.XmlAnyElement
import javax.xml.namespace.QName
import javax.xml.parsers.SAXParserFactory
import javax.xml.transform.sax.SAXSource
import kotlin.reflect.KClass

/**
 * xml工具类(基于JAXB)
 * JAXB = Java Architecture for XML Binding
 * 使用Jaxb2.0实现XML和Java的相互转化, OXM(Object XML Mapping), JAXB2在底层是用StAX(JSR 173)来处理XML文档的。
 * 注意事项：
 * 1.支持数据类和普通类,必须有空构造函数，要映射的属性必须是可读可写的(var)
 *
 * @author K
 * @since 1.0.0
 */
object XmlKit {

    private val jaxbContexts = ConcurrentHashMap<KClass<*>, JAXBContext>()

    /**
     * 序列化(编组)，按指定编码将bean转为xml
     *
     * @param root 待序列化的根对象
     * @param encoding 编码名称,缺省为UTF-8
     * @return 序列化后的xml字符串
     * @author K
     * @since 1.0.0
     */
    fun toXml(root: Any, encoding: String = "UTF-8"): String {
        val writer = StringWriter()
        createMarshaller(root::class, encoding).marshal(root, writer)
        return writer.toString()
    }

    /**
     * 序列化(编组)，特别支持Root Element是Collection的情形.
     *
     * @param T 集合元素类型
     * @param root 待序列化的根容器对象
     * @param rootName 根的名称
     * @param clazz 类
     * @param encoding 编码名称,缺省为UTF-8
     * @return 序列化后的xml字符串
     * @author K
     * @since 1.0.0
     */
    fun <T: Any> toXml(root: Collection<T>, rootName: String, clazz: KClass<T>, encoding: String = "UTF-8"): String {
        val wrapper = CollectionWrapper(root)
        val wrapperElement = JAXBElement(QName(rootName), CollectionWrapper::class.java, wrapper)
        val writer = StringWriter()
        createMarshaller(clazz, encoding).marshal(wrapperElement, writer)
        return writer.toString()
    }

    /**
     * 反序列化(解组)，将xml转为指定类的实例
     *
     * @param T 目标类型
     * @param xml xml字符串
     * @param clazz 实例的类型
     * @param ignoreNameSpace 是否忽略命名空间
     * @return 指定类的实例
     * @author K
     * @since 1.0.0
     */
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    fun <T : Any> fromXml(xml: String, clazz: KClass<T>, ignoreNameSpace: Boolean = false): T {
        val reader = StringReader(xml)
        val sax = SAXParserFactory.newInstance()
        sax.isNamespaceAware = ignoreNameSpace
        val xmlReader = sax.newSAXParser().xmlReader
        val source = SAXSource(xmlReader, InputSource(reader))
        return createUnmarshaller(clazz).unmarshal(source) as T
    }

    /**
     * 创建Marshaller并设定encoding. 线程不安全，需要每次创建或pooling。
     *
     * @param clazz 类
     * @param encoding 编码名称,缺省为UTF-8
     * @return Marshaller
     * @author K
     * @since 1.0.0
     */
    private fun createMarshaller(clazz: KClass<*>, encoding: String = "UTF-8"): Marshaller {
        val jaxbContext: JAXBContext = getJaxbContext(clazz)
        val marshaller: Marshaller = jaxbContext.createMarshaller()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true) //格式化输出，即按标签自动换行，否则就是一行输出
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false); //是否省略xml头信息，默认不省略（false）
//        marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "")
        if (StringUtils.isNotBlank(encoding)) {
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding)
        }
        return marshaller
    }

    /**
     * 创建UnMarshaller. 线程不安全，需要每次创建或pooling。
     *
     * @param clazz 类
     * @return UnMarshaller
     * @author K
     * @since 1.0.0
     */
    private fun createUnmarshaller(clazz: KClass<*>): Unmarshaller = getJaxbContext(clazz).createUnmarshaller()

    private fun getJaxbContext(clazz: KClass<*>): JAXBContext {
        var jaxbContext = JAXBContext.newInstance(clazz.java, CollectionWrapper::class.java)
        jaxbContexts.putIfAbsent(clazz, jaxbContext)
        return jaxbContext
    }

    /**
     * 封装Root Element 是 Collection的情况.
     *
     * @author K
     * @since 1.0.0
     */
    class CollectionWrapper(
        @set:XmlAnyElement
        var item: Collection<*>
    )

}