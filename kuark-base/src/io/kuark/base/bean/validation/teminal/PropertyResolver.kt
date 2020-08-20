package io.kuark.base.bean.validation.teminal

import java.util.regex.Matcher
import kotlin.reflect.full.memberProperties

/**
 * 属性名处理工具
 *
 * @author K
 * @since 1.0.0
 */
object PropertyResolver {

    /**
     * 转成以点分隔的属性名(以单引号括起来)
     *
     * @param property 原属性名
     * @param propertyPrefix 属性名前缀
     * @return 以点分隔的属性名(以单引号括起来)
     */
    fun toPotQuote(property: String, propertyPrefix: String?): String {
        var property = property
        if (!property.startsWith("$") && propertyPrefix != null && "" != propertyPrefix && !property.startsWith("'") && !property.startsWith(
                propertyPrefix
            ) && !property.contains("_")
        ) {
            if (!property.contains(".")) { // 不是原始request里的参数名
                property = "'$propertyPrefix.$property'"
            }
        }
        return toPotQuote(property)
    }

    /**
     * 转成以点分隔的属性名(以单引号括起来)
     *
     * @param property 原属性名
     * @return 以点分隔的属性名(以单引号括起来)
     */
    fun toPotQuote(property: String): String {
        var property = property
        property = toPot(property)
        if (!property.startsWith("'") && (property.contains(".") || property.endsWith("[]"))) {
            property = "'$property'"
        }
        return property
    }

    /**
     * 转成以点分隔的属性名
     *
     * @param property 原属性名
     * @return 以点分隔的属性名
     */
    fun toPot(property: String): String {
        var property = property
        property = property.replace("\\$\\$".toRegex(), "[]") // 数组处理
        if (property.startsWith("$")) {
            property = property.substring(1)
        }
        property = property.replace("_".toRegex(), ".") // 有带"_"的为表单提交时属性名带"."的
        return property
    }

    /**
     * 转成以下划线分隔的属性名
     *
     * @param property 原属性名
     * @return 以下划线分隔的属性名
     */
    fun toUnderline(property: String): String {
        var property = property
        if (isArrayProperty(property)) {
            property = property.replace("\\[\\d+\\]".toRegex(), Matcher.quoteReplacement("$$"))
        }
        return if (property.contains(".")) {
            property.replace("\\.".toRegex(), "_")
        } else {
            "$$property"
        }
    }

    /**
     * 是否返回值为数组的属性
     *
     * @param property 属性名
     * @return true: 是否返回值为数组的属性，反之为false
     */
    fun isArrayProperty(property: String): Boolean {
        return property.matches("^.+\\[\\d+\\].*$".toRegex())
    }
}

data class Test(val age: Int)

fun main() {
    val prop = Test::class.memberProperties.first { it.name == "age" }
    val returnType = prop.returnType
    println(returnType)
}