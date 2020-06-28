package org.kuark.tools.codegen.fx.controller

import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.stage.DirectoryChooser
import org.kuark.base.lang.string.StringKit
import org.kuark.base.support.PropertiesLoader
import org.kuark.data.jdbc.support.RdbKit
import org.kuark.data.jdbc.metadata.RdbType
import org.kuark.tools.codegen.vo.Config
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.util.*

class ConfigController : Initializable {

    @FXML
    var webModuleTextField: TextField? = null

    @FXML
    var urlTextField: TextField? = null

    @FXML
    var userTextField: TextField? = null

    @FXML
    var passwordField: PasswordField? = null

    @FXML
    var schemaField: TextField? = null

    @FXML
    var templateChoiceBox: TextField? = null

    @FXML
    var moduleTextField: TextField? = null

    @FXML
    var locationTextField: TextField? = null

    @FXML
    var openButton: Button? = null

    val config = Config()
    private val userHome = System.getProperty("user.home")
    private val propertiesFile = File("$userHome/.kuark/CodeGenerator.properties")
    private var propertiesLoader: PropertiesLoader? = null
    private var moduleSuggestions: Set<String?>? = null
    private var webModuleSuggestions: Set<String?>? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        initConfig()
        bindProperties()
        initTempleComboBox()
        initAutoCompletion()
    }

    private fun initAutoCompletion() {
        var moduleSuggestionsStr = propertiesLoader!!.getProperty(PROP_KEY_MODULE_SUGGESTIONS, "")
        if (moduleSuggestionsStr == null) {
            moduleSuggestionsStr = ""
        }
        moduleSuggestions = HashSet(Arrays.asList(*moduleSuggestionsStr.split(",".toRegex()).toTypedArray()))
        var webModuleSuggestionsStr = propertiesLoader!!.getProperty(PROP_KEY_WEB_MODULE_SUGGESTIONS, "")
        if (webModuleSuggestionsStr == null) {
            webModuleSuggestionsStr = ""
        }
        webModuleSuggestions = HashSet(Arrays.asList(*webModuleSuggestionsStr.split(",".toRegex()).toTypedArray()))
    }

    private fun initConfig() {
        val properties = properties
        propertiesLoader = PropertiesLoader(properties)
        var schema = ""
        schema = try {
            propertiesLoader!!.getProperty(PROP_KEY_DB_SCHEMA)
        } catch (e: Exception) {
            Config.DEFAULT_SCHEMA_NAME
        }
        config.setDbUrl(propertiesLoader!!.getProperty(PROP_KEY_DB_URL))
        config.setDbUser(propertiesLoader!!.getProperty(PROP_KEY_DB_USER))
        config.setDbPassword(propertiesLoader!!.getProperty(PROP_KEY_DB_PASSWORD))
        config.setDbSchema(schema)
        config.setTemplatePath(propertiesLoader!!.getProperty(PROP_KEY_TEMPLATE_PATH))
        config.setModuleName(propertiesLoader!!.getProperty(PROP_KEY_MODULE_NAME))
        config.setWebModuleName(propertiesLoader!!.getProperty(PROP_KEY_WEB_MODULE_NAME))
        config.setCodeLoaction(propertiesLoader!!.getProperty(PROP_KEY_CODE_LOACTION))
    }

    private fun bindProperties() {
        urlTextField!!.textProperty().bindBidirectional(config.dbUrlProperty())
        userTextField!!.textProperty().bindBidirectional(config.dbUserProperty())
        passwordField!!.textProperty().bindBidirectional(config.dbPasswordProperty())
        schemaField!!.textProperty().bindBidirectional(config.dbSchemaProperty())
        templateChoiceBox!!.textProperty().bindBidirectional(config.templatePathProperty())
        moduleTextField!!.textProperty().bindBidirectional(config.moduleNameProperty())
        //        webModuleTextField.textProperty().bind(moduleTextField.textProperty());
        webModuleTextField!!.textProperty().bindBidirectional(config.webModuleNameProperty())
        locationTextField!!.textProperty().bindBidirectional(config.codeLoactionProperty())
        moduleTextField!!.textProperty()
            .addListener { observable: ObservableValue<out String?>?, oldValue: String?, newValue: String? ->
                webModuleTextField!!.textProperty().bind(moduleTextField!!.textProperty())
            }
        webModuleTextField!!.focusedProperty()
            .addListener { observable: ObservableValue<out Boolean>?, oldValue: Boolean?, newValue: Boolean ->
                if (newValue) {
                    webModuleTextField!!.textProperty().unbind()
                }
            }
    }

    private fun initTempleComboBox() {
//        List<String> strings = ListTool.newArrayList(
//                );
//        ObservableList<String> templates = FXCollections.observableArrayList(strings);
//        templateChoiceBox.setItems(templates);
    }

    fun canGoOn() {
        // test connection
        val url = urlTextField!!.text.trim { it <= ' ' }
        val user = userTextField!!.text.trim { it <= ' ' }
        val password = passwordField!!.text
        val conn = RdbKit.newConnection(RdbType.H2, url, user, password) //TODO
        conn.use {
            if (!RdbKit.testConnection(RdbType.H2, conn)) {
                throw Exception("数据库连接不上！")
            }
        }

        // test template
        if (StringKit.isBlank(templateChoiceBox!!.text)) {
            throw Exception("请选择模板！")
        }

        // test module
        if (StringKit.isBlank(moduleTextField!!.text)) {
            throw Exception("请填写模块名！")
        }

        // test location
        if (StringKit.isBlank(locationTextField!!.text)) {
            throw Exception("代码生成目录不存在！")
        }
    }

    @FXML
    private fun openFileChooser() {
        val directoryChooser = DirectoryChooser()
        val codeLoaction = config.getCodeLoaction()
        if (StringKit.isNotBlank(codeLoaction)) {
            val file = File(codeLoaction)
            if (file.exists() && file.isDirectory) {
                directoryChooser.initialDirectory = file
            }
        }
        directoryChooser.title = "选择生成目录"
        val selectedFolder = directoryChooser.showDialog(openButton!!.scene.window)
        if (selectedFolder != null) {
            locationTextField!!.text = selectedFolder.absolutePath
        }
    }

    fun storeConfig() {
        val properties = propertiesLoader!!.properties
        properties.setProperty(PROP_KEY_DB_URL, config.getDbUrl())
        properties.setProperty(PROP_KEY_DB_USER, config.getDbUser())
        properties.setProperty(PROP_KEY_DB_PASSWORD, config.getDbPassword())
        properties.setProperty(PROP_KEY_DB_SCHEMA, config.getDbSchema())
        properties.setProperty(PROP_KEY_TEMPLATE_PATH, config.getTemplatePath())
        properties.setProperty(PROP_KEY_MODULE_NAME, config.getModuleName())
        properties.setProperty(PROP_KEY_WEB_MODULE_NAME, config.getWebModuleName())
        properties.setProperty(PROP_KEY_CODE_LOACTION, config.getCodeLoaction())
        properties.setProperty(PROP_KEY_MODULE_SUGGESTIONS, moduleSuggestions?.joinToString())
        properties.setProperty(PROP_KEY_WEB_MODULE_SUGGESTIONS, webModuleSuggestions?.joinToString())
        try {
            FileOutputStream(propertiesFile).use { os -> properties.store(os, null) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private val properties: Properties
        private get() {
            val properties = Properties()
            if (!propertiesFile.exists()) {
                val parentFile = propertiesFile.parentFile
                if (!parentFile.exists()) {
                    if (!parentFile.mkdir()) {
                        throw Exception(parentFile.toString() + "目录创建失败！")
                    }
                }
                //TODO
                properties.setProperty(
                    PROP_KEY_DB_URL,
                    "jdbc:postgresql://127.0.0.1:5432/postgres?characterEncoding=UTF-8"
                )
                properties.setProperty(PROP_KEY_DB_USER, "postgres")
                properties.setProperty(PROP_KEY_DB_PASSWORD, "postgres")
                properties.setProperty(PROP_KEY_TEMPLATE_PATH, "")
                properties.setProperty(PROP_KEY_MODULE_NAME, "")
                properties.setProperty(PROP_KEY_WEB_MODULE_NAME, "")
                properties.setProperty(PROP_KEY_CODE_LOACTION, userHome)
                properties.setProperty(PROP_KEY_MODULE_SUGGESTIONS, "")
                properties.setProperty(PROP_KEY_WEB_MODULE_SUGGESTIONS, "")
            } else {
                try {
                    FileInputStream(propertiesFile).use { `is` -> properties.load(`is`) }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return properties
        }

    companion object {
        private const val PROP_KEY_DB_URL = "dbUrl"
        private const val PROP_KEY_DB_USER = "dbUser"
        private const val PROP_KEY_DB_PASSWORD = "dbPassword"
        private const val PROP_KEY_DB_SCHEMA = "dbSchema"
        private const val PROP_KEY_TEMPLATE_PATH = "templatePath"
        private const val PROP_KEY_MODULE_NAME = "moduleName"
        private const val PROP_KEY_MODULE_SUGGESTIONS = "moduleNameSuggestions"
        private const val PROP_KEY_WEB_MODULE_NAME = "webModuleName"
        private const val PROP_KEY_WEB_MODULE_SUGGESTIONS = "webModuleNameSuggestions"
        private const val PROP_KEY_CODE_LOACTION = "codeLoaction"
    }
}