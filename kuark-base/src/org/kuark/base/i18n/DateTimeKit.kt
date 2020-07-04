package org.kuark.base.i18n

import org.apache.commons.lang3.time.DateUtils
import java.text.DateFormat
import java.text.MessageFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日期/时间操作工具类
 *
 * @author K
 * @since 1.0.0
 */
object DateTimeKit {

    // 常用时间常量
    /** 一秒的毫秒数  */
    const val MILLISECONDS_OF_SECOND = 1000

    /** 一分钟的秒数  */
    const val SECONDS_OF_MINUTE = 60

    /** 一小时的秒数  */
    const val SECONDS_OF_HOUR = 60 * SECONDS_OF_MINUTE

    /** 一天的秒数  */
    const val SECONDS_OF_DAY = 24 * SECONDS_OF_HOUR

    /** 一周的秒数  */
    const val SECONDS_OF_WEEK = 7 * SECONDS_OF_DAY

    /** 一个月的秒数  */
    const val SECONDS_OF_MONTH = 30 * SECONDS_OF_DAY

    /** 一年的秒数  */
    const val SECONDS_OF_YEAR = 365 * SECONDS_OF_DAY

    const val yyMMdd = "yyMMdd"
    const val yyyyMMdd = "yyyyMMdd"
    const val yyyy_MM_dd = "yyyy-MM-dd"
    const val yyyyMMddHHmm = "yyyyMMddHHmm"
    const val yyyyMMddHHmmss = "yyyyMMddHHmmss"
    const val yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS"
    const val yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss"
    const val MM_dd_HH_mm_ss = "MM-dd HH:mm:ss"
    const val HH_mm_ss = "HH:mm:ss"

    //region formatDate
    /**
     * 根据指定的格式对日期进行格式化
     *
     * @param date 日期对象
     * @param fmt 格式化串(使用本类的以"UNFMT_"或"FMT_"打头的常量)
     * @return 格式化后的日期字符串
     * @since 1.0.0
     */
    fun formatDate(date: Date = Date(), fmt: String): String = SimpleDateFormat(fmt).format(date)

    /**
     * 根据指定的格式对毫秒表示的日期进行格式化
     *
     * @param time 日期毫秒数
     * @param dateFormat 格式化串(使用本类的以"UNFMT_"或"FMT_"打头的常量)
     * @return 格式化后的日期字符串
     */
    fun formatDate(time: Long = Date().time, dateFormat: String): String =
        SimpleDateFormat(dateFormat).format(Date(time))

    /**
     * 获取日期字符串
     *
     * @param date            指定日期
     * @param timeZone        指定时区
     * @param dateFormat    指定日期格式
     * @return                格式化后的日期字符串
     * @since 1.0.0
     */
    fun formatDate(date: Date = Date(), timeZone: TimeZone, dateFormat: String): String {
        val format: DateFormat = SimpleDateFormat(dateFormat)
        format.timeZone = timeZone
        return format.format(date)
    }

    /**
     * 获取日期字符串
     *
     * @param time            日期毫秒数
     * @param timeZone        指定时区
     * @param dateFormat    指定日期格式
     * @return                格式化后的日期字符串
     * @since 1.0.0
     */
    fun formatDate(time: Long = Date().time, timeZone: TimeZone, dateFormat: String): String {
        val format: DateFormat = SimpleDateFormat(dateFormat)
        format.timeZone = timeZone
        return format.format(Date(time))
    }

    /**
     * 获取日期字符串
     *
     * @param date            指定日期
     * @param locale        指定地区
     * @param timeZone        指定时区
     * @param dateFormat    指定日期格式
     * @return                日期字符串
     */
    fun formatDate(date: Date = Date(), locale: Locale, timeZone: TimeZone, dateFormat: String): String {
        val format: DateFormat = SimpleDateFormat(dateFormat, locale)
        format.timeZone = timeZone
        return format.format(date)
    }
    //endregion formatDate

    /**
     * 交换两个日期
     *
     * @param date1 日期1
     * @param date2 日期2
     * @since 1.0.0
     */
    fun swapDates(date1: Date, date2: Date) {
        val dateA = date1.time
        val dateB = date2.time
        date1.time = dateB
        date2.time = dateA
    }

    /**
     * 根据出生日期，计算出在某一个日期的年龄(实岁，小数点后不计)
     *
     * @param birthday 出生日期
     * @param focus 参照日期, 为null当作当前日期ip
     * @return 返回focus那一天出生日期为birthday的年龄(实岁)，如果出生日期参数为null或birthday大于focus则返回-1
     * @since 1.0.0
     */
    fun getActualAge(birthday: Date?, focus: Date? = null): Int {
        var focus = focus
        if (focus == null) {
            focus = Date()
        }
        return if (birthday == null || birthday.after(focus)) {
            -1
        } else {
            val cal1 = Calendar.getInstance()
            cal1.time = birthday
            val bornYear = cal1[Calendar.YEAR]
            val bornMonth = cal1[Calendar.MONTH]
            val cal = Calendar.getInstance()
            cal.time = focus
            val currYear = cal[Calendar.YEAR]
            val currMonth = cal[Calendar.MONTH]
            var age = currYear - bornYear
            age -= if (currMonth < bornMonth) 1 else 0
            age
        }
    }

    //region Between
    /**
     * 计算两个日期间相差的毫秒数
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 间隔的毫秒数
     * @since 1.0.0
     */
    fun millisecondsBetween(date1: Date, date2: Date): Long = timeBetween(date1, date2, Calendar.MILLISECOND)

    /**
     * 计算两个日期间相差的秒数(向下取整)
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 间隔的秒数
     * @since 1.0.0
     */
    fun secondsBetween(date1: Date, date2: Date): Long = timeBetween(date1, date2, Calendar.SECOND)

    /**
     * 计算两个日期间相差的分钟数(向下取整)
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 间隔的分钟数
     * @since 1.0.0
     */
    fun minutesBetween(date1: Date, date2: Date): Long = timeBetween(date1, date2, Calendar.MINUTE)

