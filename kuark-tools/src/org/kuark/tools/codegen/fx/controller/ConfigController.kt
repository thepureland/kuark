package org.kuark.tools.codegen.fx.controller

import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.stage.DirectoryChooser
import org.kuark.base.support.PropertiesLoader
import org.kuark.config.context.KuarkContext
import org.kuark.data.jdbc.datasource.DataSourceKit
import org.kuark.data.jdbc.datasource.setCurrentDataSource
import org.kuark.data.jdbc.metadata.RdbType
import org.kuark.data.jdbc.support.RdbKit
import org.kuark.tools.codegen.vo.Config
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.util.*

class ConfigController : Initializable {

    @FXML
    var urlTextField: TextField? = null

    @FXML
    var userTextField: TextField? = null

    @FXML
    var passwordField: PasswordField? = null

    @FXML
    var templateChoiceBox: ComboBox<String>? = null

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
        moduleSuggestions = HashSet(listOf(*moduleSuggestionsStr.split(",".toRegex()).toTypedArray()))
    }

    private fun initConfig() {
        val properties = properties
        propertiesLoader = PropertiesLoader(properties)
        config.setDbUrl(propertiesLoader!!.getProperty(PROP_KEY_DB_URL))
        config.setDbUser(propertiesLoader!!.getProperty(PROP_KEY_DB_USER))
        config.setDbPassword(propertiesLoader!!.getProperty(PROP_KEY_DB_PASSWORD))
        config.setTemplatePath(propertiesLoader!!.getProperty(PROP_KEY_TEMPLATE_PATH))
        config.setModuleName(propertiesLoader!!.getProperty(PROP_KEY_MODULE_NAME))
        config.setCodeLoaction(propertiesLoader!!.getProperty(PROP_KEY_CODE_LOACTION))
    }

    private fun bindProperties() {
        urlTextField!!.textProperty().bindBidirectional(config.dbUrlProperty())
        userTextField!!.textProperty().bindBidirectional(config.dbUserProperty())
        passwordField!!.textProperty().bindBidirectional(config.dbPasswordProperty())
        templateChoiceBox!!.selectionModelProperty().bindBidirectional(config.templatePathProperty())
        moduleTextField!!.textProperty().bindBidirectional(config.moduleNameProperty())
        //        webModuleTextField.textProperty().bind(moduleTextField.textProperty());
        locationTextField!!.textProperty().bindBidirectional(config.codeLoactionProperty())
    }

    private fun initTempleComboBox() {
        val strings = listOf("kuark") //TODO
        val templates = FXCollections.observableArrayList(strings)
        templateChoiceBox!!.items = templates
        templateChoiceBox!!.selectionModel = object : SingleSelectionModel<String>() {
            override fun getItemCount(): Int {
                return strings.size
            }

            override fun getModelItem(index: Int): String {
                return strings[index]
            }
        }
        templateChoiceBox!!.selectionModel.select(0)
    }

    fun canGoOn() {
        // test connection
        val dataSource = DataSourceKit.createDataSource(
            RdbType.productNameOf(config!!.getDbType()),
            urlTextField!!.text.trim { it <= ' ' },
            userTextField!!.text.trim { it <= ' ' },
            passwordField!!.text
        )
        KuarkContext.setCurrentDataSource(dataSource)
        dataSource.connection.use {
            if (!RdbKit.testConnection(it)) {
                throw Exception("数据库连接不上！")
            }
        }

        // test template
        if (templateChoiceBox!!.selectionModel.isEmpty) {
            throw Exception("请选择模板！")
        }

        // test module
        if (moduleTextField!!.text == null || moduleTextField!!.text.isBlank()) {
            throw Exception("请填写模块名！")
        }

        // test location
        if (locationTextField!!.text == null || locationTextField!!.text.isBlank()) {
            throw Exception("代码生成目录不存在！")
        }
    }

    @FXML
    private fun testDbConnection() {
        RdbKit.newConnection(config.getDbUrl(), config.getDbUser(), config.getDbPassword()).use {
            if (RdbKit.testConnection(it)) {
                Alert(Alert.AlertType.INFORMATION, "连接成功！").show()
            } else {
                Alert(Alert.AlertType.ERROR, "连接失败！").show()
            }
        }
    }

    @FXML
    private fun openFileChooser() {
        val directoryChooser = DirectoryChooser()
        val codeLoaction = config.getCodeLoaction()
        if (codeLoaction.isNotBlank()) {
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
        properties.setProperty(PROP_KEY_TEMPLATE_PATH, config.getTemplatePath())
        properties.setProperty(PROP_KEY_MODULE_NAME, config.getModuleName())
        properties.setProperty(PROP_KEY_CODE_LOACTION, config.getCodeLoaction())
        properties.setProperty(PROP_KEY_MODULE_SUGGESTIONS, moduleSuggestions?.joinToString())
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
                properties.setProperty(PROP_KEY_CODE_LOACTION, userHome)
                properties.setProperty(PROP_KEY_MODULE_SUGGESTIONS, "")
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
        private const val PROP_KEY_TEMPLATE_PATH = "templatePath"
        private const val PROP_KEY_MODULE_NAME = "moduleName"
        private const val PROP_KEY_MODULE_SUGGESTIONS = "moduleNameSuggestions"
        private const val PROP_KEY_CODE_LOACTION = "codeLoaction"
    }

}