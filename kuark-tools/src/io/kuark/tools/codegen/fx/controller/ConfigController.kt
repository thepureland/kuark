package io.kuark.tools.codegen.fx.controller

import io.kuark.ability.data.jdbc.datasource.DataSourceKit
import io.kuark.ability.data.jdbc.datasource.setCurrentDataSource
import io.kuark.ability.data.jdbc.support.RdbKit
import io.kuark.base.io.FilenameKit
import io.kuark.base.io.PathKit
import io.kuark.base.lang.SystemKit
import io.kuark.base.support.PropertiesLoader
import io.kuark.context.core.KuarkContextHolder
import io.kuark.tools.codegen.vo.Config
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.stage.DirectoryChooser
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.util.*

/**
 * 配置信息界面JavaFx控制器
 *
 * @author K
 * @since 1.0.0
 */
class ConfigController : Initializable {

    @FXML
    lateinit var urlTextField: TextField

    @FXML
    lateinit var userTextField: TextField

    @FXML
    lateinit var passwordField: PasswordField

    @FXML
    lateinit var templateChoiceBox: ComboBox<Config.TemplateNameAndRootDir>

    @FXML
    lateinit var packagePrefixTextField: TextField

    @FXML
    lateinit var moduleTextField: TextField

    @FXML
    lateinit var locationTextField: TextField

    @FXML
    lateinit var openButton: Button

    @FXML
    lateinit var authorTextField: TextField

    @FXML
    lateinit var versionTextField: TextField