    /**
     * 计算两个日期间相差的小时数(向下取整)
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 间隔的小时数
     * @since 1.0.0
     */
    fun hoursBetween(date1: Date, date2: Date): Long = timeBetween(date1, date2, Calendar.HOUR)

    /**
     * 计算两个日期间的天数(向下取整)
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 间隔的天数
     * @since 1.0.0
     */
    fun daysBetween(date1: Date, date2: Date): Long = timeBetween(date1, date2, Calendar.DAY_OF_YEAR)

    /**
     * 计算两个日期间的星期数(向下取整)
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 间隔的星期数
     * @since 1.0.0
     */
    fun weeksBetween(date1: Date, date2: Date): Long = timeBetween(date1, date2, Calendar.WEEK_OF_YEAR)

    /**
     * 计算两个日期间的月数(向下取整)
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 间隔的月数
     * @since 1.0.0
     */
    fun monthsBetween(date1: Date, date2: Date): Long = timeBetween(date1, date2, Calendar.MONTH)

    /**
     * 计算两个日期间的年数(向下取整)
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 间隔的年数
     * @since 1.0.0
     */
    fun yearsBetween(date1: Date, date2: Date): Long = timeBetween(date1, date2, Calendar.YEAR)

    private fun timeBetween(date1: Date, date2: Date, field: Int): Long {
        val offMs = date1.time - date2.time // 毫秒数差值
        return when (field) {
            Calendar.MILLISECOND -> offMs
            Calendar.SECOND -> offMs / MILLISECONDS_OF_SECOND
            Calendar.MINUTE -> offMs / (MILLISECONDS_OF_SECOND * SECONDS_OF_MINUTE)
            Calendar.HOUR -> offMs / (MILLISECONDS_OF_SECOND * SECONDS_OF_HOUR)
            Calendar.DAY_OF_YEAR -> offMs / (MILLISECONDS_OF_SECOND * SECONDS_OF_DAY)
            Calendar.WEEK_OF_YEAR -> offMs / (MILLISECONDS_OF_SECOND * SECONDS_OF_WEEK)
            Calendar.MONTH -> offMs / (MILLISECONDS_OF_SECOND * SECONDS_OF_MONTH)
            Calendar.YEAR -> offMs / (MILLISECONDS_OF_SECOND * SECONDS_OF_YEAR)
            else -> offMs
        }
    }
    //endregion Between

    /**
     * 返回指定时间与现在相差的毫秒数，指定时间在未来返回正数，在过去返回负数
     *
     * @param date 指定的日期
     * @return 毫秒数
     * @since 1.0.0
     */
    fun timesToNow(date: Date): Long = date.time - System.currentTimeMillis()

    /**
     * 将日期转为quartz的cron表达式
     *
     * @param date 日期对象，为null将返回空串
     * @return quartz的cron表达式，指定日期为null将返回空串
     * @since 1.0.0
     */
    fun toCronExp(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val pattern = "{0} {1} {2} {3} {4} ? {5}"
        val args = arrayOf(
            calendar[Calendar.SECOND],
            calendar[Calendar.MINUTE],
            calendar[Calendar.HOUR_OF_DAY],
            calendar[Calendar.DAY_OF_MONTH],
            calendar[Calendar.MONTH] + 1,
            calendar[Calendar.YEAR].toString()
        )
        return MessageFormat.format(pattern, *args)
    }

    /**
     * 返回秒数所表示的各时间域
     *
     * @param seconds 秒数
     * @return Map<时间域></时间域>(取Calendar类的如下常量：YEAR, MONTH, DAY_OF_YEAR, HOUR, MINUTE, SECOND)，时间域对应的值>
     * @since 1.0.0
     */
    fun timeFieldOfSeconds(seconds: Long): Map<Int, Int> {
        var seconds = seconds
        val map = hashMapOf<Int, Int>()
        // 年
        var countAndSeconds = timeOfSeconds(seconds, SECONDS_OF_YEAR)
        var allNullFront = countAndSeconds.first === 0
        if (!allNullFront) {
            map[Calendar.YEAR] = countAndSeconds.first
        }
        seconds = countAndSeconds.second
        // 月
        countAndSeconds = timeOfSeconds(seconds, SECONDS_OF_MONTH)
        allNullFront = allNullFront && countAndSeconds.first === 0
        if (!allNullFront) {
            map[Calendar.MONTH] = countAndSeconds.first
        }
        seconds = countAndSeconds.second
        // 日
        countAndSeconds = timeOfSeconds(seconds, SECONDS_OF_DAY)
        allNullFront = allNullFront && countAndSeconds.first === 0
        if (!allNullFront) {
            map[Calendar.DAY_OF_YEAR] = countAndSeconds.first
        }
        seconds = countAndSeconds.second
        // 时
        countAndSeconds = timeOfSeconds(seconds, SECONDS_OF_HOUR)
        allNullFront = allNullFront && countAndSeconds.first === 0
        if (!allNullFront) {
            map[Calendar.HOUR] = countAndSeconds.first
        }
        seconds = countAndSeconds.second
        // 分
        countAndSeconds = timeOfSeconds(seconds, SECONDS_OF_MINUTE)
        allNullFront = allNullFront && countAndSeconds.first === 0
        if (!allNullFront) {
            map[Calendar.MINUTE] = countAndSeconds.first
        }
        seconds = countAndSeconds.second
        // 秒
        map[Calendar.SECOND] = seconds.toInt()
        return map
    }

    private fun timeOfSeconds(seconds: Long, periodSeconds: Int): Pair<Int, Long> {
        var s = seconds
        var count: Long = 0
        if (s >= periodSeconds) {
            count = s / periodSeconds
            s = if (s == periodSeconds.toLong()) {
                0
            } else {
                s % periodSeconds
            }
        }
        return Pair(count.toInt(), s)
    }

    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.lang3.DateUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

    //region same
    /**
     * 检查两个Date对象是否为同一天（忽略时间）
     * 2002-3-28 13:45 和　2002-3-28 06:01 将返回true。 2002-3-28 13:45 和　2002-3-12 13:45 将返回false。
     *
     * @param date1 第一个Date对象，不会被修改，不能为null
     * @param date2 第二个Date对象，不会被修改，不能为null
     * @return true：如果两个Date对象代表相同的一天
     * @since 1.0.0
     */
    fun isSameDay(date1: Date, date2: Date): Boolean = DateUtils.isSameDay(date1, date2)

