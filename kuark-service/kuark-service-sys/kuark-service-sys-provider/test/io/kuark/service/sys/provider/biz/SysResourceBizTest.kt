package io.kuark.service.sys.provider.biz

import io.kuark.base.data.json.JsonKit
import io.kuark.service.sys.provider.ibiz.ISysResourceBiz
import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class SysResourceBizTest : SpringTest() {

    @Autowired
    private lateinit var sysResourceBiz: ISysResourceBiz

    @Test
    fun getMenus() {
        val menus = sysResourceBiz.getMenus()
        println(JsonKit.toJson(menus))
    }

}