    val config = Config()
    private val userHome = System.getProperty("user.home")
    private val propertiesFile = File("$userHome/.kuark/CodeGenerator.properties")
    private lateinit var propertiesLoader: PropertiesLoader
    private var moduleSuggestions: Set<String?>? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        initConfig()
        bindProperties()
        initTempleComboBox()
        initAutoCompletion()
    }

    private fun initAutoCompletion() {
        var moduleSuggestionsStr = propertiesLoader.getProperty(Config.PROP_KEY_MODULE_SUGGESTIONS, "")
        moduleSuggestions = HashSet(listOf(*moduleSuggestionsStr.split(",".toRegex()).toTypedArray()))
    }

    private fun initConfig() {
        val properties = properties
        propertiesLoader = PropertiesLoader(properties)
        with(config) {
            setDbUrl(propertiesLoader.getProperty(Config.PROP_KEY_DB_URL, ""))
            setDbUser(propertiesLoader.getProperty(Config.PROP_KEY_DB_USER, ""))
            setDbPassword(propertiesLoader.getProperty(Config.PROP_KEY_DB_PASSWORD, ""))
            val templateInfo = Config.TemplateNameAndRootDir(
                propertiesLoader.getProperty(Config.PROP_KEY_TEMPLATE_ROOT_DIR, ""),
                FilenameKit.normalize(propertiesLoader.getProperty(Config.PROP_KEY_TEMPLATE_ROOT_DIR, ""), true)!!
            )
            setTemplateInfo(templateInfo)
            setPackagePrefix(propertiesLoader.getProperty(Config.PROP_KEY_PACKAGE_PREFIX, ""))
            setModuleName(propertiesLoader.getProperty(Config.PROP_KEY_MODULE_NAME, ""))
            setCodeLoaction(propertiesLoader.getProperty(Config.PROP_KEY_CODE_LOACTION, ""))
            setAuthor(propertiesLoader.getProperty(Config.PROP_KEY_AUTHOR, SystemKit.getUser()))
            setVersion(propertiesLoader.getProperty(Config.PROP_KEY_VERSION, ""))
        }
    }

    private fun bindProperties() {
        with(config) {
            urlTextField.textProperty().bindBidirectional(dbUrlProperty())
            userTextField.textProperty().bindBidirectional(dbUserProperty())
            passwordField.textProperty().bindBidirectional(dbPasswordProperty())
            templateChoiceBox.selectionModelProperty().bindBidirectional(templateInfoProperty())
            moduleTextField.textProperty().bindBidirectional(moduleNameProperty())
            authorTextField.textProperty().bindBidirectional(authorProperty())
            versionTextField.textProperty().bindBidirectional(versionProperty())
            packagePrefixTextField.textProperty().bindBidirectional(packagePrefixProperty())
            locationTextField.textProperty().bindBidirectional(codeLoactionProperty())
            authorTextField.textProperty().bindBidirectional(authorProperty())
            versionTextField.textProperty().bindBidirectional(versionProperty())
        }
    }

    private fun initTempleComboBox() {
        val templatesPath = "${PathKit.getRuntimePath()}/../../../resources/main/template/"
        val files = File(templatesPath).normalize().listFiles()
        val templateNameAndPaths = mutableListOf<Config.TemplateNameAndRootDir>()
        files.forEach {
            templateNameAndPaths.add(
                Config.TemplateNameAndRootDir(it.name, FilenameKit.normalize(it.absolutePath, true)!!)
            )
        }
        templateChoiceBox.items = FXCollections.observableArrayList(*templateNameAndPaths.toTypedArray())
        templateChoiceBox.selectionModel = object : SingleSelectionModel<Config.TemplateNameAndRootDir>() {
            override fun getItemCount(): Int {
                return templateNameAndPaths.size
            }

            override fun getModelItem(index: Int): Config.TemplateNameAndRootDir {
                return templateNameAndPaths[index]
            }
        }
        templateChoiceBox.selectionModel.select(0)
    }

    fun canGoOn() {
        // test connection
        val dataSource = DataSourceKit.createDataSource(
            urlTextField.text.trim(),
            userTextField.text.trim(),
            passwordField.text
        )
        KuarkContextHolder.setCurrentDataSource(dataSource)

        _testDbConnection()

        // test template
        if (templateChoiceBox.selectionModel.isEmpty) {
            throw Exception("请选择模板！")
        }

        // package prefix
        if (packagePrefixTextField.text == null || packagePrefixTextField.text.isBlank()) {
            throw Exception("请填写包名前缀！")
        }

        // test module
        if (moduleTextField.text == null || moduleTextField.text.isBlank()) {
            throw Exception("请填写模块名！")
        }

        // test location
        if (locationTextField.text == null || locationTextField.text.isBlank()) {
            throw Exception("代码生成目录不存在！")
        }

        // author location
        if (authorTextField.text == null || authorTextField.text.isBlank()) {
            throw Exception("请填写作者！")
        }

        // version location
        if (versionTextField.text == null || versionTextField.text.isBlank()) {
            throw Exception("请填写版本号！")
        }
    }

    private fun _testDbConnection() {
        try {
            RdbKit.newConnection(config.getDbUrl(), config.getDbUser(), config.getDbPassword()).use {
                RdbKit.testConnection(it)
            }
        } catch (e: Exception) {
            Alert(Alert.AlertType.ERROR, "连接失败！").show()
            throw Exception("数据库连接不上！")
        }
    }

    @FXML
    private fun testDbConnection() {
        _testDbConnection()
        Alert(Alert.AlertType.INFORMATION, "连接成功！").show()
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
            locationTextField.text = selectedFolder.absolutePath
        }
    }

    fun storeConfig() {
        val properties = propertiesLoader.properties
        with(properties) {
            setProperty(Config.PROP_KEY_DB_URL, config.getDbUrl())
            setProperty(Config.PROP_KEY_DB_USER, config.getDbUser())
            setProperty(Config.PROP_KEY_DB_PASSWORD, config.getDbPassword())
            setProperty(Config.PROP_KEY_TEMPLATE_NAME, config.getTemplateInfo()!!.name)
            setProperty(Config.PROP_KEY_TEMPLATE_ROOT_DIR, config.getTemplateInfo()!!.rootDir)
            setProperty(Config.PROP_KEY_PACKAGE_PREFIX, config.getPackagePrefix())
            setProperty(Config.PROP_KEY_MODULE_NAME, config.getModuleName())
            setProperty(Config.PROP_KEY_CODE_LOACTION, config.getCodeLoaction())
            setProperty(Config.PROP_KEY_MODULE_SUGGESTIONS, moduleSuggestions?.joinToString())
            setProperty(Config.PROP_KEY_AUTHOR, config.getAuthor())
            setProperty(Config.PROP_KEY_VERSION, config.getVersion())
        }
        try {
            FileOutputStream(propertiesFile).use { os -> properties.store(os, null) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private val properties: Properties
        private get() {
            val properties = Properties()
            if (!propertiesFile.exists()) { // 第一次使用，预设组件默认值
                val parentFile = propertiesFile.parentFile
                if (!parentFile.exists()) {
                    if (!parentFile.mkdir()) {
                        throw Exception(parentFile.toString() + "目录创建失败！")
                    }
                }
                with(properties) {
                    setProperty(Config.PROP_KEY_DB_URL, "jdbc:h2:tcp://localhost:9092/./h2;DATABASE_TO_UPPER=false")
                    setProperty(Config.PROP_KEY_DB_USER, "sa")
                    setProperty(Config.PROP_KEY_DB_PASSWORD, "")
                    setProperty(Config.PROP_KEY_PACKAGE_PREFIX, "")
                    setProperty(Config.PROP_KEY_MODULE_NAME, "")
                    setProperty(Config.PROP_KEY_CODE_LOACTION, userHome)
                    setProperty(Config.PROP_KEY_MODULE_SUGGESTIONS, "")
                    setProperty(Config.PROP_KEY_AUTHOR, SystemKit.getUser())
                    setProperty(Config.PROP_KEY_VERSION, "1.0.0")
                }
            } else {
                try {
                    FileInputStream(propertiesFile).use { `is` -> properties.load(`is`) }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return properties
        }

}