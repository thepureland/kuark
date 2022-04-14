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

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val codes = """public void test() {//region your codes 7-->
  System.out.println(1);<!--//endregion your codes 7-->}public void test() {//region your codes 2
System.out.println(2);System.out.println(20);//endregion your codes 2}"""
            var p =
                Pattern.compile("(?<=(<!--)?//region your codes (\\d)(-->)?\\n)[\\s\\S]*?(?=(<!--)?//endregion your codes \\d(-->)?)")
            var m = p.matcher(codes)
            while (m.find()) {
                println(m.group(0))
                println(m.group(2))
            }

//        String imports =
//                "import java.util.HashMap;\n" +
//                        "import java.util.List;\n" +
//                        "import java.util.Map;\n" +
//                        "import java.util.regex.Matcher;\n" +
//                        "import java.util.regex.Pattern;\n";
            val imports = "kkjk"
            p = Pattern.compile("import .+?;")
            m = p.matcher(imports)
            while (m.find()) {
                println(m.group(0))
            }
        }
    }

}