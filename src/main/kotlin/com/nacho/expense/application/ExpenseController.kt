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
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Delete
import com.nacho.expense.domain.DeodorExpenseRequest
import com.nacho.expense.domain.CancelExpenseRequest

@ExecuteOn(TaskExecutors.IO)
@Controller("/api/v1/expenses")
class ExpenseController(private val expenseService: ExpenseService) {

	@Get
	fun getAllExpenses(): List<ExpenseDto> {
		return expenseService.getAllExpenses()
	}

	@Put
	fun createExpense(@Body expenseRequest: ExpenseRequest): ExpenseDto {
		return expenseService.createExpense(expenseRequest)
	}

	@Post
	fun addDeodorExpense(@Body deodorExpenseRequest: DeodorExpenseRequest): ExpenseDto {
		return expenseService.addDeodorExpense(deodorExpenseRequest)
	}

	@Delete
	fun cancelExpense(@Body cancelExpenseRequest: CancelExpenseRequest) {
		return expenseService.cancelExpense(cancelExpenseRequest)
	}
}
