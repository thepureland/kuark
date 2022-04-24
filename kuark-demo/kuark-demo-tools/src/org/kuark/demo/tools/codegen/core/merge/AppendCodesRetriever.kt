package org.kuark.demo.tools.codegen.core.merge

import java.util.regex.Pattern

/**
 * 拼接代码抓取器
 *
 * @author K
 * @since 1.0.0
 */
class AppendCodesRetriever(private val fileContent: String?) {

    fun retrieve(): Map<Int, Pair<AppendCodeType, String>> {
        val map = hashMapOf<Int, Pair<AppendCodeType, String>>()
        val p =
            Pattern.compile("(?<=(<!--)?#?//region append (\\w{1,10}) codes (\\d)(-->)?\\r?\\n)[\\s\\S]*?(?=(<!--)?#?//endregion append \\w+ codes \\d(-->)?)")
        val m = p.matcher(fileContent)
        while (m.find()) {
            map[m.group(3).toInt()] = Pair(AppendCodeType.valueOf(m.group(2)), m.group(0))
        }
        return map
    }

}