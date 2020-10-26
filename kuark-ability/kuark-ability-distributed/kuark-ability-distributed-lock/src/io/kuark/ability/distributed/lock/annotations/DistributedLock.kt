package io.kuark.ability.distributed.lock.annotations

import java.lang.annotation.Documented

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@Documented
annotation class DistributedLock(val description: String = "")