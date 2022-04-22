package org.kuark.demo.tools.codegen.core.merge

import java.util.regex.Pattern

/**
 * 用户自定义代码抓取器
 *
 * @author K
 * @since 1.0.0
 */
class CustomCodesRetriever(private val fileContent: String) {
    fun retrieve(): Map<Int, String> {
        val map: MutableMap<Int, String> = HashMap()
        val p =
            Pattern.compile("(?<=(<!--)?#?//region your codes (\\d)(-->)?\\r?\\n)[\\s\\S]*?(?=(<!--)?#?//endregion your codes \\d(-->)?)")
        val m = p.matcher(fileContent)
        while (m.find()) {
            map[Integer.valueOf(m.group(2))] = m.group(0)
        }
        return map
    }

}