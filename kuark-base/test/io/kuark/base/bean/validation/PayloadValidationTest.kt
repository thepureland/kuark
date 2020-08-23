package io.kuark.base.bean.validation

import io.kuark.base.bean.validation.kit.ValidationKit
import io.kuark.base.log.LogFactory
import org.hibernate.validator.constraints.Length
import org.junit.jupiter.api.Test
import javax.validation.ConstraintViolation
import javax.validation.Payload
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.Pattern

/**
 * 负载验证测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class PayloadValidationTest {

    companion object {
        private val log = LogFactory.getLog(PayloadValidationTest::class)
    }

    @Test
    fun testPayload() {
        val bean1 = TestPayloadBean("123", 61)
        val violations = ValidationKit.validateBean(bean1, failFast = false)
        violations.forEach {
            val payloads = it.constraintDescriptor.payload
            payloads.forEach { p ->
                when (p) {
                    Severity.Error::class.java -> {
                        log.error("${it.propertyPath}: ${it.message} ")
                    }
                    Severity.Info::class.java -> {
                        log.info("${it.propertyPath}: ${it.message} ")
                    }
                    ErrorLogHandler::class.java -> {
                        val handler = p.getDeclaredConstructor().newInstance() as ErrorLogHandler
                        handler.onError(it)
                    }
                    else -> {
                        error("未支持的Payload: $p")
                    }
                }
            }
        }
    }


    internal data class TestPayloadBean(

        @get:Length(min = 6, max = 32, message = "name长度必须在6到32之间", payload = [Severity.Info::class])
        @get:Pattern(regexp = "[a-zA-Z]+", message = "name必须为字母", payload = [Severity.Error::class])
        val name: String?,

        @get:Max(60, message = "必须60岁以下", payload = [ErrorLogHandler::class])
        @get:Min(18, message = "必须满18岁")
        val age: Int?

    )

    internal class ErrorLogHandler : Payload {

        fun onError(violation: ConstraintViolation<*>) {
            Companion.log.warn("${violation.propertyPath}: ${violation.message} ")
        }

    }

    internal interface Severity {
        interface Info : Payload
        interface Error : Payload
    }

}