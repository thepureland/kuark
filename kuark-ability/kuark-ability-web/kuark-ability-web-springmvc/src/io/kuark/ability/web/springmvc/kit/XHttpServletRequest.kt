package io.kuark.ability.web.springmvc.kit

import io.kuark.base.lang.string.StringKit
import io.kuark.base.net.IpKit
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest


/**
 * 获取请求的真实ip地址，支持多级反向代理
 *
 * @return ip地址
 * @author K
 * @since 1.0.0
 */
fun HttpServletRequest.getRemoteIp(): String {
    var ip = this.getHeader("x-forwarded-for")
    if (StringKit.isBlank(ip) || "unknown".equals(ip, true)) {
        ip = this.getHeader("Proxy-Client-IP")
    }
    if (StringKit.isBlank(ip) || "unknown".equals(ip, true)) {
        ip = this.getHeader("WL-Proxy-Client-IP")
    }
    if (StringKit.isBlank(ip) || "unknown".equals(ip, true)) {
        ip = this.remoteAddr
    }
    return getIP(ip)
}

/**
 * 获取请求的浏览器信息
 *
 * @return Pair(浏览器名称，版本)
 * @author K
 * @since 1.0.0
 */
fun HttpServletRequest.getBrowserInfo(): Pair<String, String> {
    val agent = this.getHeader("User-Agent")
    var name = "unknown"
    var version = "unknown"
    if (StringKit.isBlank(agent)) {
//        error("用户浏览器头未提供User-Agent信息，${this.requestURL}")
        return Pair(name, version)
    }
    var regex = "Version/([0-9.]+)"
    when {
        agent.contains("MSIE") -> {
            name = "MSIE" // 微软IE
            regex = "$name\\s([0-9.]+)"
        }
        agent.contains("Firefox") -> {
            name = "Firefox" // 火狐
            regex = "$name/([0-9.]+)"
        }
        agent.contains("Chrome") -> {
            name = "Chrome" // Google
            regex = "$name/([0-9.]+)"
        }
        agent.contains("Opera") -> name = "Opera"
        agent.contains("Safari") -> name = "Safari"
        agent.contains("app_android") -> name = "Android App"
        agent.contains("app_ios") -> name = "IOS App"
        agent.contains("Trident") -> name = "Trident"
        agent.contains("Edge") -> name = "Edge"
        agent.contains("Maxthon") -> name = "Maxthon" // 遨游浏览器
        agent.contains("qqbrowser") -> name = "qqbrowser"
        agent.contains("lbbrowser") -> name = "lbbrowser" // 猎豹浏览器
        agent.contains("UCBrowser") -> name = "UCBrowser"
        agent.contains("360SE") -> name = "360SE"
    }
    if (regex != "unknown") {
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(agent)
        if (matcher.find()) {
            version = matcher.group(1)
        }
    }
    return Pair(name, version)
}


/**
 * 获取请求的操作系统信息
 *
 * @return Pair(操作系统名称，版本)
 * @author K
 * @since 1.0.0
 */
fun HttpServletRequest.getOsInfo(): Pair<String, String> {
    val agent = this.getHeader("User-Agent")
    var name = "unknown"
    var version = "unknown"
    if (StringKit.isBlank(agent)) {
//        error("用户浏览器头未提供User-Agent信息，${this.requestURL}")
        return Pair(name, version)
    }
    when {
        agent.contains("Windows") -> {
            name = "Windows" //如：win7 = Windows NT 6.1
            val pattern = Pattern.compile("$name\\s([a-zA-Z0-9]+\\s[0-9.]+)")
            val matcher = pattern.matcher(agent)
            if (matcher.find()) {
                version = matcher.group(1)
            }
        }
        agent.contains("FreeBSD") -> name = "FreeBSD"
        agent.contains("Macintosh") -> name = "Mac"
        agent.contains("SunOS") -> name = "Solaris"
        agent.contains("app_android") -> name = "app_android"
        agent.contains("app_ios") -> name = "app_ios"
        agent.contains("Android") -> name = "Android"
        agent.contains("x11") || agent.contains("unix") -> name = "Unix"
        agent.contains("iPhone") || agent.contains("iPad") -> name = "ios"
        agent.contains("Linux") -> {
            name = "Linux"
            if (agent.contains("Ubuntu")) {
                version = "Ubuntu"
            }
        }
    }
    return Pair(name, version)
}

/**
 * 返回请求终端类型
 *
 * @return 终端类型
 * @author K
 * @since 1.0.0
 */
fun HttpServletRequest.getClientTeminal(): String {
    val agent = this.getHeader("User-Agent")
    var name = "unknown"
    if (StringKit.isBlank(agent)) {
//        LOG.warn("请求日志{0},用户浏览器头未提供User-Agent信息", request.requestURL)
        return name
    }
    name = if (agent.contains("app_android") || agent.contains("app_ios")) {
        "App"
    } else if (agent.contains("Android") || agent.contains("iPhone") || agent.contains("iPad") ||
        agent.contains("Windows Phone") || agent.contains("BlackBerry") || agent.contains("SymbianOS")
    ) {
        "Mobile"
    } else {
        "PC"
    }
    return name
}

/**
 * 获取站点的根路径，即协议+主机+端口+上下文
 *
 * @return 站点的根路径
 * @author K
 * @since 1.0.0
 */
fun HttpServletRequest.getRootPath(): String {
    val requestURL = this.requestURL
    val requestURI = this.requestURI
    val root = requestURL.substring(0, requestURL.length - requestURI.length)
    return root + this.contextPath
}

/**
 * 获取站点的根路径，即协议+主机+端口
 *
 * @return 站点的根路径
 * @author K
 * @since 1.0.0
 */
fun HttpServletRequest.getDomainPath(): String {
    val requestURL = this.requestURL
    val requestURI = this.requestURI
    return requestURL.substring(0, requestURL.length - requestURI.length)
}

private fun getIP(ip: String): String {
    // 处理多级反向代理
    var ipAddress = ip
    val ips = ipAddress.split(",").toTypedArray()
    for (i in ips.indices) {
        ipAddress = ips[i].trim { it <= ' ' }
        val ipLong: Long = IpKit.ipv4StringToLong(ipAddress)
        if (!isLocalA(ipLong) && !isLocalB(ipLong) && !isLocalC(ipLong) && !isLocal0(ipLong)) {
            break
        }
    }
    // 表示通过远程登陆访问页面
    if ("0:0:0:0:0:0:0:1" == ipAddress) {
        try {
            ipAddress = InetAddress.getLocalHost().hostAddress
        } catch (e: UnknownHostException) {
            error(e)
        }
    }
    return ipAddress
}


private fun isLocalC(ip: Long): Boolean {
    return ip >= IpKit.ipv4StringToLong("192.168.0.0") && ip <= IpKit.ipv4StringToLong("192.168.255.255")
}

private fun isLocalB(ip: Long): Boolean {
    return ip >= IpKit.ipv4StringToLong("172.16.0.0") && ip <= IpKit.ipv4StringToLong("172.31.255.255")
}

private fun isLocalA(ip: Long): Boolean {
    return ip >= IpKit.ipv4StringToLong("10.0.0.0") && ip <= IpKit.ipv4StringToLong("10.255.255.255")
}

private fun isLocal0(ip: Long): Boolean {
    return ip == IpKit.ipv4StringToLong("127.0.0.1") || ip == IpKit.ipv4StringToLong("0.0.0.0")
}