package org.kuark.tools.codegen.core

import java.io.File

class GenerateConfig {

    var outRootFolder: File? = null
    var templateRootDir: File? = null
    var module: String? = null
    var webModule: String? = null
    var topModule: String? = null
    var subModule: String? = null

    var sourceFolder: String? = null
    var packageFolder: String? = null
    var enModelName: String? = null
    var isOverWrite = true
    var hasPrintModel = false
    var hasEditModel = false
    var hasDelModel = false
    var hasGenerateOrmModel = false

    //主键
    var idStrategy: String? = null


    var isOverride = java.lang.Boolean.parseBoolean(System.getProperty("gg.isOverride", "false"))
    var ignoreOutput = false
    var mergeLocation: String? = null
    var outRoot: String? = null
    var outputEncoding: String? = null
    var sourceFile: String? = null
    var sourceDir: String? = null
    var sourceFileName: String? = null
    var sourceEncoding: String = "UTF-8"
    var deleteGeneratedFile = false

}