package org.kuark.base.enums

import org.apache.commons.lang3.EnumUtils
import org.kuark.base.lang.string.StringKit
import org.kuark.base.log.LogFactory
import java.util.*
import kotlin.reflect.KClass

/**
 * 枚举工具类
 *
 * @since 1.0.0
 * @author K
 */
object EnumKit {

    private val LOG = LogFactory.getLog(EnumKit::class)

    /**
     * 根据枚举类型和编码，取得对应的译文
     *
     * @param enumClass 枚举类型, 不能为null
     * @param code 编码，可以为null
     * @return 枚举，根据编码找不到对应枚举时返回null
     * @throws IllegalArgumentException enumClass参数为null时
     */
    fun trans(enumClass: String, code: String): String? {
        require(!StringKit.isBlank(enumClass)) { "enumClass参数不能为null" }
        if (StringKit.isBlank(code)) {
            LOG.error(Exception(), enumClass + "不存在code为【" + code + "】的枚举！")
            return null
        }
        val enumClazz = enumOf(enumClass, code)
        if (enumClazz != null) {
            return enumClazz.trans
        }
        LOG.error(Exception(), enumClass + "不存在code为【" + code + "】的枚举！")
        return null
    }

    /**
     * 根据枚举类型和编码，取得对应的枚举
     *
     * @param enumClass 枚举类型, 不能为null
     * @param code 编码
     * @return 枚举，根据编码找不到对应枚举时返回null
     * @throws IllegalArgumentException enumClass参数为null时
     * @since 1.0.0
     */
    fun <E : ICodeEnum> enumOf(enumClass: KClass<E>, code: String): E? {
        for (e in enumClass.java.enumConstants) {
            if (e.code == code) {
                return e
            }
        }
        LOG.error(Exception(), "${enumClass.java.name}不存在code为【$code】的枚举！")
        return null
    }

    /**
     * 根据枚举全类名和编码，取得对应的枚举
     *
     * @param enumClass 枚举全类名，不能为null或空串, 且对应的类必须实现ICodeEnum接口
     * @param code 表码，可以为null
     * @return 枚举，根据编码找不到对应枚举时返回null
     * @throws IllegalArgumentException 参数为空或根据参数查找失败时
     * @since 1.0.0
     */
    fun enumOf(enumClass: String, code: String): ICodeEnum? {
        val enumClazz = getCodeEnumClass(enumClass)
        return enumOf(enumClazz, code)
    }

    /**
     * 取得指定表码枚举的所有表码信息
     *
     * @param enumClass 表码枚举
     * @return Map<表码，描述>
     * @throws IllegalArgumentException 参数为null时
     * @since 1.0.0
     */
    fun getCodeMap(enumClass: KClass<out ICodeEnum>): Map<String, String?> {
        val enumConstants = enumClass.java.enumConstants
        val codeMap = LinkedHashMap<String, String?>()
        for (e in enumConstants) {
            codeMap[e.code] = e.trans
        }
        return codeMap
    }

    /**
     * 取得指定表码枚举的所有表码信息
     *
     * @param enumClass 表码枚举，不能为null或空串
     * @return Map<表码></表码>，描述>，不会为null
     * @throws IllegalArgumentException 参数为空或根据参数查找失败时
     * @since 1.0.0
     */
    fun getCodeMap(enumClass: String): Map<String, String?> {
        val enumClazz = getCodeEnumClass(enumClass)
        return getCodeMap(enumClazz)
    }

    /**
     * 根据枚举全类名，取得对应的枚举类
     *
     * @param enumClass 枚举全类名，不能为null或空串, 且对应的类必须实现ICodeEnum接口
     * @return 枚举类，不会为null
     * @throws IllegalArgumentException 参数为空或根据参数查找失败时
     * @since 1.0.0
     */
    fun getCodeEnumClass(enumClass: String): KClass<out ICodeEnum> {
        require(!StringKit.isBlank(enumClass)) { "enumClass参数不能为null！" }
        val enumClazz = try {
            Class.forName(enumClass)
        } catch (e: ClassNotFoundException) {
            throw IllegalArgumentException(enumClass + "不存在！")
        }
        require(enumClazz.isEnum) { enumClass + "不是枚举！" }
        require(ICodeEnum::class.java.isAssignableFrom(enumClazz)) { enumClass + "没有实现" + ICodeEnum::class }
        return enumClazz.kotlin as KClass<out ICodeEnum>
    }

    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.lang3.EnumUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

