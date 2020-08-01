package org.kuark.test

import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/**
 * 单元测试父类
 *
 * @author K
 * @since 1.0.0
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TestApplication::class])
open class SpringTest {
}