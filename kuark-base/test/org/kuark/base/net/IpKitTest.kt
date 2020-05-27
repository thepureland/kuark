package org.kuark.base.net

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class IpKitTest {

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
        assertTrue(IpKit.isValidIpv6("0000:0000:0000:0000:0000:0000:C0A8:0001"))
        assertTrue(IpKit.isValidIpv6("C0A8:0000:0000:0000:0000:0000:C0A8:0001"))
        assertTrue(IpKit.isValidIpv6("::0000:0000:0000:0000:C0A8:0001"))
        assertTrue(IpKit.isValidIpv6("::0001"))
        assertTrue(IpKit.isValidIpv6("::"))
        assertTrue(IpKit.isValidIpv6("FF01:0:0:0:0:0:0:1101"))
        assertTrue(IpKit.isValidIpv6("0:0:0:0:0:0:0:1"))
        assertTrue(IpKit.isValidIpv6("0:0:0:0:0:0:0:0"))
        assertTrue(IpKit.isValidIpv6("::192.168.0.1"))
        assertTrue(IpKit.isValidIpv6("FFFF::192.168.0.1"))
        assertTrue(IpKit.isValidIpv6("FFFF:FFFF::192.168.0.1"))
        assertTrue(IpKit.isValidIpv6("FFFF:FFFF::FFFF:192.168.0.1"))
        assertTrue(IpKit.isValidIpv6("::FFFF:192.168.0.1"))
        assertTrue(IpKit.isValidIpv6("0000:0000:0000:0000:0000:0000:192.168.0.1"))
        assertTrue(IpKit.isValidIpv6("0:0:0:0:0:0:192.168.0.1"))
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

