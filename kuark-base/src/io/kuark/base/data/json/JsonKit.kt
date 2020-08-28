package io.kuark.base.data.json

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.core.JsonParser.Feature
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.JSONPObject
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule
import io.kuark.base.log.LogFactory
import kotlin.reflect.KClass

/**
 * json工具类(基于jackson)
 * 注意事项：
 * 1.支持数据类和普通类,必须有空构造函数，要映射的属性必须是可读可写的(var)
 *
 * @author K
 * @since 1.0.0
 */
object JsonKit {

    private val LOG = LogFactory.getLog(JsonKit::class)

    /**
     * 将简单的Json串格式化成页面显示的字符串(去掉花括号、引号及最后面可能的逗号)
     *
     * @param simpleJsonStr 简单的Json串格式化(如：{"A":"b","B":'b'} ), 为空将返回空串
     * @return 页面显示的字符串(如：A:b, B:b)
     * @since 1.0.0
     */
    fun jsonToDisplay(simpleJsonStr: String): String {
        if (simpleJsonStr.isBlank()) {
            return ""
        }
        var displayStr = simpleJsonStr.replaceFirst("^\\{".toRegex(), "")
        displayStr = displayStr.replaceFirst("\\}$".toRegex(), "")
        displayStr = displayStr.replace("\"|'".toRegex(), "")
        displayStr = displayStr.replaceFirst(",$".toRegex(), "")
        return displayStr
    }

    /**
     * 反序列化, 将json串解析为指定Class的实例
     *
     * @param json json串
     * @param clazz Class
     * @param mapper json转换器，为null时该方法内部将新建一个默认的转换器
     * @return Class的实例，出错时返回null
     */
    fun <T : Any> fromJson(json: String, clazz: KClass<T>, mapper: ObjectMapper? = null): T? {
        try {
            return (mapper ?: createDefaultMapper()).readValue(json, clazz.java)
        } catch (e: Exception) {
            LOG.error(e, "json解析为对象出错！json: $json")
        }
        return null
    }

    /**
     * 反序列化, 将json串解析为TypeReference子类泛型参数变量指定的Class的实例
     *
     * @param json json串
     * @param typeReference TypeReference子类，用来指定泛型参数
     * @param mapper json转换器，为null时该方法内部将新建一个默认的转换器
     * @return TypeReference子类泛型参数变量指定的Class的实例，出错时返回null
     */
    fun <T> fromJson(json: String, typeReference: TypeReference<T>, mapper: ObjectMapper? = null): T? {
        try {
            return (mapper ?: createDefaultMapper()).readValue(json, typeReference)
        } catch (e: Exception) {
            LOG.error(e, "json解析为对象出错！json: $json")
        }
        return null
    }

    /**
     * 序列化，将对象转为json串
     *
     * @param obj 要序列化的对象，可以是一般对象，也可以是Collection或数组， 如果集合为空集合, 返回"[]"
     * @param mapper json转换器，为null时该方法内部将新建一个默认的转换器
     * @return 序列化后的json串
     * @since 1.0.0
     */
    fun toJson(obj: Any, mapper: ObjectMapper? = null): String =
        (mapper ?: createDefaultMapper()).writeValueAsString(obj)

    /**
     * 输出jsonP格式的数据
     *
     * @param functionName 函数名
     * @param obj 待序列化的对象，其json对象将作为函数的参数
     * @return jsonP字符串
     * @since 1.0.0
     */
    fun toJsonP(functionName: String, obj: Any): String = toJson(JSONPObject(functionName, obj))

    /**
     * 当json里含有bean的部分属性时，用json串中的值更新该bean的该部分属性
     *
     * @param jsonString json串
     * @param obj 待更新的bean
     * @param mapper json转换器，为null时该方法内部将新建一个默认的转换器
     * @return 更新后的bean，失败时返回null
     * @since 1.0.0
     */
    fun <T> updateBean(jsonString: String, obj: T, mapper: ObjectMapper? = null): T? {
        try {
            return (mapper ?: createDefaultMapper()).readerForUpdating(obj).readValue(jsonString)
        } catch (e: Exception) {
            LOG.error(e, "将json串:{0}更新到对象:{1}时出错.", jsonString, obj)
        }
        return null
    }

    /**
     * 创建指定Include枚举元素的json转换器
     *
     * @param include Include枚举元素
     * @return json转换器
     * @since 1.0.0
     */
    fun createMapper(include: Include): ObjectMapper {
        val mapper = createDefaultMapper()
        // 设置输出时包含属性的风格
        mapper.setSerializationInclusion(include)

        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        return mapper
    }

    /**
     * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的json转换器
     *
     * @return json转换器
     * @since 1.0.0
     */
    fun createNonEmptyMapper(): ObjectMapper {
        return createMapper(Include.NON_EMPTY)
    }

    /**
     * 创建只输出初始值被改变的属性到Json字符串的json转换器
     *
     * @return json转换器
     * @since 1.0.0
     */
    fun createNonDefaultMapper(): ObjectMapper {
        return createMapper(Include.NON_DEFAULT)
    }

    /**
     * 设定使用Enum的toString方法来读写Enum,
     * 注意本方法一定要在Mapper创建后, 所有的读写动作之前調用.
     *
     * @param mapper json转换器
     * @since 1.0.0
     */
    fun enableEnumUseToString(mapper: ObjectMapper) {
        mapper.run {
            enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
            enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
        }
    }

    /**
     * 支持使用Jaxb的Annotation，使得实体类上的annotation不用与Jackson耦合。
     * 默认会先查找jaxb的annotation，如果找不到再找jackson的。
     *
     * @param mapper json转换器
     * @since 1.0.0
     */
    fun enableJaxbAnnotation(mapper: ObjectMapper) = mapper.registerModule(JaxbAnnotationModule())

    /**
     * 允许单引号
     * 允许不带引号的字段名称
     *
     * @param mapper json转换器
     * @since 1.0.0
     */
    fun enableSimple(mapper: ObjectMapper) {
        mapper.run {
            configure(Feature.ALLOW_SINGLE_QUOTES, true)
            configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
        }
    }

    private fun createDefaultMapper(): ObjectMapper = ObjectMapper()

}