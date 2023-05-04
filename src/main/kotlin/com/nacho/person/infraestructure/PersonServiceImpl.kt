package com.nacho.person.infraestructure

import jakarta.inject.Singleton
import javax.transaction.Transactional
import io.micronaut.transaction.annotation.ReadOnly

import com.nacho.person.domain.Person
import com.nacho.person.domain.PersonDto
import com.nacho.person.domain.PersonRequest
import com.nacho.person.domain.PersonService

@Singleton
open class PersonServiceImpl(
	private val personRepository: PersonRepository,
	private val personMapper: PersonMapper
) : PersonService {

	@ReadOnly
	override fun getAllPersons(): List<PersonDto> {
		val persons = personRepository.findAll()
		return personMapper.toDto(persons)
	}

	@Transactional
	override fun addPerson(personRequest: PersonRequest): PersonDto {
		val newPerson = Person(name = personRequest.name)
		val personCreated = personRepository.save(newPerson)
		return personMapper.toDto(personCreated)
	}
}
