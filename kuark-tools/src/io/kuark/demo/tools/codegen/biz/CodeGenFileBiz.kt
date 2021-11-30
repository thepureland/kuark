package io.kuark.demo.tools.codegen.biz

import io.kuark.tools.codegen.core.CodeGeneratorContext
import io.kuark.tools.codegen.dao.CodeGenFileDao
import io.kuark.tools.codegen.model.po.CodeGenFile

/**
 * 生成的文件历史信息服务
 *
 * @author K
 * @since 1.0.0
 */
object CodeGenFileBiz {

    fun read(): List<String> {
        return CodeGenFileDao.searchCodeGenFileNames(CodeGeneratorContext.tableName)
    }

    fun save(files: Collection<String>): Boolean {
        val filesInDb = read()
        val codeGenFileList = mutableListOf<CodeGenFile>()
        files.filter { !filesInDb.contains(it) }.forEach { file ->
            codeGenFileList.add(
                CodeGenFile {
                    filename = file
                    objectName = CodeGeneratorContext.tableName
                }
            )
        }
        return CodeGenFileDao.batchInsert(codeGenFileList) == codeGenFileList.size
    }

}