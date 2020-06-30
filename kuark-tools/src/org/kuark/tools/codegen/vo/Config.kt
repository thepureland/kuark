package org.kuark.tools.codegen.vo

import javafx.beans.property.*
import javafx.scene.control.SingleSelectionModel

class Config {

    private val dbUrl = SimpleStringProperty()
    private val dbUser = SimpleStringProperty()
    private val dbPassword = SimpleStringProperty()
    private val dbType = SimpleStringProperty()
    private val dbCatalog = SimpleStringProperty()
    private val templatePath = SimpleObjectProperty<SingleSelectionModel<String>>()
    private val moduleName = SimpleStringProperty()
    private val codeLoaction = SimpleStringProperty()

    fun getDbType(): String = dbType.get()

    fun dbTypeProperty(): StringProperty = dbType

    fun setDbCatalog(dbCatalog: String) = this.dbCatalog.set(dbCatalog)

    fun getDbCatalog(): String = dbCatalog.get()

    fun dbCatalogProperty(): StringProperty = dbCatalog

    fun setDbType(dbType: String) = this.dbType.set(dbType)

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

    fun getTemplatePath(): String? {
        return templatePath.get().selectedItem
    }

    fun templatePathProperty(): SimpleObjectProperty<SingleSelectionModel<String>> {
        return templatePath
    }

    fun setTemplatePath(templatePath: String?) {
        this.templatePath.get()?.select(templatePath)
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

    companion object {
        const val DEFAULT_SCHEMA_NAME = "public"
    }

}