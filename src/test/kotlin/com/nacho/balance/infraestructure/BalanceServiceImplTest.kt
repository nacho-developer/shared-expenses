package com.nacho.balance.infraestructure

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import com.nacho.balance.domain.BalanceService
import jakarta.inject.Inject
import com.nacho.person.domain.Person
import com.nacho.expense.domain.Expense
import java.math.BigDecimal
import java.time.LocalDateTime
import com.nacho.balance.domain.Debt
import com.nacho.person.infraestructure.PersonRepository
import com.nacho.expense.infraestructure.ExpenseRepository

@MicronautTest
class BalanceServiceImplTest {

	@Inject
	lateinit var balanceService: BalanceService

	@Inject
	lateinit var personRepository: PersonRepository

	@Inject
	lateinit var expenseRepository: ExpenseRepository

	@Test
	fun testGetGroupBalance() {
		val person1 = Person(name = "John")
		val person2 = Person(name = "Jane")
		val expense1 = Expense(
			payer = person1,
			deodors = listOf(person2),
			description = "Test Expense",
			amount = BigDecimal.valueOf(100),
			date = LocalDateTime.now()
		)
		val expense2 = Expense(
			payer = person2,
			deodors = listOf(person1),
			description = "Test Expense 2",
			amount = BigDecimal.valueOf(50),
			date = LocalDateTime.now()
		)
		
		personRepository.saveAll(listOf(person1, person2))
		expenseRepository.saveAll(listOf(expense1, expense2))
		
		val result = balanceService.getGroupBalance()

		assertEquals(BigDecimal.valueOf(25), result[person1.name])
		assertEquals(BigDecimal.valueOf(-25), result[person2.name])
	}

	@Test
	fun testGetGroupBalanceAccurate() {
		val person1 = Person(name = "John")
		val person2 = Person(name = "Jane")
		val expense1 = Expense(
			payer = person1,
			deodors = listOf(person2),
			description = "Test Expense",
			amount = BigDecimal.valueOf(100),
			date = LocalDateTime.now()
		)
		val expense2 = Expense(
			payer = person2,
			deodors = listOf(person1),
			description = "Test Expense 2",
			amount = BigDecimal.valueOf(50),
			date = LocalDateTime.now()
		)
		
		personRepository.saveAll(listOf(person1, person2))
        expenseRepository.saveAll(listOf(expense1, expense2))

		val result = balanceService.getGroupBalanceAccurate()

		assertEquals(2, result.size)
		assertEquals(Debt("John", "Jane", BigDecimal.valueOf(25)), result[0])
		assertEquals(Debt("Jane", "John", BigDecimal.valueOf(25)), result[1])
	}
}
