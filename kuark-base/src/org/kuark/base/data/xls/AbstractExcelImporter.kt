package org.kuark.base.data.xls

import jxl.Cell
import jxl.CellType
import jxl.Sheet
import jxl.Workbook
import org.kuark.base.log.LogFactory
import java.io.InputStream
import java.lang.reflect.Method
import java.util.*

/**
 * excel数据导入器抽象类
 *
 * @since 1.0.0
 */
abstract class AbstractExcelImporter : IExcelImporter {

    /**
     * List<字段名>
     */
    protected val fieldList: List<String> = ArrayList()

    /**
     * 上传的excel文件的输入流
     */
    protected var inputStream: InputStream? = null

    /**
     * excel行对象列表
     */
    protected var rowObjectList: ArrayList<Any>? = null

    /**
     * 第一个sheet页
     */
    protected var sheet: Sheet? = null

    /**
     * 获取行对象对应的类
     * @return
     */
    protected abstract val rowObjectClass: Class<Any>

    /**
     * 初始化字段名列表
     */
    protected abstract fun initFieldList()

    /**
     * 检查模板是否正确
     * @return 模板是否正确
     */
    private fun checkTemplate(): Boolean {
        var success = true
        inputStream.use {
            try {
                val workbook: Workbook = Workbook.getWorkbook(it)
                sheet = workbook.getSheet(0)
                if (sheetName != sheet!!.name) {
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
    protected fun wrapRowObjects() {
        try {
            val rows: Int = sheet!!.rows
            // 扣掉列头
            rowObjectList = ArrayList(rows - 1)
            val setterMap: MutableMap<String, Method?> =
                HashMap(fieldList.size)
            val rowObjectClass = rowObjectClass
            for (row in 1 until rows) {
                val rowCells: Array<Cell> = sheet!!.getRow(row)
                val rowObject = rowObjectClass.getDeclaredConstructor().newInstance()
                var shouldAdd = false
                for (column in rowCells.indices) {
                    val cell: Cell = rowCells[column]
                    val fieldName = fieldList[column]
                    var setter = setterMap[fieldName]
                    if (setter == null) {
                        val field = rowObjectClass.getDeclaredField(fieldName)
                        val setterName =
                            "set" + Character.toTitleCase(fieldName[0]) + fieldName.substring(1)
                        setter = rowObjectClass.getMethod(setterName, *arrayOf(field.type))
                        setterMap[fieldName] = setter
                    }
                    val valueStr: String = cell.getContents()
                    var value: Any? = valueStr
                    val type: CellType = cell.getType()
                    if (type === CellType.NUMBER) {
                        val argType = setter!!.parameterTypes[0]
                        if (argType == Int::class.java) {
                            value = Integer.valueOf(valueStr)
                        } else if (argType == Long::class.java) {
                            value = java.lang.Long.valueOf(valueStr)
                        } else if (argType == Double::class.java) {
                            value = java.lang.Double.valueOf(valueStr)
                        } else if (argType == Float::class.java) {
                            value = java.lang.Float.valueOf(valueStr)
                        }
                    }
                    if (value != null && "" != value.toString().trim { it <= ' ' }) {
                        shouldAdd = true
                        setter!!.invoke(rowObject, *arrayOf(value))
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
     * 导入
     * @param inputStream
     * @return
     */
    override fun doImport(inputStream: InputStream?): String {
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
                msg = ex.message!!
            }
        }
        return msg
    }

    /**
     * 检查数据合法性
     */
    protected abstract fun check()

    /**
     * 检查导入的值
     * @param value 导入的值
     * @param name 名称描述
     * @param regex 正则表达式
     * @param isRequire 是否必填项
     */
    protected fun check(
        value: String,
        name: String,
        regex: String,
        isRequire: Boolean
    ) {
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

    init {
        initFieldList()
    }
}