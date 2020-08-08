package org.kuark.base.i18n

import org.kuark.base.log.LogFactory
import org.kuark.base.scanner.classpath.ClassPathScanner
import org.kuark.base.scanner.support.Resource
import org.kuark.base.support.Registry
import java.io.InputStreamReader
import java.util.*

/**
 * 国际化工具
 * 国际化propeties文件路径规范：conf/i18n/类型/模块名_两位小写语言代码_两位大写国家代码.properties。
 *
 * @author K
 * @since 1.0.0
 */
object I18nKit {

    private val LOG = LogFactory.getLog(I18nKit::class)
    private const val DEFAULT_BASE_PATH = "i18n/"
    const val DICT_I18N_KEY = "dicts"

    //总的国际化容器 key说明:  locale     type[view..] module     i18n-key
    private val i18nMap = LinkedHashMap<String, MutableMap<String, MutableMap<String, MutableMap<String, String?>>>>()

    //字典国际化容器 key说明:  locale     type[view..] dict-module     dict-key
    private val i18nMapDict =
        LinkedHashMap<String, MutableMap<String, MutableMap<String, MutableMap<String, String?>>>>()


    /**
     * 初始化的默认语言
     */
    private var DEFAULT_LOCALE: String? = null

    /**
     * 初始化运行时工程内的所有国际化文件
     */
    fun initI18n(defaultLocale: String) {
        DEFAULT_LOCALE = defaultLocale
        initAll(defaultLocale)
    }

    private fun initAll(defaultLocale: String) {
        _initI18n(defaultLocale)
        initDictByLocale(defaultLocale)
    }

    private fun _initI18n(defaultLocale: String) {
        val types = Registry.lookup(Registry.I18N_TYPE_KEY)
        val otherLocales = ArrayList<String?>(supportLocales)
        //去除默认语言
        otherLocales.remove(defaultLocale)
        for (type in types) {
            //initI18nByType(type,defaultLocale,Arrays.asList(defaultLocale),"");
            initI18nByType(
                type as String,
                defaultLocale,
                otherLocales,
                ""
            )
        }
    }

    fun initI18nByType(vararg args: String): Boolean {
        val type = if (args.isNotEmpty()) args[0] else ""
        val prefix = if (args.size > 1) args[1] else ""
        if (type.isNotBlank()) {
            val otherLocales = ArrayList<String?>(supportLocales)
            //去除默认语言
            otherLocales.remove(DEFAULT_LOCALE)
            //initI18nByType(type,DEFAULT_LOCALE,Arrays.asList(DEFAULT_LOCALE),prefix);
            initI18nByType(
                type,
                DEFAULT_LOCALE,
                otherLocales,
                prefix
            )
        }
        return true
    }

