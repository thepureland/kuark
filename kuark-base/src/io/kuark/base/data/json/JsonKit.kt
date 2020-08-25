package io.kuark.base.data.json

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.core.JsonParser.Feature
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.JSONPObject
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule
import io.kuark.base.log.LogFactory
import org.apache.commons.lang3.StringUtils
import java.io.File
import java.io.IOException
import kotlin.reflect.KClass

/**
 * json工具类，底层库使用的是fasterxml
 *
 * @author K
 * @since 1.0.0
 */
object JsonKit {

    private val LOG = LogFactory.getLog(JsonKit::class)

    /**
     * 将简单的Json串格式化成页面显示的字符串(去掉花括号、引号及最后面可能的逗号)
     *
     * @param simpleJsonStr 简单的Json串格式化(如：{"A":"b","B":'b'} ), 为null或空将返回空串
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
     * @param json json串, 为null或空将返回null, 为"[]", 返回空集合
     * @param clazz Class
     * @return Class的实例，出错时返回null
     * @see .fromJson
     */
    fun <T : Any> fromJson(json: String, clazz: KClass<T>): T? = fromJson(json, clazz, null)

    /**
     * 反序列化, 将json串解析为指定Class的实例
     *
     * @param json json串, 为null或空将返回null, 为"[]", 返回空集合
     * @param typeReference Class
     * @return Class的实例，出错时返回null
     * @see .fromJson
     */
    fun <T> fromJson(json: String, typeReference: TypeReference<T>?): T? = fromJson(json, typeReference, null)

    /**
     * 反序列化, 将json串解析为指定Class的实例
     *
     * @param json json串, 为null或空将返回null, 为"[]", 返回空集合
     * @param clazz Class
     * @param mapper json转换器，为null时该方法内部将新建一个默认的转换器
     * @return Class的实例，出错时返回null
     * @see .fromJson
     */
    fun <T: Any> fromJson(json: String, clazz: KClass<T>, mapper: ObjectMapper?): T? {
        if (json.isBlank()) {
            return null
        }
        var objectMapper = mapper
        if (objectMapper == null) {
            objectMapper = createDefaultMapper()
        }
        try {
            return objectMapper.readValue(json, clazz.java)
        } catch (e: Exception) {
            LOG.error(e, "json解析为对象出错！json: $json")
        }
        return null
    }

    /**
     * 反序列化, 将json串解析为指定Class的实例
     *
     * @param json json串, 为null或空将返回null, 为"[]", 返回空集合
     * @param typeReference Class
     * @param mapper json转换器，为null时该方法内部将新建一个默认的转换器
     * @return Class的实例，出错时返回null
     * @see .fromJson
     */
    fun <T> fromJson(json: String, typeReference: TypeReference<T>?, mapper: ObjectMapper?): T? {
        var objectMapper: ObjectMapper? = mapper
        if (json.isBlank()) {
            return null
        }
        if (objectMapper == null) {
            objectMapper = createDefaultMapper()
        }
        try {
            return objectMapper.readValue(json, typeReference)
        } catch (e: Exception) {
            LOG.error(e, "json解析为对象出错！json: $json")
        }
        return null
    }

    /**
     * 反序列化复杂Collection如List<Bean>,
     * 请先使用方法createCollectionType构造类型, 然后调用该方法。
     *
     * @param jsonString 要反序列化的json串
     * @param javaType java类型，通过createCollectionType方法创建
     * @param mapper json转换器，为null时该方法内部将新建一个默认的转换器
     * @return 反序列化后的对象
     * @see .createCollectionType
     * @since 1.0.0
     */
    fun fromJson(jsonString: String, javaType: JavaType?, mapper: ObjectMapper?): Any? {
        var objectMapper: ObjectMapper? = mapper
        if (StringUtils.isEmpty(jsonString)) {
            return null
        }
        if (objectMapper == null) {
            objectMapper = createDefaultMapper()
        }
        return try {
            objectMapper.readValue(jsonString, javaType)
        } catch (e: IOException) {
            LOG.error(e, "反序列化json串出错:$jsonString")
            null
        }
    }

    /**
     * 反序列化复杂Collection如List<Bean>,
     * 请先使用方法createCollectionType构造类型, 然后调用该方法。
     *
     * @param jsonString 要反序列化的json串
     * @param javaType java类型，通过createCollectionType方法创建
     * @return 反序列化后的对象
     * @see .createCollectionType
     * @since 1.0.0
     */
    fun fromJson(jsonString: String, javaType: JavaType?): Any? = fromJson(jsonString, javaType, null)

    /**
     * 创建带有泛型信息的容器类型
     * 如:
     * ArrayList<MyBean>, 则调用createCollectionType(mapper, ArrayList.class,MyBean.class)
     * HashMap<String></String>,MyBean>, 则调用(mapper, HashMap.class,String.class, MyBean.class)
     *
     * @param mapper json转换器，为null时该方法内部将新建一个默认的转换器
     * @param collectionClass 容器类
     * @param elemClasses 容器元素类型
     * @return 带有泛型信息的容器类型
     * @since 1.0.0
     */
    fun createCollectionType(
        mapper: ObjectMapper?, collectionClass: KClass<*>, vararg elemClasses: KClass<*>
    ): JavaType {
        var objectMapper: ObjectMapper? = mapper
        if (objectMapper == null) {
            objectMapper = createDefaultMapper()
        }
        val classes = elemClasses.map { it.java }
        return objectMapper.typeFactory.constructParametricType(collectionClass.java, *classes.toTypedArray())
    }

