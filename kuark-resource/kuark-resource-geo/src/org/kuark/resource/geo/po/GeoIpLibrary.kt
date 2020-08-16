package org.kuark.resource.geo.po

import org.kuark.data.jdbc.support.DbEntityFactory
import org.kuark.data.jdbc.support.IMaintainableDbEntity

/**
 * ip库数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface GeoIpLibrary: IMaintainableDbEntity<String, GeoIpLibrary> {
//endregion your codes 1

    companion object : DbEntityFactory<GeoIpLibrary>()

    /** ip段起，标准ipv6全格式 */
    var ipStart: String

    /** ip段止，标准ipv6全格式 */
    var ipEnd: String

    /** 国家ip，外键，geo_country表的主键 */
    var countryId: String

    /** 地区编码 */
    var regionCode: String

    /** isp名称，或其国际化key */
    var ispName: String

    /** 该IP是否是用户修正过 */
    var isRevised: Boolean


    //region your codes 2

	//endregion your codes 2

}