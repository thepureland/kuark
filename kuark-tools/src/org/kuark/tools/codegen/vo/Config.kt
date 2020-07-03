package org.kuark.tools.codegen.vo

import javafx.beans.property.*
import javafx.scene.control.SingleSelectionModel

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

    fun getDbUrl(): String {
        return dbUrl.get()
    }

    fun dbUrlProperty(): StringProperty {
        return dbUrl
    }

    fun setDbUrl(dbUrl: String) {
        this.dbUrl.set(dbUrl)
    }

    fun getDbUser(): String {
        return dbUser.get()
    }

    fun dbUserProperty(): StringProperty {
        return dbUser
    }

    fun setDbUser(dbUser: String) {
        this.dbUser.set(dbUser)
    }

    fun getDbPassword(): String {
        return dbPassword.get()
    }

    fun dbPasswordProperty(): StringProperty {
        return dbPassword
    }

    fun setDbPassword(dbPassword: String) {
        this.dbPassword.set(dbPassword)
    }

    fun getTemplateInfo(): TemplateNameAndRootDir? {
        return templateInfo.get().selectedItem
    }

    fun templateInfoProperty(): SimpleObjectProperty<SingleSelectionModel<TemplateNameAndRootDir>> {
        return templateInfo
    }

    fun setTemplateInfo(templateInfo: TemplateNameAndRootDir?) {
        this.templateInfo.get()?.select(templateInfo)
    }

    fun getPackagePrefix(): String {
        return packagePrefix.get()
    }

    fun packagePrefixProperty(): StringProperty {
        return packagePrefix
    }

    fun setPackagePrefix(packagePrefix: String) {
        this.packagePrefix.set(packagePrefix)
    }

    fun getModuleName(): String {
        return moduleName.get()
    }

    fun moduleNameProperty(): StringProperty {
        return moduleName
    }

    fun setModuleName(moduleName: String) {
        this.moduleName.set(moduleName)
    }

    fun getCodeLoaction(): String {
        return codeLoaction.get()
    }

    fun codeLoactionProperty(): StringProperty {
        return codeLoaction
    }

    fun setCodeLoaction(codeLoaction: String) {
        this.codeLoaction.set(codeLoaction)
    }

    fun getAuthor(): String {
        return author.get()
    }

    fun authorProperty(): StringProperty {
        return author
    }

    fun setAuthor(author: String) {
        this.author.set(author)
    }

    fun getVersion(): String {
        return version.get()
    }

    fun versionProperty(): StringProperty {
        return version
    }

    fun setVersion(version: String) {
        this.version.set(version)
    }

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
        const val CLASS_NAME = "className"
    }

    data class TemplateNameAndRootDir(val name: String, val rootDir: String) {

        override fun toString(): String {
            return name
        }

    }

}