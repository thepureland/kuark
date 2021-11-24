package io.kuark.base.support

import io.kuark.base.log.LogFactory
import java.io.IOException
import java.util.*


/**
 * Properties文件载入工具类. 可载入多个properties文件, 相同的属性在最后载入的文件中的值将会覆盖之前的值，但以System的Property优先.
 *
 * @author K
 * @since 1.0.0
 */
class PropertiesLoader {

    val properties: Properties

    constructor(properties: Properties) {
        this.properties = properties
    }

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    constructor(vararg resourcesPaths: String?) {
        properties = loadProperties(*resourcesPaths as Array<out String>)
    }

    /**
     * 取出Property，但以System的Property优先.
     *
     * @param key Key
     * @return Value
     * @author K
     * @since 1.0.0
     */
    private fun getValue(key: String): String? = System.getProperty(key) ?: properties.getProperty(key)

    /**
     * 取出String类型的Property，但以System的Property优先,如果都為Null则抛出异常.
     *
     * @param key Key
     * @return Value
     * @author K
     * @since 1.0.0
     */
    fun getProperty(key: String): String? = getValue(key)

    /**
     * 取出String类型的Property，但以System的Property优先.如果都為Null則返回Default值.
     *
     * @param key Key
     * @param defaultValue 默认值
     * @return Value
     * @author K
     * @since 1.0.0
     */
    fun getProperty(key: String, defaultValue: String): String = getValue(key) ?: defaultValue

    /**
     * 取出Integer类型的Property，但以System的Property优先.如果都為Null或内容错误则抛出异常.
     *
     * @param key Key
     * @return Value
     * @author K
     * @since 1.0.0
     */
    fun getInt(key: String): Int? = getValue(key)?.toInt()

    /**
     * 取出Integer类型的Property，但以System的Property优先.如果都為Null則返回Default值，如果内容错误则抛出异常
     *
     * @param key Key
     * @param defaultValue 默认值
     * @return Value
     * @author K
     * @since 1.0.0
     */
    fun getInt(key: String, defaultValue: Int): Int = getValue(key)?.toInt() ?: defaultValue

    /**
     * 取出Double类型的Property，但以System的Property优先.如果都為Null或内容错误则抛出异常.
     *
     * @param key Key
     * @return Value
     * @author K
     * @since 1.0.0
     */
    fun getDouble(key: String): Double? = getValue(key)?.toDouble()

    /**
     * 取出Double类型的Property，但以System的Property优先.如果都為Null則返回Default值，如果内容错误则抛出异常
     *
     * @param key Key
     * @param defaultValue 默认值
     * @return Value
     * @author K
     * @since 1.0.0
     */
    fun getDouble(key: String, defaultValue: Double): Double = getValue(key)?.toDouble() ?: defaultValue

    /**
     * 取出Boolean类型的Property，但以System的Property优先.如果都為Null抛出异常,如果内容不是true/false则返回false.
     *
     * @param key Key
     * @return Value
     * @author K
     * @since 1.0.0
     */
    fun getBoolean(key: String): Boolean? = getValue(key)?.toBoolean()

    /**
     * 取出Boolean类型的Property，但以System的Property优先.如果都為Null則返回Default值,如果内容不为true/false则返回false.
     *
     * @param key Key
     * @param defaultValue 默认值
     * @return Value
     * @author K
     * @since 1.0.0
     */
    fun getBoolean(key: String, defaultValue: Boolean): Boolean = getValue(key)?.toBoolean() ?: defaultValue

    /**
     * 载入多个文件, 文件路径使用Spring Resource格式.
     */
    private fun loadProperties(vararg resourcesPaths: String): Properties {
        val props = Properties()
        for (location in resourcesPaths) {
            LOG.debug("Loading properties file from:$location")
            try {
                javaClass.getResourceAsStream(location).use { props.load(it) }
            } catch (ex: IOException) {
                LOG.debug("Could not load properties from path:$location, ${ex.message}")
            }
        }
        return props
    }

    companion object {
        private val LOG = LogFactory.getLog(PropertiesLoader::class)
    }

}