    /**
     * 检查两个Calendar对象是否为同一天（忽略时间）
     * 2002-3-28 13:45 和　2002-3-28 06:01 将返回true。 2002-3-28 13:45 和　2002-3-12 13:45 将返回false。
     *
     * @param cal1 第一个Calendar对象，不会被修改，不能为null
     * @param cal2 第二个Calendar对象，不会被修改，不能为null
     * @return true：如果两个Calendar对象代表相同的一天
     * @since 1.0.0
     */
    fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean = DateUtils.isSameDay(cal1, cal2)

    /**
     * 检查两个Date对象是否为同一时间
     * 该方法比较两个Date对象的毫秒数
     *
     * @param date1 第一个Date对象，不会被修改，不能为null
     * @param date2 第二个Date对象，不会被修改，不能为null
     * @return true：如果两个Date对象代表同一时间
     * @since 1.0.0
     */
    fun isSameInstant(date1: Date, date2: Date): Boolean = DateUtils.isSameInstant(date1, date2)

    /**
     * 检查两个Calendar对象是否为同一时间
     * 该方法比较两个Calendar对象的毫秒数
     *
     * @param cal1 第一个Calendar对象，不会被修改，不能为null
     * @param cal2 第二个Calendar对象，不会被修改，不能为null
     * @return true：如果两个Calendar对象代表同一时间
     * @since 1.0.0
     */
    fun isSameInstant(cal1: Calendar, cal2: Calendar): Boolean = DateUtils.isSameInstant(cal1, cal2)

    /**
     * 检查两个Calendar对象是否代表相同的本地时间
     * 该方法比较两个对象各个时间域的值。
     *
     * @param cal1 第一个Calendar对象，不会被修改，不能为null
     * @param cal2 第二个Calendar对象，不会被修改，不能为null
     * @return true：如果两个Calendar对象代表同一时间
     * @since 1.0.0
     */
    fun isSameLocalTime(cal1: Calendar, cal2: Calendar): Boolean = DateUtils.isSameLocalTime(cal1, cal2)
    //endregion same

    //region parse
    /**
     * 用各种不同的解析模板解析代表日期的字符串(宽松)
     * 解析过程将按顺序尝试每一个解析模板。如果能够解析整个字符串，解析过程将成功终止于当前解析模板。 解析器将宽松地对待日期的解析。
     *
     * @param str 要解析的日期字符串, 不能为null
     * @param parsePatterns 要使用的日期模板数组, 见SimpleDateFormat类, 不能为null
     * @return 解析后的日期对象。如果没有匹配的解析模板，将返回null
     * @since 1.0.0
     */
    fun parseDate(str: String, vararg parsePatterns: String): Date = DateUtils.parseDate(str, *parsePatterns)

    /**
     * 用各种不同的解析模板解析代表日期的字符串（严格）
     * 解析过程将按顺序尝试每一个解析模板。如果能够解析整个字符串，解析过程将成功终止于当前解析模板。 <br></br>
     * 解析器将严格地对待日期的解析，比如它不允许这样的日期："February 942, 1996".
     *
     * @param str 要解析的日期字符串 , 不能为null
     * @param parsePatterns 要使用的日期模板数组, 见SimpleDateFormat类, 不能为null
     * @return 解析后的日期对象。如果没有匹配的解析模板，将返回null
     * @since 1.0.0
     */
    fun parseDateStrictly(str: String, vararg parsePatterns: String): Date =
        DateUtils.parseDateStrictly(str, *parsePatterns)
    //endregion parse

    //region add
    /**
     * 对Date的年份域加上一个数值，返回一个新的Date对象。原来的Date不会被改变。
     *
     * @param date Date对象，不能为null
     * @param amount 要加上数值，可以为负数
     * @return 新的Date对象
     * @since 1.0.0
     */
    fun addYears(date: Date, amount: Int): Date = DateUtils.addYears(date, amount)

    /**
     * 对Date的月份域加上一个数值，返回一个新的Date对象。原来的Date不会被改变。
     *
     * @param date Date对象，不能为null
     * @param amount 要加上数值，可以为负数
     * @return 新的Date对象
     * @since 1.0.0
     */
    fun addMonths(date: Date, amount: Int): Date = DateUtils.addMonths(date, amount)

    /**
     * 对Date的星期域加上一个数值，返回一个新的Date对象。原来的Date不会被改变。
     *
     * @param date Date对象，不能为null
     * @param amount 要加上数值，可以为负数
     * @return 新的Date对象
     * @since 1.0.0
     */
    fun addWeeks(date: Date, amount: Int): Date = DateUtils.addWeeks(date, amount)

    /**
     * 对Date的天域加上一个数值，返回一个新的Date对象。原来的Date不会被改变。
     *
     * @param date Date对象，不能为null
     * @param amount 要加上数值，可以为负数
     * @return 新的Date对象
     * @since 1.0.0
     */
    fun addDays(date: Date, amount: Int): Date = DateUtils.addDays(date, amount)

    /**
     * 对Date的小时域加上一个数值，返回一个新的Date对象。原来的Date不会被改变。
     *
     *  @param date Date对象，不能为null
     * @param amount 要加上数值，可以为负数
     * @return 新的Date对象
     * @since 1.0.0
     */
    fun addHours(date: Date, amount: Int): Date = DateUtils.addHours(date, amount)

    /**
     * 对Date的分钟域加上一个数值，返回一个新的Date对象。原来的Date不会被改变。
     *
     * @param date Date对象，不能为null
     * @param amount 要加上数值，可以为负数
     * @return 新的Date对象
     * @since 1.0.0
     */
    fun addMinutes(date: Date, amount: Int): Date = DateUtils.addMinutes(date, amount)

