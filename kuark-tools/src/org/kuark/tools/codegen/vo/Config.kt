package org.kuark.tools.codegen.vo

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty

class Config {

    private val dbUrl: StringProperty = SimpleStringProperty()
    private val dbUser: StringProperty = SimpleStringProperty()
    private val dbPassword: StringProperty = SimpleStringProperty()
    private val dbType: StringProperty = SimpleStringProperty()
    private val dbCatalog: StringProperty = SimpleStringProperty()
    private val dbSchema: StringProperty = SimpleStringProperty()
    private val templatePath: StringProperty = SimpleStringProperty()
    private val moduleName: StringProperty = SimpleStringProperty()
    private val webModuleName: StringProperty = SimpleStringProperty()
    private val codeLoaction: StringProperty = SimpleStringProperty()

    fun getDbType(): String = dbType.get()

    fun dbTypeProperty(): StringProperty = dbType

    fun setDbCatalog(dbCatalog: String) = this.dbCatalog.set(dbCatalog)

    fun getDbCatalog(): String = dbCatalog.get()

    fun dbCatalogProperty(): StringProperty = dbCatalog

    fun setDbType(dbType: String) = this.dbType.set(dbType)

    fun getDbSchema(): String = dbSchema.get()

    fun dbSchemaProperty(): StringProperty = dbSchema

    fun setDbSchema(dbSchema: String) = this.dbSchema.set(dbSchema)

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

    fun getTemplatePath(): String {
        return templatePath.get()
    }

    fun templatePathProperty(): StringProperty {
        return templatePath
    }

    fun setTemplatePath(templatePath: String) {
        this.templatePath.set(templatePath)
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

    fun getWebModuleName(): String {
        return webModuleName.get()
    }

    fun webModuleNameProperty(): StringProperty {
        return webModuleName
    }

    fun setWebModuleName(webModuleName: String) {
        this.webModuleName.set(webModuleName)
    }

    companion object {
        const val DEFAULT_SCHEMA_NAME = "public"
    }

}