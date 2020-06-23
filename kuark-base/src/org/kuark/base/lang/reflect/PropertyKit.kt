package org.kuark.base.lang.reflect

import org.apache.commons.beanutils.PropertyUtils
import java.util.*
import kotlin.reflect.KClass

/**
 * 属性工具类
 *
 * @since 1.0.0
 */
object PropertyKit {

    /**
     * 返回指定类的所有属性(必须有getter和setter的)
     *
     * @param clazz 类
     * @return Set<属性名>
    </属性名> */
    fun getAllProperties(clazz: KClass<*>?): Set<String> { //TODO junit
        val propertyDescriptors = PropertyUtils.getPropertyDescriptors(clazz)
        val set = HashSet<String>()
        for (propertyDescriptor in propertyDescriptors) {
            val writeMethod = propertyDescriptor.writeMethod
            writeMethod ?: set.add(propertyDescriptor.name)
        }
        return set
    }

    //TODO 封装apache的PropertyUtils

}