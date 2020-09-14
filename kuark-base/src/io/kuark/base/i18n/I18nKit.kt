package io.kuark.base.i18n

import io.kuark.base.lang.string.right
import io.kuark.base.log.LogFactory
import io.kuark.base.scanner.classpath.ClassPathScanner
import io.kuark.base.scanner.support.Resource
import java.io.InputStreamReader
import java.util.*

/**
 * 国际化工具
 * 国际化propeties文件路径规范：i18n/类型/模块名_两位小写语言代码_两位大写国家代码.properties。
 * 注意：国际化文件编码必须为UTF-8
 *
 * @author K
 * @since 1.0.0
 */
object I18nKit {

    /**
     * 初始化，如果不初始化，只支持zh_CN
     *
     * @param supportLocales 支持的Locale
     * @param types 国际化信息类型，自定义，为i18n目录的子目录
     * @param defaultLocale 默认Locale
     * @author K
     * @since 1.0.0
     */
    fun initI18n(supportLocales: Set<String>, types: Set<String>, defaultLocale: String = "zh_CN") {
        this.supportLocales = supportLocales
        this.types = types
        if (defaultLocale !in supportLocales) {
            error("默认Locale【$defaultLocale】不在支持的列表【${supportLocales}】中！")
        }
        if (defaultLocale.isNotBlank()) {
            this.defaultLocale = defaultLocale
        }
        initAll(defaultLocale)
    }

    fun initI18nByType(vararg args: String): Boolean {
        val type = if (args.isNotEmpty()) args[0] else ""
        val prefix = if (args.size > 1) args[1] else ""
        if (type.isNotBlank()) {
            val otherLocales = ArrayList(supportLocales)
            //去除默认语言
            otherLocales.remove(defaultLocale)
            //initI18nByType(type,DEFAULT_LOCALE,Arrays.asList(DEFAULT_LOCALE),prefix);
            initI18nByType(type, defaultLocale, otherLocales, prefix)
        }
        return true
    }

    /**
     * 根据Locale, 获取其所有类别的国际化Map
     *
     * @param locale 两位小写语言代码_两位大写国家代码
     * @return MutableMap(type, MutableMap(module, MutableMap(i18n-key, i18n-value)))
     * @author K
     * @since 1.0.0
     */
    fun getI18nMap(locale: String = defaultLocale): Map<String, MutableMap<String, MutableMap<String, String>>> {
        return if (!isSupport(locale)) {
            log.warn("不支持的Locale【${locale}】，按默认Locale【${defaultLocale}】处理！")
            i18nMap[defaultLocale]!!
        } else i18nMap[locale]!!
    }

    /**
     * 获取国际化后的字符串
     *
     * @param locale  两位小写语言代码_两位大写国家代码
     * @param type    国际化信息类型
     * @param module  模块名
     * @param i18nKey 国际化key
     * @return 国际化后的字符串，如果找不到会直接返回i18nKey的值
     * @author K
     * @since 1.0.0
     */
    fun getLocalStr(i18nKey: String, module: String, type: String, locale: String = defaultLocale): String {
        var localeStr = if (!isSupport(locale)) {
            log.warn("不支持的Locale【${locale}】，按默认Locale【${defaultLocale}】处理！")
            defaultLocale
        } else locale
        return if (i18nMap[localeStr] != null && i18nMap[localeStr]!![type] != null && i18nMap[localeStr]!![type]!![module] != null) {
            i18nMap[localeStr]!![type]!![module]!![i18nKey] ?: i18nKey
        } else i18nKey
    }

    /**
     * 是否支持指定的Locale
     *
     * @param locale Locale
     * @return true: 支持，false: 不支持
     * @author K
     * @since 1.0.0
     */
    fun isSupport(locale: String): Boolean = supportLocales.contains(locale)


    private val log = LogFactory.getLog(I18nKit::class)
    private const val DEFAULT_BASE_PATH = "i18n/"
    const val DICT_I18N_KEY = "dicts"

