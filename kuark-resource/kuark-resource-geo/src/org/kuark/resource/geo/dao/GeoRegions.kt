package org.kuark.resource.geo.dao

import me.liuwj.ktorm.schema.*
import org.kuark.resource.geo.po.GeoRegion
import org.kuark.data.jdbc.support.MaintainableTable

/**
 * 地区数据库实体DAO
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object GeoRegions: MaintainableTable<GeoRegion>("geo_region") {
//endregion your codes 1

    /** 外键，国家或地区id, geo_country表主键 */
    var countryId = varchar("country_id").bindTo { it.countryId }

    /** 区域数字编码，2位省编码+2位市编码+2位县编码 */
    var code = varchar("code").bindTo { it.code }

    /** 区域名称，或其国际化key */
    var name = varchar("name").bindTo { it.name }

    /** 父编码 */
    var parentCode = varchar("parent_code").bindTo { it.parentCode }

    /** 层级 */
    var hierarchy = varchar("hierarchy").bindTo { it.hierarchy }

    /** 区域简称，或其国际化key */
    var shortName = varchar("short_name").bindTo { it.shortName }

    /** 区域别名，或其国际化key */
    var aliasName = varchar("alias_name").bindTo { it.aliasName }

    /** 邮政编码 */
    var postcode = varchar("postcode").bindTo { it.postcode }

    /** 经度 */
    var longitude = varchar("longitude").bindTo { it.longitude }

    /** 纬度 */
    var latitude = varchar("latitude").bindTo { it.latitude }

    /** 电话区号 */
    var callingCode = varchar("calling_code").bindTo { it.callingCode }

    /** 车牌号前缀 */
    var licensePlateNoPrefix = varchar("license_plate_no_prefix").bindTo { it.licensePlateNoPrefix }

    /** 机场3位编码 */
    var airportCode = varchar("airport_code").bindTo { it.airportCode }


    //region your codes 2

	//endregion your codes 2

}