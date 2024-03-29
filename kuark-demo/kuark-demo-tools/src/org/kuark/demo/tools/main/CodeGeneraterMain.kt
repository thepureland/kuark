package org.kuark.demo.tools.main

import javafx.application.Application
import org.kuark.demo.tools.codegen.fx.ui.CodeGenerateWizard

fun main(args: Array<String>) {
    Application.launch(CodeGenerateWizard::class.java, *args) // 可继承CodeGenerateWizard，提供自定义的模板数据模型创建者
}