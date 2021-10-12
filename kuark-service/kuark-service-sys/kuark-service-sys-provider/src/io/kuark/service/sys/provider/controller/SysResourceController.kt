package io.kuark.service.sys.provider.controller

import io.kuark.service.sys.common.model.SysMenuTreeNode
import io.kuark.service.sys.provider.ibiz.ISysResourceBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sysResource")
@CrossOrigin
class SysResourceController {

    @Autowired
    private lateinit var sysResourceBiz: ISysResourceBiz

    @GetMapping("/getMenus")
    fun getMenus(): List<SysMenuTreeNode> {
        return sysResourceBiz.getMenus()
    }

    fun get() {

    }


}