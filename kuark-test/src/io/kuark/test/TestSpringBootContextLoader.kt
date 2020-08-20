package io.kuark.test

import org.springframework.boot.SpringApplication
import org.springframework.boot.test.context.SpringBootContextLoader


/**
 * 单元测试springboot上下文加载器
 * 用法是在测试类上添加@ContextConfiguration，并指定loader为TestSpringBootContextLoader类的子类
 *
 * @author K
 * @since 1.0.0
 */
open class TestSpringBootContextLoader: SpringBootContextLoader() {

    override fun getSpringApplication(): SpringApplication {
        val app = super.getSpringApplication()
        val dynamicProperties = getDynamicProperties()
        if (dynamicProperties.isNotEmpty()) {
            app.addListeners(DynamicPropertiesListener(dynamicProperties))
        }
        return app
    }

    /**
     * 得到需要动态改变的属性
     *
     * @return Map<属性名，属性值>
     */
    open fun getDynamicProperties(): Map<String, String> = mapOf()


}