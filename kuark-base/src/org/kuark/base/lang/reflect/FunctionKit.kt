package org.kuark.base.lang.reflect

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.memberFunctions

object FunctionKit {

    private const val SETTER_PREFIX = "set"
    private const val GETTER_PREFIX = "get"

    fun getGetters(clazz: KClass<*>): List<KFunction<*>> =
        clazz.memberFunctions.filter { it.name.startsWith(GETTER_PREFIX) }

    fun getDeclaredGetters(clazz: KClass<*>): List<KFunction<*>> =
        clazz.declaredMemberFunctions.filter { it.name.startsWith(GETTER_PREFIX) }

    fun getSetters(clazz: KClass<*>): List<KFunction<*>> =
        clazz.memberFunctions.filter { it.name.startsWith(SETTER_PREFIX) }

    fun getDeclaredSetters(clazz: KClass<*>): List<KFunction<*>> =
        clazz.declaredMemberFunctions.filter { it.name.startsWith(SETTER_PREFIX) }

    fun callGetter(obj: Any, propertyName: String) {

    }

}