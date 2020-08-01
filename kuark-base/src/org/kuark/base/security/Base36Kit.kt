package org.kuark.base.security

import java.security.MessageDigest

/**
 * 目的：根据输入的秘钥，对提供的字符串进行加密，和对以此加密规则生成的密文解密
 * 说明：encrypt和decrypt为完全对称的设计，你也可以把decrypt作为加密函数，使用
 *      encrypt函数来还原。
 * 用法：myEncrypt为一个使用例子，实际使用可以根据需要多次调用encrypt、outOrder；
 *      解密时，使用相同的顺序调用decrypt、deOutOrder即可还原。
 * 要求：输入的字符串只能包含数字和字母，key为不超过18位的正整数
 *
 * 修订记录
 * 版本  时间        作者     操作
 * 1.00  2016/04/22  Leisure  创建此类，包括加密，解密函数
 * 1.10  2016/04/22  Leisure  增加字符转换功能，生成的密文不再是有意义的字符组合
 * 1.11  2016/04/22  Leisure  秘钥由int改为Long类型，秘钥长度由最高9位升级为18位
 * 1.12  2016/04/23  Leisure  重构字符串排序方法
 * 1.20  2016/04/23  Leisure  增加MD5校验位
 * 1.21  2016/04/23  Leisure  修正了一个bug，使用自定义编码保证密文的格式与原文兼容
 * 1.22  2016/04/23  Leisure  字符转换功能兼容大小写
 * 1.23  2016/04/25  Leisure  校验位提取到myEncrypt,myDecrypt.原加密解密函数对称
 * 1.24  2016/04/25  Leisure  支持大写字母和数字组合转换后仍为大写字母和数字组合
 * 2.00  2013/04/26  Leisure  修改函数名，避免歧义；修改函数的作用域。撒手不管版
 */
object Base36Kit {

    private const val key = 999999999999999999L

    /**
     * 对源字符串进行加密，并在头部增加一个字符作为源字符串的校验码
     * 要求源字符串只包含大写字母和数字，小写字母将按大写字母处理
     * 使用默认的加密Key
     * @param src
     * @return
     */
    fun encryptIgnoreCase(src: String): String {
        return encryptIgnoreCase(src, key)
    }

    /**
     * 对源字符串进行加密，并在头部增加一个字符作为源字符串的校验码
     * 要求源字符串只包含大写字母和数字，小写字母将按大写字母处理
     * @param src
     * @param key
     * @return
     */
    fun encryptIgnoreCase(src: String, key: Long): String { //小写字母以大写字母来处理
        var srcString = src
        srcString = srcString.toUpperCase()
        //生成校验位
        val checkBit = getMD5(srcString)!!.substring(0, 1)
        //加密
        val targStr = encrypt(srcString, key, true)
        //增加校验位
        return checkBit + targStr
    }

    /**
     * 接收含有校验位的加密字符串，对其解密，并验证与校验位是否匹配
     * 使用默认的加密Key
     * @param srcString
     * @return
     */
    fun decryptIgnoreCase(srcString: String): String {
        return decryptIgnoreCase(srcString, key)
    }

    /**
     * 接收含有校验位的加密字符串，对其解密，并验证与校验位是否匹配
     * @param src
     * @param key
     * @return
     */
    fun decryptIgnoreCase(src: String, key: Long): String { //取校验位
        var srcString = src
        val checkBit = srcString.substring(0, 1)
        //取密文
        srcString = srcString.substring(1, srcString.length)
        //解密
        val targStr = decrypt(srcString, key, true)
        //验证校验位
        return if (checkBit != getMD5(targStr)!!.substring(0, 1)) {
            "校验位不匹配！"
        } else targStr
    }

    /**
     * 加密函数
     * @param src
     * @param key
     * @param capitalOnly
     * @return
     */
    fun encrypt(src: String, key: Long, capitalOnly: Boolean): String { //乱序
        var targStr = outOrder(src, key)
        //字符串转换
        return transStr(targStr, key, true, capitalOnly)
    }

    /**
     * 解密函数
     * @param src
     * @param key
     * @param capitalOnly
     * @return
     */
    fun decrypt(src: String, key: Long, capitalOnly: Boolean): String { //字符串转换
        var srcString = src
        srcString = transStr(srcString, key, false, capitalOnly)
        //恢复排序
        return deOutOrder(srcString, key)
    }

    private fun outOrder(src: String, key: Long): String {
        var srcString = src
        val keyStr = key.toString()
        //String[] keyArr = keyStr.split("");
        val len1 = srcString.length
        val len2 = keyStr.length
        val skipNum = (key % 100 % len1).toInt()
        val len = if (len1 < len2) len1 else len2
        var num = Array(len) { arrayOfNulls<String>(3) }
        //字符串移位
        srcString = (srcString + srcString).substring(skipNum, skipNum + len1)
        //秘钥转换
        val keyArr = IntArray(keyStr.length)
        for (i in keyStr.indices) {
            keyArr[i] = keyStr.substring(i, i + 1).toInt()
            keyArr[i] = 100 - (10 * keyArr[i] + i)
        }
        for (i in 0 until len) {
            num[i][0] = i.toString()
            num[i][1] = keyArr[i].toString()
            num[i][2] = srcString.substring(i, i + 1)
        }
        //对数组按秘钥列重新排序
        num = sortArr(num, 1)
        var targStr = String()
        for (i in 0 until len) {
            targStr += num[i][2]
        }
        targStr += srcString.substring(len, len1)
        return targStr
    }

