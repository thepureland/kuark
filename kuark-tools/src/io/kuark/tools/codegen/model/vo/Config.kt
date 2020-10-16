package io.kuark.tools.codegen.model.vo

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.scene.control.SingleSelectionModel

/**
 * 配置信息值对象
 *
 * @author K
 * @since 1.0.0
 */
class Config {

    private val dbUrl = SimpleStringProperty()
    private val dbUser = SimpleStringProperty()
    private val dbPassword = SimpleStringProperty()
    private val templateInfo = SimpleObjectProperty<SingleSelectionModel<TemplateNameAndRootDir>>()
    private val packagePrefix = SimpleStringProperty()
    private val moduleName = SimpleStringProperty()
    private val codeLoaction = SimpleStringProperty()
    private val author = SimpleStringProperty()
    private val version = SimpleStringProperty()

    fun getDbUrl(): String = dbUrl.get()

    fun dbUrlProperty(): StringProperty = dbUrl

    fun setDbUrl(dbUrl: String) = this.dbUrl.set(dbUrl)

    fun getDbUser(): String = dbUser.get()

    fun dbUserProperty(): StringProperty = dbUser

    fun setDbUser(dbUser: String) = this.dbUser.set(dbUser)

    fun getDbPassword(): String = dbPassword.get()

    fun dbPasswordProperty(): StringProperty = dbPassword

    fun setDbPassword(dbPassword: String) = this.dbPassword.set(dbPassword)

    fun getTemplateInfo(): TemplateNameAndRootDir? = templateInfo.get().selectedItem

    fun templateInfoProperty(): SimpleObjectProperty<SingleSelectionModel<TemplateNameAndRootDir>> = templateInfo

    fun setTemplateInfo(templateInfo: TemplateNameAndRootDir?) = this.templateInfo.get()?.select(templateInfo)

    fun getPackagePrefix(): String = packagePrefix.get()

    fun packagePrefixProperty(): StringProperty = packagePrefix

    fun setPackagePrefix(packagePrefix: String) = this.packagePrefix.set(packagePrefix)

    fun getModuleName(): String = moduleName.get()

    fun moduleNameProperty(): StringProperty = moduleName

    fun setModuleName(moduleName: String) = this.moduleName.set(moduleName)

    fun getCodeLoaction(): String = codeLoaction.get()

    fun codeLoactionProperty(): StringProperty = codeLoaction

    fun setCodeLoaction(codeLoaction: String) = this.codeLoaction.set(codeLoaction)

    fun getAuthor(): String = author.get()

    fun authorProperty(): StringProperty = author

    fun setAuthor(author: String) = this.author.set(author)

    fun getVersion(): String = version.get()

    fun versionProperty(): StringProperty = version

    fun setVersion(version: String) = this.version.set(version)

    companion object {
        const val PROP_KEY_DB_URL = "dbUrl"
        const val PROP_KEY_DB_USER = "dbUser"
        const val PROP_KEY_DB_PASSWORD = "dbPassword"
        const val PROP_KEY_TEMPLATE_NAME = "templateName"
        const val PROP_KEY_TEMPLATE_ROOT_DIR = "templateRootDir"
        const val PROP_KEY_PACKAGE_PREFIX = "packagePrefix"
        const val PROP_KEY_MODULE_NAME = "moduleName"
        const val PROP_KEY_MODULE_SUGGESTIONS = "moduleNameSuggestions"
        const val PROP_KEY_CODE_LOACTION = "codeLoaction"
        const val PROP_KEY_AUTHOR = "author"
        const val PROP_KEY_VERSION = "version"
    }

    data class TemplateNameAndRootDir(val name: String, val rootDir: String) {

        override fun toString(): String = name

    }

}