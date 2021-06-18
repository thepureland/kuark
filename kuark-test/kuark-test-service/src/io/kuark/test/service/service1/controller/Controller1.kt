package io.kuark.test.service.service1.controller

import io.kuark.test.service.service1.ibiz.IBiz1
import io.kuark.test.service.service1.model.Result1
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/service1")
class Controller1 {

    @Autowired
    private lateinit var biz1: IBiz1

    @GetMapping("/list")
    fun list(): List<Result1> {
        return biz1.list()
    }

}