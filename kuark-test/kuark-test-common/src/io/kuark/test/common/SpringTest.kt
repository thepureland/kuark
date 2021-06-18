package io.kuark.test.common

import io.kuark.base.lang.SystemKit
import org.springframework.boot.test.context.SpringBootTest

/**
 * 单元测试父类
 *
 * @author K
 * @since 1.0.0
 */
@SpringBootTest(classes = [TestApplication::class])
open class SpringTest {

    constructor() {
        SystemKit.setEnvVars(mapOf("spring.cloud.config.enabled" to "false"))
    }

}