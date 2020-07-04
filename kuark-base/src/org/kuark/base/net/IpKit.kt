package org.kuark.base.net

import org.kuark.base.lang.string.countMatches
import org.kuark.base.lang.string.repeatAndSeparate
import org.kuark.base.log.LogFactory
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.*
import java.util.regex.Pattern

/**
 * IP工具类，支持ipv4和ipv6
 *
 * @author K
 * @since 1.0.0
 */
object IpKit {

    private val LOG = LogFactory.getLog(this::class)

    // 二进制32位为全1的整数值
    private const val ALL32ONE = 4294967295L

    /**
     * 验证指定IP地址是否合法的ipv4
     *
     * @param ip 待验证的ip串
     * @return true: 为合法的ipv4地址
     * @since 1.0.0
     */
    fun isValidIpv4(ip: String): Boolean {
        if (ip.isBlank()) {
            return false
        }
        val st = StringTokenizer(ip, ".")
        var i = 0
        while (st.hasMoreTokens()) {
            val n = try {
                Integer.valueOf(st.nextToken())
            } catch (e: Exception) {
                return false
            }
            if (n > 255 || n < 0) {
                return false
            }
            i++
        }
        return i == 4
    }

    /**
     * 将ipv4地址字符串转换为数字表示
     *
     * @param ipv4 ipv4地址
     * @return ipv4的数值表示，非ipv4返回-1
     * @since 1.0.0
     */
    fun ipv4StringToLong(ipv4: String): Long {
        var ip = ipv4
        if (!isValidIpv4(ip)) {
            return -1
        }
        var temp: Long = 0
        var cur: String
        var pos = ip.indexOf(".", 0)
        while (pos != -1) {
            cur = ip.substring(0, pos)
            ip = ip.substring(pos + 1, ip.length)
            temp = temp shl 8 or java.lang.Long.valueOf(cur)
            pos = ip.indexOf(".", 0)
        }
        return temp shl 8 or java.lang.Long.valueOf(ip)
    }

    /**
     * 将IP地址数字转换成字符串表示
     *
     * @param ipv4Long ipv4长整型值, 小于0或大于4294967295将返回空串
     * @return ipv4地址，参数小于0或大于4294967295将返回空串
     * @since 1.0.0
     */
    fun ipv4LongToString(ipv4Long: Long): String {
        var ipLong = ipv4Long
        if (ipLong < 0 || ipLong > ALL32ONE) {
            return ""
        }
        val mask = 255L
        var result = ipLong and mask
        var temp = result.toString()
        for (i in 0..2) {
            ipLong = ipLong shr 8
            result = ipLong and mask
            temp = "$result.$temp"
        }
        return temp
    }

    /**
     * 取得定长的ipv4地址(每个段不足三位在前面用0补足)。 例如: 1.2.13.224 => 001.002.013.224
     *
     * @param ipv4 待处理的ipv4，如果ip非法返回空串
     * @return 定长的ipv4地址
     * @since 1.0.0
     */
    fun getFixLengthIpv4(ipv4: String): String {
        if (!isValidIpv4(ipv4)) {
            return ""
        }
        val parts = ipv4.split(".")
        return parts.joinToString(".") { it.padStart(3, '0') }
    }

    /**
     * 将定长的ipv4还原(每个段去掉左边的0). 例如: 001.002.013.224 => 1.2.13.224
     *
     * @param ipv4 待处理的ipv4，如果ip非法返回空串
     * @return 非定长的ipv4
     * @since 1.0.0
     */
    fun getNormalIpv4(ipv4: String): String {
        if (!isValidIpv4(ipv4)) {
            return ""
        }
        val parts = ipv4.split(".")
        return parts.joinToString(".") { it.toInt().toString() }
    }