    /**
     * 对Date的秒域加上一个数值，返回一个新的Date对象。原来的Date不会被改变。
     *
     * @param date Date对象，不能为null
     * @param amount 要加上数值，可以为负数
     * @return 新的Date对象
     * @since 1.0.0
     */
    fun addSeconds(date: Date, amount: Int): Date = DateUtils.addSeconds(date, amount)

    /**
     * 对Date的毫秒域加上一个数值，返回一个新的Date对象。原来的Date不会被改变。
     *
     * @param date Date对象，不能为null
     * @param amount 要加上数值，可以为负数
     * @return 新的Date对象
     * @since 1.0.0
     */
    fun addMilliseconds(date: Date, amount: Int): Date = DateUtils.addMilliseconds(date, amount)
    //endregion add

    //region set
    /**
     * 设置日期的年份域，并返回新的日期对象。原来的日期对象不会被改变。
     *
     * @param date Date对象，不能为null
     * @param amount 要设置的数值
     * @return 新的Date对象
     * @since 1.0.0
     */
    fun setYears(date: Date, amount: Int): Date = DateUtils.setYears(date, amount)

    /**
     * 设置日期的月份域，并返回新的日期对象。原来的日期对象不会被改变。
     *
     * @param date Date对象，不能为null
     * @param amount 要设置的数值
     * @return 新的Date对象
     * @since 1.0.0
     */
    fun setMonths(date: Date, amount: Int): Date = DateUtils.setMonths(date, amount)

    /**
     * 设置日期的天域，并返回新的日期对象。原来的日期对象不会被改变。
     *
     * @param date Date对象，不能为null
     * @param amount 要设置的数值
     * @return 新的Date对象
     * @since 1.0.0
     */
    fun setDays(date: Date, amount: Int): Date = DateUtils.setDays(date, amount)

    /**
     * 设置日期的小时域，并返回新的日期对象。原来的日期对象不会被改变。
     *
     * @param date Date对象，不能为null
     * @param amount 要设置的数值
     * @return 新的Date对象
     * @since 1.0.0
     */
    fun setHours(date: Date, amount: Int): Date = DateUtils.setHours(date, amount)

    /**
     * 设置日期的分钟域，并返回新的日期对象。原来的日期对象不会被改变。
     *
     * @param date Date对象，不能为null
     * @param amount 要设置的数值
     * @return 新的Date对象
     * @since 1.0.0
     */
    fun setMinutes(date: Date, amount: Int): Date = DateUtils.setMinutes(date, amount)

    /**
     * 设置日期的秒域，并返回新的日期对象。原来的日期对象不会被改变。
     *
     * @param date Date对象，不能为null
     * @param amount 要设置的数值
     * @return 新的Date对象
     * @since 1.0.0
     */
    fun setSeconds(date: Date, amount: Int): Date = DateUtils.setSeconds(date, amount)

    /**
     * 设置日期的毫秒域，并返回新的日期对象。原来的日期对象不会被改变。
     *
     * @param date Date对象，不能为null
     * @param amount 要设置的数值
     * @return 新的Date对象
     * @since 1.0.0
     */
    fun setMilliseconds(date: Date, amount: Int): Date = DateUtils.setMilliseconds(date, amount)
    //endregion set

    //region round
    /**
     * 日期取整（日期精度调节）
     * 例如，如果日期为2002-3-28 13:45:01.231，域为HOUR，该方法将返回2002-3-28 14:00:00.000， 如果域为ＭONTH，将返回2002-4-1 ０:00:00.000，
     * 对于一个时区的日期处理夏令时的变化，四舍五入到Calendar.HOUR_OF_DAY的表现如下。 假设日光节约时间开始于3月30日02:00。取整一个超过此时间的日期，会产生以下值：
     *
     *  * 2003-3-30 01:10 取整为 2003-3-30 01:00
     *  * 2003-3-30 01:40 取整为 2003-3-30 03:00
     *  * 2003-3-30 02:10 取整为 2003-3-30 03:00
     *  * 2003-3-30 02:40 取整为 2003-3-30 04:00
     *
     * @param date 要处理的日期, 不能为null
     * @param field 通过`Calendar`或`SEMI_MONTH`指定的域
     * @return 取整后的日期对象, 不会为null
     * @throws ArithmeticException 如果年超过2.8亿
     * @since 1.0.0
     */
    fun round(date: Date, field: Int): Date = DateUtils.round(date, field)

    /**
     * 日期取整（日期精度调节）
     * 例如，如果日期为2002-3-28 13:45:01.231，域为HOUR，该方法将返回2002-3-28 14:00:00.000， 如果域为ＭONTH，将返回2002-4-1 ０:00:00.000，
     * 对于一个时区的日期处理夏令时的变化，四舍五入到Calendar.HOUR_OF_DAY的表现如下。 假设日光节约时间开始于3月30日02:00。取整一个超过此时间的日期，会产生以下值：
     *
     *  * 2003-3-30 01:10 取整为 2003-3-30 01:00
     *  * 2003-3-30 01:40 取整为 2003-3-30 03:00
     *  * 2003-3-30 02:10 取整为 2003-3-30 03:00
     *  * 2003-3-30 02:40 取整为 2003-3-30 04:00
     *
     * @param date 要处理的日期, 不能为null
     * @param field 通过`Calendar`或`SEMI_MONTH`指定的域
     * @return 取整后的日期对象, 不会为null
     * @throws ArithmeticException 如果年超过2.8亿
     * @since 1.0.0
     */
    fun round(date: Calendar, field: Int): Calendar = DateUtils.round(date, field)

