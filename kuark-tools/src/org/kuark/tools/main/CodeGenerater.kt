package org.kuark.tools.main

import javafx.application.Application
import org.kuark.tools.codegen.fx.ui.CodeGenerateWizard
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
open class CodeGenerater

fun main(args: Array<String>) {
    Application.launch(CodeGenerateWizard::class.java, *args)
}