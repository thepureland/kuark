package io.kuark.base.i18n

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * I18nKit测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class I18nKitTest {

    @BeforeEach
    fun setup() {
        I18nKit.initI18n(setOf("zh_CN", "zh_TW", "en_US"), setOf("test"), "zh_CN")
    }

    @Test
    fun getI18nMap() {
        var i18nMap = I18nKit.getI18nMap("zh_TW")
        assertEquals("中文簡體", i18nMap["test"]!!["kuark-base-test"]!!["language.zh_CN"])

        // 默认Locale
        i18nMap = I18nKit.getI18nMap()
        assertEquals("中文简体", i18nMap["test"]!!["kuark-base-test"]!!["language.zh_CN"])

        // 不支持的Locale，按默认Locale处理
        i18nMap = I18nKit.getI18nMap("zh_cn")
        assertEquals("中文简体", i18nMap["test"]!!["kuark-base-test"]!!["language.zh_CN"])
    }

    @Test
    fun getLocalStr() {
        assertEquals("中文簡體", I18nKit.getLocalStr("language.zh_CN", "kuark-base-test", "test", "zh_TW"))

        // 默认Locale
        assertEquals("中文简体", I18nKit.getLocalStr("language.zh_CN", "kuark-base-test", "test"))

        // 找不到的情况
        assertEquals("xxx", I18nKit.getLocalStr("xxx", "kuark-base-test", "test", "zh_TW"))
        assertEquals("language.zh_CN", I18nKit.getLocalStr("language.zh_CN", "xxx", "test", "zh_TW"))
        assertEquals("language.zh_CN", I18nKit.getLocalStr("language.zh_CN", "kuark-base-test", "xxx", "zh_TW"))
        assertEquals("xxx", I18nKit.getLocalStr("xxx", "xx", "test", "zh_TW"))

        // 不支持的Locale，按默认Locale处理
        assertEquals("中文简体", I18nKit.getLocalStr("language.zh_CN", "kuark-base-test", "test", "zh_cn"))
    }

    @Test
    fun initI18n() {
        // 默认Locale不在支持的列表中，初始化失败 (相当于未调用初始化方法initI18n，以默认的zh_CN处理)
        assertThrows<IllegalStateException> { I18nKit.initI18n(setOf(), setOf("test"), "en_US") }
        assertEquals("中文简体", I18nKit.getLocalStr("language.zh_CN", "kuark-base-test", "test", "zh_TW"))

        // 重新初始化
        I18nKit.initI18n(setOf("zh_CN", "zh_TW", "en_US"), setOf("test"), "en_US")
        assertEquals("Simplified Chinese", I18nKit.getLocalStr("language.zh_CN", "kuark-base-test", "test"))
    }

}