    /**
     * 日期取整（日期精度调节）
     * 例如，如果日期为2002-3-28 13:45:01.231，域为HOUR，该方法将返回2002-3-28 14:00:00.000， 如果域为ＭONTH，将返回2002-4-1 ０:00:00.000，
     * 对于一个时区的日期处理夏令时的变化，四舍五入到Calendar.HOUR_OF_DAY的表现如下。 假设日光节约时间开始于3月30日02:00。取整一个超过此时间的日期，会产生以下值：
     *
     *  * 2003-3-30 01:10 取整为 2003-3-30 01:00
     *  * 2003-3-30 01:40 取整为 2003-3-30 03:00
     *  * 2003-3-30 02:10 取整为 2003-3-30 03:00
     *  * 2003-3-30 02:40 取整为 2003-3-30 04:00
     *
     * @param date 要处理的日期,可以为`Date`或`Calendar`，不能为null
     * @param field 通过`Calendar`或`SEMI_MONTH`指定的域
     * @return 取整后的日期对象, 不会为null
     * @throws ClassCastException 如果对象类型不是`Date`或`Calendar`
     * @throws ArithmeticException 如果年超过2.8亿
     * @since 1.0.0
     */
    fun round(date: Any, field: Int): Date = DateUtils.round(date, field)
    //endregion round

    //region truncate
    /**
     * 日期向下取整
     * 例如，如果日期为2002-3-28 13:45:01.231，域为HOUR，该方法将返回2002-3-28 13:00:00.000， 如果域为ＭONTH，将返回2002-3-1 ０:00:00.000，
     *
     * @param date 要处理的日期, 不能为null
     * @param field 通过`Calendar`或`SEMI_MONTH`指定的域
     * @return t取整后的日期对象, 不会为null
     * @throws ArithmeticException 如果年超过2.8亿
     * @since 1.0.0
     */
    fun truncate(date: Date, field: Int): Date = DateUtils.truncate(date, field)

    /**
     * 日期向下取整
     * 例如，如果日期为2002-3-28 13:45:01.231，域为HOUR，该方法将返回2002-3-28 13:00:00.000， 如果域为ＭONTH，将返回2002-3-1 ０:00:00.000，
     *
     * @param date 要处理的日期, 不能为null
     * @param field 通过`Calendar`或`SEMI_MONTH`指定的域
     * @return t取整后的日期对象, 不会为null
     * @throws ArithmeticException 如果年超过2.8亿
     * @since 1.0.0
     */
    fun truncate(date: Calendar, field: Int): Calendar = DateUtils.truncate(date, field)

    /**
     * 日期向下取整
     * 例如，如果日期为2002-3-28 13:45:01.231，域为HOUR，该方法将返回2002-3-28 13:00:00.000， 如果域为ＭONTH，将返回2002-3-1 ０:00:00.000，
     *
     * @param date 要处理的日期,可以为`Date`或`Calendar`，不能为null
     * @param field 通过`Calendar`或`SEMI_MONTH`指定的域
     * @return t取整后的日期对象, 不会为null
     * @throws ClassCastException 如果对象类型不是`Date`或`Calendar`
     * @throws ArithmeticException 如果年超过2.8亿
     * @since 1.0.0
     */
    fun truncate(date: Any, field: Int): Date = DateUtils.truncate(date, field)
    //endregion truncate

    //region ceiling
    /**
     * 日期向上取整
     * 例如，如果日期为2002-3-28 13:45:01.231，域为HOUR，该方法将返回2002-3-28 14:00:00.000， 如果域为ＭONTH，将返回2002-4-1 ０:00:00.000，
     *
     * @param date 要处理的日期 , 不能为null
     * @param field 通过`Calendar`或`SEMI_MONTH`指定的域
     * @return t取整后的日期对象, 不会为null
     * @throws ArithmeticException 如果年超过2.8亿
     * @since 1.0.0
     */
    fun ceiling(date: Date, field: Int): Date = DateUtils.ceiling(date, field)

    /**
     * 日期向上取整
     * 例如，如果日期为2002-3-28 13:45:01.231，域为HOUR，该方法将返回2002-3-28 14:00:00.000， 如果域为ＭONTH，将返回2002-4-1 ０:00:00.000，
     *
     * @param date 要处理的日期 , 不能为null
     * @param field 通过`Calendar`或`SEMI_MONTH`指定的域
     * @return t取整后的日期对象, 不会为null
     * @throws ArithmeticException 如果年超过2.8亿
     * @since 1.0.0
     */
    fun ceiling(date: Calendar, field: Int): Calendar = DateUtils.ceiling(date, field)

    /**
     * 日期向上取整
     * 例如，如果日期为2002-3-28 13:45:01.231，域为HOUR，该方法将返回2002-3-28 14:00:00.000， 如果域为ＭONTH，将返回2002-4-1 ０:00:00.000，
     *
     * @param date 要处理的日期 ,可以为`Date`或`Calendar`，不能为null
     * @param field 通过`Calendar`或`SEMI_MONTH`指定的域
     * @return t取整后的日期对象, 不会为null
     * @throws ClassCastException 如果对象类型不是`Date`或`Calendar`
     * @throws ArithmeticException 如果年超过2.8亿
     * @since 1.0.0
     */
    fun ceiling(date: Any, field: Int): Date = DateUtils.ceiling(date, field)
    //endregion ceiling

    //region iterator
    /**
     * 构造一个由指定日期范围中的每一天构成的`Iterator`
     * 比如，传递的参数为“2002-7-4（星期四）”　和`RANGE_MONTH_SUNDAY`，将返回从 “2002-6-30 星期天”到“2002-８-3
     * （星期六）”中间的每一天的Calendar的实例的`Iterator`
     *
     * @param focus 日期, 不能为null
     * @param rangeStyle 范围类型常量。必须为下面中的一个： [DateTool.RANGE_MONTH_SUNDAY], [DateTool.RANGE_MONTH_MONDAY],
     * [DateTool.RANGE_WEEK_SUNDAY], [DateTool.RANGE_WEEK_MONDAY],
     * [DateTool.RANGE_WEEK_RELATIVE], [DateTool.RANGE_WEEK_CENTER]
     * @return 日期迭代器
     * @throws IllegalArgumentException 如果范围类型参数非法
     * @since 1.0.0
     */
    fun iterator(focus: Date, rangeStyle: Int): Iterator<Calendar> = DateUtils.iterator(focus, rangeStyle)

