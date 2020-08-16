package org.kuark.resource.geo.po

import org.kuark.data.jdbc.support.DbEntityFactory
import org.kuark.data.jdbc.support.IMaintainableDbEntity

/**
 * 地区数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface GeoRegion: IMaintainableDbEntity<String, GeoRegion> {
//endregion your codes 1

    companion object : DbEntityFactory<GeoRegion>()

    /** 外键，国家或地区id, geo_country表主键 */
    var countryId: String

    /** 区域数字编码，2位省编码+2位市编码+2位县编码 */
    var code: String

    /** 区域名称，或其国际化key */
    var name: String

    /** 父编码 */
    var parentCode: String

    /** 层级 */
    var hierarchy: String

    /** 区域简称，或其国际化key */
    var shortName: String

    /** 区域别名，或其国际化key */
    var aliasName: String

    /** 邮政编码 */
    var postcode: String

    /** 经度 */
    var longitude: String

    /** 纬度 */
    var latitude: String

    /** 电话区号 */
    var callingCode: String

    /** 车牌号前缀 */
    var licensePlateNoPrefix: String

    /** 机场3位编码 */
    var airportCode: String


    //region your codes 2

	//endregion your codes 2

}