    /**
     * 创建带有泛型信息的容器类型
     * 如:
     * ArrayList<MyBean>, 则调用createCollectionType(mapper, ArrayList.class,MyBean.class)
     * HashMap<String></String>,MyBean>, 则调用(mapper, HashMap.class,String.class, MyBean.class)
     *
     * @param collectionClass 容器类
     * @param elementClasses 容器元素类型
     * @return 带有泛型信息的容器类型
     * @since 1.0.0
     */
    fun createCollectionType(collectionClass: KClass<*>, vararg elementClasses: KClass<*>): JavaType =
        createCollectionType(null, collectionClass, *elementClasses)

    /**
     * 将json文件的内容解析为指定Class的实例
     *
     * @param jsonFile json文件
     * @param clazz Class
     *
     * @return Class的实例，出错时返回null
     */
    fun <T : Any> fromJson(jsonFile: File, clazz: KClass<T>): T? {
        return fromJson(jsonFile, clazz, null)
    }

    /**
     * 将json文件的内容解析为指定Class的实例
     *
     * @param jsonFile json文件
     * @param typeReference TypeReference
     *
     * @return Class的实例，出错时返回null
     */
    fun <T> fromJson(jsonFile: File, typeReference: TypeReference<T>): T? {
        return fromJson(jsonFile, typeReference, null)
    }

    /**
     * 将json文件的内容解析为指定Class的实例
     *
     * @param jsonFile json文件
     * @param clazz Class
     * @param mapper json转换器，为null时该方法内部将新建一个默认的转换器
     * @return Class的实例，出错时返回null
     */
    fun <T : Any> fromJson(jsonFile: File, clazz: KClass<T>, mapper: ObjectMapper?): T? {
        var objectMapper = mapper
        objectMapper = objectMapper ?: createDefaultMapper()
        try {
            return objectMapper.readValue(jsonFile, clazz.java)
        } catch (e: Exception) {
            LOG.error(e, "json解析为对象出错！")
        }
        return null
    }

    /**
     * 将json文件的内容解析为指定Class的实例
     *
     * @param jsonFile json文件
     * @param typeReference TypeReference
     * @param mapper json转换器，为null时该方法内部将新建一个默认的转换器
     * @return Class的实例，出错时返回null
     */
    fun <T> fromJson(jsonFile: File, typeReference: TypeReference<T>, mapper: ObjectMapper?): T? {
        var objectMapper = mapper
        objectMapper = objectMapper ?: createDefaultMapper()
        try {
            return objectMapper.readValue(jsonFile, typeReference)
        } catch (e: Exception) {
            LOG.error(e, "json解析为对象出错！")
        }
        return null
    }

    /**
     * 序列化，将对象转为json串
     *
     * @param object 要序列化的对象，可以是POJO，也可以是Collection或数组，
     * 如果对象为Null, 返回"null"；如果集合为空集合, 返回"[]"
     * @return 序列化后的json串
     * @since 1.0.0
     */
    fun toJson(`object`: Any, mapper: ObjectMapper? = null): String {
        var objectMapper = mapper
        objectMapper = objectMapper ?: createDefaultMapper()
        return objectMapper.writeValueAsString(`object`)
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
     * 当json里含有bean的部分属性时，更新该bean的该部分属性
     *
     * @param jsonString json串
     * @param object 待更新的bean
     * @param mapper json转换器，为null时该方法内部将新建一个默认的转换器
     * @return 更新后的bean，失败时返回null
     * @since 1.0.0
     */
    fun <T> updateBean(jsonString: String, `object`: T, mapper: ObjectMapper?): T? {
        var objectMapper = mapper
        objectMapper = objectMapper ?: createDefaultMapper()
        try {
            return objectMapper.readerForUpdating(`object`).readValue(jsonString)
        } catch (e: Exception) {
            LOG.error(e, "将json串:{0}更新到对象:{1}时出错.", jsonString, `object`)
        }
        return null
    }

    /**
     * 当json里含有bean的部分属性时，更新该bean的该部分属性
     *
     * @param jsonString json串
     * @param object 待更新的bean
     * @return 更新后的bean，失败时返回null
     * @since 1.0.0
     */
    fun <T> updateBean(jsonString: String, `object`: T): T? {
        return updateBean(jsonString, `object`, null)
    }

    /**
     * 输出jsonP格式的数据
     *
     * @param functionName 函数名
     * @param object 待序列化的对象
     * @return jsonP字符串
     * @since 1.0.0
     */
    fun toJsonP(functionName: String?, `object`: Any?): String? {
        return toJson(JSONPObject(functionName, `object`))
    }

    /**
     * 设定使用Enum的toString方法来读写Enum,
     * 注意本方法一定要在Mapper创建后, 所有的读写动作之前調用.
     *
     * @param mapper json转换器，为null将什么也不做
     * @since 1.0.0
     */
    fun enableEnumUseToString(mapper: ObjectMapper?) {
        mapper?.run {
            enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
            enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
        }
    }

    /**
     * 支持使用Jaxb的Annotation，使得POJO上的annotation不用与Jackson耦合。
     * 默认会先查找jaxb的annotation，如果找不到再找jackson的。
     *
     * @param mapper json转换器，为null将什么也不做
     * @since 1.0.0
     */
    fun enableJaxbAnnotation(mapper: ObjectMapper?) {
        if (mapper != null) {
            val module = JaxbAnnotationModule()
            mapper.registerModule(module)
        }
    }

    /**
     * 允许单引号
     * 允许不带引号的字段名称
     *
     * @param mapper json转换器，为null将什么也不做
     * @since 1.0.0
     */
    fun enableSimple(mapper: ObjectMapper?) {
        mapper?.run {
            configure(Feature.ALLOW_SINGLE_QUOTES, true)
            configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
        }
    }

    private fun createDefaultMapper(): ObjectMapper {
        return ObjectMapper()
    }
}