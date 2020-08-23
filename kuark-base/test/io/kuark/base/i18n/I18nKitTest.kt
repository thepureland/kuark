package io.kuark.base.i18n

/**
 * I18nKit测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class I18nKitTest {

    private val DEFAUTL_LOCALE = "zh_TW"

//    /**
//     * 测试场景: 默认语言正常
//     */
//    @Test
//    fun test_initI18n() {
//        test_init_by_locale(DEFAUTL_LOCALE)
//    }
//
//    private fun test_init_by_locale(locale: String) {
//        val rs = I18nKit.getLocalStr("sex.male", "module1", I18nKit.DICT_I18N_KEY, LocaleKit.getLocale(locale)!!)
//        val i18nMap = I18nKit.getI18nMap(locale)
//        if (DEFAUTL_LOCALE.equals(locale)) {
//            assertEquals("男", rs)
//            assertEquals("男", i18nMap[I18nKit.DICT_I18N_KEY]!!["module1"]!!["sex.male"])
//        } else if ("en_US" == locale) {
//            assertEquals("male", rs)
//            assertEquals("male", i18nMap[I18nKit.DICT_I18N_KEY]!!["module1"]!!["sex.male"])
//        }
////        val msg: String = I18nKit.getScriptViewObject(locale)
////        assertTrue(msg.indexOf("OK") != -1)
//    }
//
//    /**
//     * 测试场景: 缺失的类型
//     */
//    @Test
//    fun test_initI18n_miss_type() {
//        I18nKit.initI18n(DEFAUTL_LOCALE)
//        val trans = "sex.male"
//        val rs = I18nKit.getLocalStr("sex.male", "module1", "_no_exist_", LocaleKit.getLocale("zh_CN")!!)
//        assertNull(rs)
//        val i18nMap = I18nKit.getI18nMap(DEFAUTL_LOCALE)
////        assertNull(trans, i18nMap["_no_exist_"])
//    }
//
//    /**
//     * 测试场景: 缺失的模块
//     */
//    @Test
//    fun test_initI18n_miss_module() {
//        I18nKit.initI18n(DEFAUTL_LOCALE)
//        val trans = "sex.male"
//        val rs = I18nKit.getLocalStr("sex.male", "_no_exist_", I18nKit.DICT_I18N_KEY, LocaleKit.getLocale("zh_CN")!!)
//        assertNull(trans, rs)
//        val i18nMap = I18nKit.getI18nMap(DEFAUTL_LOCALE)
////        assertNull(trans, i18nMap[I18nKit.DICT_I18N_KEY]!!["_no_exist_"])
//    }
//
//    /**
//     * 测试场景: 缺失的语言,返回默认语言正常
//     */
//    @Test
//    fun test_initI18n_miss_locale() {
//        I18nKit.initI18n(DEFAUTL_LOCALE)
//        val trans = "男"
//        val rs = I18nKit.getLocalStr("sex.male", "module1", I18nKit.DICT_I18N_KEY, LocaleKit.getLocale("aa_BB")!!)
//        assertEquals(trans, rs)
//    }
//
//    /**
//     * 测试场景: 普通国际化获取方式
//     */
//    @Test
//    fun test_getLocalStr() {
//        var localStr =
//            I18nKit.getLocalStr("sex.male", "module1", I18nKit.DICT_I18N_KEY, Locale("zh", "CN"))
//        assertEquals("男", localStr)
//        localStr = I18nKit.getLocalStr("OK", "module2", "messages", Locale("zh", "TW"))
//        assertEquals("確定", localStr)
//    }
//
//    /**
//     * 测试场景: 重新初始化
//     */
//    @Test
//    fun test_re_init() {
//        I18nKit.initI18n(DEFAUTL_LOCALE)
//        test_init_by_locale(DEFAUTL_LOCALE)
//        I18nKit.initI18n(DEFAUTL_LOCALE)
//        test_init_by_locale("en_US")
//    }

}