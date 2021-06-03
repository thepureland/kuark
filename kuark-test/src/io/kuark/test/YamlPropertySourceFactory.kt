package io.kuark.test

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.env.PropertySource
import org.springframework.core.io.support.EncodedResource
import org.springframework.core.io.support.PropertySourceFactory


/**
 * yml配置文件属性源工厂
 *
 * @author https://zhuanlan.zhihu.com/p/99738603
 * @author K
 * @since 1.0.0
 */
internal class YamlPropertySourceFactory : PropertySourceFactory {

    override fun createPropertySource(name: String?, resource: EncodedResource): PropertySource<*> {
        val factory = YamlPropertiesFactoryBean()
        factory.setResources(resource.resource)
        factory.afterPropertiesSet()
        val ymlProperties = factory.getObject()
        val propertyName = name ?: resource.resource.filename!!
        return PropertiesPropertySource(propertyName, ymlProperties!!)
    }

}