    private fun deOutOrder(srcString: String, key: Long): String {
        val keyStr = key.toString()
        //String[] keyArr = keyStr.split("");
        val len1 = srcString.length
        val len2 = keyStr.length
        val skipNum = (key % 100 % len1).toInt()
        val len = if (len1 < len2) len1 else len2
        var num =
            Array(len) { arrayOfNulls<String>(3) }
        //秘钥转换
        val keyArr = IntArray(keyStr.length)
        for (i in keyStr.indices) {
            keyArr[i] = keyStr.substring(i, i + 1).toInt()
            keyArr[i] = 100 - (10 * keyArr[i] + i)
        }
        for (i in 0 until len) {
            num[i][0] = i.toString()
            num[i][1] = keyArr[i].toString()
            //num[i][2] = srcString.substring(i, i+1);
        }
        //先对数组按秘钥列重新排序
        num = sortArr(num, 1)
        //将字符插入数组
        for (i in 0 until len) {
            num[i][2] = srcString.substring(i, i + 1)
        }
        //对数组按索引列重新排序
        num = sortArr(num, 0)
        var targStr = String()
        for (i in 0 until len) {
            targStr += num[i][2]
        }
        targStr += srcString.substring(len, len1)
        //字符串移位
        targStr = (targStr + targStr).substring(len1 - skipNum, 2 * len1 - skipNum)
        return targStr
    }

    //数组排序
    private fun sortArr(arr: Array<Array<String?>>, col: Int): Array<Array<String?>> {
        var temp: Array<String?>
        for (i in 0 until arr.size - 1) {
            for (j in arr.size - 1 downTo i + 1) {
                if (arr[j][col]!!.toInt() < arr[j - 1][col]!!.toInt()) {
                    temp = arr[j]
                    arr[j] = arr[j - 1]
                    arr[j - 1] = temp
                }
            }
        }
        return arr
    }

    //字符串转换
    private fun transStr(inStr: String, transNum: Long, plus_minus: Boolean, band36_62: Boolean): String {
        var s = inStr
        var band = 62
        if (band36_62) {
            band = 36
            //36进制不包括小写字母，小写字母以大写字母来处理
            s = s.toUpperCase()
        }
        val len1 = s.length
        val len2 = transNum.toString().length
        val len = if (len1 < len2) len1 else len2
//        val outStr = String()
        val ch = s.toCharArray()
        for (i in 0 until len) {
            val j = len1 - 1 - i
            //转为自定义的36位或/62位编码
            ch[j] = asciiToDiy(ch[j].toInt()).toChar()
            if (plus_minus) { //System.out.print((int)ch[j] + " " + ch[j] + " ");
                ch[j] = ((ch[j].toInt() + transNum.toString().substring(i, i + 1).toInt()) % band).toChar()
                //System.out.print((int)ch[j] + " " + ch[j] + " ");
            } else { //System.out.print((int)ch[j] + " " + ch[j] + " ");
                ch[j] =
                    ((ch[j].toInt() - transNum.toString().substring(i, i + 1).toInt() + band) % band).toChar()
                //System.out.print((int)ch[j] + " " + ch[j] + " ");
            }
            //把自定义编码转回ASCII码
            ch[j] = diyToascii(ch[j].toInt()).toChar()
        }
        return String(ch)
    }

    //按自定义的36位或/62位编码重新编码，0~9为数字，10~35为大写字母，36~51位为小写字母
    private fun asciiToDiy(codeNum: Int): Int { //如果n的ASCII码在48~57，则n是数字, 65~90则是大写字母
        return when(codeNum) {
            in 48..57 -> codeNum - 48
            in 65..90 -> codeNum - 65 + 10
            in 97..122 -> codeNum - 97 + 36
            else -> codeNum
        }
    }

    //把自定义的36位或/62位编码重新转为ASCII码，0~9为数字，10~35为大写字母，36~51位为小写字母
    private fun diyToascii(codeNum: Int): Int { //把自定义编码转回ASCII码
        return when(codeNum) {
            in -1..10 -> codeNum + 48
            in 9..36 -> codeNum - 10 + 65
            in 35..62 -> codeNum - 36 + 97
            else -> codeNum
        }
    }

    private fun getMD5(s: String): String? {
        val hexDigits =
            charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
        return try {
            val btInput = s.toByteArray()
            // 获得MD5摘要算法的 MessageDigest 对象
            val mdInst = MessageDigest.getInstance("MD5")
            // 使用指定的字节更新摘要
            mdInst.update(btInput)
            // 获得密文
            val md = mdInst.digest()
            // 把密文转换成十六进制的字符串形式
            val j = md.size
            val str = CharArray(j * 2)
            var k = 0
            for (i in 0 until j) {
                val byte0 = md[i]
                str[k++] = hexDigits[byte0.toInt().ushr(4) and 0xf]
                str[k++] = hexDigits[byte0.toInt() and 0xf]
            }
            String(str)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}