package org.kuark.validation.constraints.support

/**
 * 正则表达式常量
 *
 * Create by (admin) on 2015/2/4.
 */
object RegExpConstants {

    /**
     * 手机号码
     */
    const val CELL_PHONE = "^1(3[0-9]|4[579]|5[0-35-9]|6[0-9]|7[0-9]|8[0-9]|9[0-9])\\d{8}$"

    /**
     * QQ号码
     */
    const val QQ = "^\\d{5,11}$"

    /**
     * 手机或电话号码，7-20位数字
     */
    const val NUMBER_PHONE = "^\\d{7,20}$"

    /**
     * ipv4
     */
    const val IPV4 = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$"

    /**
     * SKYPE
     */
    const val SKYPE = "^[a-zA-Z][a-zA-Z0-9]{5,31}$"

    /**
     * url地址
     */
    const val URL =
        ("^(https?|s?ftp):\\/\\/(((([A-Za-z]|\\d|-|\\.|_|~|" + "[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:)*@)?(((\\d|" + "[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d\\d|"
                + "2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]))|((([A-Za-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|" + "(([A-Za-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([A-Za-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*"
                + "([A-Za-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([A-Za-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|" + "(([A-Za-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([A-Za-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*"
                + "([A-Za-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.?)(:\\d*)?)(\\/((([A-Za-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|"
                + "(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:|@)+(\\/(([A-Za-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(%[\\da-f]{2})|"
                + "[!\\$&'\\(\\)\\*\\+,;=]|:|@)*)*)?)?(\\?((([A-Za-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:"
                + "|@)|[\\uE000-\\uF8FF]|\\/|\\?)*)?(#((([A-Za-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:|@)|\\/|\\?)*)?$")

    /**
     * 电话或手机号
     */
    const val TEL_OR_CELL =
        "^0\\d{2,3}-?\\d{7,8}$|^(0\\d{2,3})-?(\\d{7,8})-?(\\d{1,4})$|^1(3[0-9]|4[57]|5[0-35-9]|7[0-9]|8[0-35-9])\\d{8}$"

    /**
     * 姓名（汉字、英文和·）
     */
    const val NAME = "^[a-z\\u4E00-\\u9FA5\\u0800-\\u4e00\\\\A-Z\\·]{2,30}$"

    /**
     * 真实姓名 英文名、字母、空格、分隔圆点、英文句号；中文名：中文、分隔圆点 不能纯数; 日文名
     */
    const val REALNAME = "^[a-zA-Z\\u0020\\u4E00-\\u9FA5\\u0800-\\u4e00\\*]" +
            "[a-zA-Z0-9\\u0020\\u4E00-\\u9FA5\\u0800-\\u4e00\\·\\.\\* ]{0,28}" +
            "[a-zA-Z\\u0020\\u4E00-\\u9FA5\\u0800-\\u4e00\\*]$"

    /**
     * 银行账户中姓名校验
     */
    const val BANKNAME = "^[a-zA-Z\\u4E00-\\u9FA5\\u0800-\\u4e00\\*]" +
            "[a-zA-Z0-9\\u4E00-\\u9FA5\\u0800-\\u4e00\\·\\.()（）\\* ]{0,28}" +
            "[a-zA-Z\\u4E00-\\u9FA5\\u0800-\\u4e00\\*()（）]$"

    /**
     * 存款人
     */
    const val PAYERNAME =
        "^[a-zA-Z\\u4E00-\\u9FA5\\u0800-\\u4e00][a-zA-Z\\u4E00-\\u9FA5\\u0800-\\u4e00\\·\\. ]{0,28}[a-zA-Z\\u4E00-\\u9FA5\\u0800-\\u4e00]$"

    /**
     * 不包含特殊字符
     */
    const val SPECIAL = "^[^&*=|{}<>/\\…—]*$"

    /**
     * 邮箱
     */
    const val EMAIL = "^[a-zA-Z0-9]+([._\\-]*[a-zA-Z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$"

    /**
     * 邮箱或手机号
     */
    const val MAIL_OR_CELL =
        "^([a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+)|(1(3[0-9]|5[0-35-9]|7[0-9]|8[0-35-9])\\d{8})$"

    /**
     * 电话号码
     */
    const val TELPHONE =
        "^(((0\\d{2})[-](\\d{8})$)|(^(0\\d{3})[-](\\d{7,8})$)|(^(0\\d{2})[-](\\d{8})-(\\d+)$)|(^(0\\d{3})[-](\\d{7,8})-(\\d+)))$"

    /**
     * 登录密码
     */
    const val LOGIN_PWD = "^[A-Za-z0-9~\\\\\\-!@#$%^&*()_+\\{\\}\\[\\]|\\:;'\"<>,./?]{6,20}$"

    /**
     * 安全密码
     */
    const val SECURITY_PWD = "^[0-9]{6}$"

