package io.kuark.base.data.xls

import java.io.InputStream

/**
 * excel数据导入器接口
 *
 * @author
 * @since 1.0.0
 */
interface IExcelImporter {
    /**
     * 执行导入
     *
     * @param inputStream 上传的excel文件的输入流
     * @return 执行结果信息
     * @since 1.0.0
     */
    fun doImport(inputStream: InputStream?): String
}