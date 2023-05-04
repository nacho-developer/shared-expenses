package com.nacho.person.infraestructure

import io.micronaut.data.annotation.Repository

import com.nacho.person.domain.Person
import io.micronaut.data.repository.CrudRepository

@Repository
interface PersonRepository : CrudRepository<Person, Long> {}