    /**
     * 将枚举中的元素放以Map的形式返回
     *
     * @param <E> 枚举类型
     * @param enumClass 待查找的枚举类
     * @return 可修改的map, 不会为null. Map<枚举元素name></枚举元素name>，枚举元素>
     * @throws IllegalArgumentException enumClass参数为null时
     * @since 1.0.0
     */
    fun <E : Enum<E>> getEnumMap(enumClass: KClass<E>): Map<String, E> =
        EnumUtils.getEnumMap(enumClass.java)

    /**
     * 将枚举中的元素放以List的形式返回
     *
     *
     * @param <E> 枚举类型
     * @param enumClass 待查找的枚举类
     * @return 可修改的list, 不会为null. List<枚举元素>
     * @throws IllegalArgumentException enumClass参数为null时
     * @since 1.0.0
     */
    fun <E : Enum<E>> getEnumList(enumClass: KClass<E>): List<E> =
        EnumUtils.getEnumList(enumClass.java)

    /**
     * 检查指定是名字是否为指定的枚举类的有效枚举元素
     * 该方法[Enum.valueOf]不同，当枚举名无效时它不会抛出异常。
     *
     * @param <E> 枚举类型
     * @param enumClass  待查找的枚举类
     * @param enumName   枚举元素名， null将返回false
     * @return true 如果枚举元素名有效, 否则为 false
     * @since 1.0.0
     */
    fun <E : Enum<E>> isValidEnum(enumClass: KClass<E>, enumName: String?): Boolean =
        EnumUtils.isValidEnum(enumClass.java, enumName)

    /**
     * 根据枚举元素名称获取对应的枚举元素，如果没找到返回null
     * 该方法[Enum.valueOf]不同，当枚举名无效时它不会抛出异常。
     *
     * @param <E> 枚举类型
     * @param enumClass  待查找的枚举类
     * @param enumName   枚举元素名， null将返回null
     * @return 枚举元素, 如果没找到返回null
     * @since 1.0.0
     */
    fun <E : Enum<E>> getEnum(enumClass: KClass<E>, enumName: String?): E =
        EnumUtils.getEnum(enumClass.java, enumName)

    /**
     * 创建一个long型位向量来表示指定的枚举子集。
     * 该方法生成的值可以作为[processBitVector]的输入
     * 当您的枚举中有超过64个值时不要使用该方法，因为这将创建一个超过long型所允许的最大值的值。
     *
     * @param <E>       枚举类型
     * @param enumClass 枚举类
     * @param values    需要转换的枚举元素的迭代器
     * @return 一个长整形值， 它的位值代表枚举元素的值
     * @throws NullPointerException 如果 `enumClass` 或 `values` 为 `null`
     * @throws IllegalArgumentException 如果 `enumClass` 不是一个枚举类或超过64个枚举元素
     * @since 1.0.0
     */
    fun <E : Enum<E>> generateBitVector(enumClass: KClass<E>, values: Iterable<E>): Long =
        EnumUtils.generateBitVector(enumClass.java, values)

    /**
     * 创建一个long型位向量来表示指定的枚举数组
     * 该方法生成的值可以作为[processBitVector]的输入
     * 当您的枚举中有超过64个值时不要使用该方法，因为这将创建一个超过long型所允许的最大值的值。
     *
     * @param <E>       枚举类型
     * @param enumClass 枚举类, 不能为 `null`
     * @param values    需要转换的枚举元素的可变数组, 不能为 `null`
     * @return 一个长整形值， 它的位值代表枚举元素的值
     * @throws NullPointerException 如果 `enumClass` 或 `values` 为 `null`
     * @throws IllegalArgumentException 如果 `enumClass` 不是一个枚举类或超过64个枚举元素
     * @since 1.0.0
     */
    fun <E : Enum<E>> generateBitVector(enumClass: KClass<E>, vararg values: E): Long =
        EnumUtils.generateBitVector(enumClass.java, *values)

    /**
     * 将[generateBitVector]创建的长整形值转换为它所表示的枚举元素集合
     * 如果您存储了该值，谨防枚举任何更改会影响序号值。
     *
     * @param <E>       枚举类型
     * @param enumClass 枚举类
     * @param value     表示的枚举元素集合的长整形值
     * @return 枚举元素集合
     * @throws NullPointerException 如果 `enumClass` 为 `null`
     * @throws IllegalArgumentException 如果 `enumClass` 不是一个枚举类或超过64个枚举元素
     * @since 1.0.0
     */
    fun <E : Enum<E>> processBitVector(enumClass: KClass<E>, value: Long): EnumSet<E> =
        EnumUtils.processBitVector(enumClass.java, value)

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.lang3.EnumUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
}