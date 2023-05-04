package com.nacho.expense.domain

import com.nacho.expense.domain.ExpenseRequest
import com.nacho.expense.domain.ExpenseDto

interface ExpenseService {
    fun getAllExpenses(): List<ExpenseDto>
    fun createExpense(expenseRequest: ExpenseRequest): ExpenseDto
}
