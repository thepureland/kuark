package org.kuark.base.net

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

/**
 * IpKit测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class IpKitTest {

    @Test
    fun isValidIpv4() {
        assert(IpKit.isValidIpv4("192.168.0.123"))
        assert(IpKit.isValidIpv4("127.0.0.1"))
        assert(IpKit.isValidIpv4("127.0.0.01"))
        assert(IpKit.isValidIpv4("0.0.0.0"))
        assert(IpKit.isValidIpv4("255.255.255.255"))
        assertFalse(IpKit.isValidIpv4("0.0.0.0.0"))
        assertFalse(IpKit.isValidIpv4("192.168.0.256"))
        assertFalse(IpKit.isValidIpv4(""))
        assertFalse(IpKit.isValidIpv4("invalid ip"))
        assertFalse(IpKit.isValidIpv4("..."))
    }

    @Test
    fun ipv4StringToLong() {
        assert(2130706433L == IpKit.ipv4StringToLong("127.0.0.1"))
        assert(2130706433L == IpKit.ipv4StringToLong("127.0.0.01"))
        assertEquals(-1, IpKit.ipv4StringToLong("192.168.0.256"))
        assertEquals(-1, IpKit.ipv4StringToLong(""))
        assertEquals(-1, IpKit.ipv4StringToLong("invalid ip"))
        assertEquals(-1, IpKit.ipv4StringToLong("..."))
    }

    @Test
    fun ipv4LongToString() {
        assertEquals("127.0.0.1", IpKit.ipv4LongToString(2130706433L))
        assertEquals("", IpKit.ipv4LongToString(-1))
        assertEquals("", IpKit.ipv4LongToString(99999999999L))
        assertEquals("0.0.0.0", IpKit.ipv4LongToString(0))
    }

    @Test
    fun getIpv4sBetween() {
        var ipv4s: List<String?> = IpKit.getIpv4sBetween("127.0.0.1", "127.0.0.3")
        assertEquals(3, ipv4s.size)
        assertEquals("127.0.0.1", ipv4s[0])
        assertEquals("127.0.0.2", ipv4s[1])
        assertEquals("127.0.0.3", ipv4s[2])

        ipv4s = IpKit.getIpv4sBetween("127.0.0.3", "127.0.0.1")
        assertEquals(3, ipv4s.size)
        assertEquals("127.0.0.1", ipv4s[0])
        assertEquals("127.0.0.2", ipv4s[1])
        assertEquals("127.0.0.3", ipv4s[2])

        ipv4s = IpKit.getIpv4sBetween("127.0.0.0", "")
        assertEquals(0, ipv4s.size)

        ipv4s = IpKit.getIpv4sBetween("", "127.0.0.3")
        assertEquals(0, ipv4s.size)

        ipv4s = IpKit.getIpv4sBetween("invalid ip", "127.0.0.3")
        assertEquals(0, ipv4s.size)

        ipv4s = IpKit.getIpv4sBetween("127.0.0.0", "127.0.0.0")
        assertEquals(1, ipv4s.size)
    }

    @Test
    fun isSameIpv4Seg() {
        assert(IpKit.isSameIpv4Seg("255.255.255.0", "1.2.3.4", "1.2.3.5"))
        assert(IpKit.isSameIpv4Seg("255.255.0.0", "1.2.4.5", "1.2.6.7"))
        assert(IpKit.isSameIpv4Seg("255.0.0.0", "1.3.4.5", "1.6.7.8"))
        assert(IpKit.isSameIpv4Seg("0.0.0.0", "1.2.3.4", "5.6.7.8"))
        assertFalse(IpKit.isSameIpv4Seg("255.255.255.0", "1.2.3.4", "1.2.5.6"))
        assertFalse(IpKit.isSameIpv4Seg("255.255.0.0", "1.3.4.5", "1.6.7.8"))
        assertFalse(IpKit.isSameIpv4Seg("255.0.0.0", "1.2.3.4", "5.6.7.8"))
        assertFalse(IpKit.isSameIpv4Seg("255.255.255.0", "1.2.3.4", "5.6.7.888"))
        assertFalse(IpKit.isSameIpv4Seg("255.255.255.0"))
    }

    @Test
    fun getFixLengthIpv4() {
        assertEquals("001.002.013.224", IpKit.getFixLengthIpv4("1.2.13.224"))
        assertEquals("", IpKit.getFixLengthIpv4("1.2.13.256"))
    }

    @Test
    fun getNormalIpv4() {
        assertEquals("1.2.13.224", IpKit.getNormalIpv4("001.002.013.224"))
        assertEquals("", IpKit.getNormalIpv4("1.2.13.256"))
    }

    @Test
    fun isLocalIpv4() {
        assert(IpKit.isLocalIpv4("127.0.0.1"))
        assert(IpKit.isLocalIpv4("192.168.0.123"))
        assertFalse(IpKit.isLocalIpv4("220.160.156.242"))
    }

    @Test
    fun ipv4ToIpv6() {
        assertEquals("0000:0000:0000:0000:0000:0000:C0A8:0001", IpKit.ipv4ToIpv6("192.168.0.1"))
        assertEquals("0000:0000:0000:0000:0000:0000:C0A8:0001", IpKit.ipv4ToIpv6("192.168.0.001"))
        assertEquals("999.168.0.001", IpKit.ipv4ToIpv6("999.168.0.001"))
        assertEquals("", IpKit.ipv4ToIpv6(""))
    }

    @Test
    fun isValidIpv6() {
        assertFalse(IpKit.isValidIpv6("192.168.0.1"))
        assertFalse(IpKit.isValidIpv6("sdfdsflksdkj"))
        assertFalse(IpKit.isValidIpv6(""))
        assertFalse(IpKit.isValidIpv6("C0A8:0001"))
        assertFalse(IpKit.isValidIpv6(":C0A8:0001"))
        assertFalse(IpKit.isValidIpv6(":192.168.0.1"))
        assertFalse(IpKit.isValidIpv6("0000:0000:0000:0000:0000:0000:0000:C0A8:0001"))
        assertFalse(IpKit.isValidIpv6("0000:0000:0000:0000::0000:0000:0000:C0A8:0001"))
        assertFalse(IpKit.isValidIpv6("0000:0000::0000::0000:C0A8:0001"))
        assertFalse(IpKit.isValidIpv6("0000:0000::0000:::0000:C0A8:0001"))
        assertFalse(IpKit.isValidIpv6("0000:0000::0000:::0000:C0A8:0001:192.168.0.1"))
        assertFalse(IpKit.isValidIpv6("0000:0000:0000:0000:0000:0000:G0A8:0001"))
        assertFalse(IpKit.isValidIpv6("0000:0000:0000:0000:0000:0000:*:0001"))
        assertFalse(IpKit.isValidIpv6("0000:0000:0000:0000:0000:0000::192.168.0.1"))
        assertFalse(IpKit.isValidIpv6("::::192.168.0.1"))
        assertFalse(IpKit.isValidIpv6("0:0:0:0:0:0:0:"))
        assertFalse(IpKit.isValidIpv6(":0:0:0:0:0:0:0"))
        assertFalse(IpKit.isValidIpv6("::::"))
        assertFalse(IpKit.isValidIpv6(":"))
        assertFalse(IpKit.isValidIpv6(":192.168.0.1"))
        assertFalse(IpKit.isValidIpv6("FFFF:FFFF:0000:FFFF:0000:FFFF::192.168.0.1"))
        assert(IpKit.isValidIpv6("0000:0000:0000:0000:0000:0000:C0A8:0001"))
        assert(IpKit.isValidIpv6("C0A8:0000:0000:0000:0000:0000:C0A8:0001"))
        assert(IpKit.isValidIpv6("::0000:0000:0000:0000:C0A8:0001"))
        assert(IpKit.isValidIpv6("::0001"))
        assert(IpKit.isValidIpv6("::"))
        assert(IpKit.isValidIpv6("FF01:0:0:0:0:0:0:1101"))
        assert(IpKit.isValidIpv6("0:0:0:0:0:0:0:1"))
        assert(IpKit.isValidIpv6("0:0:0:0:0:0:0:0"))
        assert(IpKit.isValidIpv6("::192.168.0.1"))
        assert(IpKit.isValidIpv6("FFFF::192.168.0.1"))
        assert(IpKit.isValidIpv6("FFFF:FFFF::192.168.0.1"))
        assert(IpKit.isValidIpv6("FFFF:FFFF::FFFF:192.168.0.1"))
        assert(IpKit.isValidIpv6("::FFFF:192.168.0.1"))
        assert(IpKit.isValidIpv6("0000:0000:0000:0000:0000:0000:192.168.0.1"))
        assert(IpKit.isValidIpv6("0:0:0:0:0:0:192.168.0.1"))
    }

    @Test
    fun standardizeIpv6() {
        assertEquals("0000:0000:0000:0000:0000:0000:C0A8:0001", IpKit.standardizeIpv6("192.168.0.1"))
        assertEquals(
            "ABCD:EF01:2345:6789:ABCD:EF01:2345:6789", IpKit.standardizeIpv6("ABCD:EF01:2345:6789:ABCD:EF01:2345:6789")
        )
        assertEquals("2001:0DB8:0000:0023:0008:0800:200C:417A", IpKit.standardizeIpv6("2001:DB8:0:23:8:800:200C:417A"))
        assertEquals("FF01:0000:0000:0000:0000:0000:0000:1101", IpKit.standardizeIpv6("FF01:0:0:0:0:0:0:1101"))
        assertEquals("FF01:0000:0000:0000:0000:0000:0000:1101", IpKit.standardizeIpv6("FF01::1101"))
        assertEquals("0000:0000:0000:0000:0000:0000:0000:0001", IpKit.standardizeIpv6("0:0:0:0:0:0:0:1"))
        assertEquals("0000:0000:0000:0000:0000:0000:0000:0001", IpKit.standardizeIpv6("::1"))
        assertEquals("0000:0000:0000:0000:0000:0000:0000:0000", IpKit.standardizeIpv6("0:0:0:0:0:0:0:0"))
        assertEquals("0000:0000:0000:0000:0000:0000:0000:0000", IpKit.standardizeIpv6("::"))
        assertEquals("0000:0000:0000:0000:0000:0000:C0A8:0001", IpKit.standardizeIpv6("::192.168.0.1"))
        assertEquals("0000:0000:0000:0000:0000:FFFF:C0A8:0001", IpKit.standardizeIpv6("::FFFF:192.168.0.1"))
    }


}

