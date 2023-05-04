package com.nacho.expense.application

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.Body

import com.nacho.expense.domain.ExpenseService
import com.nacho.expense.domain.Expense
import com.nacho.expense.domain.ExpenseRequest
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.scheduling.TaskExecutors
import com.nacho.expense.domain.ExpenseDto

@ExecuteOn(TaskExecutors.IO)
@Controller("/expenses")
class ExpenseController(private val expenseService: ExpenseService) {

	@Get
	fun getAllExpenses(): List<ExpenseDto> {
		return expenseService.getAllExpenses()
	}

	@Put
	fun createExpense(@Body expenseRequest: ExpenseRequest): ExpenseDto {
		return expenseService.createExpense(expenseRequest)
	}
}
