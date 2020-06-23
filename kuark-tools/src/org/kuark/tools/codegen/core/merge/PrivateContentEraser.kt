package org.kuark.tools.codegen.core.merge

import org.kuark.base.io.FileKit
import java.io.File

/**
 * Create by (admin) on 7/13/15.
 */
object PrivateContentEraser {
    fun erase(file: File) {
        var content = FileKit.readFileToString(file)
        content = content.replace("(<!--)?#?//region append \\w+ codes (\\d)(-->)?\\r\\n".toRegex(), "")
            .replace("(<!--)?#?//endregion append \\w+ codes \\d(-->)?".toRegex(), "")
        FileKit.write(file, content)
    }
}