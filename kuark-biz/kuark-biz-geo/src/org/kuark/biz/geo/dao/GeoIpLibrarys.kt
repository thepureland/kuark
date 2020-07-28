package org.kuark.biz.geo.dao

import me.liuwj.ktorm.schema.*
import org.kuark.biz.geo.po.GeoIpLibrary
import org.kuark.data.jdbc.support.MaintainableTable

/**
 * ip库数据库实体DAO
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object GeoIpLibrarys: MaintainableTable<GeoIpLibrary>("geo_ip_library") {
//endregion your codes 1

    /** ip段起，标准ipv6全格式 */
    var ipStart = varchar("ip_start").bindTo { it.ipStart }

    /** ip段止，标准ipv6全格式 */
    var ipEnd = varchar("ip_end").bindTo { it.ipEnd }

    /** 国家ip，外键，geo_country表的主键 */
    var countryId = varchar("country_id").bindTo { it.countryId }

    /** 地区编码 */
    var regionCode = varchar("region_code").bindTo { it.regionCode }

    /** isp名称，或其国际化key */
    var ispName = varchar("isp_name").bindTo { it.ispName }

    /** 该IP是否是用户修正过 */
    var isRevised = boolean("is_revised").bindTo { it.isRevised }


    //region your codes 2

	//endregion your codes 2

}