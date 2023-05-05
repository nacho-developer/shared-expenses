package com.nacho.expense.infraestrucutre

import jakarta.inject.Singleton
import javax.transaction.Transactional

import com.nacho.expense.domain.Expense
import com.nacho.expense.domain.ExpenseRequest
import com.nacho.expense.domain.ExpenseService
import com.nacho.expense.infraestructure.ExpenseRepository
import com.nacho.person.infraestructure.PersonRepository
import io.micronaut.transaction.annotation.ReadOnly
import com.nacho.expense.infraestructure.ExpenseMapper
import com.nacho.expense.domain.ExpenseDto
import com.nacho.person.domain.Person
import com.nacho.expense.domain.DeodorExpenseRequest
import com.nacho.expense.domain.CancelExpenseRequest

@Singleton
open class ExpenseServiceImpl(
	private val expenseRepository: ExpenseRepository,
	private val personRepository: PersonRepository,
	private val expenseMapper: ExpenseMapper
) : ExpenseService {

	@ReadOnly
	override fun getAllExpenses(): List<ExpenseDto> {
		val expensesEntitySort = expenseRepository.findAllOrderByDateDesc()
		return expenseMapper.toDto(expensesEntitySort)
	}

	@Transactional
	override fun createExpense(expenseRequest: ExpenseRequest): ExpenseDto {
		val payer = personRepository.findById(expenseRequest.payerId)
			.orElseThrow { IllegalArgumentException("Invalid payer ID") }
		val deodors = mutableListOf<Person>()
		expenseRequest.deodorsIds.forEach {
			deodors.add(personRepository.findById(it).orElseThrow { IllegalArgumentException("Invalid deodor ID") })
		}
		val expense =
			Expense(
				description = expenseRequest.description,
				amount = expenseRequest.amount,
				date = expenseRequest.date,
				payer = payer,
				deodors = deodors.toList()
			)
		val expenseCreated = expenseRepository.save(expense)
		return expenseMapper.toDto(expenseCreated)
	}

	@Transactional
	override fun addDeodorExpense(deodorExpenseRequest: DeodorExpenseRequest): ExpenseDto {
		var expenseEntity = expenseRepository.findById(deodorExpenseRequest.expenseId).get()
		
		val currentDeodors = expenseEntity.deodors
		val newDeodors = mutableListOf<Person>()
		deodorExpenseRequest.deodorsIds.forEach {
			newDeodors.add(personRepository.findById(it).orElseThrow { IllegalArgumentException("Invalid deodor ID") })
		}
		
		newDeodors.addAll(currentDeodors)
		expenseEntity.deodors = newDeodors
		
		val updatedExpense = expenseRepository.save(expenseEntity)
		
		return expenseMapper.toDto(updatedExpense)
	}

	@Transactional
	override fun cancelExpense(cancelExpenseRequest: CancelExpenseRequest) {
		expenseRepository.deleteById(cancelExpenseRequest.expenseId)
	}
}
