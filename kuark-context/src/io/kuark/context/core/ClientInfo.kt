package io.kuark.context.core

import java.util.*

/**
 * 客户端信息
 *
 * @author K
 * @since 1.0.0
 */
class ClientInfo private constructor(builder: Builder) {

    /** 请求的ip */
    var ip: String? = null

    /** 访问的域名 */
    var domain: String? = null

    /** 访问的url */
    var url: String? = null

    /** 请求的参数 */
    var params: String? = null

    /** 请求内容字节数据表示 */
    var requestContent: ByteArray? = null

    /** 请求内容字符串表示 */
    var requestContentString: String? = requestContent?.toString()

    /** 请求referer */
    var requestReferer: String? = null

    /** 请求类型(GET/POST等) */
    var requestType: String? = null

    /** 客户端操作系统 */
    var os: String? = null

    /** 客户端浏览器 */
    var browser: String? = null

    /** 客户端地区-语言 */
    var locale: Locale? = null

    /** 客户端时区 */
    var timeZone: TimeZone? = null


    init {
        ip = builder.ip
        domain = builder.domain
        url = builder.url
        params = builder.params
        requestContent = builder.requestContent
        requestContentString = builder.requestContentString
        requestReferer = builder.requestReferer
        requestType = builder.requestType
        os = builder.os
        browser = builder.browser
        locale = builder.locale
        timeZone = builder.timeZone
    }

    /**
     * 客户端信息对象构建者
     *
     * @author K
     * @since 1.0.0
     */
    class Builder {

        /** 请求的ip */
        internal var ip: String? = null

        /** 访问的域名 */
        internal var domain: String? = null

        /** 访问的url */
        internal var url: String? = null

        /** 请求的参数 */
        internal var params: String? = null

        /** 请求内容字节数据表示 */
        internal var requestContent: ByteArray? = null

        /** 请求内容字符串表示 */
        internal var requestContentString: String? = requestContent?.toString()

        /** 请求referer */
        internal var requestReferer: String? = null

        /** 请求类型(GET/POST等) */
        internal var requestType: String? = null

        /** 客户端操作系统 */
        internal var os: String? = null

        /** 客户端浏览器 */
        internal var browser: String? = null

        /** 客户端地区-语言 */
        internal var locale: Locale? = null

        /** 客户端时区 */
        internal var timeZone: TimeZone? = null


        fun build(): ClientInfo = ClientInfo(this)


        fun ip(ip: String?): Builder {
            this.ip = ip
            return this
        }

        fun domain(domain: String?): Builder {
            this.domain = domain
            return this
        }

        fun url(url: String?): Builder {
            this.url = url
            return this
        }

        fun params(params: String?): Builder {
            this.params = params
            return this
        }

        fun requestContent(requestContent: ByteArray?): Builder {
            this.requestContent = requestContent
            return this
        }

        fun requestContentString(requestContentString: String?): Builder {
            this.requestContentString = requestContentString
            return this
        }

        fun requestReferer(requestReferer: String?): Builder {
            this.requestReferer = requestReferer
            return this
        }

        fun requestType(requestType: String?): Builder {
            this.requestType = requestType
            return this
        }

        fun os(os: String?): Builder {
            this.os = os
            return this
        }

        fun browser(browser: String?): Builder {
            this.browser = browser
            return this
        }

        fun locale(locale: Locale?): Builder {
            this.locale = locale
            return this
        }

        fun timeZone(timeZone: TimeZone?): Builder {
            this.timeZone = timeZone
            return this
        }

    }

}