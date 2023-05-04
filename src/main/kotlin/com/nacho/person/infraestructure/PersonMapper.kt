package com.nacho.person.infraestructure

import jakarta.inject.Singleton
import com.nacho.person.domain.Person
import com.nacho.person.domain.PersonDto

@Singleton
class PersonMapper {
	fun toDto(persons: Iterable<Person>): List<PersonDto> =
		persons.map {
			PersonDto(it.id, it.name)
		}

	fun toDto(person: Person): PersonDto =
		PersonDto(person.id, person.name)
}