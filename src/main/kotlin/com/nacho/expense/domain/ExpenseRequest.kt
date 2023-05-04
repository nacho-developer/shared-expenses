package com.nacho.expense.domain

import java.math.BigDecimal
import java.time.LocalDateTime

data class ExpenseRequest(
	val payerId: Long,
	val deodorsIds: List<Long>,
	val description: String,
    val amount: BigDecimal,
    val date: LocalDateTime
)