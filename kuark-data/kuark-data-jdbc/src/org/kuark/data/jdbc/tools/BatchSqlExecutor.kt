package org.kuark.data.jdbc.tools

import org.kuark.base.io.FileKit
import java.io.File
import java.sql.DriverManager

fun main() {
    val file = "C:\\Users\\hanfei\\Desktop\\area2019.sql" // 文件编码一定要是UTF8无签名，不然会出现莫名其妙的错误
    val lineIterator = FileKit.lineIterator(File(file), "UTF-8")


//    Class.forName("org.h2.Driver")
    val conne = DriverManager.getConnection(
        "jdbc:h2:tcp://localhost:9092/D:/dev/kuark/kuark-data/kuark-data-jdbc/h2/h2",
        "sa",
        null
    );
    var stm = conne.createStatement();
    val start = System.currentTimeMillis();
    conne.autoCommit = false;
    var i = 0

    while (lineIterator.hasNext()) {
        stm.addBatch(lineIterator.next())
        i++
        if (i % 5000 == 0) {
            stm.executeBatch();
            conne.commit();
            println("共提交${i}条")
        }
    }

    if(!lineIterator.hasNext()) {
        stm.executeBatch();
        conne.commit();
    }

    val end = System.currentTimeMillis();
    println("添加${i}条数据总共耗时：${end - start}ms");
    stm.close()
    conne.close()

}