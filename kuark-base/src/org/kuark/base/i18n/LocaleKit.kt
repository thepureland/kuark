package org.kuark.base.i18n

import java.util.*

/**
 * 本地化工具类
 *
 * @since 1.0.0
 * @author K
 */
object LocaleKit {

    /**
     * 通过字符串获取国际化地区对象
     *
     * @param locale 国际化地区字符串
     * @return
     */
    fun getLocale(locale: String): Locale? {
        var l: Locale? = null
        if (locale.isNotBlank()) {
            val pos = locale.indexOf("_")
            l = if (pos != -1) {
                Locale(locale.substring(0, pos), locale.substring(pos + 1))
            } else {
                Locale(locale)
            }
        }
        return l
    }

    /**
     * 通过字符串获取国际化地区对象
     *
     * @param locale        国际化地区字符串
     * @param defaultLocale 默认国际化地区字符串
     * @return
     */
    fun getLocale(locale: String, defaultLocale: String): Locale? {
        val l = getLocale(locale)
        return l ?: getLocale(defaultLocale)
    }

//    /**
//     * 翻译字典
//     *
//     * @param module 字典类型枚举
//     * @param dictCode 字典代码
//     * @return 国际化后的值
//     */
//    fun tranDict(module: String?, dictType: String, dictCode: String): String {
//        val i18nKey = "$dictType.$dictCode"
//        return I18nKit.getLocalStr(i18nKey, module, I18nType.DICT.getCode(), CommonContext.get().getLocale())
//    }
//
//    /**
//     * 翻译字典
//     *
//     * @param module 字典类型枚举
//     * @param dictCode 字典代码
//     * @param locale 指定语言
//     * @return 国际化后的值
//     */
//    fun tranDict(
//        module: String?,
//        dictType: String,
//        dictCode: String,
//        locale: Locale?
//    ): String {
//        val i18nKey = "$dictType.$dictCode"
//        return I18nKit.getLocalStr(i18nKey, module, I18nType.DICT.getCode(), locale)
//    }
//
//    /**
//     * 翻译字典
//     *
//     * @param dictType 字典类型枚举
//     * @param dictCode 字典代码
//     * @return 国际化后的值
//     */
//    fun tranParam(dictType: IParamTypeEnum, dictCode: String): String {
//        val i18nKey: String = dictType.getType().toString() + "." + dictCode
//        return I18nKit.getLocalStr(
//            i18nKey,
//            dictType.getModule().getCode(),
//            I18nType.PARAMS.getCode(),
//            CommonContext.get().getLocale()
//        )
//    }
//
//    /**
//     * 翻译视图上的文本
//     *
//     * @param module  模块枚举
//     * @param i18nKey 国际化key
//     * @param args    消息模板参数
//     * @param <M>     模块枚举类型
//     * @return 国际化后的值
//    </M> */
//    fun <M> tranView(
//        module: M,
//        i18nKey: String,
//        vararg args: Any?
//    ): String where M : Enum<*>?, M : ICodeEnum? {
//        val messagePattern: String =
//            I18nKit.getLocalStr(i18nKey, module.getCode(), "view", CommonContext.get().getLocale())
//        if (StringKit.isNotBlank(messagePattern) && ArrayTool.isNotEmpty(args)) {
//            return MessageFormat.format(messagePattern, *args)
//        }
//        return if (StringKit.isNotBlank(messagePattern)) {
//            messagePattern
//        } else "views." + module.getCode().toString() + "." + i18nKey
//    }
//
//    /**
//     * 翻译视图上的文本(框架调用)
//     *
//     * @param moduleCode 模块编码
//     * @param i18nKey    国际化key
//     * @param args       消息模板参数
//     * @return 国际化后的值
//     */
//    fun tranView(moduleCode: String, i18nKey: String, vararg args: Any?): String {
//        val messagePattern: String =
//            I18nKit.getLocalStr(i18nKey, moduleCode, "view", CommonContext.get().getLocale())
//        if (StringKit.isNotBlank(messagePattern) && ArrayTool.isNotEmpty(args)) {
//            return MessageFormat.format(messagePattern, *args)
//        }
//        return if (StringKit.isNotBlank(messagePattern)) {
//            messagePattern
//        } else "views.$moduleCode.$i18nKey"
//    }
}