    //总的国际化容器: MutableMap<locale, MutableMap<type, MutableMap<module, MutableMap<i18n-key, i18n-value>>>>
    private val i18nMap = mutableMapOf<String, MutableMap<String, MutableMap<String, MutableMap<String, String>>>>()

    //字典国际化容器: MutableMap<locale, MutableMap<type, MutableMap<module, MutableMap<i18n-key, i18n-value>>>>
    private val i18nMapDict =
        mutableMapOf<String, MutableMap<String, MutableMap<String, MutableMap<String, String?>>>>()

    /**
     * 初始化的默认语言
     */
    private var defaultLocale: String = "zh_CN"

    private lateinit var supportLocales: Set<String>

    private lateinit var types: Set<String>

    private fun initAll(defaultLocale: String) {
        initI18n(defaultLocale)
//        initDictByLocale(defaultLocale)
    }

    private fun initI18n(defaultLocale: String) {
        val otherLocales = ArrayList(supportLocales)
        //去除默认语言
        otherLocales.remove(defaultLocale)
        for (type in types) {
            //initI18nByType(type,defaultLocale,Arrays.asList(defaultLocale),"");
            initI18nByType(type, defaultLocale, otherLocales, "")
        }
    }

    /**
     * 绑定本地运行环境和资源文件
     *
     * @param file 资源文件
     * @author K
     * @since 1.0.0
     */
    @Synchronized
    private fun bundle(file: String): ResourceBundle {
        I18nKit::class.java.classLoader.getResourceAsStream(file).use { it ->
            InputStreamReader(it, "UTF-8").use {
                return PropertyResourceBundle(it)
            }
        }
    }

    private fun initI18nByType(type: String, defaultLocale: String?, otherLocales: List<String?>, prefix: String) {
        val resources = ClassPathScanner.scanForResources(DEFAULT_BASE_PATH + type, prefix, ".properties")
        val resourceGroup = resourceGroup(resources)
        //先初始化:默认语言
        initOneLocale(defaultLocale, type, resourceGroup[defaultLocale]!!)

        //后初始化:其它语言
        for (locale in otherLocales) {
            initOneLocale(locale, type, resourceGroup[locale]!!)
            //补足缺失的国际化
            compareToSetDefaultLocale(defaultLocale, type, locale)
        }
    }

    /**
     * 按语言,类型,进行初始化
     * @param locale
     * @param type
     * @param resourceGroup
     */
    private fun initOneLocale(locale: String?, type: String, resourceGroup: List<Resource>) {
        val moduleSet = HashSet<String>()
        for (resource in resourceGroup) {
            var typeMap = i18nMap[locale]
            if (typeMap == null) {
                typeMap = HashMap()
                typeMap[type] = mutableMapOf()
                i18nMap[locale as String] = typeMap
            }
            val moduleAndLocale = getModuleAndLocale(resource)
            val moduleName = moduleAndLocale.first
            moduleSet.add(moduleName)
            var moduleMap = typeMap[type]
            if (moduleMap == null) {
                moduleMap = HashMap()
                typeMap[type] = moduleMap
            }
            if (!moduleMap.containsKey(moduleName)) {
                moduleMap[moduleName] = LinkedHashMap()
            }
            initLocaleByResourceType(moduleMap, resource, type)
        }
    }

    /**
     * 对比默认语言,不存在,或者值为空,即使用默认语言
     *
     * @param defaultLocale
     * @param type
     * @param locale
     */
    private fun compareToSetDefaultLocale(defaultLocale: String?, type: String, locale: String?) {
        val moduleMapDef = i18nMap[defaultLocale]!![type]
        var moduleMap = i18nMap[locale]!![type]
        if (moduleMap == null) {
            moduleMap = LinkedHashMap()
            i18nMap[locale]!![type] = moduleMap
        }
        if (moduleMapDef == null) {
            return
        }
        for ((module, keyValueMapDef) in moduleMapDef) {
            if (!moduleMap.containsKey(module)) {
                moduleMap[module] = keyValueMapDef //use default locale
                //                LOG.debug("i18n:缺失语言:{0},类型:{1},模块:{2}",locale,type,module);
                continue
            }
            val keyValueMap = moduleMap[module]
            for ((key, value) in keyValueMapDef) {
                if (!keyValueMap!!.containsKey(key) || keyValueMap[key] == null || keyValueMap[key]!!.isBlank()) {
                    keyValueMap[key] = value
                    //                    LOG.debug("i18n:缺失语言:{0},类型:{1},模块:{2},键:{3}",locale,type,module,key);
                }
            }
        }
    }