    /**
     * 检查指定的ipv4地址是否都在同一网段
     *
     * @param maskAddress 子网掩码地址，非法将返回false
     * @param ipv4s ipv4地址可变数组，为空或其中某个ip非法都将返回false
     * @return true: 指定的ipv4地址均在同一网段
     * @since 1.0.0
     */
    fun isSameIpv4Seg(maskAddress: String, vararg ipv4s: String): Boolean {
        if (maskAddress.isBlank() || ipv4s.isEmpty()) {
            return false
        }
        val maskIp = ipv4StringToLong(maskAddress)
        if (maskIp == -1L) {
            return false
        }
        var firstValue: Long? = null
        for (ipv4 in ipv4s) {
            val ipLong = ipv4StringToLong(ipv4)
            val value = maskIp and ipLong
            if (firstValue == null) {
                firstValue = value
            } else {
                if (firstValue != value) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * 返回指定的两个ipv4地址(大小不分先后)间的所有ipv4地址,
     * 包括指定的两个ipv4地址，按从小到大的顺序, 两个ip一样将只返回一个
     *
     * 只支持最多65536个的ip地址，超过的话将返回空列表
     *
     * @param beginIp 开始值，包括, 非法ip将返回空列表
     * @param endIp 结束值，包括, 非法ip将返回空列表
     * @return 一个包含指定的两个ipv4地址间的所有ipv4地址的列表, 两个参数任一个非法或超过65536个的ip地址将返回空列表
     * @since 1.0.0
     */
    fun getIpv4sBetween(beginIp: String, endIp: String): List<String> {
        var beginIpStr = beginIp
        var endIpStr = endIp
        if (beginIpStr.isEmpty() && endIpStr.isEmpty()) {
            return emptyList()
        }
        if (beginIpStr.isEmpty()) {
            beginIpStr = "0.0.0.0"
        }
        var longBeginip = ipv4StringToLong(beginIpStr)
        if (longBeginip == -1L) {
            return emptyList()
        }
        if (endIpStr.isEmpty()) {
            endIpStr = "255.255.255.255"
        }
        var longEndip = ipv4StringToLong(endIpStr)
        if (longEndip == -1L) {
            return emptyList()
        }
        if (longBeginip > longEndip) {
            val temp = longBeginip
            longBeginip = longEndip
            longEndip = temp
        }
        // 求解范围之内的IP地址
        val size = (longEndip - longBeginip).toInt() + 1
        if (size > 65536 || size < 0) {
            return emptyList()
        } else if (size == 1) {
            return listOf(beginIpStr)
        }
        val longIps = LongArray(size)
        for (k in 0 until size) {
            longIps[k] = longBeginip + k.toLong()
        }
        // 各个段装换成字符串
        val strip = arrayOfNulls<String>(4)
        val ipList: MutableList<String> = ArrayList(size)
        for (longIp in longIps) {
            strip[0] = (longIp and 0x00000000000000ff).toString()
            strip[1] = (longIp shr 8 and 0x00000000000000ff).toString()
            strip[2] = (longIp shr 16 and 0x00000000000000ff).toString()
            strip[3] = (longIp shr 24 and 0x00000000000000ff).toString()
            ipList.add(strip[3].toString() + "." + strip[2] + "." + strip[1] + "." + strip[0])
        }
        return ipList
    }

    /**
     * 判断是否为本地ipv4地址。如：127.0.0.1、192.168.0.123
     *
     * @param ipv4 待检查的ipv4地址
     * @return true: 为本地ipv4地址
     * @since 1.0.0
     */
    fun isLocalIpv4(ipv4: String): Boolean {
        if ("127.0.0.1" == ipv4) {
            return true
        }
        val l = ipv4StringToLong(ipv4)
        return if (l >= 3232235520L) {
            l <= 3232301055L
        } else l in 167772160L..184549375L
    }

    /**
     * 返回本机的本地ip地址
     *
     * @return 本机的本地ip地址
     * @since 1.0.0
     */
    fun getLocalIp(): String {
        return try {
            InetAddress.getLocalHost().hostAddress
        } catch (e: UnknownHostException) {
            LOG.error(e)
            ""
        }
    }

    /**
     * 判断给定的字符串是否为有效的ipv6(包含冒分十六进制表示法、0位压缩表示法、内嵌IPv4地址表示法)
     *
     * @param ipStr ip串
     * @return ture: ipv6，false: 非ipv6
     */
    fun isValidIpv6(ipStr: String): Boolean {
        if (ipStr.isBlank()) return false
        val colonCount = ipStr.countMatches(":")
        if (ipStr.length > 45 || colonCount > 7) {
            return false
        } else if (colonCount == 7 && !ipStr.contains("::") && ipStr.length <= 39 &&
            Regex("^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$").matches(ipStr)
        ) {
            return true
        } else if (ipStr.length < 39 && Regex("^(([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4})*)?)::((([0-9A-Fa-f]{1,4}:)*[0-9A-Fa-f]{1,4})?)$").matches(
                ipStr
            )
        ) {
            return true
        } else if (Regex("""^([0-9a-fA-F]{1,4}:){6}(25[0-5]|2[0-4]\d|[0-1]?\d?\d)(\.(25[0-5]|2[0-4]\d|[0-1]?\d?\d)){3}$""").matches(
                ipStr
            )
        ) {
            return colonCount < 7
        } else if (Regex("""^(([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4})*)?)::((([0-9A-Fa-f]{1,4}:)*[0-9A-Fa-f]{1,4}:)?)(25[0-5]|2[0-4]\d|[0-1]?\d?\d)(\.(25[0-5]|2[0-4]\d|[0-1]?\d?\d)){3}$""").matches(
                ipStr
            )
        ) {
            return colonCount < 7
        }
        return false
    }

    /**
     * 将ipv4转为标准全格式的ipv6(每组定长4位的16进制，共8组，每组间用半角冒号分隔)
     *
     * @param ipv4 ipv4地址
     * @return 如果传入参数不是ipv4，将返回原始传入串
     */
    fun ipv4ToIpv6(ipv4: String): String {
        if (isValidIpv4(ipv4)) {
            val parts = ipv4.split(".").toTypedArray()
            val part1 = Integer.toHexString(Integer.valueOf(parts[0])).toUpperCase()
            val part2 = Integer.toHexString(Integer.valueOf(parts[1])).toUpperCase()
            val part3 = Integer.toHexString(Integer.valueOf(parts[2])).toUpperCase()
            val part4 = Integer.toHexString(Integer.valueOf(parts[3])).toUpperCase()
            val ipv6Str = part1.padStart(2, '0') + part2.padStart(2, '0') +
                    ":" + part3.padStart(2, '0') + part4.padStart(2, '0')
            return "0000:0000:0000:0000:0000:0000:$ipv6Str"
        }
        return ipv4
    }

    /**
     * 标准化ip地址为全格式的ipv6地址(每组定长4位的16进制，共8组，每组间用半角冒号分隔)
     * 对以下情况的参数进行标准化，其他情况将直接返回原始参数：
     * 1. ipv4，如192.168.0.1 => 0000:0000:0000:0000:0000:0000:C0A8:0001
     * 2. 0位压缩表示法的ipv6，如FF01::1101 => FF01:0000:0000:0000:0000:0000:0000:1101、
     * ::1 => 0000:0000:0000:0000:0000:0000:0000:0001、
     * :: => 0000:0000:0000:0000:0000:0000:0000:0000、
     * FF01:0:0:0:0:0:0:1101 => FF01:0000:0000:0000:0000:0000:0000:1101
     * 3. 内嵌IPv4地址表示法的ipv6，如::192.168.0.1  => 0000:0000:0000:0000:0000:0000:C0A8:0001 、
     * ::FFFF:192.168.0.1 => 0000:0000:0000:0000:0000:FFFF:C0A8:0001
     *
     * @param ipStr ip地址，可以是ipv4或ipv6的三种表示法(冒分十六进制表示法、0位压缩表示法、内嵌IPv4地址表示法)
     * @return 标准全格式的ipv6
     */
    fun standardizeIpv6(ipStr: String): String? {
        // 处理ipv6
        if (ipStr == "::") {
            return "0000:0000:0000:0000:0000:0000:0000:0000"
        }
        if (isValidIpv6(ipStr)) {
            if (ipStr.length == 39) {
                return ipStr
            }
            var ipv6 = ipStr
            if (ipStr.contains(".")) { // 处理内嵌IPv4地址表示法的ipv6
                val regExp = "((25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3})"
                val pattern = Pattern.compile(regExp)
                val matcher = pattern.matcher(ipStr)
                matcher.find()
                val ipv4 = matcher.group(0)
                val ipv4Part = ipv4ToIpv6(ipv4).substring(30)
                ipv6 = if (ipStr.contains("::")) {
                    val colonCount = ipStr.countMatches(":")
                    var leakGroupCount = 6 - colonCount
                    if (ipStr.startsWith("::")) {
                        leakGroupCount++
                    }
                    val leakStr = "0000".repeatAndSeparate(":", leakGroupCount)
                    ipStr.replace("::", ":$leakStr:").replace(ipv4, ipv4Part)
                } else {
                    ipStr.replace(ipv4, ipv4Part)
                }
            } else if (ipStr.contains("::")) { // 处理0位压缩表示法的ipv6
                val colonCount = ipStr.countMatches(":")
                val leakGroupCount = 8 - colonCount
                val leakStr = "0000".repeatAndSeparate(":", leakGroupCount)
                ipv6 = ipStr.replace("::", ":$leakStr:")
            }

            // 补全
            if (ipv6.length != 39) {
                val parts = ipv6.split(":").toTypedArray()
                val sb = StringBuilder()
                parts.forEach { sb.append(it.padStart(4, '0')).append(":") }
                ipv6 = sb.toString().substring(0, sb.length - 1)
            }
            return ipv6
        }

        // 处理ipv4
        return ipv4ToIpv6(ipStr)
    }

}