package io.kuark.demo.tools.codegen.biz

import io.kuark.demo.tools.codegen.model.po.CodeGenFile

/**
 * 生成的文件历史信息服务
 *
 * @author K
 * @since 1.0.0
 */
object CodeGenFileBiz {

    fun read(): List<String> {
        return io.kuark.demo.tools.codegen.dao.CodeGenFileDao.searchCodeGenFileNames(io.kuark.demo.tools.codegen.core.CodeGeneratorContext.tableName)
    }

    fun save(files: Collection<String>): Boolean {
        val filesInDb = io.kuark.demo.tools.codegen.biz.CodeGenFileBiz.read()
        val codeGenFileList = mutableListOf<CodeGenFile>()
        files.filter { !filesInDb.contains(it) }.forEach { file ->
            codeGenFileList.add(
                CodeGenFile {
                    filename = file
                    objectName = io.kuark.demo.tools.codegen.core.CodeGeneratorContext.tableName
                }
            )
        }
        return io.kuark.demo.tools.codegen.dao.CodeGenFileDao.batchInsert(codeGenFileList) == codeGenFileList.size
    }

}