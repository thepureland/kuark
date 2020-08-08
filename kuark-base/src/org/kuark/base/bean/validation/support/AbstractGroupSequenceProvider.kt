package org.kuark.base.bean.validation.support

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider
import org.kuark.base.lang.GenericKit
import kotlin.reflect.KClass

/**
 * 抽象的分组顺序提供者。
 * 该类可以不让用户关注以下点：
 * 1."分组必须添加Bean类自己"这一规则。
 * 2.bean必须判空
 *
 * @author K
 * @since 1.0.0
 */
abstract class AbstractGroupSequenceProvider<T : Any?> : DefaultGroupSequenceProvider<T> {

    override fun getValidationGroups(bean: T?): MutableList<Class<*>> {
        val defaultGroupSequence = mutableListOf<Class<*>>()
        val beanClass = GenericKit.getSuperClassGenricType(this::class)
        defaultGroupSequence.add(beanClass!!.java) // 必须添加Bean类自己，否则Default分组都不会执行了，会抛错

        if (bean != null) {
            getGroups(bean).forEach { defaultGroupSequence.add(it.java) }
        }

        return defaultGroupSequence
    }

    /**
     * 返回分组
     *
     * @param bean 待校验的bean对象
     * @return 分组列表
     */
    abstract fun getGroups(bean: T): List<KClass<*>>

}