    /**
     * 正整数
     */
    const val POSITIVE_INTEGER = "^[1-9]\\d*$"

    /**
     * IPv4地址 集合
     */
    const val IPV4_LIST =
        "^((25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3})(;((25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}))*$"

    /**
     * IPv6 标准
     */
    const val IPV6_STD = "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$"

    /**
     * IPv6 十六进制压缩
     */
    const val IPV6_HEX =
        "^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$"

    /**
     * 字符重复
     */
    const val CHAR_REPEAT = "^(.)\\1+$"

    /**
     * 纯数值
     */
    const val DIGIT = "^\\d+$"

    /**
     * 纯字母
     */
    const val CHARACTOR = "^[a-zA-Z]+$"

    /**
     * 纯字母
     */
    const val LOWCASECHARACTOR = "^[a-z]+$"

    /**
     * 只匹配数字和字母组合，但不能是纯数字
     * 如：asd123
     */
    const val ONLY_CHARACTER_AND_DIGIT = "^(?!\\d+$)[\\da-z]+$"
    const val NICK_NAME = "^[a-zA-Z0-9\\u4E00-\\u9FA5\\u0800-\\u4e00]{3,15}$"
    const val ANSWER = "^(.){1,30}$"

    /**
     * MSN
     * 修改为和邮箱相同的规则
     */
    const val MSN = EMAIL

    /*密码强度*/
    const val PASSWORD_LEVEL_1 = "^[a-zA-Z]+$"
    const val PASSWORD_LEVEL_2 = "^[0-9]+$"
    const val PASSWORD_LEVEL_3 = "^[0-9a-zA-Z]+$"
    const val PASSWORD_LEVEL_4 = "^[A-Za-z0-9~!@#$%^&*()_+\\\\{\\\\}\\\\[\\\\]|\\\\:;\\'\\\"<>,./?]+$"
    const val CONCEDE_POINTS =
        "^(\\-?)((?:[0-9]{1,4})(?:\\.\\d{1,2})?)(\\/?|\\-?)((?:[0-9]{1,4}|0)(?:\\.\\d{1,2})?)?$"

    /**
     * 正数
     */
    const val PLUS_QUANTITY = "^(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}$"

    /**
     * 匹配整数小数
     */
    const val QUANTITY = "^([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])$"

    const val ALL_NUMBER = "^(-|\\+)?\\d+$"

    /**
     * 銀行卡
     */
    const val BANK = "^[0-9]{10,25}$"

    /**
     * 验证比特币值(含8位小数点且大于0.00001)
     */
    const val BIT_AMOUNT = "^(?!0+(?:\\.0+)?$)(((?:[1-9]\\d*(\\.\\d{1,8})?))|(0\\.\\d{1,5}))?$"

    /***
     * 订单号后5为（字母或数字）
     */
    const val BANKORDER = "^[a-zA-Z0-9]{5}$"

    /***
     * 判断多个站点ID由逗号隔开
     */
    const val SITE_ID_COMMA = "^\\d+(,\\d+)*$|^\\d+$|^(\\s)*$"

    /**
     * 空或正整数
     */
    const val NULL_POSITIVE_INTEGER = "^([0-9]*[1-9][0-9]*)?$"

    /**
     * 数字长度
     */
    const val NUMBER_LENGTH = "^\\d{0,9}$"

    /**
     * 匹配金额
     */
    const val MONEY_NUM = "^\\-?([1-9]\\d{0,9}|0)([.]?|(\\.\\d{1,2})?)$"

    /**
     * 微信
     */
    const val WECHAT = "^[a-zA-Z0-9]{1}[-_a-zA-Z0-9]{5,19}$"

    /**
     * 玩家账号(含游客账号),单个账号
     */
    const val ACCOUNT_AND_GUEST_ACCOUNT = "^[a-zA-Z0-9_\\$][a-zA-Z0-9_]{3,14}$"

    /**
     * uuid
     */
    const val UUID_RE = "^\\w{8}(-\\w{4}){3}-\\w{12}$"

    const val DOMAIN = "^(([a-zA-Z0-9]+\\.)+([a-zA-Z0-9]+){1}\\,)*(([a-zA-Z0-9]+\\.)+([a-zA-Z0-9]+){1}){1}$"

    const val DIGITS = "^[0-1].*$"

    /**
     * 验证金额
     */
    const val MONEY = "^(?!0+(?:\\.0+)?$)(?:[1-9]\\d*|0)(?:\\.\\d{1,2})?$"

    /**
     * 含0和正数
     */
    const val ZERO_POSITIVE = "^[0-9].*$"

    /**
     * 验证手机（验证只带数字）
     */
    const val MOBILE = "^[0-9]*$"

    /**
     * 中文或英文大小写和数字
     */
    const val CNANDEN_NUMBER = "^[0-9a-zA-Z\u4e00-\u9fa5]+$"

}