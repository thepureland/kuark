package io.kuark.tools.main

import io.kuark.tools.codegen.fx.ui.CodeGenerateWizard
import javafx.application.Application
import org.springframework.boot.autoconfigure.SpringBootApplication


//TODO ??? 为什么动不动会报找不到Application类

@SpringBootApplication
open class CodeGeneraterMain

fun main(args: Array<String>) {
    Application.launch(CodeGenerateWizard::class.java, *args) // 可继承CodeGenerateWizard，提供自定义的模板数据模型创建者
}