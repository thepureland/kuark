package org.kuark.tools.codegen.vo

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty

class GenFile : Comparable<GenFile> {

    private val generate: BooleanProperty = SimpleBooleanProperty()
    private val filename: StringProperty = SimpleStringProperty()
    private val directory: StringProperty = SimpleStringProperty()

    constructor(generate: Boolean, filename: String?, directory: String) {
        setGenerate(generate)
        setFilename(filename!!)
        setDirectory(directory)
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