package io.kuark.base.cn

import io.kuark.base.lang.string.isNumeric
import io.kuark.base.support.enums.Sex
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 身份证工具类.
 *
 * @author K
 * @since 1.0.0
 */
object IdCardNoKit {

    /** 中国大陆公民身份证号码最小长度。  */
    private const val MAINLAND_ID_MIN_LENGTH = 15

    /** 中国大陆公民身份证号码最大长度。  */
    private const val MAINLAND_ID_MAX_LENGTH = 18

    /**
     * 每位加权因子
     */
    private val power = intArrayOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)

    /**
     * 第18位校检码
     */
    private val verifyCode = arrayOf("1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2")

    /** 最低年限  */
    private const val MIN = 1930

    /** 台湾身份首字母对应数字  */
    private val twFirstCode = mutableMapOf<String, Int>()

    /**
     * 将15位身份证号码转换为18位(大陆)
     *
     * @param idCardNo15 15位身份编码, 非法值将返回null
     * @return 18位身份编码
     * @since 1.0.0
     */
    fun convert15To18(idCardNo15: String): String? {
        if (idCardNo15!!.isBlank() || idCardNo15.length != MAINLAND_ID_MIN_LENGTH) {
            return null
        }
        var idCard18: String
        if (idCardNo15.isNumeric()) {
            // 获取出生年月日
            val birthday = idCardNo15.substring(6, 12)
            var birthDate: Date? = null
            try {
                birthDate = SimpleDateFormat("yyMMdd").parse(birthday)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val cal = Calendar.getInstance()
            if (birthDate != null) {
                cal.time = birthDate
            }
            // 获取出生年(完全表现形式,如：2010)
            val sYear = cal[Calendar.YEAR].toString()
            idCard18 = idCardNo15.substring(0, 6) + sYear + idCardNo15.substring(8)
            // 转换字符数组
            val cArr = idCard18.toCharArray()
            val iCard = converCharToInt(cArr)
            val iSum17 = getPowerSum(iCard)
            // 获取校验位
            val sVal = getCheckCode18(iSum17)
            idCard18 += if (sVal.isNotEmpty()) {
                sVal
            } else {
                return null
            }
        } else {
            return null
        }
        return idCard18
    }

    /**
     * 检查指定字符串是否为身份证号(包括大陆、港、澳、台)
     *
     * @param str 待检查的字符串, 为null返回false
     * @return true: 为身份证号
     * @since 1.0.0
     */
    fun isIdCardNo(str: String): Boolean {
        return if (str.isBlank()) {
            false
        } else isIdCardNo18(str) || isIdCardNo15(str) || isHkIdCardNo(str) || isMacauIdCardNo(str) || isTwIdCardNo(str)
    }

    /**
     * 检查是否为18位身份号(大陆)
     *
     * @param str 待检查的字符串, 为null返回false
     * @return true: 为18位身份证号
     * @since 1.0.0
     */
    fun isIdCardNo18(str: String): Boolean {
        if (str.isBlank()) {
            return false
        }
        var bTrue = false
        if (str.length == MAINLAND_ID_MAX_LENGTH) {
            // 前17位
            val code17 = str.substring(0, 17)
            // 第18位
            if (code17.isNumeric()) {
                val cArr = code17.toCharArray()
                val iCard = converCharToInt(cArr)
                val iSum17 = getPowerSum(iCard)
                // 获取校验位
                val `val` = getCheckCode18(iSum17)
                if (`val`.isNotEmpty()) {
                    val code18 = str.last().toString()
                    if (`val`.equals(code18, ignoreCase = true)) {
                        bTrue = true
                    }
                }
            }
        }
        return bTrue
    }

    /**
     * 检查是否为15位身份号(大陆)
     *
     * @param str 待检查的字符串, 为null返回false
     * @return true: 为18位身份证号
     * @since 1.0.0
     */
    fun isIdCardNo15(str: String): Boolean {
        if (str.isBlank()) {
            return false
        }
        if (str.length != MAINLAND_ID_MIN_LENGTH) {
            return false
        }
        if (str.isNumeric()) {
            val proCode = str.substring(0, 2)
            if (Province.enumOf(proCode) == null) {
                return false
            }
            val birthCode = str.substring(6, 12)
            var birthDate: Date? = null
            try {
                birthDate = SimpleDateFormat("yy").parse(birthCode.substring(0, 2))
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val cal = Calendar.getInstance()
            if (birthDate != null) {
                cal.time = birthDate
            }
            if (!valiDate(
                    cal[Calendar.YEAR], Integer.valueOf(birthCode.substring(2, 4)),
                    Integer.valueOf(birthCode.substring(4, 6))
                )
            ) {
                return false
            }
        } else {
            return false
        }
        return true
    }

    /**
     * 检查是否为台湾身份号
     *
     * @param str 待检查的字符串, 为null返回false
     * @return true: 为台湾身份证号
     * @since 1.0.0
     */
    fun isTwIdCardNo(str: String): Boolean {
        if (str!!.isBlank()) {
            return false
        }
        if (!str.matches("^[a-zA-Z][0-9]{9}$".toRegex())) {
            return false
        }
        val start = str.substring(0, 1)
        val mid = str.substring(1, 9)
        val end = str.substring(9, 10)
        val iStart = twFirstCode[start] ?: return false
        var sum = iStart / 10 + iStart % 10 * 9
        val chars = mid.toCharArray()
        var iflag = 8
        for (c in chars) {
            sum += Integer.valueOf(c.toString() + "") * iflag
            iflag--
        }
        return (if (sum % 10 == 0) 0 else 10 - sum % 10) == Integer.valueOf(end)
    }

    /**
     * 检查是否为香港身份号(存在Bug，部份特殊身份证无法检查)
     * 身份证前2位为英文字符，如果只出现一个英文字符则表示第一位是空格，对应数字58 前2位英文字符A-Z分别对应数字10-35
     * 最后一位校验码为0-9的数字加上字符"A"，"A"代表10
     * 将身份证号码全部转换为数字，分别对应乘9-1相加的总和，整除11则证件号码有效
     *
     * @param str 待检查的字符串, 为null返回false
     * @return true: 为香港身份证号
     * @since 1.0.0
     */
    fun isHkIdCardNo(str: String): Boolean {
        if (str.isBlank()) {
            return false
        }
        if (!str.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$".toRegex())) {
            return false
        }
        var card = str.replace("[\\(|\\)]".toRegex(), "")
        var sum: Int
        if (card.length == 9) {
            sum = ((card.substring(0, 1).toUpperCase().toCharArray()[0].toInt() - 55) * 9
                    + (card.substring(1, 2).toUpperCase().toCharArray()[0].toInt() - 55) * 8)
            card = card.substring(1, 9)
        } else {
            sum = 522 + (card.substring(0, 1).toUpperCase().toCharArray()[0].toInt() - 55) * 8
        }
        val mid = card.substring(1, 7)
        val end = card.substring(7, 8)
        val chars = mid.toCharArray()
        var iflag = 7
        for (c in chars) {
            sum += Integer.valueOf(c.toString() + "") * iflag
            iflag--
        }
        sum = if ("A" == end.toUpperCase()) {
            sum + 10
        } else {
            sum + Integer.valueOf(end)
        }
        return sum % 11 == 0
    }

    /**
     * 检查是否为澳门身份号
     *
     * @param str 待检查的字符串, 为null返回false
     * @return true: 为澳门身份证号
     * @since 1.0.0
     */
    fun isMacauIdCardNo(str: String): Boolean {
        return !str.isBlank() && str.matches("^[1|5|7][0-9]{6}\\(?[0-9A-Z]\\)?$".toRegex())
    }

    private fun converCharToInt(ca: CharArray): IntArray {
        val len = ca.size
        val iArr = IntArray(len)
        for (i in 0 until len) {
            iArr[i] = ca[i].toInt() - 48
        }
        return iArr
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     *
     * @param iArr
     * @return 身份证编码。
     * @since 1.0.0
     */
    private fun getPowerSum(iArr: IntArray): Int {
        var iSum = 0
        if (power.size == iArr.size) {
            for (i in iArr.indices) {
                for (j in power.indices) {
                    if (i == j) {
                        iSum += iArr[i] * power[j]
                    }
                }
            }
        }
        return iSum
    }

    /**
     * 将power和值与11取模获得余数进行校验码判断
     *
     * @param iSum
     * @return 校验位
     * @since 1.0.0
     */
    private fun getCheckCode18(iSum: Int): String {
        return verifyCode[iSum % 11]
    }

    /**
     * 根据身份编号获取生日(仅限大陆身份证)
     *
     * @param idCardNo 身份证号, 为null或空或不是大陆身份证将返回null
     * @return 生日(yyyyMMdd)
     * @since 1.0.0
     */
    fun getBirthday(idCardNo: String): String? {
        var idCardNo = idCardNo
        if (idCardNo.isBlank()) {
            return null
        }
        val len = idCardNo!!.length
        if (len < MAINLAND_ID_MIN_LENGTH) {
            return null
        } else if (len == MAINLAND_ID_MIN_LENGTH) {
            idCardNo = convert15To18(idCardNo)!!
        }
        return idCardNo!!.substring(6, 14)
    }

    /**
     * 根据身份证号获取性别(仅限大陆和台湾)
     *
     * @param idCardNo 身份证号，为null返回Sex.UNKNOWN
     * @return 性别枚举
     * @since 1.0.0
     */
    fun getSex(idCardNo: String): Sex {
        var idCardNo = idCardNo
        if (idCardNo!!.isBlank()) {
            return Sex.SECRET
        }
        if (isTwIdCardNo(idCardNo)) {
            return if (idCardNo[1] == '1') Sex.MALE else Sex.FEMALE
        }
        if (idCardNo.length != MAINLAND_ID_MIN_LENGTH && idCardNo.length != MAINLAND_ID_MAX_LENGTH) {
            return Sex.SECRET
        }
        val sGender: Sex
        if (idCardNo.length == MAINLAND_ID_MIN_LENGTH) {
            idCardNo = convert15To18(idCardNo)!!
        }
        val sCardNum = idCardNo!!.substring(16, 17)
        sGender = if (sCardNum.toInt() % 2 != 0) {
            Sex.MALE
        } else {
            Sex.FEMALE
        }
        return sGender
    }

    /**
     * 根据身份证号获取户籍省份(包括大陆、港、澳、台)
     *
     * @param idCardNo 身份证号 为null或空返回null
     * @return 省枚举，未匹配返回null
     * @since 1.0.0
     */
    fun getProvince(idCardNo: String): Province? {
        if (idCardNo.isBlank()) {
            return null
        }
        var code: String? = null
        if (isIdCardNo15(idCardNo) || isIdCardNo18(idCardNo)) {
            code = idCardNo.substring(0, 2)
        } else if (isHkIdCardNo(idCardNo)) {
            return Province.XIANG_GANG
        } else if (isTwIdCardNo(idCardNo)) {
            return Province.TAI_WAN
        } else if (isMacauIdCardNo(idCardNo)) {
            return Province.AO_MEN
        }
        return if (code == null) {
            null
        } else {
            Province.enumOf(code)
        }
    }

    /**
     * 验证小于当前日期 是否有效
     *
     * @param iYear 待验证日期(年)
     * @param iMonth 待验证日期(月 1-12)
     * @param iDate 待验证日期(日)
     * @return 是否有效
     * @since 1.0.0
     */
    private fun valiDate(iYear: Int, iMonth: Int, iDate: Int): Boolean {
        val cal = Calendar.getInstance()
        val year = cal[Calendar.YEAR]
        val datePerMonth: Int
        if (iYear < MIN || iYear >= year) {
            return false
        }
        if (iMonth < 1 || iMonth > 12) {
            return false
        }
        datePerMonth = when (iMonth) {
            4, 6, 9, 11 -> 30
            2 -> {
                val dm =
                    (iYear % 4 == 0 && iYear % 100 != 0 || iYear % 400 == 0) && iYear > MIN && iYear < year
                if (dm) 29 else 28
            }
            else -> 31
        }
        return iDate in 1..datePerMonth
    }

    //	/** 香港身份首字母对应数字 */
    //	private static Map<String, Integer> hkFirstCode = new HashMap<String, Integer>();
    init {
        twFirstCode["A"] = 10
        twFirstCode["B"] = 11
        twFirstCode["C"] = 12
        twFirstCode["D"] = 13
        twFirstCode["E"] = 14
        twFirstCode["F"] = 15
        twFirstCode["G"] = 16
        twFirstCode["H"] = 17
        twFirstCode["J"] = 18
        twFirstCode["K"] = 19
        twFirstCode["L"] = 20
        twFirstCode["M"] = 21
        twFirstCode["N"] = 22
        twFirstCode["P"] = 23
        twFirstCode["Q"] = 24
        twFirstCode["R"] = 25
        twFirstCode["S"] = 26
        twFirstCode["T"] = 27
        twFirstCode["U"] = 28
        twFirstCode["V"] = 29
        twFirstCode["X"] = 30
        twFirstCode["Y"] = 31
        twFirstCode["W"] = 32
        twFirstCode["Z"] = 33
        twFirstCode["I"] = 34
        twFirstCode["O"] = 35

//		hkFirstCode.put("A", 1);
//		hkFirstCode.put("B", 2);
//		hkFirstCode.put("C", 3);
//		hkFirstCode.put("R", 18);
//		hkFirstCode.put("U", 21);
//		hkFirstCode.put("Z", 26);
//		hkFirstCode.put("X", 24);
//		hkFirstCode.put("W", 23);
//		hkFirstCode.put("O", 15);
//		hkFirstCode.put("N", 14);
    }
}