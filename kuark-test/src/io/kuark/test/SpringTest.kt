package io.kuark.test

import org.springframework.boot.test.context.SpringBootTest

/**
 * 单元测试父类
 *
 * @author K
 * @since 1.0.0
 */
@SpringBootTest(classes = [TestApplication::class])
open class SpringTest {
}