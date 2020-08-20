package io.kuark.tools.main

import javafx.application.Application
import io.kuark.tools.codegen.fx.ui.CodeGenerateWizard
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication //TODO ??? 为什么去掉会报找不到Application类
open class CodeGeneraterMain

fun main(args: Array<String>) {
    Application.launch(CodeGenerateWizard::class.java, *args) // 可继承CodeGenerateWizard，提供自定义的模板数据模型创建者
}