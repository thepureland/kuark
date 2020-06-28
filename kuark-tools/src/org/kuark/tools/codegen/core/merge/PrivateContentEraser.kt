package org.kuark.tools.codegen.core.merge

import org.kuark.base.io.FileKit
import java.io.File

object PrivateContentEraser {
    fun erase(file: File) {
        var content = FileKit.readFileToString(file)
        content = content.replace("(<!--)?#?//region append \\w+ codes (\\d)(-->)?\\r\\n".toRegex(), "")
            .replace("(<!--)?#?//endregion append \\w+ codes \\d(-->)?".toRegex(), "")
        FileKit.write(file, content)
    }
}