    /**
     * 构造一个由指定日期范围中的每一天构成的`Iterator`
     * 比如，传递的参数为“2002-7-4（星期四）”　和`RANGE_MONTH_SUNDAY`，将返回从 “2002-6-30 星期天”到“2002-８-3
     * （星期六）”中间的每一天的Calendar的实例的`Iterator`
     *
     * @param focus 日期, 不能为null
     * @param rangeStyle 范围类型常量。必须为下面中的一个： [DateTool.RANGE_MONTH_SUNDAY], [DateTool.RANGE_MONTH_MONDAY],
     * [DateTool.RANGE_WEEK_SUNDAY], [DateTool.RANGE_WEEK_MONDAY],
     * [DateTool.RANGE_WEEK_RELATIVE], [DateTool.RANGE_WEEK_CENTER]
     * @return 日期迭代器
     * @throws IllegalArgumentException 如果范围类型参数非法
     * @since 1.0.0
     */
    fun iterator(focus: Calendar, rangeStyle: Int): Iterator<Calendar> = DateUtils.iterator(focus, rangeStyle)

    /**
     * 构造一个由指定日期范围中的每一天构成的`Iterator`
     * 比如，传递的参数为“2002-7-4（星期四）”　和`RANGE_MONTH_SUNDAY`，将返回从 “2002-6-30 星期天”到“2002-８-3
     * （星期六）”中间的每一天的Calendar的实例的`Iterator`
     *
     * @param focus 日期,可以为`Date`或`Calendar`，不能为null
     * @param rangeStyle 范围类型常量。必须为下面中的一个： [DateTool.RANGE_MONTH_SUNDAY], [DateTool.RANGE_MONTH_MONDAY],
     * [DateTool.RANGE_WEEK_SUNDAY], [DateTool.RANGE_WEEK_MONDAY],
     * [DateTool.RANGE_WEEK_RELATIVE], [DateTool.RANGE_WEEK_CENTER]
     * @return 日期迭代器
     * @throws IllegalArgumentException 如果范围类型参数非法
     * @throws ClassCastException 如果对象类型不是`Date`或`Calendar`
     * @since 1.0.0
     */
    fun iterator(focus: Any, rangeStyle: Int): Iterator<*> = DateUtils.iterator(focus, rangeStyle)
    //endregion iterator

    //region getFragment
    /**
     * 返回片段内的毫秒数。大于该片段的所有日期域将被忽略。
     * 获取任何日期的毫秒将只返回当前秒的毫秒数(0-999)。该方法将取得任何片断的毫秒数。 例如，如果你想要计算今天过去的毫秒数，你的片断为Calendar.DATE或Calendar.DAY_OF_YEAR，
     * 其结果将是所有过去的小时、分、秒的毫秒数。
     * 合法的片断取值为： Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and Calendar.DATE, Calendar.HOUR_OF_DAY,
     * Calendar.MINUTE, Calendar.SECOND 和 Calendar.MILLISECOND。 一个小于等于秒域的片断将返回０
     *  * 日期2008-1-1 7:15:10.538 和 片断Calendar.SECOND将返回 538
     *  * 日期2008-1-6 7:15:10.538 和 片断Calendar.SECOND将返回 538
     *  * 日期2008-1-6 7:15:10.538 和 片断Calendar.MINUTE将返回 10538 (10*1000 + 538)
     *  * 日期2008-1-16 7:15:10.538 和 片断Calendar.MILLISECOND将返回 0
     *
     * @param date 日期对象，不能为null
     * @param fragment `Calendar`的域常量
     * @return 片断对应的毫秒数
     * @since 1.0.0
     */
    fun getFragmentInMilliseconds(date: Date, fragment: Int): Long = DateUtils.getFragmentInMilliseconds(date, fragment)

    /**
     * 返回片段内的秒数。大于该片段的所有日期域将被忽略。
     * 获取任何日期的秒数将只返回当前分钟的秒数(0-59)。该方法将取得任何片断的秒数。 例如，如果你想要计算今天过去的秒数，你的片断为Calendar.DATE或Calendar.DAY_OF_YEAR，
     * 其结果将是所有过去的小时、分的秒数。
     * 合法的片断取值为： Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and Calendar.DATE, Calendar.HOUR_OF_DAY,
     * Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND 一个小于等于秒域的片断将返回０
     *  * 日期2008-1-1 7:15:10.538 和 片断Calendar.MINUTE将返回 10
     *  * 日期2008-1-6 7:15:10.538 和 片断Calendar.MINUTE将返回 10
     *  * 日期2008-1-6 7:15:10.538 和 片断Calendar.DAY_OF_YEAR将返回 26110 (7*3600 + 15*60 + 10)
     *  * 日期2008-1-16 7:15:10.538 和 片断Calendar.MILLISECOND将返回 0
     *
     * @param date 日期对象，不能为null
     * @param fragment `Calendar`的域常量
     * @return 片断对应的秒数
     * @since 1.0.0
     */
    fun getFragmentInSeconds(date: Date, fragment: Int): Long = DateUtils.getFragmentInSeconds(date, fragment)

    /**
     * 返回片段内的分钟数。大于该片段的所有日期域将被忽略。
     * 获取任何日期的分钟数将只返回当前小时的分钟数(0-59)。该方法将取得任何片断的分钟数。 例如，如果你想要计算今天过去的分钟数，你的片断为Calendar.DATE或Calendar.DAY_OF_YEAR，
     * 其结果将是所有过去的天、小时的分钟数。
     * 合法的片断取值为： Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and Calendar.DATE, Calendar.HOUR_OF_DAY,
     * Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND。 一个小于等于秒域的片断将返回０
     *  * 日期2008-1-1 7:15:10.538 和 片断Calendar.HOUR_OF_DAY将返回 15
     *  * 日期2008-1-6 7:15:10.538 和 片断Calendar.HOUR_OF_DAY将返回 15
     *  * 日期2008-1-1 7:15:10.538 和 片断Calendar.MONTH将返回 15
     *  * 日期2008-1-6 7:15:10.538 和 片断Calendar.MONTH将返回 435 (7*60 + 15)
     *  * 日期2008-1-16 7:15:10.538 和 片断Calendar.MILLISECOND将返回 0
     *
     * @param date 日期对象，不能为null
     * @param fragment `Calendar`的域常量
     * @return 片断对应的分钟数
     * @since 1.0.0
     */
    fun getFragmentInMinutes(date: Date, fragment: Int): Long = DateUtils.getFragmentInMinutes(date, fragment)

