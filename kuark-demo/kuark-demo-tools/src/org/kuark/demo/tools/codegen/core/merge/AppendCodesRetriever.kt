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

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val str = """
                #这里放置所有的表对应的字段名称的国际化数据
                #格式: TableName.ColumnName=xxxxxx
                #//region your codes 1

                #//region append PARTIBLE codes 1
                SysResource.name=资源名称
                SysResource.url=资源url
                SysResource.remark=资源简要描述
                SysResource.parentId=父项id
                SysResource.structure=资源的层级结构
                SysResource.sortNum=排序号
                SysResource.subsysCode=所属子系统编号
                SysResource.permission=权限
                SysResource.resourceType=资源类型
                SysResource.icon=图标
                #//endregion append PARTIBLE codes 1

                #//endregion your codes 1

                """.trimIndent()
            val p =
                Pattern.compile("(?<=(<!--)?#?//region append (\\w{1,10}) codes (\\d)(-->)?\\n)[\\s\\S]*?(?=(<!--)?#?//endregion append \\w+ codes \\d(-->)?)")
            val m = p.matcher(str)
            while (m.find()) {
//            map.put(Integer.valueOf(m.group(3)), m.group(0));
                println(m.group(2))
                println(m.group(3))
                println(m.group(0))
            }
        }
    }

}