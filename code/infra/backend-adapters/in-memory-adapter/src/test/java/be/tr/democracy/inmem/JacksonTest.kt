package be.tr.democracy.inmem

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.Test

internal class JacksonTest {
    @Test
    fun basicMapping() {
        val mapper = ObjectMapper()
            .registerModule(Jdk8Module())
            .registerKotlinModule()

        val str = mapper.writeValueAsString(Person("karel", "vervaeke"))
        println(str)

        println(mapper.readValue(str, Person::class.java))
    }

    data class Person(
        val firstName: String,
        val lastName: String
    )
}