    private fun initI18nByType(type: String, defaultLocale: String?, otherLocales: List<String?>, prefix: String) {
        val resources: Array<Resource> =
            ClassPathScanner.scanForResources(DEFAULT_BASE_PATH + type, prefix, ".properties")
        val resourceGroup = resourceGroup(
            resources,
            Registry.lookup(Registry.I18N_KEY_LOCALES)
        )
        //先初始化:默认语言
        _initOneLocale(
            defaultLocale,
            type,
            resourceGroup[defaultLocale]!!
        )

        //后初始化:其它语言
        for (locale in otherLocales) {
            _initOneLocale(locale, type, resourceGroup[locale]!!)
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
    private fun _initOneLocale(locale: String?, type: String, resourceGroup: List<Resource>) {
        val moduleSet = HashSet<String>()
        for (resource in resourceGroup) {
            var typeMap = i18nMap[locale]
            if (typeMap == null) {
                typeMap = HashMap()
                typeMap[type] = LinkedHashMap<String, MutableMap<String, String?>>()
                i18nMap[locale as String] = typeMap
            }
            val moduleAndLocale = getModuleAndLocale(resource)
            val moduleName: String = moduleAndLocale.first
            moduleSet.add(moduleName)
            var moduleMap = typeMap[type]
            if (moduleMap == null) {
                moduleMap = HashMap()
                typeMap[type] = moduleMap
            }
            if (!moduleMap.containsKey(moduleName)) {
                moduleMap[moduleName] = LinkedHashMap()
            }
            _initLocaleByResourceType(moduleMap, resource, type)
        }
    }

    /**
     * 对比默认语言,不存在,或者值为空,即使用默认语言
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
    private fun resourceGroup(resources: Array<Resource>, lookup: List<*>): Map<String, List<Resource>> {
        val resourceGroup = HashMap<String, List<Resource>>()
        val locales = Registry.lookup(Registry.I18N_KEY_LOCALES)
        for (locale in locales) {
            val list = ArrayList<Resource>()
            resourceGroup[locale as String] = list
            for (resource in resources) {
                if (resource.filename.indexOf(locale) != -1) {
                    list.add(resource)
                }
            }
        }
        return resourceGroup
    }

    /**
     * 按类型
     * @param moduleMap
     * @param resource
     * @param type
     */
    private fun _initLocaleByResourceType(
        moduleMap: MutableMap<String, MutableMap<String, String?>>, resource: Resource, type: String
    ) {
        val moduleAndLocale = getModuleAndLocale(resource)
        val moduleName: String = moduleAndLocale.first
//        val locale: String = moduleAndLocale.second
        val resPrefix = "$DEFAULT_BASE_PATH$type/"
        val bundle = bundle(resPrefix + resource.filename)
        val map = createMapByModule(moduleMap, moduleName)
        val keys = bundle!!.keySet()
        for (k in keys) {
            val s = bundle.getString(k)
            map!![k] = s
        }
    }

    private fun getModuleAndLocale(resource: Resource): Pair<String, String> {
        val fileName: String = resource.filename
        var baseName = fileName.replaceFirst(".properties$".toRegex(), "")
        if (baseName.contains("-")) {
            baseName = baseName.substring(baseName.indexOf("-") + 1)
        }
        val locale = baseName.substring(baseName.length - 5, baseName.length) //locale
        val moduleName = baseName.substring(0, baseName.length - 5 + -1) // sub last _locale
        return Pair(moduleName, locale)
    }

    private fun createMapByModule(
        moduleMap: MutableMap<String, MutableMap<String, String?>>, module: String
    ): MutableMap<String, String?>? {
        var map: MutableMap<String, String?>?
        if (moduleMap.containsKey(module)) {
            map = moduleMap[module]
        } else {
            map = HashMap()
            moduleMap[module] = map
        }
        return map
    }

    /**
     * 指定语言,获取所有类别的国际化Map
     * @param locale
     * @return key:    type[view|messages|dicts]
     * value:  key:[module]
     * value:  key:[i18n-key] value:[i18n]
     */
    fun getI18nMap(locale: String?): Map<String, MutableMap<String, MutableMap<String, String?>>> {
        var localeStr = locale
        if (!isSupport(localeStr)) {
            LOG.error("出现未支持的国际化地区:[{0}]", localeStr)
        }
        return i18nMap[localeStr]!!
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
                            LOG.error("i18n:字典国际化模块:{0},类型:{1},缺少Code！", module, oneType)
                        }
                    } catch (ex: Exception) {
                        LOG.error("i18n:字典国际化模块:{0},类型:{1},缺少Code！", module, oneType)
                    }
                }
            }
        }
    }

    /**
     * 绑定本地运行环境和资源文件
     *
     * @param file 资源文件
     * @since 1.0.0
     */
    @Synchronized
    fun bundle(file: String?): ResourceBundle? {
        I18nKit::class.java.classLoader.getResourceAsStream(file).use { it ->
            InputStreamReader(it, "UTF-8").use {
                return PropertyResourceBundle(it)
            }
        }
    }

    /**
     * 获取国际化后的字符串
     *
     * @param i18nKey 国际化key
     * @param module  模块名
     * @param type    国际化信息类型
     * @return 国际化后的字符串 || 国际化的key
     */
    fun getLocalStr(i18nKey: String, module: String, type: String, localeObj: Locale): String? {
        val locale = localeObj.toString()
        val rs = _getLocalStr(i18nKey, module, type, locale)
        return if (rs!=null && rs.isNotBlank()) {
            rs
        } else _getLocalStr(
            i18nKey,
            module,
            type,
            DEFAULT_LOCALE
        )
    }

    private fun _getLocalStr(i18nKey: String, module: String, type: String, locale: String?): String? {
        return if (i18nMap[locale] != null && i18nMap[locale]!![type] != null && i18nMap[locale]!![type]!![module] != null) {
            i18nMap[locale]!![type]!![module]!![i18nKey]
        } else null
    }

    /**
     * 获取支持的locale
     *
     * @return Set<两位小写语言代码_两位大写国家代码>
     */
    val supportLocales: Set<String?>
        get() {
            val lookup = Registry.lookup(Registry.I18N_KEY_LOCALES) as List<String>
            return HashSet(lookup)
        }

    private fun isSupport(locale: String?): Boolean {
        val lookup: List<*> = Registry.lookup(Registry.I18N_KEY_LOCALES)
        return lookup.contains(locale)
    }

    /**
     * 在没有所有的应用都增加守护线程预加载的情况下,不可以删除此方法
     */
    init {
        TODO()
//        for (locale in I18nLocale.values()) {
//            Registry.register(Registry.I18N_KEY_LOCALES, org.kuark.base.support.enums.locale.getCode())
//        }
//        for (i18nType in I18nType.values()) {
//            Registry.register(Registry.I18N_TYPE_KEY, org.kuark.base.support.enums.i18nType.getCode())
//        }
    }

}