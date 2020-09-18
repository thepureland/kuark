package io.kuark.service.geo.dao

import io.kuark.ability.data.jdbc.support.MaintainableTable
import io.kuark.service.geo.po.GeoCountry
import me.liuwj.ktorm.schema.date
import me.liuwj.ktorm.schema.varchar

/**
 * 国家|地区数据库实体DAO
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object GeoCountrys: MaintainableTable<GeoCountry>("geo_country") {
//endregion your codes 1

    /** 从属国家 */
    var parentId = varchar("parent_id").bindTo { it.parentId }

    /** 国家地区3位数字编码，ISO 3166-1 */
    var digitalCode = varchar("digital_code").bindTo { it.digitalCode }

    /** 国家地区2位字母编码(存在重复项)，ISO 3166-1 */
    var letterCode = varchar("letter_code").bindTo { it.letterCode }

    /** 国家地区GEC编码，2位字母编码(存在重复项) */
    var gecCode = varchar("gec_code").bindTo { it.gecCode }

    /** 国家地区名称，或其国际化key */
    var name = varchar("name").bindTo { it.name }

    /** 国家地区英文名称 */
    var englishName = varchar("english_name").bindTo { it.englishName }

    /** 全名，或其国际化key */
    var fullName = varchar("full_name").bindTo { it.fullName }

    /** 国家地区英文全称 */
    var englishFullName = varchar("english_full_name").bindTo { it.englishFullName }

    /** 互联网域名后缀 */
    var domainSuffix = varchar("domain_suffix").bindTo { it.domainSuffix }

    /** 旗帜url */
    var flagUrl = varchar("flag_url").bindTo { it.flagUrl }

    /** 首都/行政中心名称，或其国际化key */
    var capital = varchar("capital").bindTo { it.capital }

    /** 首府纬度 */
    var capitalLatitude = varchar("capital_latitude").bindTo { it.capitalLatitude }

    /** 首府经度 */
    var capitalLongitude = varchar("capital_longitude").bindTo { it.capitalLongitude }

    /** 官方语言代码 */
    var localeDictCode = varchar("locale_dict_code").bindTo { it.localeDictCode }

    /** 所属大洲大洋代码 */
    var continentOceanDictCode = varchar("continent_ocean_dict_code").bindTo { it.continentOceanDictCode }

    /** 币种代码 */
    var currencyDictCode = varchar("currency_dict_code").bindTo { it.currencyDictCode }

    /** 国际电话区号 */
    var callingCode = varchar("calling_code").bindTo { it.callingCode }

    /** UTC时区，多个以半角逗号分隔 */
    var timezoneUtc = varchar("timezone_utc").bindTo { it.timezoneUtc }

    /** 日期格式 */
    var dateFormat = varchar("date_format").bindTo { it.dateFormat }

    /** 建国日 */
    var foundingDay = date("founding_day").bindTo { it.foundingDay }

    /** 驾驶方向代码 */
    var drivingSideDictCode = varchar("driving_side_dict_code").bindTo { it.drivingSideDictCode }


    //region your codes 2

	//endregion your codes 2

}