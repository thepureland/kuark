package io.kuark.ability.rules

import io.kuark.test.SpringTest
import org.junit.jupiter.api.Test
import org.kie.api.runtime.KieSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner


//@RunWith(SpringRunner::class)
//@SpringBootTest(classes = [BlogAdminApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DroolsTest: SpringTest() {

    @Autowired
    private val session: KieSession? = null

    @Test
    fun people() {
        val people = People()
        people.name = "YC"
        people.sex = 1
        people.drlType = "people"
        session!!.insert(people) //插入
        session.fireAllRules() //执行规则
    }

}
