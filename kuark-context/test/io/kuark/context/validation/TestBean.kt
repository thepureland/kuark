package io.kuark.context.validation

import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ModelAttribute
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank


@Component
open class TestBean {

    @ArgValid
    open fun test(
        @NotBlank(message = "参数不能为空")
        @ModelAttribute // 模拟非校验的注解
        arg: String,
        arg1: Int,
        @Min(value=10, message = "最小值不能小于10")
        @Max(value=100, message = "最大值不能大于100")
        arg2: Int
    ): String {
        return "test"
    }

}