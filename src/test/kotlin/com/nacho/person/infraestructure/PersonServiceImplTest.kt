package com.nacho.person.infraestructure

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import jakarta.inject.Inject
import com.nacho.person.domain.PersonService
import com.nacho.person.domain.PersonRequest

@MicronautTest
class PersonServiceImplTest {
    
    @Inject
    lateinit var personService: PersonService
    
    @Test
    fun testGetAllPersons() {
        // Given
        val person1 = personService.addPerson(PersonRequest("John"))
        val person2 = personService.addPerson(PersonRequest("Jane"))
        
        // When
        val persons = personService.getAllPersons()
        
        // Then
        assertEquals(2, persons.size)
        assertEquals(person1, persons[0])
        assertEquals(person2, persons[1])
    }
    
    @Test
    fun testAddPerson() {
        // Given
        val personRequest = PersonRequest("John")
        
        // When
        val person = personService.addPerson(personRequest)
        
        // Then
        assertEquals(personRequest.name, person.name)
        assertNotNull(person.id)
    }
    
    @Test
    fun testAddPersonWithEmptyName() {
        // Given
        val personRequest = PersonRequest("")
        
        // When
        try {
            personService.addPerson(personRequest)
        } catch (e: IllegalArgumentException) {
            // Then
            assertEquals("Name cannot be empty", e.message)
        }
    }
}

