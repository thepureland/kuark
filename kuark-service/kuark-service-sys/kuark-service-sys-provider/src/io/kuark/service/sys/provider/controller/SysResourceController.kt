package io.kuark.service.sys.provider.controller

import io.kuark.ability.web.springmvc.BaseController
import io.kuark.service.sys.common.model.SysMenuTreeNode
import io.kuark.service.sys.common.model.resource.SysResourcePayload
import io.kuark.service.sys.provider.ibiz.ISysResourceBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.KClass

@RestController
@RequestMapping("/sysResource")
@CrossOrigin
open class SysResourceController: BaseController() {

    @Autowired
    private lateinit var sysResourceBiz: ISysResourceBiz

    @GetMapping("/getMenus")
    fun getMenus(): List<SysMenuTreeNode> {
        return sysResourceBiz.getMenus()
    }

    fun get() {

    }

    override fun getFormModelClass(): KClass<*> = SysResourcePayload::class

}