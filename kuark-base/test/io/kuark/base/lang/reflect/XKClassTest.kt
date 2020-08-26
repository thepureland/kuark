package io.kuark.base.lang.reflect

import io.kuark.base.bean.validation.constraint.annotaions.AtLeast
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.io.Serializable
import kotlin.reflect.full.starProjectedType

class XKClassTest {

    @Test
    fun newInstance() {
        val parrot = Parrot::class.newInstance("name", 3)
        assertEquals(3, parrot.age)

        val dog = Dog::class.newInstance()
        assertEquals(0, dog.age)

        assertThrows<IllegalStateException> { Animal::class.newInstance() }
    }

    @Test
    fun isAnnotationPresent() {
        assert(Organism::class.isAnnotationPresent(TestAnno::class))
        assert(Animal::class.isAnnotationPresent(TestAnno::class))
        assert(Bird::class.isAnnotationPresent(TestAnno::class))
        assertFalse(Flyable::class.isAnnotationPresent(TestAnno::class))
        assertFalse(Organism::class.isAnnotationPresent(AtLeast::class))
        assertFalse(Organism::class.isAnnotationPresent(SinceKotlin::class)) // 对于像SinceKotlin的注解无效
    }

    @Test
    fun getMemberProperty() {
        assertDoesNotThrow { Parrot::class.getMemberProperty("age") }
        assertDoesNotThrow { Parrot::class.getMemberProperty("height") }
        assertDoesNotThrow { Parrot::class.getMemberProperty("name") }
        assertThrows<NoSuchElementException> { Parrot::class.getMemberProperty("weight") != null } // private
        assertThrows<NoSuchElementException> { Parrot::class.getMemberProperty("xxxxx") }
        assertThrows<NoSuchElementException> { Parrot::class.getMemberProperty("ageValue") } // 只是参数，未定义为属性
    }

    @Test
    fun getMemberPropertyValue() {
        val parrot = Parrot("polly", 3)
        assertEquals(3, Parrot::class.getMemberPropertyValue(parrot, "age"))
        assertEquals(0.0, Parrot::class.getMemberPropertyValue(parrot, "height"))
        assertEquals("polly", Parrot::class.getMemberPropertyValue(parrot, "name"))
        assertThrows<NoSuchElementException> { Parrot::class.getMemberPropertyValue(parrot, "weight") }
        assertThrows<NoSuchElementException> { Parrot::class.getMemberPropertyValue(parrot, "xxxxx") }
        assertThrows<NoSuchElementException> { Parrot::class.getMemberPropertyValue(parrot, "ageValue") }
    }

    @Test
    fun getMemberFunction() {
        assertDoesNotThrow { Parrot::class.getMemberFunction("sleep") }
        assertDoesNotThrow { Parrot::class.getMemberFunction("move") }
        assertDoesNotThrow { Parrot::class.getMemberFunction("fly") }
        assertDoesNotThrow { Parrot::class.getMemberFunction("speak", *Parrot::speak.parameters.toTypedArray()) }
        assertThrows<NoSuchElementException> { Parrot::class.getMemberFunction("xxxxx") }
    }

    @Test
    fun getSuperClass() {
        assertEquals(Bird::class, Parrot::class.getSuperClass())
        assertEquals(Any::class, Animal::class.getSuperClass())
        assertEquals(Any::class, Flyable::class.getSuperClass())
    }

    @Test
    fun getSuperInterfaces() {
        assertEquals(listOf(Speakable::class), Parrot::class.getSuperInterfaces())
        assertEquals(listOf(Flyable::class, Serializable::class), Bird::class.getSuperInterfaces())
        assertEquals(listOf(Organism::class), Speakable::class.getSuperInterfaces())
    }

    @Test
    fun getAllInterfaces() {
        assertEquals(
            listOf(Organism::class, Flyable::class, Serializable::class, Speakable::class),
            Parrot::class.getAllInterfaces()
        )
    }

    @Test
    fun firstMatchTypeOf() {
        assertEquals(
            Parrot::class,
            Parrot::class.firstMatchTypeOf(
                listOf(Animal::class.starProjectedType, Parrot::class.starProjectedType)
            ).classifier
        )
    }

    @Test
    fun getClassUpThatPresentAnnotation() {
        assertEquals(
            setOf(Bird::class, Animal::class, Organism::class),
            Parrot::class.getClassUpThatPresentAnnotation(TestAnno::class)
        )

        // 对于像SinceKotlin的注解无效
        assert(Parrot::class.getClassUpThatPresentAnnotation(SinceKotlin::class).isEmpty())
    }

    @Test
    fun getLocationOnDisk() {
        print(Organism::class.getLocationOnDisk())
    }


    @TestAnno
    @SinceKotlin("1.0.0")
    internal interface Organism {
        fun eat()
    }

    internal interface Flyable {
        fun fly()
    }

    internal interface Speakable : Organism {
        fun speak(content: String)
    }

    @TestAnno
    internal abstract class Animal : Organism {
        abstract val age: Int
        abstract fun move()
    }

    @TestAnno
    internal open class Bird(override val age: Int) : Animal(), Flyable, Serializable {
        private val weight: Double = 0.0
        val height: Double = 0.0
        override fun move() {}
        override fun eat() {}
        override fun fly() {}
        fun sleep() {}
    }

    internal class Parrot(val name: String, ageValue: Int) : Bird(ageValue), Speakable {
        override fun speak(content: String) {}
    }

    internal class Dog : Animal() {
        override val age: Int
            get() = 0

        override fun move() {}

        override fun eat() {}
    }

    internal annotation class TestAnno


}