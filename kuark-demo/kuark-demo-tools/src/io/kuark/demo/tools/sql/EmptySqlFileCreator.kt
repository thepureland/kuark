package io.kuark.demo.tools.sql

import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class EmptySqlFileCreator

/**
 * 在当前目录，生成规范文件名的空SQL文件。以jar包方式提供于各resources/sql目录。
 *
 * @author K
 * @since 1.0.0
 */
fun main() {    //TODO 怎么将该类单独打成jar包到resources/sql目录下
    val path = "D:/dev/kuark/kuark-data/kuark-data-jdbc/resources/sql/"
    val files = File(path).walk()
    val file = files.maxDepth(1)
        .filter { it.extension == "sql" }
        .sortedDescending()
        .first()
    val version = file.name.substring(0,5)
    val time = SimpleDateFormat("yyMMddHHmmss").format(Date())
    val fileName = "${version}.${time}__x_xxxx.sql"
    File(path + fileName).writeText("")
}

