package io.kuark.service.geo.po

import io.kuark.ability.data.jdbc.support.DbEntityFactory
import io.kuark.ability.data.jdbc.support.IMaintainableDbEntity
import java.time.LocalDate

/**
 * 国家|地区数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface GeoCountry: IMaintainableDbEntity<String, GeoCountry> {
//endregion your codes 1

    companion object : DbEntityFactory<GeoCountry>()

    /** 从属国家 */
    var parentId: String?

    /** 国家地区3位数字编码，ISO 3166-1 */
    var digitalCode: String?

    /** 国家地区2位字母编码(存在重复项)，ISO 3166-1 */
    var letterCode: String

    /** 国家地区GEC编码，2位字母编码(存在重复项) */
    var gecCode: String

    /** 国家地区名称，或其国际化key */
    var name: String

    /** 国家地区英文名称 */
    var englishName: String?

    /** 全名，或其国际化key */
    var fullName: String?

    /** 国家地区英文全称 */
    var englishFullName: String?

    /** 互联网域名后缀 */
    var domainSuffix: String?

    /** 旗帜url */
    var flagUrl: String?

    /** 首都/行政中心名称，或其国际化key */
    var capital: String?

    /** 首府纬度 */
    var capitalLatitude: String?

    /** 首府经度 */
    var capitalLongitude: String?

    /** 官方语言代码 */
    var localeDictCode: String?

    /** 所属大洲大洋代码 */
    var continentOceanDictCode: String?

    /** 币种代码 */
    var currencyDictCode: String?

    /** 国际电话区号 */
    var callingCode: String?

    /** UTC时区，多个以半角逗号分隔 */
    var timezoneUtc: String?

    /** 日期格式 */
    var dateFormat: String?

    /** 建国日 */
    var foundingDay: LocalDate?

    /** 驾驶方向代码 */
    var drivingSideDictCode: String?


    //region your codes 2

	//endregion your codes 2

}