package org.kuark.web.form.validation.support;

import java.util.regex.Matcher;

/**
 * 属性名转换器
 *
 * @author admin
 * @time 8/14/15 5:36 PM
 */
public class FormPropertyConverter {

    private FormPropertyConverter() {
    }

    /**
     * 转成以点分隔的属性名(以单引号括起来)
     *
     * @param property 原属性名
     * @param propertyPrefix 属性名前缀
     * @return 以点分隔的属性名(以单引号括起来)
     */
    public static String toPotQuote(String property, String propertyPrefix) {
        if (!property.startsWith("$") && propertyPrefix!=null &&!"".equals(propertyPrefix) && !property.startsWith("'") && !property.startsWith(propertyPrefix) && !property.contains("_")) {
            if (!property.contains(".")) { // 不是原始request里的参数名
                property = "'" + propertyPrefix + "." + property + "'";
            }
        }
        return toPotQuote(property);
    }

    /**
     * 转成以点分隔的属性名(以单引号括起来)
     *
     * @param property 原属性名
     * @return 以点分隔的属性名(以单引号括起来)
     */
    public static String toPotQuote(String property) {
        property = toPot(property);
        if (!property.startsWith("'") && (property.contains(".") || property.endsWith("[]"))) {
            property = "'" + property + "'";
        }
        return property;
    }

    /**
     * 转成以点分隔的属性名
     *
     * @param property 原属性名
     * @return 以点分隔的属性名
     */
    public static String toPot(String property) {
        property = property.replaceAll("\\$\\$", "[]"); // 数组处理
        if (property.startsWith("$")) {
            property = property.substring(1);
        }
        property = property.replaceAll("_", "."); // 有带"_"的为表单提交时属性名带"."的
        return property;
    }

    /**
     * 转成以下划线分隔的属性名
     *
     * @param property 原属性名
     * @return 以下划线分隔的属性名
     */
    public static String toUnderline(String property) {
        if(isArrayProperty(property)) {
            property = property.replaceAll("\\[\\d+\\]", Matcher.quoteReplacement("$$"));
        }
        if (property.contains(".")) {
            return property.replaceAll("\\.", "_");
        } else {
            return "$" + property;
        }
    }

    /**
     * 是否返回值为数组的属性
     *
     * @param property 属性名
     * @return true: 是否返回值为数组的属性，反之为false
     */
    public static boolean isArrayProperty(String property) {
        return property.matches("^.+\\[\\d+\\].*$");
    }

}
