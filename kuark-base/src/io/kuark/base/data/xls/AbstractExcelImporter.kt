package io.kuark.base.data.xls

import io.kuark.base.bean.validation.kit.ValidationKit
import io.kuark.base.io.FileKit
import io.kuark.base.lang.GenericKit
import io.kuark.base.lang.reflect.getMemberProperty
import io.kuark.base.lang.reflect.newInstance
import io.kuark.base.lang.string.toType
import io.kuark.base.log.LogFactory
import jxl.Cell
import jxl.CellType
import jxl.Sheet
import jxl.Workbook
import java.io.File
import java.io.InputStream
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.primaryConstructor

/**
 * excel数据导入器抽象类
 * 注意事项：
 * 1. Excel第一行为列描述信息，并非要导入的数据，在导入时第一行会被忽略
 * 2. 行对象类支持数据类和普通类。
 *    为数据类时，属性必须全部定义在主构造函数中，可以是只读的(val);为普通类时，必须存在空构造函数，属性只能是可读可写的(var)。
 * 3. 数据校验的默认实现是Kuark的bean校验方式(ValidationKit)
 * 4. 错误消息全部通过IllegalStateException异常抛出
 * 5. 如果需要对单元格的值作特殊处理，可重写getPropertyValue方法
 * 6. 如果需要复杂的校验逻辑，可重写validate方法
 *
 * @author K
 * @since 1.0.0
 */
abstract class AbstractExcelImporter<T : Any> : IExcelImporter<T> {

    private val log = LogFactory.getLog(AbstractExcelImporter::class)

    /**
     * 上传的excel文件的输入流
     */
    private lateinit var inputStream: InputStream

    /**
     * 第一个sheet页
     */
    private lateinit var sheet: Sheet

    private val propertyMap = mutableMapOf<String, KProperty1<T, Any?>>()

    private lateinit var propertyNames: List<String>

    /**
     * 按excel中的列顺序返回对应的属性名列表
     */
    protected abstract fun getPropertyNames(): List<String>

    /**
     * 返回sheet页的名称
     * @return sheet页的名称
     */
    protected abstract fun getSheetName(): String

    /**
     * 保存数据
     *
     * @param rowObjects 行对象列表
     */
    protected abstract fun save(rowObjects: List<T>)

    /**
     * 检查数据合法性
     *
     * @param rowObjects 行对象列表
     */
    protected open fun validate(rowObjects: List<T>) {
        for (rowObject in rowObjects) {
            val violations = ValidationKit.validateBean(rowObject)
            if (violations.isNotEmpty()) {
                error("导入的数据校验不通过：${violations.first().message}")
            }
        }
    }

    /**
     * 导入
     * @param xlsFile excel文件
     * @return 提示消息
     */
    override fun import(xlsFile: File): List<T> {
        return FileKit.openInputStream(xlsFile).use {
            this.inputStream = it
            checkTemplate()
            val rowObjects = wrapRowObjects()
            validate(rowObjects)
            save(rowObjects)
            rowObjects
        }
    }

    /**
     * 检查模板是否正确
     */
    protected open fun checkTemplate() {
        inputStream.use {
            val workbook = Workbook.getWorkbook(it)
            val sheetName = getSheetName()
            sheet = workbook.getSheet(sheetName) ?: error("找不到名称【$sheetName】对应的Sheet页！")
        }
    }

    /**
     * 将excel的每行包装成对象
     */
    protected open fun wrapRowObjects(): List<T> {
        val rowObjectList = mutableListOf<T>()
        propertyNames = getPropertyNames()
        try {
            val rows = sheet.rows
            val rowObjectClass = GenericKit.getSuperClassGenricClass(this::class) as KClass<T>
            lateinit var rowObject: T
            for (row in 1 until rows) { // 扣掉列头
                val rowCells = sheet.getRow(row)
                val propNameValueMap = mutableMapOf<String, Any>()
                if (!rowObjectClass.isData) {
                    rowObject = rowObjectClass.newInstance()
                }
                for (columnIndex in rowCells.indices) {
                    val cell = rowCells[columnIndex]
                    val value = getPropertyValue(rowObjectClass, columnIndex, cell)
                    val propertyName = propertyNames[columnIndex]
                    if (rowObjectClass.isData) {
                        propNameValueMap[propertyName] = value
                    } else {
                        val prop = propertyMap[propertyName] as KMutableProperty1
                        prop.set(rowObject, value)
                    }
                }
                if (rowObjectClass.isData) {
                    val values = rowObjectClass.primaryConstructor!!.parameters.map { propNameValueMap[it.name]!! }
                    rowObject = rowObjectClass.newInstance(*values.toTypedArray())
                }
                rowObjectList.add(rowObject)
            }
        } catch (ex: Exception) {
            log.error(ex)
            error("读取excel数据出错!")
        }
        return rowObjectList
    }

    /**
     * 得到属性值
     *
     * @param rowObjectClass 行对象类
     * @param columnIndex 列序（从0开始）
     * @param cell 单元格对象
     * @return 属性值
     */
    protected open fun getPropertyValue(rowObjectClass: KClass<T>, columnIndex: Int, cell: Cell): Any {
        val propertyName = propertyNames[columnIndex]
        var prop = propertyMap[propertyName]
        if (prop == null) {
            prop = rowObjectClass.getMemberProperty(propertyName)
            propertyMap[propertyName] = prop
        }
        val valueStr = cell.contents
        var value: Any = valueStr
        val type = cell.type
        if (type === CellType.NUMBER) {
            val argType = prop.returnType.classifier as KClass<*>
            value = valueStr.toType(argType)
        }
        return value
    }

}