    /**
     * 返回片段内的小时数。大于该片段的所有日期域将被忽略。
     * 获取任何日期的小时数将只返回当天的小时数(0-23)。该方法将取得任何片断的小时数。 例如，如果你想要计算今天过去的小时数，你的片断为Calendar.DATE或Calendar.DAY_OF_YEAR，
     * 其结果将是所有过去天的小时数。
     * 合法的片断取值为： Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and Calendar.DATE, Calendar.HOUR_OF_DAY,
     * Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND。 一个小于等于小时域的片断将返回０
     *  * 日期2008-1-1 7:15:10.538 和 片断Calendar.DAY_OF_YEAR将返回 7
     *  * 日期2008-1-6 7:15:10.538 和 片断Calendar.DAY_OF_YEAR将返回 7
     *  * 日期2008-1-1 7:15:10.538 和 片断Calendar.MONTH将返回 7
     *  * 日期2008-1-6 7:15:10.538 和 片断Calendar.MONTH将返回 127 (5*24 + 7)
     *  * 日期2008-1-16 7:15:10.538 和 片断Calendar.MILLISECOND将返回 0
     *
     * @param date 日期对象，不能为null
     * @param fragment `Calendar`的域常量
     * @return 片断对应的分钟数
     * @since 1.0.0
     */
    fun getFragmentInHours(date: Date, fragment: Int): Long = DateUtils.getFragmentInHours(date, fragment)

    /**
     * 返回片段内的天数。大于该片段的所有日期域将被忽略。
     * 获取任何日期的天数将只返回当月的天数(1-31)。该方法将取得任何片断的天数。 例如，如果你想要计算今年过去的天数，你的片断为Calendar.YEAR， 其结果将是所有过去月份的天数。
     * 合法的片断取值为： Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and Calendar.DATE, Calendar.HOUR_OF_DAY,
     * Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND。 一个小于等于天域的片断将返回０
     *  * 日期2008-1-28 和 片断Calendar.MONTH将返回 28
     *  * 日期2008-1-28 和 片断Calendar.MONTH将返回 28
     *  * 日期2008-1-28 和 片断Calendar.YEAR将返回 28
     *  * 日期2008-1-28 和 片断Calendar.YEAR将返回 59
     *  * 日期2008-1-28 和 片断Calendar.MILLISECOND将返回 0
     *
     * @param date 日期对象，不能为null
     * @param fragment `Calendar`的域常量
     * @return 片断对应的分钟数
     * @since 1.0.0
     */
    fun getFragmentInDays(date: Date, fragment: Int): Long = DateUtils.getFragmentInDays(date, fragment)

    /**
     * 返回片段内的毫秒数。大于该片段的所有日期域将被忽略。
     * 获取任何日期的毫秒将只返回当前秒的毫秒数(0-999)。该方法将取得任何片断的毫秒数。 例如，如果你想要计算今天过去的毫秒数，你的片断为Calendar.DATE或Calendar.DAY_OF_YEAR，
     * 其结果将是所有过去的小时、分、秒的毫秒数。
     * 合法的片断取值为： Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and Calendar.DATE, Calendar.HOUR_OF_DAY,
     * Calendar.MINUTE, Calendar.SECOND 和 Calendar.MILLISECOND。 一个小于等于秒域的片断将返回０
     *  * 日期2008-1-1 7:15:10.538 和 片断Calendar.SECOND将返回 538
     *  * 日期2008-1-6 7:15:10.538 和 片断Calendar.SECOND将返回 538
     *  * 日期2008-1-6 7:15:10.538 和 片断Calendar.MINUTE将返回 10538 (10*1000 + 538)
     *  * 日期2008-1-16 7:15:10.538 和 片断Calendar.MILLISECOND将返回 0
     *
     * @param calendar 日期对象，不能为null
     * @param fragment `Calendar`的域常量
     * @return 片断对应的毫秒数
     * @since 1.0.0
     */
    fun getFragmentInMilliseconds(calendar: Calendar, fragment: Int): Long =
        DateUtils.getFragmentInMilliseconds(calendar, fragment)

    /**
     * 返回片段内的秒数。大于该片段的所有日期域将被忽略。
     * 获取任何日期的秒数将只返回当前分钟的秒数(0-59)。该方法将取得任何片断的秒数。 例如，如果你想要计算今天过去的秒数，你的片断为Calendar.DATE或Calendar.DAY_OF_YEAR，
     * 其结果将是所有过去的小时、分的秒数。
     * 合法的片断取值为： Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and Calendar.DATE, Calendar.HOUR_OF_DAY,
     * Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND 一个小于等于秒域的片断将返回０
     *  * 日期2008-1-1 7:15:10.538 和 片断Calendar.MINUTE将返回 10
     *  * 日期2008-1-6 7:15:10.538 和 片断Calendar.MINUTE将返回 10
     *  * 日期2008-1-6 7:15:10.538 和 片断Calendar.DAY_OF_YEAR将返回 26110 (7*3600 + 15*60 + 10)
     *  * 日期2008-1-16 7:15:10.538 和 片断Calendar.MILLISECOND将返回 0
     *
     * @param calendar 日期对象，不能为null
     * @param fragment `Calendar`的域常量
     * @return 片断对应的秒数
     * @since 1.0.0
     */
    fun getFragmentInSeconds(calendar: Calendar, fragment: Int): Long =
        DateUtils.getFragmentInSeconds(calendar, fragment)

