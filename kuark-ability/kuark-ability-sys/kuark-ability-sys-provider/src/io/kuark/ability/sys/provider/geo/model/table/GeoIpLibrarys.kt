package io.kuark.ability.sys.provider.geo.model.table

import io.kuark.ability.data.rdb.support.MaintainableTable
import io.kuark.ability.sys.provider.geo.model.po.GeoIpLibrary
import org.ktorm.schema.boolean
import org.ktorm.schema.varchar

/**
 * ip库数据库表-实体关联对象
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
    var revised = boolean("revised").bindTo { it.revised }


    //region your codes 2

	//endregion your codes 2

}