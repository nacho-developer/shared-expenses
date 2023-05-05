package com.nacho.expense.domain

data class DeodorExpenseRequest(
    val expenseId: Long,
    val deodorsIds: List<Long>
)