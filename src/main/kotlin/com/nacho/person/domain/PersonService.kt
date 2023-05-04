package com.nacho.person.domain

interface PersonService {
	fun getAllPersons(): List<PersonDto>
	fun addPerson(personRequest: PersonRequest): PersonDto
}