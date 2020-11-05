package io.kuark.base.cn

import io.kuark.base.lang.EnumKit
import io.kuark.base.support.enums.IDictEnum

/**
 * 中国省份枚举
 *
 * @author K
 * @since 1.0.0
 */
enum class Province(override val code: String, override val trans: String, vararg abbr: String) : IDictEnum {

    BEI_JING("11", "北京", "京"),
    TIAN_JIN("12", "天津", "津"),
    HE_BEI("13", "河北", "冀"),
    SHAN_XI("14", "山西", "晋"),
    NEI_MENG_GU("15", "内蒙古", "蒙"),
    LIAO_NING("21", "辽宁", "辽"),
    JI_LIN("22", "吉林", "吉"),
    HE_LONG_JIANG("23", "黑龙江", "黑"),
    SHANG_HAI("31", "上海", "沪"),
    JIANG_SU("32", "江苏", "苏"),
    ZHE_JIANG("33", "浙江", "浙"),
    AN_WEI("34", "安徽", "皖"),
    FU_JIAN("35", "福建", "闽"),
    JIANG_XI("36", "江西", "赣"),
    SHAN_DONG("37", "山东", "鲁"),
    HE_NAN("41", "河南", "豫"),
    HU_BEI("42", "湖北", "鄂"),
    HU_NAN("43", "湖南", "湘"),
    GUANG_DONG("44", "广东", "粤"),
    GUANG_XI("45", "广西", "桂"),
    HAI_NAN("46", "海南", "琼"),
    CHONG_QING("50", "重庆", "渝"),
    SI_CHUAN("51", "四川", "川", "蜀"),
    GUI_ZHOU("52", "贵州", "贵", "黔"),
    YUN_NAN("53", "云南", "云", "滇"),
    XI_ZANG("54", "西藏", "藏"),
    SHAN__XI("61", "陕西", "陕", "秦"),
    GAN_SU("62", "甘肃", "甘", "陇"),
    QING_DAO("63", "青海", "青"),
    NING_XIA("64", "宁夏", "宁"),
    XIN_JIANG("65", "新疆", "新"),
    TAI_WAN("71", "台湾", "台"),
    XIANG_GANG("81", "香港", "港"),
    AO_MEN("82", "澳门", "澳");

    override fun toString(): String {
        return trans
    }

    companion object {
        fun enumOf(code: String): Province? {
            return EnumKit.enumOf(Province::class, code)
        }
    }

}