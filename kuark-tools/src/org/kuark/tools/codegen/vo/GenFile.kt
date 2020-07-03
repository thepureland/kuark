package org.kuark.tools.codegen.vo

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty

class GenFile : Comparable<GenFile> {

    private val generate = SimpleBooleanProperty()
    private val filename = SimpleStringProperty()
    private val directory = SimpleStringProperty()
    var finalFileRelativePath: String // 参数化后的文件相对路径
    var templateFileRelativePath: String // 模板文件相对路径，为了生成时能找得到模板文件

    constructor(generate: Boolean, filename: String, directory: String, finalFileRelativePath: String, templateFileRelativePath: String) {
        setGenerate(generate)
        setFilename(filename!!)
        setDirectory(directory)
        this.finalFileRelativePath = finalFileRelativePath
        this.templateFileRelativePath = templateFileRelativePath
    }

    fun getGenerate(): Boolean {
        return generate.get()
    }

    fun setGenerate(generate: Boolean) {
        this.generate.set(generate)
    }

    fun getFilename(): String {
        return filename.get()
    }

    fun setFilename(filename: String) {
        this.filename.set(filename)
    }

    fun getDirectory(): String {
        return directory.get()
    }

    fun setDirectory(directory: String) {
        this.directory.set(directory)
    }

    override fun compareTo(o: GenFile): Int {
        return getDirectory().compareTo(o.getDirectory())
    }
}