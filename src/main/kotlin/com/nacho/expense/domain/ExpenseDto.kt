package com.nacho.expense.domain

import java.math.BigDecimal
import java.time.LocalDateTime

data class ExpenseDto(
	val id: Long?,
    val payer: String,
    val deodors: List<String>,
    val amount: BigDecimal,
    val description: String,
    val date: LocalDateTime
)