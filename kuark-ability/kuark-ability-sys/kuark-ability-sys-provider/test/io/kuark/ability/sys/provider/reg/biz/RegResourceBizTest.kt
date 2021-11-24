package io.kuark.ability.sys.provider.reg.biz

import io.kuark.ability.sys.provider.reg.ibiz.IRegResourceBiz
import io.kuark.base.data.json.JsonKit
import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class RegResourceBizTest : SpringTest() {

    @Autowired
    private lateinit var regResourceBiz: IRegResourceBiz

    @Test
    fun getMenus() {
        val menus = regResourceBiz.getMenus()
        println(JsonKit.toJson(menus))
    }

}