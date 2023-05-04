package com.nacho.balance.domain

import com.nacho.person.domain.Person
import java.math.BigDecimal

data class Debt(
	val from: String,
	val to: String,
	var amount: BigDecimal
)
