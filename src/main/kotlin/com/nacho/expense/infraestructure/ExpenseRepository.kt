package com.nacho.expense.infraestructure

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

import com.nacho.expense.domain.Expense
import com.nacho.person.domain.Person

@Repository interface ExpenseRepository : CrudRepository<Expense, Long> {
	fun findAllOrderByDateDesc() : Iterable<Expense>
	fun findByPayer(payer: Person): Iterable<Expense>
}
