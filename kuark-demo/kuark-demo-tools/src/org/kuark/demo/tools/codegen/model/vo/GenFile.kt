package org.kuark.demo.tools.codegen.model.vo

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty

/**
 * 生成的文件信息值对象
 *
 * @author K
 * @since 1.0.0
 */
class GenFile(
    generate: Boolean,

    filename: String,

    directory: String,

    // 参数化后的文件相对路径
    var finalFileRelativePath: String,

    // 模板文件相对路径，为了生成时能找得到模板文件
    var templateFileRelativePath: String
) : Comparable<GenFile> {

    private val generate = SimpleBooleanProperty()
    private val filename = SimpleStringProperty()
    private val directory = SimpleStringProperty()

    init {
        setGenerate(generate)
        setFilename(filename)
        setDirectory(directory)
    }

    fun getGenerate(): Boolean = generate.get()

    fun generateProperty(): BooleanProperty = generate

    fun setGenerate(generate: Boolean) = this.generate.set(generate)

    fun getFilename(): String = filename.get()

    fun setFilename(filename: String) = this.filename.set(filename)

    fun filenameProperty(): StringProperty = filename

    fun getDirectory(): String = directory.get()

    fun setDirectory(directory: String) = this.directory.set(directory)

    fun directoryProperty(): StringProperty = directory

    override fun compareTo(other: GenFile): Int = getDirectory().compareTo(other.getDirectory())
}