package org.kuark.base.support

import org.kuark.base.log.Log
import org.kuark.base.log.LogFactory
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

    constructor(vararg resourcesPaths: String?) {
        properties = loadProperties(*resourcesPaths as Array<out String>)
    }

    /**
     * 取出Property，但以System的Property优先.
     */
    private fun getValue(key: String): String {
        val systemProperty = System.getProperty(key)
        return systemProperty ?: properties.getProperty(key)
    }

    /**
     * 取出String类型的Property，但以System的Property优先,如果都為Null则抛出异常.
     */
    fun getProperty(key: String): String = getValue(key)

    /**
     * 取出String类型的Property，但以System的Property优先.如果都為Null則返回Default值.
     */
    fun getProperty(key: String, defaultValue: String): String = getValue(key)

    /**
     * 取出Integer类型的Property，但以System的Property优先.如果都為Null或内容错误则抛出异常.
     */
    fun getInteger(key: String): Int = Integer.valueOf(getValue(key))

    /**
     * 取出Integer类型的Property，但以System的Property优先.如果都為Null則返回Default值，如果内容错误则抛出异常
     */
    fun getInteger(key: String, defaultValue: Int): Int {
        val value = getValue(key)
        return if (value != null) Integer.valueOf(value) else defaultValue
    }

    /**
     * 取出Double类型的Property，但以System的Property优先.如果都為Null或内容错误则抛出异常.
     */
    fun getDouble(key: String): Double = java.lang.Double.valueOf(getValue(key))

    /**
     * 取出Double类型的Property，但以System的Property优先.如果都為Null則返回Default值，如果内容错误则抛出异常
     */
    fun getDouble(key: String, defaultValue: Double): Double {
        val value = getValue(key)
        return if (value != null) java.lang.Double.valueOf(value) else defaultValue
    }

    /**
     * 取出Boolean类型的Property，但以System的Property优先.如果都為Null抛出异常,如果内容不是true/false则返回false.
     */
    fun getBoolean(key: String): Boolean {
        val value = getValue(key)
        return java.lang.Boolean.valueOf(value)
    }

    /**
     * 取出Boolean类型的Property，但以System的Property优先.如果都為Null則返回Default值,如果内容不为true/false则返回false.
     */
    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        val value = getValue(key)
        return if (value != null) java.lang.Boolean.valueOf(value) else defaultValue
    }

    /**
     * 载入多个文件, 文件路径使用Spring Resource格式.
     */
    private fun loadProperties(vararg resourcesPaths: String): Properties {
        val props = Properties()
        for (location in resourcesPaths) {
            LOG.debug("Loading properties file from:$location")
            try {
                javaClass.getResourceAsStream(location).use {
                    props.load(it)
                }
            } catch (ex: IOException) {
                LOG.debug("Could not load properties from path:$location, ${ex.message}")
            }
        }
        return props
    }

    companion object {
        private val LOG: Log = LogFactory.getLog(PropertiesLoader::class)
    }
}