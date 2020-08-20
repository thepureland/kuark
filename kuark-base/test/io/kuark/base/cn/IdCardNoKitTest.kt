package io.kuark.base.cn

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import io.kuark.base.support.enums.Sex

/**
 * IdCardNoKit测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class IdCardNoKitTest {

    private val MAIN_F_18 = "210502198412020944" // 大陆，辽宁，女, 18位

    private val MAIN_F_15 = "210502841202094" // 大陆，辽宁，女, 15位


    private val HK = "R6728757"

    private val MACAU = "5215299(8)"

    private val TW_M = "J109830254"
    private val TW_F = "T240298298"

    @Test
    fun conver15CardTo18() {
        assertEquals(MAIN_F_18, IdCardNoKit.convert15To18(MAIN_F_15))
        assertNull(IdCardNoKit.convert15To18(""))
        assertNull(IdCardNoKit.convert15To18(MAIN_F_18))
    }

    @Test
    fun isIdCardNo() {
        assert(IdCardNoKit.isIdCardNo(MAIN_F_15))
        assert(IdCardNoKit.isIdCardNo(MAIN_F_18))
        assert(IdCardNoKit.isIdCardNo(HK))
        assert(IdCardNoKit.isIdCardNo(MACAU))
        assert(IdCardNoKit.isIdCardNo(TW_M))
        assert(IdCardNoKit.isIdCardNo(TW_F))
        assertFalse(IdCardNoKit.isIdCardNo(""))
        assertFalse(IdCardNoKit.isIdCardNo("1234556"))
    }

    @Test
    fun isIdCardNo18() {
        assert(IdCardNoKit.isIdCardNo18(MAIN_F_18))
        assertFalse(IdCardNoKit.isIdCardNo18(MAIN_F_15))
        assertFalse(IdCardNoKit.isIdCardNo18(HK))
        assertFalse(IdCardNoKit.isIdCardNo18(MACAU))
        assertFalse(IdCardNoKit.isIdCardNo18(TW_M))
        assertFalse(IdCardNoKit.isIdCardNo18(TW_F))
        assertFalse(IdCardNoKit.isIdCardNo18(""))
        assertFalse(IdCardNoKit.isIdCardNo18("1234556"))
    }

    @Test
    fun isIdCardNo15() {
        assert(IdCardNoKit.isIdCardNo15(MAIN_F_15))
        assertFalse(IdCardNoKit.isIdCardNo15(MAIN_F_18))
        assertFalse(IdCardNoKit.isIdCardNo15(HK))
        assertFalse(IdCardNoKit.isIdCardNo15(MACAU))
        assertFalse(IdCardNoKit.isIdCardNo15(TW_M))
        assertFalse(IdCardNoKit.isIdCardNo15(TW_F))
        assertFalse(IdCardNoKit.isIdCardNo15(""))
        assertFalse(IdCardNoKit.isIdCardNo15("1234556"))
    }

    @Test
    fun isTwIdCardNo() {
        assert(IdCardNoKit.isTwIdCardNo(TW_M))
        assert(IdCardNoKit.isTwIdCardNo(TW_F))
        assertFalse(IdCardNoKit.isTwIdCardNo(MAIN_F_15))
        assertFalse(IdCardNoKit.isTwIdCardNo(MAIN_F_18))
        assertFalse(IdCardNoKit.isTwIdCardNo(HK))
        assertFalse(IdCardNoKit.isTwIdCardNo(MACAU))
        assertFalse(IdCardNoKit.isTwIdCardNo(""))
        assertFalse(IdCardNoKit.isTwIdCardNo("1234556"))
    }

    @Test
    fun isHkIdCardNo() {
        assert(IdCardNoKit.isHkIdCardNo(HK))
        assertFalse(IdCardNoKit.isHkIdCardNo(MACAU))
        assertFalse(IdCardNoKit.isHkIdCardNo(TW_M))
        assertFalse(IdCardNoKit.isHkIdCardNo(TW_F))
        assertFalse(IdCardNoKit.isHkIdCardNo(MAIN_F_15))
        assertFalse(IdCardNoKit.isHkIdCardNo(MAIN_F_18))
        assertFalse(IdCardNoKit.isHkIdCardNo(""))
        assertFalse(IdCardNoKit.isHkIdCardNo("1234556"))
    }

    @Test
    fun isMacauIdCardNo() {
        assert(IdCardNoKit.isMacauIdCardNo(MACAU))
        assertFalse(IdCardNoKit.isMacauIdCardNo(HK))
        assertFalse(IdCardNoKit.isMacauIdCardNo(TW_M))
        assertFalse(IdCardNoKit.isMacauIdCardNo(TW_F))
        assertFalse(IdCardNoKit.isMacauIdCardNo(MAIN_F_15))
        assertFalse(IdCardNoKit.isMacauIdCardNo(MAIN_F_18))
        assertFalse(IdCardNoKit.isMacauIdCardNo(""))
        assertFalse(IdCardNoKit.isMacauIdCardNo("1234556"))
    }

    @Test
    fun getBirthday() {
        assertEquals("19841202", IdCardNoKit.getBirthday(MAIN_F_15))
        assertEquals("19841202", IdCardNoKit.getBirthday(MAIN_F_18))
        assertNull(IdCardNoKit.getBirthday(HK))
        assertNull(IdCardNoKit.getBirthday(MACAU))
        assertNull(IdCardNoKit.getBirthday(TW_M))
        assertNull(IdCardNoKit.getBirthday(TW_F))
        assertNull(IdCardNoKit.getBirthday(""))
        assertNull(IdCardNoKit.getBirthday("1234556"))
    }

    @Test
    fun getSex() {
        assertEquals(Sex.FEMALE, IdCardNoKit.getSex(MAIN_F_15))
        assertEquals(Sex.FEMALE, IdCardNoKit.getSex(MAIN_F_18))
        assertEquals(Sex.SECRET, IdCardNoKit.getSex(HK))
        assertEquals(Sex.MALE, IdCardNoKit.getSex(TW_M))
        assertEquals(Sex.FEMALE, IdCardNoKit.getSex(TW_F))
        assertEquals(Sex.SECRET, IdCardNoKit.getSex(""))
        assertEquals(Sex.SECRET, IdCardNoKit.getSex("1234556"))
    }

    @Test
    fun getProvince() {
        assertEquals(Province.LIAO_NING, IdCardNoKit.getProvince(MAIN_F_15))
        assertEquals(Province.LIAO_NING, IdCardNoKit.getProvince(MAIN_F_18))
        assertEquals(Province.XIANG_GANG, IdCardNoKit.getProvince(HK))
        assertEquals(Province.AO_MEN, IdCardNoKit.getProvince(MACAU))
        assertEquals(Province.TAI_WAN, IdCardNoKit.getProvince(TW_M))
        assertEquals(Province.TAI_WAN, IdCardNoKit.getProvince(TW_F))
        assertEquals(null, IdCardNoKit.getProvince(""))
        assertEquals(null, IdCardNoKit.getProvince("1234556"))
    }
    
}