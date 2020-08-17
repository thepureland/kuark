package org.kuark.base.bean.validation.support

import kotlin.reflect.KClass

enum class ConstraintRule(val annotationClass: KClass<out Annotation>) {

    NotNull(javax.validation.constraints.NotNull::class)

}