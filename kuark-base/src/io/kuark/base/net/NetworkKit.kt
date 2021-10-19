package io.kuark.base.net

import io.kuark.base.lang.SystemKit
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 网络工具
 *
 * @author K
 * @since 1.0.0
 */
object NetworkKit {

    const val LOCALHOST_IP = "127.0.0.1"
    const val ANYHOST_IP = "0.0.0.0"

    /**
     * 判断端口是否启用
     *
     * @param ip ip地址
     * @param port 端口号
     * @author K
     * @since 1.0.0
     */
    fun isPortActive(ip: String, port: Int): Boolean {
        try {
            Socket(ip, port).close()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    /**
     * 获取MAC地址
     *
     * @return 所有网卡的MAC地址列表
     * @author K
     * @since 1.0.0
     */
    fun getMacAddress(): List<String> {
        val macs: MutableList<String> = ArrayList()
        // The Runtime.exec() method returns an instance of a subclass of Process
        val myProc: Process
        // surround with a try catch block
        var currentLine: String
        // the operating systems name as referenced by the System
        val osName = SystemKit.getOSName()
        // a regular expression used to match the area of text we want
        var macRegExp: String
        if (osName.startsWith("windows")) { // Windows operating system will run this
            // the regular expression we will be matching for the Mac address on Windows
            macRegExp = "[\\da-zA-Z]{1,2}\\-[\\da-zA-Z]{1,2}\\-[\\da-zA-Z]" +
                    "{1,2}\\-[\\da-zA-Z]{1,2}\\-[\\da-zA-Z]{1,2}\\-[\\da-zA-Z]{1,2}"
            myProc = Runtime.getRuntime().exec("ipconfig /all")
        } else if (osName.startsWith("linux")) { // Linux operating system runs this
            // the regular expression we will be matching for the Mac address on Linux
            macRegExp = "[\\da-zA-Z]{1,2}\\:[\\da-zA-Z]{1,2}\\:[\\da-zA-Z]" +
                    "{1,2}\\:[\\da-zA-Z]{1,2}\\:[\\da-zA-Z]{1,2}\\:[\\da-zA-Z]{1,2}"
            myProc = Runtime.getRuntime().exec("/sbin/ifconfig -a")
        } else {
            throw UnsupportedOperationException("不支持的操作系统")
        }
        // we'll wrap a buffer around the InputStream we get from the "myProc" Process
        val `in` = BufferedReader(
            InputStreamReader(myProc.inputStream)
        )
        // compile the macRegExp string into a Pattern
        val macPattern = Pattern.compile(".*($macRegExp).*")
        // a Matcher object for matching the regular expression to the string
        var macMtch: Matcher?
        while (`in`.readLine().also {
                currentLine = it
            } != null) { // walk through each line and try to match the pattern
            macMtch = macPattern.matcher(currentLine)
            if (macMtch.matches()) { // it matched so we split the line
                val splitLine = currentLine.split(macRegExp).toTypedArray()
                for (a in splitLine.indices) { // REPLACE ALL PORTIONS of the currentLine
                    // that do not match the expression
                    // with an empty string
                    currentLine = currentLine.replace(
                        splitLine[a].replace(
                            "\\(".toRegex(),
                            "\\\\("
                        ).replace("\\)".toRegex(), "\\\\)").toRegex(), ""
                    )
                }
                // mac address(es) returned in the StringBuffer
                macs.add(currentLine)
                // reset the matcher just in case we have more than one mac address
                macMtch.reset()
            }
        }
        myProc.destroy()
        return macs
    }

}