package io.kuark.test.service.service1.biz

import io.kuark.test.service.service1.ibiz.IBiz1
import io.kuark.test.service.service1.model.Result1
import org.springframework.stereotype.Service

@Service
class Biz1 : IBiz1 {

    override fun list(): List<Result1> {
        return listOf(Result1("1", "name1"), Result1("2", "name2"))
    }

}