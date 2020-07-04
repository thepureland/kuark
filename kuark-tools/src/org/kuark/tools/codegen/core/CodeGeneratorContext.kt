package org.kuark.tools.codegen.core

import org.kuark.tools.codegen.vo.ColumnInfo
import org.kuark.tools.codegen.vo.Config

/**
 * 代码生成器上下文
 *
 * @author K
 * @since 1.0.0
 */
object CodeGeneratorContext {

    lateinit var tableName: String
    lateinit var tableComment: String
    lateinit var columns: List<ColumnInfo>
    lateinit var config: Config
    lateinit var templateModelCreator: TemplateModelCreator

}