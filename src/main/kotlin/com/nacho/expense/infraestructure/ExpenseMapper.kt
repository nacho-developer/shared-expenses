package com.nacho.expense.infraestructure

import jakarta.inject.Singleton
import com.nacho.expense.domain.Expense
import com.nacho.expense.domain.ExpenseDto

@Singleton
class ExpenseMapper {
	fun toDto(expenses: Iterable<Expense>): List<ExpenseDto> =
		expenses.map {
			ExpenseDto(
				id = it.id,
				payer = it.payer.name,
				deodors = it.deodors.map { it.name },
				amount = it.amount,
				description = it.description,
				date = it.date
			)
		}

	fun toDto(expense: Expense): ExpenseDto =
		ExpenseDto(
			id = expense.id,
			payer = expense.payer.name,
			deodors = expense.deodors.map { it.name },
			amount = expense.amount,
			description = expense.description,
			date = expense.date
		)
}