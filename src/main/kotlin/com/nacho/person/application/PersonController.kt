package com.nacho.person.application

import io.micronaut.scheduling.TaskExecutors
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.Body
import io.micronaut.scheduling.annotation.ExecuteOn
import com.nacho.person.domain.PersonService
import com.nacho.person.domain.PersonDto
import com.nacho.person.domain.PersonRequest

@ExecuteOn(TaskExecutors.IO)
@Controller("/persons")
class PersonController(private val personService: PersonService) {

    @Get
    fun getAllPersons(): List<PersonDto> {
        return personService.getAllPersons()
    }

    @Put
    fun createPerson(@Body personRequest: PersonRequest): PersonDto {
        return personService.addPerson(personRequest)
    }

}
