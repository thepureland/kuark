package io.kuark.base.data.xls

import io.kuark.base.lang.GenericKit
import io.kuark.base.lang.reflect.getMemberMutableProperty
import io.kuark.base.lang.reflect.newInstance
import io.kuark.base.lang.string.toType
import io.kuark.base.log.LogFactory
import jxl.CellType
import jxl.Sheet
import jxl.Workbook
import java.io.InputStream
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1

/**
 * excel数据导入器抽象类
 *
 * @author K
 * @since 1.0.0
 */
abstract class AbstractExcelImporter<T : Any> : IExcelImporter {

    /**
     * 上传的excel文件的输入流
     */
    private lateinit var inputStream: InputStream

    /**
     * excel行对象列表
     */
    private lateinit var rowObjectList: MutableList<T>

    /**
     * 第一个sheet页
     */
    private lateinit var sheet: Sheet

    /**
     * 按列顺序返回属性名列表
     */
    protected abstract fun getPropertyNames(): List<String>


    /**
     * 导入
     * @param inputStream
     * @return
     */
    override fun import(inputStream: InputStream): String {
        var msg = "导入成功！"
        this.inputStream = inputStream
        if (!checkTemplate()) {
            msg = "请选择正确的模板!"
        } else {
            wrapRowObjects()
            try {
                check()
                save()
            } catch (ex: Exception) {
                msg = ex.message ?: ex.toString()
            }
        }
        return msg
    }

    /**
     * 检查模板是否正确
     * @return 模板是否正确
     */
    private fun checkTemplate(): Boolean {
        var success = true
        inputStream.use {
            try {
                val workbook = Workbook.getWorkbook(it)
                sheet = workbook.getSheet(0)
                if (sheetName != sheet.name) {
                    success = false
                }
            } catch (ex: Exception) {
                LOG.error(ex, "检查模板出错!")
            }
        }
        return success
    }

    /**
     * 取得第一个sheet页的名称
     * @return sheet页的名称
     */
    protected abstract val sheetName: String

    /**
     * 将excel的每行包装成对象
     */
    private fun wrapRowObjects() {
        try {
            val rows: Int = sheet.rows
            rowObjectList = mutableListOf()
            val propertyMap = mutableMapOf<String, KMutableProperty1<T, Any?>>()
            val rowObjectClass = GenericKit.getSuperClassGenricClass(this::class) as KClass<T>
            val properties = getPropertyNames()
            for (row in 1 until rows) { // 扣掉列头
                val rowCells = sheet.getRow(row)
                val rowObject = rowObjectClass.newInstance()
                var shouldAdd = false
                for (columnIndex in rowCells.indices) {
                    val cell = rowCells[columnIndex]
                    val propertyName = properties[columnIndex]
                    var prop = propertyMap[propertyName]
                    if (prop == null) {
                        prop = rowObjectClass.getMemberMutableProperty(propertyName)
                        propertyMap[propertyName] = prop
                    }
                    val valueStr = cell.contents
                    var value: Any = valueStr
                    val type = cell.type
                    if (type === CellType.NUMBER) {
                        val argType = prop.returnType.classifier as KClass<*>
                        value = valueStr.toType(argType)
                    }
                    if ("" != value.toString().trim()) {
                        shouldAdd = true
                        prop.set(rowObject, value)
                    }
                }
                if (shouldAdd) {
                    rowObjectList?.add(rowObject)
                }
            }
        } catch (ex: Exception) {
            LOG.error(ex, "读取excel数据出错!")
            throw RuntimeException(ex)
        }
    }

    /**
     * 检查数据合法性
     */
    protected abstract fun check()

    /**
     * 检查导入的值
     *
     * @param value 导入的值
     * @param name 名称描述
     * @param regex 正则表达式
     * @param isRequire 是否必填项
     */
    protected fun check(value: String, name: String, regex: String, isRequire: Boolean) {
        if (value.isBlank()) {
            if (isRequire) {
                throw Exception("$name 是必填项！")
            }
        } else {
            if (!value.matches(Regex(regex))) {
                throw Exception("$name 数据不合法，请参照说明！\t$value")
            }
        }
    }

    /**
     * 保存数据
     */
    protected abstract fun save()


    companion object {
        private val LOG = LogFactory.getLog(AbstractExcelImporter::class)
    }

}