    /**
     * 资源文件按语言分组
     * @param resources
     * @param lookup
     * @return
     */
    private fun resourceGroup(resources: Array<Resource>): Map<String, List<Resource>> {
        val resourceGroup = mutableMapOf<String, List<Resource>>()
        for (locale in supportLocales) {
            resourceGroup[locale] = resources.filter { it.filename.contains(locale) }
        }
        return resourceGroup
    }

    /**
     * 按类型
     * @param moduleMap
     * @param resource
     * @param type
     */
    private fun initLocaleByResourceType(
        moduleMap: MutableMap<String, MutableMap<String, String>>, resource: Resource, type: String
    ) {
        val moduleAndLocale = getModuleAndLocale(resource)
        val moduleName = moduleAndLocale.first
        val bundle = bundle("$DEFAULT_BASE_PATH$type/${resource.filename}")
        val map = createMapByModule(moduleMap, moduleName)
        bundle.keySet().forEach { map[it] = bundle.getString(it) }
    }

    private fun getModuleAndLocale(resource: Resource): Pair<String, String> {
        var baseName = resource.filename.substringBefore(".")
        val moduleName = baseName.substring(0, baseName.length - 5 + -1)
        val locale = baseName.right(5)!!
        return Pair(moduleName, locale)
    }

    private fun createMapByModule(
        moduleMap: MutableMap<String, MutableMap<String, String>>, module: String
    ): MutableMap<String, String> {
        lateinit var map: MutableMap<String, String>
        if (moduleMap.containsKey(module)) {
            map = moduleMap[module]!!
        } else {
            map = HashMap()
            moduleMap[module] = map
        }
        return map
    }

    /**
     * 将i18nMap里的字典,组织成字典专用的i18nMapDict
     * @param locale
     */
    private fun initDictByLocale(locale: String) {
        synchronized(i18nMapDict) {
            val allDicts = getI18nMap(locale)[DICT_I18N_KEY]
            if (!i18nMapDict.containsKey(locale)) {
                i18nMapDict[locale] = LinkedHashMap()
            }
            for (module in allDicts!!.keys) {
                if (!i18nMapDict[locale]!!.containsKey(module)) {
                    i18nMapDict[locale]!![module] = LinkedHashMap()
                }
                val allDictType: Set<String> = allDicts[module]!!.keys
                for (oneType in allDictType) {
                    try {
                        val dictTypeAndKey = oneType.split("\\.").toTypedArray()
                        val dictType = dictTypeAndKey[0]
                        val realKey = dictTypeAndKey[1]
                        if (!i18nMapDict[locale]!![module]!!.containsKey(dictType)) {
                            i18nMapDict[locale]!![module]!![dictType] = LinkedHashMap()
                        }
                        if (dictTypeAndKey.size > 1 && allDicts[module] != null) {
                            val realValue = allDicts[module]!![oneType]
                            i18nMapDict[locale]!![module]!![dictType]!![realKey] = realValue
                        } else {
                            i18nMapDict[locale]!![module]!![dictType]!![dictType] = module + "_" + oneType
                            log.error("i18n:字典国际化模块:{0},类型:{1},缺少Code！", module, oneType)
                        }
                    } catch (ex: Exception) {
                        log.error("i18n:字典国际化模块:{0},类型:{1},缺少Code！", module, oneType)
                    }
                }
            }
        }
    }

}