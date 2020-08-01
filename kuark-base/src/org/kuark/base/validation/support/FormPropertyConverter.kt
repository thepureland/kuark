package org.kuark.base.validation.support

import java.util.regex.Matcher

/**
 * 属性名转换器
 *
 * @author admin
 * @time 8/14/15 5:36 PM
 */
object FormPropertyConverter {
    /**
     * 转成以点分隔的属性名(以单引号括起来)
     *
     * @param property 原属性名
     * @param propertyPrefix 属性名前缀
     * @return 以点分隔的属性名(以单引号括起来)
     */
    fun toPotQuote(property: String, propertyPrefix: String?): String {
        var prop = property
        if (!prop.startsWith("$") && propertyPrefix != null && "" != propertyPrefix && !prop.startsWith("'") && !prop.startsWith(
                propertyPrefix
            ) && !prop.contains("_")
        ) {
            if (!prop.contains(".")) { // 不是原始request里的参数名
                prop = "'$propertyPrefix.$prop'"
            }
        }
        return toPotQuote(prop)
    }

    /**
     * 转成以点分隔的属性名(以单引号括起来)
     *
     * @param property 原属性名
     * @return 以点分隔的属性名(以单引号括起来)
     */
    fun toPotQuote(property: String): String {
        var prop = property
        prop = toPot(prop)
        if (!prop.startsWith("'") && (prop.contains(".") || prop.endsWith("[]"))) {
            prop = "'$prop'"
        }
        return prop
    }

    /**
     * 转成以点分隔的属性名
     *
     * @param property 原属性名
     * @return 以点分隔的属性名
     */
    fun toPot(property: String): String {
        var prop = property
        prop = prop.replace("\\$\\$".toRegex(), "[]") // 数组处理
        if (prop.startsWith("$")) {
            prop = prop.substring(1)
        }
        prop = prop.replace("_".toRegex(), ".") // 有带"_"的为表单提交时属性名带"."的
        return prop
    }

    /**
     * 转成以下划线分隔的属性名
     *
     * @param property 原属性名
     * @return 以下划线分隔的属性名
     */
    fun toUnderline(property: String): String {
        var prop = property
        if (isArrayProperty(prop)) {
            prop = prop.replace("\\[\\d+\\]".toRegex(), Matcher.quoteReplacement("$$"))
        }
        return if (prop.contains(".")) {
            prop.replace("\\.".toRegex(), "_")
        } else {
            "$$prop"
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