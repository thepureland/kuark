package io.kuark.demo.tools.codegen.core.merge

import io.kuark.base.io.FileKit
import java.io.File

/**
 * 私有内容擦除器
 *
 * @author K
 * @since 1.0.0
 */
object PrivateContentEraser {

    fun erase(file: File) {
        var content = FileKit.readFileToString(file)
        content = content.replace("(<!--)?#?//region append \\w+ codes (\\d)(-->)?\\r\\n".toRegex(), "")
            .replace("(<!--)?#?//endregion append \\w+ codes \\d(-->)?".toRegex(), "")
        FileKit.write(file, content)
    }

}