    /**
     * 返回片段内的分钟数。大于该片段的所有日期域将被忽略。
     * 获取任何日期的分钟数将只返回当前小时的分钟数(0-59)。该方法将取得任何片断的分钟数。 例如，如果你想要计算今天过去的分钟数，你的片断为Calendar.DATE或Calendar.DAY_OF_YEAR，
     * 其结果将是所有过去的天、小时的分钟数。
     * 合法的片断取值为： Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and Calendar.DATE, Calendar.HOUR_OF_DAY,
     * Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND。 一个小于等于秒域的片断将返回０
     *  * 日期2008-1-1 7:15:10.538 和 片断Calendar.HOUR_OF_DAY将返回 15
     *  * 日期2008-1-6 7:15:10.538 和 片断Calendar.HOUR_OF_DAY将返回 15
     *  * 日期2008-1-1 7:15:10.538 和 片断Calendar.MONTH将返回 15
     *  * 日期2008-1-6 7:15:10.538 和 片断Calendar.MONTH将返回 435 (7*60 + 15)
     *  * 日期2008-1-16 7:15:10.538 和 片断Calendar.MILLISECOND将返回 0
     *
     * @param calendar 日期对象，不能为null
     * @param fragment `Calendar`的域常量
     * @return 片断对应的分钟数
     * @since 1.0.0
     */
    fun getFragmentInMinutes(calendar: Calendar, fragment: Int): Long =
        DateUtils.getFragmentInMinutes(calendar, fragment)

    /**
     * 返回片段内的小时数。大于该片段的所有日期域将被忽略。
     * 获取任何日期的小时数将只返回当天的小时数(0-23)。该方法将取得任何片断的小时数。 例如，如果你想要计算今天过去的小时数，你的片断为Calendar.DATE或Calendar.DAY_OF_YEAR，
     * 其结果将是所有过去天的小时数。
     * 合法的片断取值为： Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and Calendar.DATE, Calendar.HOUR_OF_DAY,
     * Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND。 一个小于等于小时域的片断将返回０
     *  * 日期2008-1-1 7:15:10.538 和 片断Calendar.DAY_OF_YEAR将返回 7
     *  * 日期2008-1-6 7:15:10.538 和 片断Calendar.DAY_OF_YEAR将返回 7
     *  * 日期2008-1-1 7:15:10.538 和 片断Calendar.MONTH将返回 7
     *  * 日期2008-1-6 7:15:10.538 和 片断Calendar.MONTH将返回 127 (5*24 + 7)
     *  * 日期2008-1-16 7:15:10.538 和 片断Calendar.MILLISECOND将返回 0
     *
     * @param calendar 日期对象，不能为null
     * @param fragment `Calendar`的域常量
     * @return 片断对应的分钟数
     * @since 1.0.0
     */
    fun getFragmentInHours(calendar: Calendar, fragment: Int): Long = DateUtils.getFragmentInHours(calendar, fragment)

    /**
     * 返回片段内的天数。大于该片段的所有日期域将被忽略。
     * 获取任何日期的天数将只返回当月的天数(1-31)。该方法将取得任何片断的天数。 例如，如果你想要计算今年过去的天数，你的片断为Calendar.YEAR， 其结果将是所有过去月份的天数。
     * 合法的片断取值为： Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and Calendar.DATE, Calendar.HOUR_OF_DAY,
     * Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND。 一个小于等于天域的片断将返回０
     *  * 日期2008-1-28 和 片断Calendar.MONTH将返回 28
     *  * 日期2008-1-28 和 片断Calendar.MONTH将返回 28
     *  * 日期2008-1-28 和 片断Calendar.YEAR将返回 28
     *  * 日期2008-1-28 和 片断Calendar.YEAR将返回 59
     *  * 日期2008-1-28 和 片断Calendar.MILLISECOND将返回 0
     *
     * @param calendar 日期对象，不能为null
     * @param fragment `Calendar`的域常量
     * @return 片断对应的分钟数
     * @since 1.0.0
     */
    fun getFragmentInDays(calendar: Calendar, fragment: Int): Long = DateUtils.getFragmentInDays(calendar, fragment)
    //endregion getFragment

    //region truncated
    /**
     * 判断给定的两个日期在不超过指定的域的情况下是否相等
     *
     * @param cal1 日期对象１，不能为 `null`
     * @param cal2 日期对象２，不能为 `null`
     * @param field `Calendar`的域常量
     * @return `true` 如果相等; 否则 `false`
     * @see .truncate
     * @see .truncatedEquals
     * @since 1.0.0
     */
    fun truncatedEquals(cal1: Calendar, cal2: Calendar, field: Int): Boolean =
        DateUtils.truncatedEquals(cal1, cal2, field)

    /**
     * 判断给定的两个日期在不超过指定的域的情况下是否相等
     *
     * @param date1 日期对象１，不能为 `null`
     * @param date2 日期对象２，不能为 `null`
     * @param field `Calendar`的域常量
     * @return `true` 如果相等; 否则 `false`
     * @see .truncate
     * @see .truncatedEquals
     * @since 1.0.0
     */
    fun truncatedEquals(date1: Date, date2: Date, field: Int): Boolean =
        DateUtils.truncatedEquals(date1, date2, field)

    /**
     * 在不超过指定的域的情况下，比较给定的两个日期
     *
     * @param cal1 日期对象１，不能为 `null`
     * @param cal2 日期对象２，不能为 `null`
     * @param field `Calendar`的域常量
     * @return 负数、０、或正数，分别当第一个日期小于、等于或大于第二个时
     * @see .truncate
     * @see .truncatedCompareTo
     * @since 1.0.0
     */
    fun truncatedCompareTo(cal1: Calendar, cal2: Calendar, field: Int): Int =
        DateUtils.truncatedCompareTo(cal1, cal2, field)

    /**
     * 在不超过指定的域的情况下，比较给定的两个日期
     *
     * @param date1 日期对象１，不能为 `null`
     * @param date2 日期对象２，不能为 `null`
     * @param field `Calendar`的域常量
     * @return 负数、０、或正数，分别当第一个日期小于、等于或大于第二个时
     * @see .truncate
     * @see .truncatedCompareTo
     * @since 1.0.0
     */
    fun truncatedCompareTo(date1: Date, date2: Date, field: Int): Int =
        DateUtils.truncatedCompareTo(date1, date2, field)
    //endregion truncated

    /**
     * 将Date对象转化为Calendar对象
     *
     * @param date 待转化的Date对象
     * @return Calendar对象
     * @since 1.0.0
     */
    fun toCalendar(date: Date): Calendar = DateUtils.toCalendar(date)

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.lang3.DateUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
}