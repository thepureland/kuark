package org.kuark.demo.tools.codegen.core

import org.kuark.demo.tools.codegen.model.vo.ColumnInfo
import org.kuark.demo.tools.codegen.model.vo.Config


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